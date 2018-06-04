package com.king.tooth.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.internal.HbmConfPropMetadata;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
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
	 * 保存数据时，初始化基本属性值
	 * 包括ID，CreateTime等
	 * @param entityName 
	 * @param json
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 */
	public static void initBasicPropValsForSave(String entityName, Map<String, Object> data, String shortDesc) {
		data.put(ResourceNameConstants.PROJECT_ID, CurrentThreadContext.getProjectId());
		data.put(ResourceNameConstants.ID, getIdentity());
		
		if(!ResourceNameConstants.COMMON_DATALINK_RESOURCENAME.equals(entityName) 
				&& !entityName.endsWith(ResourceNameConstants.DATALINK_RESOURCENAME_SUFFIX)){// 不是关系表，才要这些值
			Date currentDate = new Date();
			data.put(ResourceNameConstants.CREATE_TIME, currentDate);
			data.put(ResourceNameConstants.LAST_UPDATE_TIME, currentDate);
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccount() != null){
				String currentAccountId = CurrentThreadContext.getCurrentAccount().getId();
				data.put(ResourceNameConstants.CREATE_USER_ID, currentAccountId);
				data.put(ResourceNameConstants.SET_LAST_UPDATED_USER_ID,  currentAccountId);
			}else{
				data.put(ResourceNameConstants.CREATE_USER_ID, shortDesc);
				data.put(ResourceNameConstants.SET_LAST_UPDATED_USER_ID,  shortDesc);
			}
		}
	}

	/**
	 * 保存数据时，初始化基本属性值
	 * 包括ID，CreateTime等
	 * @param json
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return id
	 */
	public static String initBasicPropValsForSave(Object data, String shortDesc) {
		ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_PROJECT_ID, new Class[]{String.class}, new Object[]{CurrentThreadContext.getProjectId()});
		
		String id = getIdentity();
		ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_ID, new Class[]{String.class}, new Object[]{id});
		
		Date currentDate = new Date();
		ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_CREATE_TIME, new Class[]{Date.class}, new Object[]{ currentDate });
		ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_LAST_UPDATE_TIME, new Class[]{Date.class}, new Object[]{ currentDate });
		
		// 比如注册操作，肯定没有创建人
		if(CurrentThreadContext.getCurrentAccount() != null){
			String currentAccountId = CurrentThreadContext.getCurrentAccount().getId();
			ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_CREATE_USER_ID, new Class[]{String.class}, new Object[]{ currentAccountId});
			ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_LAST_UPDATED_USER_ID, new Class[]{String.class}, new Object[]{ currentAccountId});
		}else{
			ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_CREATE_USER_ID, new Class[]{String.class}, new Object[]{ shortDesc});
			ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_LAST_UPDATED_USER_ID, new Class[]{String.class}, new Object[]{ shortDesc});
		}
		return id;
	}
	
	
	/**
	 * 修改数据时，初始化基本属性值
	 * 包括ID，CreateTime等
	 * @param json
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 */
	public static void initBasicPropValsForUpdate(Object data, String shortDesc) {
		ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_LAST_UPDATE_TIME, new Class[]{Date.class}, new Object[]{ new Date() });
		
		// 比如注册操作，肯定没有创建人
		if(CurrentThreadContext.getCurrentAccount() != null){
			ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_LAST_UPDATED_USER_ID, new Class[]{String.class}, new Object[]{ CurrentThreadContext.getCurrentAccount().getId() });
		}else{
			ReflectUtil.invokeMethod(data, ResourceNameConstants.SET_LAST_UPDATED_USER_ID, new Class[]{String.class}, new Object[]{ shortDesc });
		}
	}

	/**
	 * 验证要操作的数据的属性名是否和hbm配置定义的属性名一致
	 * <p>同时校验一下日期类型，如果是日期类型，则要转换为日期类型</p>
	 * @param resourceName
	 * @param data
	 * @return
	 */
	public static Map<String, Object> validDataProp(String resourceName, Map<String, Object> data) {
		if(data == null || data.size() == 0){
			throw new NullPointerException("要进行验证的数据对象为null");
		}
		
		Map<String, Object> resultData = new HashMap<String, Object>(data.size());
		HbmConfPropMetadata[] hibernateDefineResourceProps = HibernateUtil.getHibernateDefineResourceProps(resourceName);
		Set<String> reqPropnames = data.keySet();
		HbmConfPropMetadata propMetadata = null;
		for (String rpn : reqPropnames) {
			propMetadata = HibernateUtil.getDefinePropMetadata(hibernateDefineResourceProps, rpn);
			if(DataTypeConstants.HIBERNATE_TIMESTAMP.equals(propMetadata.getPropDataType())){
				resultData.put(propMetadata.getPropName(), DateUtil.parseDate(data.get(rpn)+""));
			}else{
				resultData.put(propMetadata.getPropName(), data.get(rpn));
			}
		}
		data.clear();
		return resultData;
	}
	
	/**
	 * 获取关联关系map对象
	 * @param leftId
	 * @param rightId
	 * @param orderCode
	 * @param leftResourceName
	 * @param rightResourceName
	 * @return
	 */
	public static Map<String, Object> getDataLinksObject(String leftId, String rightId, int orderCode, String leftResourceName, String rightResourceName){
		Map<String, Object> dataLinks = new HashMap<String, Object>(4);
		dataLinks.put(ResourceNameConstants.LEFT_ID, leftId);
		dataLinks.put(ResourceNameConstants.RIGHT_ID, rightId);
		dataLinks.put(ResourceNameConstants.ORDER_CODE, orderCode);
		if(StrUtils.notEmpty(leftResourceName)){
			dataLinks.put(ResourceNameConstants.LEFT_RESOURCE_NAME, leftResourceName);
		}
		if(StrUtils.notEmpty(rightResourceName)){
			dataLinks.put(ResourceNameConstants.RIGHT_RESOURCE_NAME, rightResourceName);
		}
		return dataLinks;
	}
}
