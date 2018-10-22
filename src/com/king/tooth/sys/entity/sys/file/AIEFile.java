package com.king.tooth.sys.entity.sys.file;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ie.IEResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ie.IETableResourceMetadataInfo;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 导入导出文件父类
 * <p>Import Export File</p>
 * @author DougLei
 */
public abstract class AIEFile {
	
	/**
	 * 资源元数据信息
	 */
	protected List<IEResourceMetadataInfo> ieResourceMetadataInfos;
	public List<IEResourceMetadataInfo> getIeResourceMetadataInfos() {
		return ieResourceMetadataInfos;
	}
	
	/**
	 * 操作的资源名
	 */
	protected String resourceName;
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public void clear(){
		if(ieResourceMetadataInfos != null){
			ieResourceMetadataInfos.clear();
		}
	}
	
	/**
	 * 关联的文件id
	 */
	protected String fileId;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	/**
	 * 是否是支持的文件后缀
	 * @param fileSuffix
	 * @return
	 */
	protected boolean isSupportFileSuffix(String fileSuffix){
		for (String supportFileSuffix : supportFileSuffixArray) {
			if(supportFileSuffix.equals(fileSuffix)){
				return true;
			}
		}
		return false;
	}
	/** 系统目前支持的文件后缀 */
	protected static final String[] supportFileSuffixArray = {"xls", "xlsx"};
	
	/**
	 * 获得要[导入/导出]的资源，元数据信息集合
	 * @param resourceName
	 * @param isImport 是否导入，不是导入，就是导出
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getIEResourceMetadataInfos(SysResource resource, String resourceName, int isImport){
		if(resource == null){
			resource = BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).findResourceByResourceName(resourceName);
		}
		
		Object obj = null;
		if(resource.isTableResource()){
			obj = getIETableResourceMetadataInfos(resource, isImport);
		}else if(resource.isSqlResource()){
			if(isImport == 1){
				return "系统目前只支持查询表资源的导入元数据信息";
			}
			obj = getIESqlExportMetadataInfos(resource);
		}else{
			return "系统目前只支持表资源的导入、导出操作，以及sql资源的导出操作";
		}
		if(obj == null){
			return "[AIEFile.getIEResourceMetadataInfos()]中的obj参数值为null，请联系后端系统开发人员";
		}
		if(obj instanceof String){
			return obj;
		}
		List<IEResourceMetadataInfo> ieResourceMetadataInfos = (List<IEResourceMetadataInfo>) obj;
		if(ieResourceMetadataInfos == null || ieResourceMetadataInfos.size() == 0){
			return "没有查询到名为["+resourceName+"]资源的"+(isImport==1?"导入":"导出")+"元数据信息集合，请检查配置，或联系后端系统开发人员";
		}
		return ieResourceMetadataInfos;
	}
	
	/**
	 * 查询导入导出时，表资源的元数据信息集合
	 * @param resource
	 * @param isImport 
	 * @return
	 */
	private List<IEResourceMetadataInfo> getIETableResourceMetadataInfos(SysResource resource, int isImport){
		List<IEResourceMetadataInfo> ieResourceMetadataInfos = null;
		String resourceId = resource.getRefResourceId();
		String resourceName = resource.getResourceName();
		
		if(resource.isBuiltinResource()){
			ieResourceMetadataInfos = getIEBuiltinTableResourceMetadataInfos(resourceName, isImport);
		}else{
			String hql = null;
			if(isImport == 1){
				hql = queryTableImportMetadataInfosHql;
			}else{
				hql = queryTableExportMetadataInfosHql;
			}
			ieResourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(IEResourceMetadataInfo.class, null, null, hql, resourceId);
		}
		return ieResourceMetadataInfos;
	}
	/** 查询表资源配置的导入导出元数据信息集合hql头 */
	private static final String queryTableIEMetadataInfosHqlHead = "select new map(columnName as columnName,propName as propName,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName) from CfgColumn where tableId=? and isEnabled=1 and operStatus="+CfgColumn.CREATED;
	/** 查询表资源配置的导入元数据信息集合的hql */
	private static final String queryTableImportMetadataInfosHql = queryTableIEMetadataInfosHqlHead + " and isImport=1 order by importOrderCode asc";
	/** 查询表资源配置的导出元数据信息集合的hql */
	private static final String queryTableExportMetadataInfosHql = queryTableIEMetadataInfosHqlHead + " and isExport=1 order by exportOrderCode asc";
	
	/**
	 * 获取内置表资源的元数据信息集合
	 * @param tableResourceName
	 * @param isImport
	 * @return
	 */
	private static List<IEResourceMetadataInfo> getIEBuiltinTableResourceMetadataInfos(String tableResourceName, int isImport){
		ITable itable = BuiltinResourceInstance.getInstance(tableResourceName, ITable.class);
		List<CfgColumn> columns = itable.getColumnList();
		List<IEResourceMetadataInfo> ieResourceMetadataInfos = new ArrayList<IEResourceMetadataInfo>(columns.size());
		for (CfgColumn column : columns) {
			if((isImport == 1 && (column.getIsImport() != null && column.getIsImport() == 1))
					|| (isImport == 0 && (column.getIsExport() != null && column.getIsExport() == 1))){
				ieResourceMetadataInfos.add(new IETableResourceMetadataInfo(
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
		return ieResourceMetadataInfos;
	}
	
	/**
	 * 获取sql语句导出的元数据信息集合
	 * @param resource
	 * @return
	 */
	private Object getIESqlExportMetadataInfos(SysResource resource) {
		String resourceId = resource.getRefResourceId();
		Object sqlType = HibernateUtil.executeUniqueQueryByHqlArr("select sqlScriptType from ComSqlScript where "+ResourcePropNameConstants.ID+"=?", resourceId);
		if(sqlType == null){
			return "没有查询到名为["+resource.getResourceName()+"]的sql资源，请联系后台系统开发人员";
		}
		if(!sqlType.equals(SqlStatementTypeConstants.SELECT)){
			return"系统只支持查询select类型的sql语句，配置的导出元数据信息";
		}
		
		List<IEResourceMetadataInfo> ieResourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(IEResourceMetadataInfo.class, null, null, querySqlExportMetadataInfosHql, resourceId);
		return ieResourceMetadataInfos;
	}
	/** 查询sql资源配置的导出元数据信息集合的hql */
	private static final String querySqlExportMetadataInfosHql = "select new map(columnName as columnName,propName as propName, descName as descName) from CfgSqlResultset where sqlScriptId=? and isExport=1 order by exportOrderCode asc";
}
