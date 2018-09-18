package com.king.tooth.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.cfg.ComTabledata;
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
}
