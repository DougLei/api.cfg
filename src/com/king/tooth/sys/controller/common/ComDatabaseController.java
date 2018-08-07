package com.king.tooth.sys.controller.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.util.StrUtils;

/**
 * 数据库数据信息资源对象控制器
 * @author DougLei
 */
public class ComDatabaseController extends AbstractPublishController{
	
	/**
	 * 添加数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComDatabase> databases = getDataInstanceList(ijson, ComDatabase.class);
		analysisResourceProp(databases);
		if(analysisResult == null){
			if(databases.size() == 1){
				resultObject = BuiltinInstance.databaseService.saveDatabase(databases.get(0));
			}else{
				for (ComDatabase database : databases) {
					resultObject = BuiltinInstance.databaseService.saveDatabase(database);
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
		List<ComDatabase> databases = getDataInstanceList(ijson, ComDatabase.class);
		analysisResourceProp(databases);
		if(analysisResult == null){
			if(databases.size() == 1){
				resultObject = BuiltinInstance.databaseService.updateDatabase(databases.get(0));
			}else{
				for (ComDatabase database : databases) {
					resultObject = BuiltinInstance.databaseService.updateDatabase(database);
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
		String databaseIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(databaseIds)){
			return "要删除的数据库id不能为空";
		}
		
		String[] databaseIdArr = databaseIds.split(",");
		for (String databaseId : databaseIdArr) {
			resultObject = BuiltinInstance.databaseService.deleteDatabase(databaseId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, databaseIds);
		return getResultObject();
	}
	
	/**
	 * 测试数据库连接
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object testLink(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "测试连接的数据库id不能为空";
		}
		resultObject = BuiltinInstance.databaseService.databaseLinkTest(jsonObject.getString(ResourceNameConstants.ID));
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
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的数据库id不能为空";
		}
		resultObject = BuiltinInstance.databaseService.publishDatabase(jsonObject.getString(ResourceNameConstants.ID));
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
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的数据库id不能为空";
		}
		
		resultObject = BuiltinInstance.databaseService.cancelPublishDatabase(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
