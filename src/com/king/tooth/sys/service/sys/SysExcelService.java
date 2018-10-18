package com.king.tooth.sys.service.sys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SysFileConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.JSONArrayExtend;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysFile;
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
	 * @param buildInType
	 * @return
	 */
	private Object createExcelFile(Workbook workbook, String fileName, String suffix, int buildInType){
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
		
		SysFile importExcelFileTemplate = new SysFile();
		importExcelFileTemplate.setActName("【"+fileName+"】excel"+DateUtil.formatDatetime(new Date()) + "." + suffix);
		importExcelFileTemplate.setCode(fileCode);
		importExcelFileTemplate.setSuffix(suffix);
		importExcelFileTemplate.setSavePath(excelFileSavePath);
		importExcelFileTemplate.setSaveType(SysFileConstants.saveType);
		importExcelFileTemplate.setBatch(ResourceHandlerUtil.getBatchNum());
		importExcelFileTemplate.setBuildInType(buildInType);
		return HibernateUtil.saveObject(importExcelFileTemplate, null);
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
	 * <p>将生成为excel导入模版保存到服务器上，并在sysfile中插入一条数据</p>
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
		Row row = sheet.createRow(0);
		Cell cell;
		
		List<ResourceMetadataInfo> resourceMetadataInfos = importFileTemplate.getResourceMetadataInfos();
		int size = resourceMetadataInfos.size();
		ResourceMetadataInfo rmi = null;
		for (int i=0;i<size ;i++) {
			rmi = resourceMetadataInfos.get(i);
			cell = row.createCell(i+1);
			cell.setCellValue(rmi.getDescName());
		}
		
		return createExcelFile(workbook, importFileTemplate.getResourceName(), suffix, SysFileConstants.BUILD_IN_TYPE_IMPORT_TEMPLATE);
	}
	
	// ---------------------------------------------------------------------
	/**
	 * 导出excel
	 * <p>将生成为导出excel文件保存到服务器上，并在sysfile中插入一条数据</p>
	 * @param request
	 * @return
	 */
	public Object exportExcel(HttpServletRequest request) {
		
		
		Workbook workbook = null;
		String fileName = null;
		String suffix = null;
		return createExcelFile(workbook, fileName, suffix, SysFileConstants.BUILD_IN_TYPE_EXPORT);
	}
}
