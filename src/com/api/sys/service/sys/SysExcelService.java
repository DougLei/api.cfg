package com.api.sys.service.sys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Query;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Service;
import com.api.constants.DataTypeConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SysFileConstants;
import com.api.plugins.ijson.IJson;
import com.api.plugins.ijson.JSONArrayExtend;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.code.resource.CodeResourceProcesser;
import com.api.sys.entity.cfg.CfgPropExtendConf;
import com.api.sys.entity.cfg.propextend.query.data.PropExtendConfQueryData;
import com.api.sys.entity.sys.SysFile;
import com.api.sys.entity.sys.file.ie.ExportFile;
import com.api.sys.entity.sys.file.ie.ImportFile;
import com.api.sys.entity.sys.file.ie.ImportFileTemplate;
import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.api.sys.entity.tools.resource.metadatainfo.ie.IEResourceMetadataInfo;
import com.api.sys.service.AService;
import com.api.util.CloseUtil;
import com.api.util.DateUtil;
import com.api.util.ExceptionUtil;
import com.api.util.FileUtil;
import com.api.util.PoiExcelUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;
import com.api.web.entity.resulttype.PageResultEntity;

/**
 * excel操作service
 * @author DougLei
 */
@Service
public class SysExcelService extends AService{
	
	/**
	 * 生成excel文件，并将该文件信息保存到文件表中
	 * @param workbook
	 * @param fileName
	 * @param suffix
	 * @param fileId
	 * @param buildInType
	 * @return
	 */
	private Object createExcelFile(Workbook workbook, String fileName, String suffix, String fileId, int buildInType){
		String fileCode = FileUtil.getFileCode();
		String excelFileSavePath = BuiltinResourceInstance.getInstance("SysFileService", SysFileService.class).validSaveFileDirIsExists(buildInType, null)+fileCode+"."+suffix;
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(excelFileSavePath);
			workbook.write(fo);
		} catch (IOException e) {
			return "生成excel文件时出现IO异常：" + ExceptionUtil.getErrMsg(e);
		} finally {
			CloseUtil.closeIO(fo);
		}
		
