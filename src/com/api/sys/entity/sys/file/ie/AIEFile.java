package com.api.sys.entity.sys.file.ie;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.constants.ResourceInfoConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.entity.ITable;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgPropExtendConf;
import com.api.sys.entity.cfg.CfgResource;
import com.api.sys.entity.tools.resource.metadatainfo.ie.IEResourceMetadataInfo;
import com.api.sys.entity.tools.resource.metadatainfo.ie.IETableResourceMetadataInfo;
import com.api.sys.service.cfg.CfgResourceService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.hibernate.HibernateUtil;

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
	
	/**
	 * 要操作的资源类型
	 */
	private int resourceType;
	/**
	 * 是否是表资源
	 * @return
	 */
	public boolean isTableResource(){
		return resourceType == ResourceInfoConstants.TABLE;
	}
	/**
	 * 是否是sql资源
	 * @return
	 */
	public boolean isSqlResource(){
		return resourceType == ResourceInfoConstants.SQL;
	}
	
	
	@JSONField(serialize = false)
	public String getId(){
		return null;
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
	public static final String[] supportFileSuffixArray = {"xls", "xlsx"};
	
	/**
	 * 获得要[导入/导出]的资源，元数据信息集合
	 * @param resourceName
	 * @param isImport 是否导入，不是导入，就是导出
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getIEResourceMetadataInfos(CfgResource resource, String resourceName, int isImport){
		if(resource == null){
			resource = BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).findResourceByResourceName(resourceName);
		}
		
		Object obj = null;
		if(resource.isTableResource()){
			resourceType = ResourceInfoConstants.TABLE;
			obj = getIETableResourceMetadataInfos(resource, isImport);
		}else if(resource.isSqlResource()){
			resourceType = ResourceInfoConstants.SQL;
			obj = getIESqlExportMetadataInfos(resource, isImport);
		}else{
			return "系统目前只支持表资源的导入、导出操作，以及sql资源的导入、导出操作";
		}
		if(obj == null){
			return ((isImport==1)?"导入":"导出") + "数据时，没有查询到资源["+resourceName+"]的元数据信息，请联系后端系统开发人员";
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
	private List<IEResourceMetadataInfo> getIETableResourceMetadataInfos(CfgResource resource, int isImport){
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
	/** 查询表资源元数据信息集合的hql头 */
	private static final String queryTableMetadataInfosHqlHead = "select new map("+ResourcePropNameConstants.ID+" as id,columnName as columnName,propName as propName,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName, isIgnoreValid as isIgnoreValid) from CfgColumn where tableId=? and operStatus="+CfgColumn.CREATED;
	/** 查询表资源配置的导入元数据信息集合的hql */
	private static final String queryTableImportMetadataInfosHql = queryTableMetadataInfosHqlHead + " and isImport=1 order by importOrderCode asc";
	/** 查询表资源配置的导出元数据信息集合的hql */
	private static final String queryTableExportMetadataInfosHql = queryTableMetadataInfosHqlHead + " and isExport=1 order by exportOrderCode asc";
	
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
									column.getIsIgnoreValid(),
									ResourceInfoConstants.BUILTIN_RESOURCE,
									column.getIeConfExtend(),
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
	 * @param isImport 
	 * @return
	 */
	private Object getIESqlExportMetadataInfos(CfgResource resource, int isImport) {
		String resourceId = resource.getRefResourceId();
		Object sqlType = HibernateUtil.executeUniqueQueryByHqlArr("select type from CfgSql where "+ResourcePropNameConstants.ID+"=?", resourceId);
		if(sqlType == null){
			return "没有查询到名为["+resource.getResourceName()+"]的sql资源，请联系后台系统开发人员";
		}
		String hql = null;
		if(isImport == 1){
			if(!sqlType.equals(SqlStatementTypeConstants.PROCEDURE)){
				return "导入时，系统只支持procedure类型的sql语句";
			}
			hql = querySqlImportMetadataInfosHql;
		}
		if(isImport == 0){
			if(!sqlType.equals(SqlStatementTypeConstants.SELECT)){
				return "导出时，系统只支持select类型的sql语句";
			}
			hql = querySqlExportMetadataInfosHql;
		}
		return HibernateUtil.extendExecuteListQueryByHqlArr(IEResourceMetadataInfo.class, null, null, hql, resourceId);
	}
	/** 查询sql资源配置的导入元数据信息集合的hql */
	private static final String querySqlImportMetadataInfosHql = "select new map("+ResourcePropNameConstants.ID+" as id,name as columnName,name as propName, remark as descName, dataType as dataType, length as length, precision as precision, isNullabled as isNullabled) from CfgSqlParameter where sqlScriptId=? order by orderCode asc";
	/** 查询sql资源配置的导出元数据信息集合的hql */
	private static final String querySqlExportMetadataInfosHql = "select new map("+ResourcePropNameConstants.ID+" as id,columnName as columnName,propName as propName, descName as descName) from CfgSqlResultset where sqlScriptId=? and isExport=1 order by exportOrderCode asc";
	
	// -------------------------------------------------------------------------------
	/**
	 * 有扩展配置的资源元数据数量
	 */
	@JSONField(serialize = false)
	private int resourceMetadataInfoOfConfExtendCount;
	public int getResourceMetadataInfoOfConfExtendCount() {
		return resourceMetadataInfoOfConfExtendCount;
	}
	
	/**
	 * 设置资源元数据的扩展信息
	 * @param ieResourceMetadataInfos
	 */
	protected void setResourceMetadataExtendInfo(List<IEResourceMetadataInfo> ieResourceMetadataInfos) {
		for (IEResourceMetadataInfo ieResourceMetadataInfo : ieResourceMetadataInfos) {
			if(!ResourceInfoConstants.BUILTIN_RESOURCE.equals(ieResourceMetadataInfo.getId())){
				ieResourceMetadataInfo.setIeConfExtend(HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgPropExtendConf.class, queryPropIEConfExtendInfoHql, ieResourceMetadataInfo.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId()));
			}
			if(ieResourceMetadataInfo.getIeConfExtend() != null){
				resourceMetadataInfoOfConfExtendCount++;
			}
		}
	}
	/** 查询属性配置的扩展的导入/导出信息对象hql */
	private static final String queryPropIEConfExtendInfoHql = "from CfgPropExtendConf where refPropId=? and projectId=? and customerId=?";
	
}
