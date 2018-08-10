package com.king.tooth.sys.controller.cfg;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * 数据库信息表Controller
 * @author DougLei
 */
public class CfgDatabaseController extends AbstractPublishController{
	
	/**
	 * 添加数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<CfgDatabase> databases = getDataInstanceList(ijson, CfgDatabase.class);
		analysisResourceProp(databases);
		if(analysisResult == null){
			if(databases.size() == 1){
				resultObject = BuiltinObjectInstance.databaseService.saveDatabase(databases.get(0));
			}else{
				for (CfgDatabase database : databases) {
					resultObject = BuiltinObjectInstance.databaseService.saveDatabase(database);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 修改数据库
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<CfgDatabase> databases = getDataInstanceList(ijson, CfgDatabase.class);
		analysisResourceProp(databases);
		if(analysisResult == null){
			if(databases.size() == 1){
				resultObject = BuiltinObjectInstance.databaseService.updateDatabase(databases.get(0));
			}else{
				for (CfgDatabase database : databases) {
					resultObject = BuiltinObjectInstance.databaseService.updateDatabase(database);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 删除数据库
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String databaseIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(databaseIds)){
			return "要删除的数据库id不能为空";
		}
		
		String[] databaseIdArr = databaseIds.split(",");
		for (String databaseId : databaseIdArr) {
			resultObject = BuiltinObjectInstance.databaseService.deleteDatabase(databaseId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, databaseIds);
		return getResultObject();
	}
	
	/**
	 * 测试数据库连接
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object testLink(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "测试连接的数据库id不能为空";
		}
		resultObject = BuiltinObjectInstance.databaseService.databaseLinkTest(jsonObject.getString(ResourcePropNameConstants.ID));
		String linkMsg = resultObject.toString();
		if(linkMsg.startsWith("ok:")){
			jsonObject.put("linkMsg", linkMsg.replaceAll("ok:", ""));
			resultObject = jsonObject;
			return getResultObject();
		}else{
			return resultObject;
		}
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要发布的数据库id不能为空";
		}
		resultObject = BuiltinObjectInstance.databaseService.publishDatabase(jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消发布数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelPublish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要取消发布的数据库id不能为空";
		}
		
		resultObject = BuiltinObjectInstance.databaseService.cancelPublishDatabase(jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
