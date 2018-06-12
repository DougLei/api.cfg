package com.king.tooth.util;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.internal.HbmConfPropMetadata;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
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
	 * @param entityName 
	 * @param json
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 */
	public static void initBasicPropValsForSave(String entityName, Map<String, Object> data, String shortDesc) {
		data.put(ResourceNameConstants.PROJECT_ID, CurrentThreadContext.getProjectId());
		// 当没有id值的时候，再赋予id值
		if(StrUtils.isEmpty(data.get(ResourceNameConstants.ID))){
			data.put(ResourceNameConstants.ID, getIdentity());
		}
		if(!ResourceNameConstants.COMMON_DATALINK_RESOURCENAME.equals(entityName) 
				&& !entityName.endsWith(ResourceNameConstants.DATALINK_RESOURCENAME_SUFFIX)){// 不是关系表，才要这些值
			Date currentDate = new Date();
			data.put(ResourceNameConstants.CREATE_TIME, currentDate);
			data.put(ResourceNameConstants.LAST_UPDATE_TIME, currentDate);
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccountOnlineStatus() != null){
				String currentAccountId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
				data.put(ResourceNameConstants.CREATE_USER_ID, currentAccountId);
				data.put(ResourceNameConstants.LAST_UPDATED_USER_ID,  currentAccountId);
			}else{
				data.put(ResourceNameConstants.CREATE_USER_ID, shortDesc);
				data.put(ResourceNameConstants.LAST_UPDATED_USER_ID,  shortDesc);
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
		if(!ResourceNameConstants.COMMON_DATALINK_RESOURCENAME.equals(entityName) 
				&& !entityName.endsWith(ResourceNameConstants.DATALINK_RESOURCENAME_SUFFIX)){// 不是关系表，才要修改这些值
			data.put(ResourceNameConstants.LAST_UPDATE_TIME,  new Date());
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccountOnlineStatus() != null){
				data.put(ResourceNameConstants.LAST_UPDATED_USER_ID,  CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
			}else{
				data.put(ResourceNameConstants.LAST_UPDATED_USER_ID,  shortDesc);
			}
		}
	}
	
	/**
	 * 验证要操作的数据的属性名是否和hbm配置定义的属性名一致
	 * <p>同时校验一下日期类型，如果是日期类型，则要转换为日期类型</p>
	 * @param resourceName
	 * @param data
	 * @return
	 */
	public static JSONObject validDataProp(String resourceName, JSONObject data) {
		if(data == null || data.size() == 0){
			throw new NullPointerException("要进行验证的数据对象为null");
		}
		
		JSONObject resultData = new JSONObject(data.size());
		HbmConfPropMetadata[] hibernateDefineResourceProps = HibernateUtil.getHibernateDefineResourceProps(resourceName);
		Set<String> reqPropnames = data.keySet();
		HbmConfPropMetadata propMetadata = null;
		for (String rpn : reqPropnames) {
			propMetadata = HibernateUtil.getDefinePropMetadata(hibernateDefineResourceProps, rpn);
			if(data.get(rpn) instanceof String){
				if(DataTypeConstants.HIBERNATE_TIMESTAMP.equals(propMetadata.getPropDataType())){
					resultData.put(propMetadata.getPropName(), DateUtil.parseDate(data.getString(rpn)));
				}else{
					resultData.put(propMetadata.getPropName(), data.getString(rpn));
				}
			}else{
				resultData.put(propMetadata.getPropName(), data.get(rpn));
			}
		}
		data.clear();
		return resultData;
	}
	
	/**
	 * 获取关联关系jsonObject对象
	 * @param leftId
	 * @param rightId
	 * @param orderCode
	 * @param leftResourceName
	 * @param rightResourceName
	 * @return
	 */
	public static JSONObject getDataLinksObject(String leftId, String rightId, int orderCode, String leftResourceName, String rightResourceName){
		JSONObject dataLinks = new JSONObject(6);
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

	/**
	 * 可以忽略登录验证的请求url集合
	 */
	private static final String[] ignoreLoginValidRequrl;
	static{
		ignoreLoginValidRequrl = SysConfig.getSystemConfig("ignore.loginvalid.requrl").split(",");
	}
	/**
	 * 是否需要忽略登录验证
	 * @param request
	 * @return
	 */
	public static boolean isIgnoreLoginValid(HttpServletRequest request) {
		for (String ignore : ignoreLoginValidRequrl) {
			if(request.getRequestURI().endsWith(ignore)){
				return true;
			}
		}
		return false;
	}
}
