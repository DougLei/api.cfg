package com.king.tooth.util;

import java.util.Date;
import java.util.List;
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
import com.king.tooth.sys.entity.cfg.ComTabledata;
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
		data.put("projectId", CurrentThreadContext.getProjectId());
		// 当没有id值的时候，再赋予id值
		if(StrUtils.isEmpty(data.get(ResourceNameConstants.ID))){
			data.put(ResourceNameConstants.ID, getIdentity());
		}
		if(!entityName.endsWith("Links")){// 不是关系表，才要这些值
			Date currentDate = new Date();
			data.put("createDate", currentDate);
			data.put("lastUpdateDate", currentDate);
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccountOnlineStatus() != null){
				String currentAccountId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
				data.put("createUserId", currentAccountId);
				data.put("lastUpdatedUserId",  currentAccountId);
			}else{
				data.put("createUserId", shortDesc);
				data.put("lastUpdatedUserId",  shortDesc);
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
		if(!"ComDataLinks".equals(entityName) 
				&& !entityName.endsWith("Links")){// 不是关系表，才要修改这些值
			data.put("lastUpdateDate",  new Date());
			
			// 比如注册操作，肯定没有创建人
			if(CurrentThreadContext.getCurrentAccountOnlineStatus() != null){
				data.put("lastUpdatedUserId",  CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
			}else{
				data.put("lastUpdatedUserId",  shortDesc);
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
		if(resourceName.endsWith("Links")){
			return data;
		}
		if(data == null || data.size() == 0){
			throw new NullPointerException("[ResourceHandlerUtil.validDataProp()]要进行验证的数据对象为null");
		}
		
		JSONObject resultData = new JSONObject(data.size());
		HbmConfPropMetadata[] hibernateDefineResourceProps = HibernateUtil.getHibernateDefineResourceProps(resourceName);
		if(hibernateDefineResourceProps == null){
			throw new NullPointerException("[ResourceHandlerUtil.validDataProp()]hibernateDefineResourceProps对象为null");
		}
		
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
			if(request.getRequestURI().equals(ignore)){
				return true;
			}
		}
		return false;
	}
	
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
