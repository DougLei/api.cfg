package com.king.tooth.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.SqlResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.TableResourceMetadataInfo;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.build.model.DynamicBasicColumnUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 资源工具类
 * @author DougLei
 */
public class ResourceHandlerUtil {
	
	/**
	 * 获取唯一标识id
	 * @return
	 */
	public static String getIdentity(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 获取token值
	 * @return
	 */
	public static String getToken() {
		return getIdentity();
	}
	
	/**
	 * 获取登录密码的密钥
	 * @return
	 */
	public static String getLoginPwdKey() {
		return getIdentity();
	}
	
	/**
	 * 获取批次编号
	 */
	public static String getBatchNum(){
		return getIdentity();
	}
	
	/**
	 * 获取随机数
	 * @param seed
	 * @return
	 */
	public static int getRandom(int seed) {
		return ThreadLocalRandom.current().nextInt(seed);
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 给对象设置基础属性值
	 * @param entity
	 * @param currentAccountId
	 * @param projectId
	 * @param customerId
	 * @param currentDate
	 */
	public static void setBasicPropVals(BasicEntity entity, String currentAccountId, String projectId, String customerId, Date currentDate) {
		if(StrUtils.isEmpty(entity.getId())){
			entity.setId(getIdentity());
		}
		entity.setCreateUserId(currentAccountId);
		entity.setLastUpdateUserId(currentAccountId);
		entity.setProjectId(projectId);
		entity.setCustomerId(customerId);
		entity.setCreateDate(currentDate);
		entity.setLastUpdateDate(currentDate);
	}
	
	/**
	 * 保存数据时，初始化基本属性值
	 * @param entityName 
	 * @param json
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 */
	public static void initBasicPropValsForSave(String entityName, Map<String, Object> data, String shortDesc) {
		data.put("projectId", CurrentThreadContext.getProjectId());
		data.put("customerId", CurrentThreadContext.getCustomerId());
		
		// 当没有id值的时候，再赋予id值
		if(StrUtils.isEmpty(data.get(ResourcePropNameConstants.ID))){
			data.put(ResourcePropNameConstants.ID, getIdentity());
		}
		if(!entityName.endsWith("Links")){// 不是关系表，才要这些值
			Date currentDate = new Date();
			data.put("createDate", currentDate);
			data.put("lastUpdateDate", currentDate);
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccountOnlineStatus() != null){
				String currentAccountId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
				data.put("createUserId", currentAccountId);
				data.put("lastUpdateUserId",  currentAccountId);
			}else{
				data.put("createUserId", shortDesc);
				data.put("lastUpdateUserId",  shortDesc);
			}
		}
	}
	
	/**
	 * 修改数据时，初始化基本属性值
	 * 包括ID，CreateTime等
	 * @param entityName
	 * @param data
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 */
	public static void initBasicPropValsForUpdate(String entityName, Map<String, Object> data, String shortDesc) {
		if(!entityName.endsWith("Links")){// 不是关系表，才要修改这些值
			data.put("lastUpdateDate",  new Date());
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccountOnlineStatus() != null){
				data.put("lastUpdateUserId",  CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
			}else{
				data.put("lastUpdateUserId",  shortDesc);
			}
		}
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取关联关系jsonObject对象
	 * @param leftId
	 * @param rightId
	 * @param orderCode
	 * @param leftResourceName
	 * @param rightResourceName
	 * @return
	 */
	public static JSONObject getDataLinksObject(String leftId, String rightId, String orderCode, String leftResourceName, String rightResourceName){
		JSONObject dataLinks = new JSONObject(6);
		dataLinks.put("leftId", leftId);
		dataLinks.put("rightId", rightId);
		dataLinks.put("orderCode", orderCode);
		if(StrUtils.notEmpty(leftResourceName)){
			dataLinks.put("leftResourceName", leftResourceName);
		}
		if(StrUtils.notEmpty(rightResourceName)){
			dataLinks.put("rightResourceName", rightResourceName);
		}
		return dataLinks;
	}

	// ------------------------------------------------------------------------------------------
	/**
	 * 初始化配置参数值
	 * @param configKey
	 * @param defaultValue
	 * @return
	 */
	public static String initConfValue(String configKey, String defaultValue){
		String confValue = SysConfig.getSystemConfig(configKey);
		if(StrUtils.isEmpty(confValue)){
			confValue = defaultValue;
		}
		return confValue;
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 清除表信息
	 * @param tables
	 */
	public static void clearTables(List<ComTabledata> tables){
		if(tables != null && tables.size() > 0){
			for (ComTabledata table : tables) {
				table.clear();
			}
			tables.clear();
		}
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取表资源的元数据信息集合
	 * @param resource
	 * @param operType 1:数据验证、2:excel导入、3:excel导出
	 * @return
	 */
	public static List<ResourceMetadataInfo> getTableResourceMetadataInfos(SysResource resource, int operType){
		List<ResourceMetadataInfo> resourceMetadataInfos = null;
		String resourceId = resource.getRefResourceId();
		String resourceName = resource.getResourceName();
		
		if(resource.isBuiltinResource()){
			resourceMetadataInfos = getBuiltinTableResourceMetadataInfos(resourceName, operType);
		}else{
			String hql = null;
			if(operType == 1){
				hql = queryTableMetadataInfosHql;
			}else if(operType == 2){
				hql = queryTableImportMetadataInfosHql;
			}else if(operType == 3){
				hql = queryTableExportMetadataInfosHql;
			}
			resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, hql, resourceId);
			if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
				throw new NullPointerException("没有查询到表资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
			}
			if(operType == 1){
				DynamicBasicColumnUtil.initBasicMetadataInfos(resourceName, resourceMetadataInfos);
			}
		}
		return resourceMetadataInfos;
	}
	private static final String queryTableMetadataInfosHqlHead = "select new map(columnName as columnName,propName as propName,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName) from ComColumndata where tableId=? and isEnabled=1 and operStatus="+ComColumndata.CREATED;
	/** 查询表资源元数据信息集合的hql */
	private static final String queryTableMetadataInfosHql = queryTableMetadataInfosHqlHead + " order by orderCode asc";
	/** 查询表资源配置的导入excel的元数据信息集合的hql */
	private static final String queryTableImportMetadataInfosHql = queryTableMetadataInfosHqlHead + " and isImportExcel=1 order by importExcelOrderCode asc";
	/** 查询表资源配置的导出excel的元数据信息集合的hql */
	private static final String queryTableExportMetadataInfosHql = queryTableMetadataInfosHqlHead + " and isExportExcel=1 order by exportExcelOrderCode asc";
	
	/**
	 * 获取内置表资源的元数据信息集合
	 * @param tableResourceName
	 * @param operType @see getTableResourceMetadataInfos() operType 
	 * @return
	 */
	private static List<ResourceMetadataInfo> getBuiltinTableResourceMetadataInfos(String tableResourceName, int operType){
		ITable itable = BuiltinResourceInstance.getInstance(tableResourceName, ITable.class);
		List<ComColumndata> columns = itable.getColumnList();
		List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(columns.size());
		for (ComColumndata column : columns) {
			if((operType == 1)
					|| (operType == 2 && (column.getIsImportExcel() != null && column.getIsImportExcel() == 1))
					|| (operType == 3 && (column.getIsExportExcel() != null && column.getIsExportExcel() == 1))){
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
		if(operType == 1){
			DynamicBasicColumnUtil.initBasicMetadataInfos(tableResourceName, metadataInfos);
		}
		columns.clear();
		return metadataInfos;
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取sql资源的参数元数据信息集合
	 * @param sqlParams
	 * @return
	 */
	public static List<ResourceMetadataInfo> getSqlResourceParamsMetadataInfos(List<ComSqlScriptParameter> sqlParams){
		List<ResourceMetadataInfo> metadataInfos = null;
		if(sqlParams != null && sqlParams.size() > 0){
			metadataInfos = new ArrayList<ResourceMetadataInfo>(sqlParams.size());
			for (ComSqlScriptParameter sqlParam : sqlParams) {
				metadataInfos.add(new SqlResourceMetadataInfo(
						null,
						sqlParam.getParameterDataType(),
						sqlParam.getLength(),
						sqlParam.getPrecision(),
						0, // sql脚本参数不需要唯一约束
						0, // sql脚本参数不能为空
						sqlParam.getParameterName(),
						sqlParam.getRemark()));
			}
		}
		return metadataInfos;
	}
	
	/**
	 * 获取sql资源的传入表对象参数的元数据信息集合
	 * <p>主要针对procedure</p>
	 * @param sql
	 * @return
	 */
	public static List<List<ResourceMetadataInfo>> getSqlInResultSetMetadataInfoList(ComSqlScript sql){
		if(SqlStatementTypeConstants.PROCEDURE.equals(sql.getSqlScriptType())){
			List<CfgSqlResultset> inSqlResultsets = sql.getInSqlResultsets();
			if(inSqlResultsets != null && inSqlResultsets.size() > 0){
				List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList = new ArrayList<List<ResourceMetadataInfo>>(inSqlResultsets.size());
				for (CfgSqlResultset cfgSqlResultset : inSqlResultsets) {
					inSqlResultSetMetadataInfoList.add(cfgSqlResultset.getInSqlResultSetMetadataInfos());
				}
				return inSqlResultSetMetadataInfoList;
			}
		}
		return null;
	}
	
	/**
	 * 获取sql资源传出的结果集元数据信息集合
	 * <p>主要针对select</p>
	 * @param sql
	 * @return
	 */
	public static List<ResourceMetadataInfo> getSqlOutResultSetMetadataInfos(ComSqlScript sql){
		if(SqlStatementTypeConstants.SELECT.equals(sql.getSqlScriptType())){
			List<CfgSqlResultset> outSqlResultSet = sql.getOutSqlResultsetsList().get(0);
			List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(outSqlResultSet.size());
			for (CfgSqlResultset csr : outSqlResultSet) {
				metadataInfos.add(new SqlResourceMetadataInfo(csr.getPropName()));
			}
			return metadataInfos;
		}
		return null;
	}
	
	// --------------------------------------------------------------------------
	/**
	 * 验证数据是否合法
	 * @param dataValue
	 * @param rmi
	 * @return
	 */
	public static String validDataIsLegal(Object dataValue, ResourceMetadataInfo rmi){
		String dataValueStr;
		// 验证数据类型、数据长度、数据精度
		if(DataTypeConstants.BOOLEAN.equals(rmi.getDataType())){
			if(!DataValidUtil.isBoolean(dataValue)){
				return "["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
			}
		}else if(DataTypeConstants.INTEGER.equals(rmi.getDataType())){
			if(!DataValidUtil.isInteger(dataValue)){
				return "["+rmi.getDescName()+"] 的值不合法，应为整数类型";
			}
			if(rmi.getLength() != -1 && dataValue.toString().length() > rmi.getLength()){
				return "["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
		}else if(DataTypeConstants.DOUBLE.equals(rmi.getDataType())){
			if(!DataValidUtil.isNumber(dataValue)){
				return "["+rmi.getDescName()+"] 的值不合法，应为浮点类型[或数字类型]";
			}
			dataValueStr = dataValue.toString();
			if(rmi.getLength() != -1 && (dataValueStr.length()-1) > rmi.getLength()){
				return "["+rmi.getDescName()+"]的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
			if(rmi.getPrecision() != -1 && dataValueStr.indexOf(".")!=-1 && dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
				return "["+rmi.getDescName()+"] 的值精度，大于实际配置的精度("+rmi.getPrecision()+")";
			}
		}else if(DataTypeConstants.DATE.equals(rmi.getDataType())){
			if(!DataValidUtil.isDate(dataValue)){
				return "["+rmi.getDescName()+"] 的值不合法，应为日期类型";
			}
		}else if(DataTypeConstants.STRING.equals(rmi.getDataType())){
			if(rmi.getLength() != -1 && StrUtils.calcStrLength(dataValue.toString()) > rmi.getLength()){
				return "["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
		}else{
			return "["+rmi.getDescName()+"]，系统目前不支持["+rmi.getDataType()+"]数据类型，请联系后端开发人员";
		}
		return null;
	}
}
