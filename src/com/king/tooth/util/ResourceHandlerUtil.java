package com.king.tooth.util;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.thread.current.CurrentThreadContext;

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
	
	/**
	 * 获取基础属性的元数据信息
	 * <p>Id,customerId,projectId,createDate,lastUpdateDate,createUserId,lastUpdateUserId</p>
	 * @param basicPropName
	 * @param builtinResource
	 * @return
	 */
	public static ResourceMetadataInfo getBasicPropMetadataInfo(String basicPropName, boolean builtinResource){
		if(StrUtils.notEmpty(basicPropName) && isBuildInProps(basicPropName)){
			if(ResourcePropNameConstants.ID.equals(basicPropName)){
				if(builtinResource){
					return BuiltinObjectInstance.idColumn32.toTableResourceMetadataInfo();
				}
				return BuiltinObjectInstance.idColumn50.toTableResourceMetadataInfo();
			}else if("customerId".equals(basicPropName)){
				return BuiltinObjectInstance.customerIdColumn.toTableResourceMetadataInfo();
			}else if("projectId".equals(basicPropName)){
				return BuiltinObjectInstance.projectIdColumn.toTableResourceMetadataInfo();
			}else if("createDate".equals(basicPropName)){
				return BuiltinObjectInstance.createDateColumn.toTableResourceMetadataInfo();
			}else if("lastUpdateDate".equals(basicPropName)){
				return BuiltinObjectInstance.lastUpdateDateColumn.toTableResourceMetadataInfo();
			}else if("createUserId".equals(basicPropName)){
				return BuiltinObjectInstance.createUserIdColumn.toTableResourceMetadataInfo();
			}else if("lastUpdateUserId".equals(basicPropName)){
				return BuiltinObjectInstance.lastUpdateUserIdColumn.toTableResourceMetadataInfo();
			}
		}
		return null;
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 是否是系统内置的列名
	 * @param columnName
	 * @return
	 */
	public static boolean isBuildInColumns(String columnName){
		for(String builtinColumnName: ResourceInfoConstants.BUILTIN_COLUMN_NAMES){
			if(columnName.equalsIgnoreCase(builtinColumnName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是系统内置的资源名
	 * @param propName
	 * @return
	 */
	public static boolean isBuildInProps(String propName){
		for(String builtinPropName: ResourceInfoConstants.BUILTIN_PROP_NAMES){
			if(propName.equals(builtinPropName)){
				return true;
			}
		}
		return false;
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