		SysFile excelFile = new SysFile();
		excelFile.setId(fileId);
		excelFile.setActName(DateUtil.formatDate(new Date(), sdfSimple) + "_" + fileName + "." + suffix);
		excelFile.setCode(fileCode);
		excelFile.setSuffix(suffix);
		excelFile.setSavePath(excelFileSavePath);
		excelFile.setSaveType(SysFileConstants.saveType);
		excelFile.setBatch(ResourceHandlerUtil.getBatchNum());
		excelFile.setBuildInType(buildInType);
		return HibernateUtil.saveObject(excelFile, null);
	}
	private static final SimpleDateFormat sdfSimple = new SimpleDateFormat("yyyyMMdd");
	
	// --------------------------------------------------------------------------------------------------
	/**
	 * 导入excel
	 * @param request
	 * @param importExcel
	 * @return
	 */
	public Object importExcel(HttpServletRequest request, ImportFile importFile) {
		SysFile file = importFile.getImportFile();
		Object wb = PoiExcelUtil.getReadWorkBookInstance(file.getSavePath(), file.getSuffix());
		if(wb instanceof String){
			return wb;
		}
		Workbook workbook = (Workbook) wb;
		Sheet sheet = workbook.getSheetAt(0);
		if(sheet.getRow(1) != null){
			int rowCount = sheet.getLastRowNum()+1; // 行数
			importFile.setImportTotalCount(rowCount);
			
			String resourceName = importFile.getResourceName();
			
			List<IEResourceMetadataInfo> ieResourceMetadataInfos = importFile.getIeResourceMetadataInfos();
			IEResourceMetadataInfo rmi = null;
			int ieResourceMetadataInfoCount = ieResourceMetadataInfos.size();
			
			IJson ijson = new JSONArrayExtend(importFile.getBatchImportCount());
			JSONObject json = null;
			
			String importResult = null;
			
			Row row = null;// excel行对象
			short columnCount = 0; // 列数
			
			int rowIndex = 1, cellIndex, index;
			while(importFile.hasMoreImport()){
				for(; rowIndex <= importFile.getCurrentLoopSize(); rowIndex++){
					row = sheet.getRow(rowIndex);
					if(row != null){
						columnCount = row.getLastCellNum();
						
						json = new JSONObject(ieResourceMetadataInfoCount);
						ijson.add(json);
						
						for (cellIndex=0, index=0; cellIndex<columnCount; cellIndex++) {
							if(index > ieResourceMetadataInfoCount){
								break;
							}
							rmi = ieResourceMetadataInfos.get(index++);
							if(rmi.getIeConfExtend() != null){
								cellIndex++;
							}
							json.put(rmi.getPropName(), getCellValue(row.getCell(cellIndex)));
						}
					}
				}
				importResult = importDatas("导入excel文件["+file.getActName()+"]，", resourceName, ijson, ieResourceMetadataInfos, importFile, request);
				if(importResult != null){
					return importResult;
				}
			}
			importResult = importDatas("导入excel文件["+file.getActName()+"]，", resourceName, ijson, ieResourceMetadataInfos, importFile, request);
			if(importResult != null){
				return importResult;
			}
		}
		return importFile;
	}
	
	/**
	 * 获得单元格中的数据值
	 * @param cell
	 * @return
	 */
	private Object getCellValue(Cell cell) {
		if(cell == null){
			return null;
		}
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC && org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)){
			return cell.getDateCellValue();
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell.getStringCellValue();
	}

	/**
	 * 导入数据
	 * @param desc
	 * @param resourceName
	 * @param ijson
	 * @param ieResourceMetadataInfos
	 * @param importFile
	 * @param request
	 * @return
	 */
	private String importDatas(String desc, String resourceName, IJson ijson, List<IEResourceMetadataInfo> ieResourceMetadataInfos, ImportFile importFile, HttpServletRequest request) {
		String result = validImportDatas(desc , resourceName, ijson, ieResourceMetadataInfos, importFile);
		if(result != null){
			return result;
		}
		result = saveImportDatas(resourceName, ijson, importFile.getExtendParamMap(), request, importFile);
		if(result != null){
			return result;
		}
		return null;
	}
	
	/**
	 * 验证导入的数据
	 * @param desc
	 * @param resourceName
	 * @param ijson
	 * @param ieResourceMetadataInfos 
	 * @return
	 */
	private String validImportDatas(String desc, String resourceName, IJson ijson, List<IEResourceMetadataInfo> ieResourceMetadataInfos, ImportFile importFile) {
		int size = ijson.size();
		int rowIndex = 1;
		int cellIndex =1;
		
		if(importFile.isTableResource()){
			int i;
			Set<ResourceMetadataInfo> uniqueConstraintProps = new HashSet<ResourceMetadataInfo>(ieResourceMetadataInfos.size());
			JSONObject data = null;
			boolean dataValueIsNull;
			Object dataValue = null;
			String validDataIsLegalResult = null;
			for(cellIndex=1, i=0;i<size;i++){
				data = ijson.get(i);
				data.get(ResourcePropNameConstants.ID);
				
				for (IEResourceMetadataInfo rmi : ieResourceMetadataInfos) {
					if(rmi.getIsIgnoreValid() == 1){
						continue;
					}
					dataValue = data.get(rmi.getPropName());
					dataValueIsNull = StrUtils.isEmpty(dataValue);
					
					// 验证不能为空
					if(rmi.getIsNullabled() == 0 && dataValueIsNull){
						return desc + "第"+rowIndex+"行，第"+cellIndex+"列["+rmi.getDescName()+"]的数据值不能为空";
					}
					
					if(!dataValueIsNull){
						validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(dataValue, rmi);
						if(validDataIsLegalResult != null){
							return desc+"第"+rowIndex+"行，第"+cellIndex+"列" + validDataIsLegalResult;
						}
						
						// 验证唯一约束
						if(rmi.getIsUnique() == 1){
							uniqueConstraintProps.add(rmi);
							if(validDataIsExists(resourceName, rmi, dataValue)){
								return desc+"第"+rowIndex+"行，第"+cellIndex+"列["+rmi.getDescName()+"]的数据值已经存在，不能重复添加";
							}
						}
					}
					cellIndex++;
				}
			}
			
			// 验证一次提交的数组中，是否有重复的值，违反了唯一约束 // TODO 没有办法做唯一性验证，因为是分批次的数据处理
			if(size > 1 && uniqueConstraintProps.size()>0){
				for (ResourceMetadataInfo uniqueConstraintProp : uniqueConstraintProps) {
					for(int i=0;i<size-1;i++){
						dataValue = ijson.get(i).get(uniqueConstraintProp.getPropName());
						if(StrUtils.notEmpty(dataValue)){
							for(int j=i+1;j<size;j++){
								if(dataValue.equals(ijson.get(j).get(uniqueConstraintProp.getPropName()))){
									return desc+"第"+rowIndex+"行和第"+(j+2)+"行的，第"+cellIndex+"列数据["+uniqueConstraintProp.getDescName()+"]值重复，导入失败";
								}
							}
						}
					}
				}
			}
			
			if(uniqueConstraintProps.size() > 0){
				uniqueConstraintProps.clear();
			}
		}else if(importFile.isSqlResource()){
			// TODO 验证sql
			
			
		}else{
			return "导入数据时，只支持表资源或sql资源的导入前数据验证";
		}
		return null;
	}
	
	/**
	 * 验证数据是否已经存在
	 * @param resourceName
	 * @param rmi  .getPropName()
	 * @param dataValue
	 * @return
	 */
	private boolean validDataIsExists(String resourceName, ResourceMetadataInfo rmi, Object dataValue) {
		Object id = ResourceHandlerUtil.getUniqueDataId(resourceName, rmi, dataValue);
		return (id != null);
	}
	
	/**
	 * 保存导入的数据
	 * @param resourceName
	 * @param ijson
	 * @param extendParamMap 
	 * @param request 
	 * @return
	 */
	private String saveImportDatas(String resourceName, IJson ijson, Map<String, Object> extendParamMap, HttpServletRequest request, ImportFile importFile) {
		int size = ijson.size();
		if(size > 0){
			String codeResourceKey = CodeResourceProcesser.getImportDataCodeResourceKey(resourceName);
			if(CodeResourceProcesser.isCodeResource(codeResourceKey)){
				if(extendParamMap != null && extendParamMap.size() > 0){
					JSONObject json = null;
					Set<Entry<String, Object>> sets = extendParamMap.entrySet();
					for(int i =0;i<size;i++){
						json = ijson.get(i);
						for (Entry<String, Object> set : sets) {
							json.put(set.getKey(), set.getValue());
						}
					}
					extendParamMap.clear();
				}
				Object result = CodeResourceProcesser.invokeCodeResource(codeResourceKey, request, ijson);
				if(result instanceof String){
					return result.toString();
				}
			}else{
				if(importFile.isTableResource()){
					for(int i =0;i<size;i++){
						HibernateUtil.saveObject(resourceName, ijson.get(i), null);
					}
				}else if(importFile.isSqlResource()){
					for(int i =0;i<size;i++){
						// TODO 保存sql语句
						
						
						
						
					}
				}else{
					return "导入数据时，只支持表资源或sql资源的导入操作";
				}
			}
		}
		return null;
	}

	// ---------------------------------------------------------------------
	/**
	 * 生成excel导入模版
	 * <p>将生成excel导入模版保存到服务器上，并在sysfile中插入一条数据</p>
	 * @param importFileTemplate
	 * @return
	 */
	public Object createImportExcelTemplate(ImportFileTemplate importFileTemplate) {
		String suffix = importFileTemplate.getFileSuffix();
		Object wb = PoiExcelUtil.getWriteWorkBookInstance(suffix);
		if(wb instanceof String){
			return wb;
		}
		Workbook workbook = (Workbook) wb;
		Sheet sheet = workbook.createSheet();
		Row headRow = sheet.createRow(0);
		createImportExcelTemplateHeadRow(workbook, importFileTemplate, sheet, headRow);
		return createExcelFile(workbook, importFileTemplate.getResourceName(), suffix, importFileTemplate.getFileId(), SysFileConstants.BUILD_IN_TYPE_IMPORT_TEMPLATE);
	}
	
	/**
	 * 创建excel导入模版的头行
	 * @param workbook
	 * @param importFileTemplate
	 * @param sheet
	 * @param headRow
	 */
	private void createImportExcelTemplateHeadRow(Workbook workbook, ImportFileTemplate importFileTemplate, Sheet sheet, Row headRow) {
		String fileSuffix = importFileTemplate.getFileSuffix();
		List<IEResourceMetadataInfo> ieResourceMetadataInfos = importFileTemplate.getIeResourceMetadataInfos();
		int resourceMetadataInfoOfConfExtendCount = importFileTemplate.getResourceMetadataInfoOfConfExtendCount();
		
		int resourceMetadataInfoCount = ieResourceMetadataInfos.size();
		int propConfExtendInfoCellIndex = resourceMetadataInfoCount+resourceMetadataInfoOfConfExtendCount;// 属性配置的扩展信息，要在excel中插入单元格的下标
		
		IEResourceMetadataInfo rmi = null;
		List<Object[]> dataList = null;
		Row hiddenRow = null;
		int hiddenRowIndex = 1;
		int cellIndex = 0;
		Cell valueCell = null;// 设置成隐藏列，并且加函数的
		
		String valueArrayWord = null;
		String valueWord = null;
		String compareWord = null;
		CfgPropExtendConf propExtendConf = null;
		PropExtendConfQueryData propExtendConfQueryData = null;
		for (int i=0;i<resourceMetadataInfoCount ;i++) {
			rmi = ieResourceMetadataInfos.get(i);
			PoiExcelUtil.setHeadCellStyle(workbook, setCellValue(headRow.createCell(cellIndex++), rmi.getDescName()));
			
			// 如果是日期类型，则要修改单元格的格式为yyyy-MM-dd HH:mm:ss
			if(DataTypeConstants.DATE.equals(rmi.getDataType())){
				
			}
			
			propExtendConf = rmi.getIeConfExtend();
			if(propExtendConf != null){
				propExtendConfQueryData = new PropExtendConfQueryData(propExtendConf, importFileTemplate.getQueryPropExtendConfDataParam(rmi.getPropName()));
				if(propExtendConfQueryData.getDataListTotalCount() > 0){
					sheet.setColumnHidden(cellIndex, true);
					valueCell = setCellValue(headRow.createCell(cellIndex++), rmi.getPropName());
					
					// 设置存储实际值的隐藏列的计算公式：=INDEX(H:H,MATCH(D:D,I:I,0))
					valueCell.setCellType(Cell.CELL_TYPE_FORMULA);
					valueArrayWord = PoiExcelUtil.getColumnCharWordByIndex(propConfExtendInfoCellIndex);
					valueWord = PoiExcelUtil.getColumnCharWordByIndex(cellIndex-1);
					compareWord = PoiExcelUtil.getColumnCharWordByIndex(propConfExtendInfoCellIndex+1);
					valueCell.setCellFormula("=INDEX("+valueArrayWord+":"+valueArrayWord+",MATCH("+valueWord+":"+valueWord+","+compareWord+":"+compareWord+",0))");
					
					while((dataList = propExtendConfQueryData.getDataList()) != null){
						hiddenRowIndex = setCellValueRecursive(dataList, sheet, hiddenRow, hiddenRowIndex, propConfExtendInfoCellIndex, propConfExtendInfoCellIndex+1, 0);
					}
					
					// 设置数据有效性，默认1列就够了，剩下的用户去修改和拖拉excel单元格即可
					PoiExcelUtil.setDataValidation(fileSuffix, sheet, 1, 1, cellIndex-2, cellIndex-2, compareWord, 1, compareWord, hiddenRowIndex);
					// 隐藏列
					sheet.setColumnHidden(propConfExtendInfoCellIndex++, true);
					sheet.setColumnHidden(propConfExtendInfoCellIndex++, true);
					hiddenRowIndex=1;
				}
				propExtendConfQueryData.clear();
			}
			// 设置列的自适应宽度
			sheet.autoSizeColumn(i, true);
		}
	}
	/** 递归设置子单元格的值，设置完成后，会清空dataList */
	@SuppressWarnings("unchecked")
	private int setCellValueRecursive(List<Object[]> dataList, Sheet sheet, Row hiddenRow, int hiddenRowIndex, int index1, int index2, int recursiveLevel) {
		if(dataList != null && dataList.size() > 0){
			for (Object[] data : dataList) {
				hiddenRow = sheet.createRow(hiddenRowIndex++);
				setCellValue(hiddenRow.createCell(index1), data[0]);
				setCellValue(hiddenRow.createCell(index2), getIndent(recursiveLevel)+data[1]);
				if(data.length == 3 && data[3] != null && data[3] instanceof List){
					hiddenRowIndex = setCellValueRecursive((List<Object[]>)data[3], sheet, hiddenRow, hiddenRowIndex, index1, index2, recursiveLevel+1);
				}
			}
			dataList.clear();
		}
		return hiddenRowIndex;
	}
	/** 设置单元格的值 */
	private Cell setCellValue(Cell cell, Object value){
		if(value != null){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value.toString());
		}
		return cell;
	}
	/** 获得缩进的标识，一个层级，多加一个> */
	private String getIndent(int recursiveLevel) {
		if(recursiveLevel < 1){
			return "";
		}
		return indentBuffer.substring(0,recursiveLevel);
	}
	private static final String indentBuffer = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
	
	// ---------------------------------------------------------------------
	/**
	 * 生成导出excel文件
	 * <p>将生成导出excel文件保存到服务器上，并在sysfile中插入一条数据</p>
	 * @param exportFile
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object createExportExcelFile(ExportFile exportFile) {
		String suffix = exportFile.getExportFileSuffix();
		Object wb = PoiExcelUtil.getWriteWorkBookInstance(suffix);
		if(wb instanceof String){
			return wb;
		}
		String title = exportFile.analyzeExportTitle();
		
		Workbook workbook = (Workbook) wb;
		Sheet sheet = workbook.createSheet();
		
		List<IEResourceMetadataInfo> ieResourceMetadataInfos = exportFile.getIeResourceMetadataInfos();
		int ieResourceMetadataInfoCount = ieResourceMetadataInfos.size();
		
		createTitleRow(workbook, sheet, title, 0, 0, 0, 0, 0, ieResourceMetadataInfos.size()-1);
		
		Row headRow = sheet.createRow(1);
		createExportExcelHeadRow(workbook, sheet, headRow, ieResourceMetadataInfos, ieResourceMetadataInfoCount);
		
		PageResultEntity pageResultEntity = exportFile.getPageResultEntity();
		Query query = exportFile.getQuery();
		
		String validDataListResult = null;
		Object data = null;
		List dataList = null;
		Object[] dataArr = null;// sql语句查询多个字段，会返回数组
		Map<String, Object> dataMap = null;// hql语句查询，会返回map集合
		int size;
		
		int rowNum = 2;// 第一行是标题，第二行是头，所以这里从第三行开始
		Row row = null;
		Cell cell = null;
		int loopCount = pageResultEntity.getPageTotalCount();
		for(int i=0;i<loopCount;i++){
			dataList = executeQuery(query, pageResultEntity, i);
			validDataListResult = validDataList(dataList, ieResourceMetadataInfoCount, data, dataArr, dataMap);
			if(validDataListResult != null){
				return validDataListResult;
			}
			
			for (Object object : dataList) {
				row = sheet.createRow(rowNum++);
				if(object instanceof Object[]){
					dataArr = (Object[]) object;
					size = dataArr.length;
					for(int j=0;j<size;j++){
						createCell(workbook, j, row, cell, dataArr[j], ieResourceMetadataInfos.get(j));
					}
				}else if(object instanceof Map){
					dataMap = (Map<String, Object>) object;
					size = dataMap.size();
					for(int j=0;j<size;j++){
						createCell(workbook, j, row, cell, dataMap.get(ieResourceMetadataInfos.get(j).getPropName()), ieResourceMetadataInfos.get(j));
					}
					if(size>0){
						dataMap.clear();
					}
				}else if(object instanceof Object){
					createCell(workbook, 0, row, cell, object, ieResourceMetadataInfos.get(0));
				}
			}
			dataList.clear();
		}
		
		// 设置列的自适应宽度
		for (int i=0;i<ieResourceMetadataInfoCount ;i++) {
			sheet.autoSizeColumn(i, true);
		}
		return createExcelFile(workbook, title, suffix, exportFile.getFileId(), SysFileConstants.BUILD_IN_TYPE_EXPORT);
	}

	/**
	 * 创建标题行
	 * @param workbook
	 * @param sheet
	 * @param title
	 * @param titleRowIndex
	 * @param titleCellIndex
	 * @param mergeFirstRow
	 * @param mergeLastRow
	 * @param mergeFirstCol
	 * @param mergeLastCol
	 */
	private void createTitleRow(Workbook workbook, Sheet sheet, String title, int titleRowIndex, int titleCellIndex, int mergeFirstRow, int mergeLastRow, int mergeFirstCol, int mergeLastCol){
		Row titleRow = sheet.createRow(PoiExcelUtil.calcIndex(titleRowIndex));
		Cell titleCell = titleRow.createCell(PoiExcelUtil.calcIndex(titleCellIndex));
		titleCell.setCellType(Cell.CELL_TYPE_STRING);
		titleCell.setCellValue(title);
		PoiExcelUtil.setTitleCellStyle(workbook, sheet, titleCell, mergeFirstRow, mergeLastRow, mergeFirstCol, mergeLastCol);
	}
	
	/**
	 * 创建导出excel的头行
	 * @param workbook
	 * @param sheet
	 * @param headRow
	 * @param ieResourceMetadataInfos
	 * @param ieResourceMetadataInfoCount
	 */
	private void createExportExcelHeadRow(Workbook workbook, Sheet sheet, Row headRow, List<IEResourceMetadataInfo> ieResourceMetadataInfos, int ieResourceMetadataInfoCount){
		Cell headCell;
		ResourceMetadataInfo rmi = null;
		for (int i=0;i<ieResourceMetadataInfoCount ;i++) {
			rmi = ieResourceMetadataInfos.get(i);
			headCell = headRow.createCell(i);
			headCell.setCellValue(rmi.getDescName());
			PoiExcelUtil.setHeadCellStyle(workbook, headCell);
		}
	}
	
	/**
	 * 执行查询
	 * @param query
	 * @param pageResultEntity
	 * @param loopTime 第几次循环 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List executeQuery(Query query, PageResultEntity pageResultEntity, int loopTime) {
		query.setFirstResult(pageResultEntity.getPageSize()*loopTime);
		query.setMaxResults(pageResultEntity.getPageSize());
		return query.list();
	}
	
	/**
	 * 验证查询的数据结果集
	 * @param dataList
	 * @param resourceMetadataInfoCount
	 * @param data 
	 * @param dataMap 
	 * @param dataArr 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String validDataList(List dataList, int resourceMetadataInfoCount, Object data, Object[] dataArr, Map<String, Object> dataMap) {
		if(dataList == null || dataList.size() == 0){
			return "生成excel导出文件时，没有查询到任何数据，请联系后端系统开发人员";
		}
		data = dataList.get(0);
		if(data instanceof Object[]){
			dataArr = (Object[]) data;
			if(dataArr.length != resourceMetadataInfoCount){
				return "生成excel导出文件时，查询数据列的数量，与配置的导出列的数量不一致，请修改";
			}
		}else if(data instanceof Map){
			dataMap = (Map<String, Object>) data;
			dataMap.remove(ResourcePropNameConstants.HQL_QUERY_RETURN_TYPE_PROP);
			if(dataMap.size() != resourceMetadataInfoCount){
				return "生成excel导出文件时，查询数据列的数量，与配置的导出列的数量不一致，请修改";
			}
		}else if(data instanceof Object){
			if(resourceMetadataInfoCount > 1){
				return "生成excel导出文件时，查询数据列的数量，与配置的导出列的数量不一致，请修改";
			}
		}
		return null;
	}
	
	/**
	 * 创建单元格，并写入值
	 * @param workbook
	 * @param columnIndex
	 * @param row
	 * @param valueCell
	 * @param object
	 * @param ieResourceMetadataInfo
	 */
	private void createCell(Workbook workbook, int columnIndex, Row row, Cell valueCell, Object object, IEResourceMetadataInfo ieResourceMetadataInfo) {
		valueCell = row.createCell(columnIndex);
		if(object == null){
			return;
		}else if(ieResourceMetadataInfo == null){
			valueCell.setCellType(Cell.CELL_TYPE_STRING);
		}else if(DataTypeConstants.INTEGER.equals(ieResourceMetadataInfo.getDataType()) || DataTypeConstants.DOUBLE.equals(ieResourceMetadataInfo.getDataType())){
			valueCell.setCellType(Cell.CELL_TYPE_NUMERIC);
		}else if(DataTypeConstants.INTEGER.equals(ieResourceMetadataInfo.getDataType())){
			valueCell.setCellType(Cell.CELL_TYPE_BOOLEAN);
		}else{
			valueCell.setCellType(Cell.CELL_TYPE_STRING);
		}
		valueCell.setCellValue(object.toString());
		PoiExcelUtil.setValueCellStyle(workbook, valueCell);
	}
}
