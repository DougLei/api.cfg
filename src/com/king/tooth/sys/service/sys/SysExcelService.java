package com.king.tooth.sys.service.sys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.JSONArrayExtend;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.sys.file.ImportFile;
import com.king.tooth.sys.entity.sys.file.ImportFileTemplate;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.TableResourceMetadataInfo;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
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
	 * 获得导入excel的表资源元数据信息集合
	 * @param resourceName
	 * @return
	 */
	private List<ResourceMetadataInfo> getImportExcelTableResourceMetadataInfos(String resourceName){
		SysResource resource = BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).findResourceByResourceName(resourceName);
		if(resource.isTableResource()){
			return getTableResourceMetadataInfos(resource, 1);
		}else{
			throw new IllegalArgumentException("系统目前只支持表资源的excel导入");
		}
	}
	
	/**
	 * 查询导入导出时，表资源的元数据信息集合
	 * @param resource
	 * @param isImport 是否导入，如果不是导入，就是导出
	 * @return
	 */
	private List<ResourceMetadataInfo> getTableResourceMetadataInfos(SysResource resource, int isImport){
		List<ResourceMetadataInfo> resourceMetadataInfos = null;
		String resourceId = resource.getRefResourceId();
		String resourceName = resource.getResourceName();
		
		if(resource.isBuiltinResource()){
			resourceMetadataInfos = getBuiltinTableResourceMetadataInfos(resourceName, isImport);
		}else{
			String hql = null;
			if(isImport == 1){
				hql = queryTableImportMetadataInfosHql;
			}else{
				hql = queryTableExportMetadataInfosHql;
			}
			resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, hql, resourceId);
			if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
				throw new NullPointerException("没有查询到表资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
			}
		}
		return resourceMetadataInfos;
	}
	/** 查询表资源配置的导入excel的元数据信息集合的hql */
	private static final String queryTableImportMetadataInfosHql = ResourceHandlerUtil.queryTableMetadataInfosHqlHead + " and isImportExcel=1 order by importExcelOrderCode asc";
	/** 查询表资源配置的导出excel的元数据信息集合的hql */
	private static final String queryTableExportMetadataInfosHql = ResourceHandlerUtil.queryTableMetadataInfosHqlHead + " and isExportExcel=1 order by exportExcelOrderCode asc";
	
	/**
	 * 获取内置表资源的元数据信息集合
	 * @param tableResourceName
	 * @param isImport
	 * @return
	 */
	private static List<ResourceMetadataInfo> getBuiltinTableResourceMetadataInfos(String tableResourceName, int isImport){
		ITable itable = BuiltinResourceInstance.getInstance(tableResourceName, ITable.class);
		List<ComColumndata> columns = itable.getColumnList();
		List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(columns.size());
		for (ComColumndata column : columns) {
			if((isImport == 1 && (column.getIsImport() != null && column.getIsImport() == 1))
					|| (isImport == 0 && (column.getIsExport() != null && column.getIsExport() == 1))){
				metadataInfos.add(new TableResourceMetadataInfo(
						column.getColumnName(),
						column.getColumnType(),
						column.getLength(),
						column.getPrecision(),
						column.getIsUnique(), 
						column.getIsNullabled(),
						column.getPropName(),
						column.getName()));
			}
		}
		columns.clear();
		return metadataInfos;
	}
	
	// ----------------------------------------------------------------------------------
	/**
	 * 导入excel
	 * @param importExcel
	 * @return
	 */
	public Object importExcel(ImportFile importFile) {
		SysFile file = getObjectById(importFile.getFileId(), SysFile.class);
		Object wb = PoiExcelUtil.getWorkBookInstance(file.getSavePath(), file.getSuffix());
		if(wb instanceof String){
			return wb;
		}
		Workbook workbook = (Workbook) wb;
		
		String resourceName = importFile.getResourceName();
		int i, j, columnIndex=0;
		List<ResourceMetadataInfo> resourceMetadataInfos = null;
		IJson ijson = null;
		JSONObject json = null;
		String validResult = null;
		
		Row row = null;// excel行对象
		Cell cell;// excel列对象
		int rowCount = 0;// 行数
		short columnCount = 0; // 列数
		
		Sheet sheet = workbook.getSheetAt(0);
		rowCount = sheet.getLastRowNum()+1;
		if(sheet.getRow(1) != null){
			resourceMetadataInfos = getImportExcelTableResourceMetadataInfos(resourceName);
			ijson = new JSONArrayExtend(rowCount-1);
			
			for(i=1;i<rowCount;i++){
				row = sheet.getRow(i);
				if(row != null){
					columnCount = sheet.getRow(i).getLastCellNum();
					if(columnCount > resourceMetadataInfos.size()){
						return "导入excel文件，第"+(i+1)+"行数据的列数量("+columnCount+"个)大于资源["+resourceName+"]配置的导入字段数量("+resourceMetadataInfos.size()+"个)，系统无法匹配，请调整配置，或sheet中的列";
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
			validResult = validImportDatas(resourceName, ijson, resourceMetadataInfos);
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
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
			return cell.getDateCellValue();
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell.getStringCellValue();
	}

	/**
	 * 验证导入的数据
	 * @param resourceName
	 * @param ijson
	 * @param resourceMetadataInfos 
	 * @return
	 */
	private String validImportDatas(String resourceName, IJson ijson, List<ResourceMetadataInfo> resourceMetadataInfos) {
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
					return "导入excel文件，第"+(i+2)+"行，第"+columnIndex+"列["+rmi.getDescName()+"]的数据值不能为空";
				}
				
				if(!dataValueIsNull){
					validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(dataValue, rmi);
					if(validDataIsLegalResult != null){
						return "导入excel文件，第"+(i+2)+"行，第"+columnIndex+"列" + validDataIsLegalResult;
					}
					
					// 验证唯一约束
					if(rmi.getIsUnique() == 1){
						uniqueConstraintProps.add(rmi);
						if(validDataIsExists(resourceName, rmi.getPropName(), dataValue, dataIdValue)){
							return "导入excel文件，第"+(i+2)+"行，第"+columnIndex+"列["+rmi.getDescName()+"]的数据值已经存在，不能重复添加";
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
								return "导入excel文件，第"+(i+2)+"行和第"+(j+2)+"行的，第"+columnIndex+"列数据["+uniqueConstraintProp.getDescName()+"]值重复，导入失败";
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
	 * @param importFileTemplates
	 * @return
	 */
	public Object createImportExcelTemplate(ImportFileTemplate importFileTemplates) {
		getImportExcelTableResourceMetadataInfos(importFileTemplates.getResourceName());
		
		return null;
	}
	
	// ---------------------------------------------------------------------
	/**
	 * 导出excel
	 * @param request
	 * @return
	 */
	public Object exportExcel(HttpServletRequest request) {
		return null;
	}
}
