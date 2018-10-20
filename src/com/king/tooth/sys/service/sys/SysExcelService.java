package com.king.tooth.sys.service.sys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Query;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SysFileConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.JSONArrayExtend;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.sys.file.ExportFile;
import com.king.tooth.sys.entity.sys.file.ImportFile;
import com.king.tooth.sys.entity.sys.file.ImportFileTemplate;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.FileUtil;
import com.king.tooth.util.PoiExcelUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * excel操作service
 * @author DougLei
 */
@Service
public class SysExcelService extends AService{
	
	/**
	 * 创建头行
	 * @param headRow
	 * @param resourceMetadataInfos
	 * @param resourceMetadataInfoCount
	 */
	private void createHeadRow(Row headRow, List<ResourceMetadataInfo> resourceMetadataInfos, int resourceMetadataInfoCount){
		Cell cell;
		ResourceMetadataInfo rmi = null;
		for (int i=0;i<resourceMetadataInfoCount ;i++) {
			rmi = resourceMetadataInfos.get(i);
			cell = headRow.createCell(i);
			cell.setCellValue(rmi.getDescName());
		}
	}
	
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
		String excelFileSavePath = BuiltinResourceInstance.getInstance("SysFileService", SysFileService.class).validSaveFileDirIsExists(buildInType)+fileCode+"."+suffix;
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
		excelFile.setActName(DateUtil.formatDatetime(new Date()) + "_" + fileName + "." + suffix);
		excelFile.setCode(fileCode);
		excelFile.setSuffix(suffix);
		excelFile.setSavePath(excelFileSavePath);
		excelFile.setSaveType(SysFileConstants.saveType);
		excelFile.setBatch(ResourceHandlerUtil.getBatchNum());
		excelFile.setBuildInType(buildInType);
		return HibernateUtil.saveObject(excelFile, null);
	}
	
	// --------------------------------------------------------------------------------------------------
	/**
	 * 导入excel
	 * @param importExcel
	 * @return
	 */
	public Object importExcel(ImportFile importFile) {
		SysFile file = importFile.getImportFile();
		Object wb = PoiExcelUtil.getReadWorkBookInstance(file.getSavePath(), file.getSuffix());
		if(wb instanceof String){
			return wb;
		}
		Workbook workbook = (Workbook) wb;
		
		int rowCount = 0;// 行数
		Sheet sheet = workbook.getSheetAt(0);
		rowCount = sheet.getLastRowNum()+1;
		if(sheet.getRow(1) != null){
			String resourceName = importFile.getResourceName();
			
			String desc = "导入excel文件["+file.getActName()+"]，";
			int i, j, columnIndex=0;
			List<ResourceMetadataInfo> resourceMetadataInfos = importFile.getResourceMetadataInfos();
			IJson ijson = new JSONArrayExtend(rowCount-1);;
			JSONObject json = null;
			String validResult = null;
			
			Row row = null;// excel行对象
			Cell cell;// excel列对象
			short columnCount = 0; // 列数
			
			for(i=1;i<rowCount;i++){
				row = sheet.getRow(i);
				if(row != null){
					columnCount = sheet.getRow(i).getLastCellNum();
					if(columnCount > resourceMetadataInfos.size()){
						return "第"+(i+1)+"行数据的列数量("+columnCount+"个)大于资源["+resourceName+"]配置的导入字段数量("+resourceMetadataInfos.size()+"个)，系统无法匹配，请调整配置，或sheet中的列";
					}
					json = new JSONObject(columnCount);
					ijson.add(json);
					
					for (j=0; j<columnCount; j++) {
						cell = row.getCell(j);
						if(cell != null){
							json.put(resourceMetadataInfos.get(columnIndex).getPropName(), getCellValue(cell));
						}
						columnIndex++;
					}
					columnIndex=0;
				}
			}
			validResult = validImportDatas(desc , resourceName, ijson, resourceMetadataInfos);
			if(validResult != null){
				return validResult;
			}
			saveImportDatas(resourceName, ijson);
		}
		
		return importFile;
	}
	
	/**
	 * 获得单元格中的数据值
	 * @param cell
	 * @return
	 */
	private Object getCellValue(Cell cell) {
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC && org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)){
			return cell.getDateCellValue();
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell.getStringCellValue();
	}

	/**
	 * 验证导入的数据
	 * @param desc
	 * @param resourceName
	 * @param ijson
	 * @param resourceMetadataInfos 
	 * @return
	 */
	private String validImportDatas(String desc, String resourceName, IJson ijson, List<ResourceMetadataInfo> resourceMetadataInfos) {
		int size = ijson.size();
		
		int columnIndex =1;
		Set<ResourceMetadataInfo> uniqueConstraintProps = new HashSet<ResourceMetadataInfo>(resourceMetadataInfos.size());
		JSONObject data = null;
		Object dataIdValue = null;
		boolean dataValueIsNull;
		Object dataValue = null;
		String validDataIsLegalResult = null;
		for(int i=0;i<size;i++){
			data = ijson.get(i);
			dataIdValue = data.get(ResourcePropNameConstants.ID);
			
			for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
				dataValue = data.get(rmi.getPropName());
				dataValueIsNull = StrUtils.isEmpty(dataValue);
				
				// 验证不能为空
				if(rmi.getIsNullabled() == 0 && dataValueIsNull){
					return desc + "第"+(i+2)+"行，第"+columnIndex+"列["+rmi.getDescName()+"]的数据值不能为空";
				}
				
				if(!dataValueIsNull){
					validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(dataValue, rmi);
					if(validDataIsLegalResult != null){
						return desc+"第"+(i+2)+"行，第"+columnIndex+"列" + validDataIsLegalResult;
					}
					
					// 验证唯一约束
					if(rmi.getIsUnique() == 1){
						uniqueConstraintProps.add(rmi);
						if(validDataIsExists(resourceName, rmi.getPropName(), dataValue, dataIdValue)){
							return desc+"第"+(i+2)+"行，第"+columnIndex+"列["+rmi.getDescName()+"]的数据值已经存在，不能重复添加";
						}
					}
				}
				columnIndex++;
			}
			columnIndex=1;
		}
		
		// 验证一次提交的数组中，是否有重复的值，违反了唯一约束
		if(size > 1 && uniqueConstraintProps.size()>0){
			for (ResourceMetadataInfo uniqueConstraintProp : uniqueConstraintProps) {
				for(int i=0;i<size-1;i++){
					dataValue = ijson.get(i).get(uniqueConstraintProp.getPropName());
					if(StrUtils.notEmpty(dataValue)){
						for(int j=i+1;j<size;j++){
							if(dataValue.equals(ijson.get(j).get(uniqueConstraintProp.getPropName()))){
								return desc+"第"+(i+2)+"行和第"+(j+2)+"行的，第"+columnIndex+"列数据["+uniqueConstraintProp.getDescName()+"]值重复，导入失败";
							}
						}
					}
				}
			}
		}
		
		if(uniqueConstraintProps.size() > 0){
			uniqueConstraintProps.clear();
		}
		return null;
	}
	
	/**
	 * 验证数据是否已经存在
	 * @param resourceName
	 * @param propName
	 * @param dataValue
	 * @param dataIdValue 
	 * @return
	 */
	private boolean validDataIsExists(String resourceName, String propName, Object dataValue, Object dataIdValue) {
		Object id = HibernateUtil.executeUniqueQueryByHqlArr("select "+ResourcePropNameConstants.ID+" from " + resourceName + " where " + propName + "=? and projectId=? and customerId=?", dataValue, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		return (id != null);
	}
	
	/**
	 * 保存导入的数据数据
	 * @param resourceName
	 * @param ijson
	 */
	private void saveImportDatas(String resourceName, IJson ijson) {
		int size = ijson.size();
		if(size > 0){
			for(int i =0;i<size;i++){
				HibernateUtil.saveObject(resourceName, ijson.get(i), null);
			}
		}
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
		createHeadRow(headRow, importFileTemplate.getResourceMetadataInfos(), importFileTemplate.getResourceMetadataInfos().size());
		
		return createExcelFile(workbook, importFileTemplate.getResourceName(), suffix, importFileTemplate.getFileId(), SysFileConstants.BUILD_IN_TYPE_IMPORT_TEMPLATE);
	}
	
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
		String title = "tmp title 这是一个临时标题";
		
		Workbook workbook = (Workbook) wb;
		Sheet sheet = workbook.createSheet();
		
		List<ResourceMetadataInfo> resourceMetadataInfos = exportFile.getResourceMetadataInfos();
		int resourceMetadataInfoCount = resourceMetadataInfos.size();
		
		createTitleRow(sheet, title, resourceMetadataInfos.size());
		
		Row headRow = sheet.createRow(1);
		createHeadRow(headRow, resourceMetadataInfos, resourceMetadataInfoCount);
		
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
			validDataListResult = validDataList(dataList, resourceMetadataInfoCount, data, dataArr, dataMap);
			if(validDataListResult != null){
				return validDataListResult;
			}
			
			for (Object object : dataList) {
				row = sheet.createRow(rowNum++);
				if(object instanceof Object[]){
					dataArr = (Object[]) object;
					size = dataArr.length;
					for(int j=0;j<size;j++){
						createCell(j, row, cell, dataArr[j], resourceMetadataInfos.get(j));
					}
				}else if(object instanceof Map){
					dataMap = (Map<String, Object>) object;
					size = dataMap.size();
					for(int j=0;j<size;j++){
						createCell(j, row, cell, dataMap.get(resourceMetadataInfos.get(j).getPropName()), resourceMetadataInfos.get(j));
					}
				}else if(object instanceof Object){
					createCell(1, row, cell, object, resourceMetadataInfos.get(0));
				}
			}
		}
		return createExcelFile(workbook, title, suffix, exportFile.getFileId(), SysFileConstants.BUILD_IN_TYPE_EXPORT);
	}

	/**
	 * 创建标题行
	 * @param sheet
	 * @param title
	 * @param colspanLength 和并列的数量
	 */
	private void createTitleRow(Sheet sheet, String title, int colspanLength){
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colspanLength-1));
		Row titleRow = sheet.createRow(0);
		Cell cell = titleRow.createCell(0);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(title);
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
	 * 创建列
	 * @param columnIndex
	 * @param row
	 * @param cell
	 * @param object
	 * @param resourceMetadataInfo
	 */
	private void createCell(int columnIndex, Row row, Cell cell, Object object, ResourceMetadataInfo resourceMetadataInfo) {
		cell = row.createCell(columnIndex);
		if(StrUtils.isEmpty(object)){
			cell.setCellType(Cell.CELL_TYPE_BLANK);
			return;
		}else if(resourceMetadataInfo == null){
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}else if(DataTypeConstants.INTEGER.equals(resourceMetadataInfo.getDataType()) || DataTypeConstants.DOUBLE.equals(resourceMetadataInfo.getDataType())){
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		}else if(DataTypeConstants.INTEGER.equals(resourceMetadataInfo.getDataType())){
			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
		}else{
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		cell.setCellValue(object.toString());
	}
}
