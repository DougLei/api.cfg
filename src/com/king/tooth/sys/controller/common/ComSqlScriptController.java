package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本资源对象控制器
 * @author DougLei
 */
public class ComSqlScriptController extends AbstractPublishController{
	
	private ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	
	/**
	 * 添加sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json){
		List<ComSqlScript> sqlScripts = getDataInstanceList(json, ComSqlScript.class);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			if(sqlScripts.size() == 1){
				resultObject = sqlScriptService.saveSqlScript(sqlScripts.get(0));
			}else{
				for (ComSqlScript sqlScript : sqlScripts) {
					resultObject = sqlScriptService.saveSqlScript(sqlScript);
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
	 * 修改sql脚本
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json){
		List<ComSqlScript> sqlScripts = getDataInstanceList(json, ComSqlScript.class);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			if(sqlScripts.size() == 1){
				resultObject = sqlScriptService.updateSqlScript(sqlScripts.get(0));
			}else{
				for (ComSqlScript sqlScript : sqlScripts) {
					resultObject = sqlScriptService.updateSqlScript(sqlScript);
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
	 * 删除sql脚本
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String sqlScriptIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(sqlScriptIds)){
			return "要删除的sql脚本id不能为空";
		}

		String[] sqlScriptIdArr = sqlScriptIds.split(",");
		for (String sqlScriptId : sqlScriptIdArr) {
			resultObject = sqlScriptService.deleteSqlScript(sqlScriptId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, sqlScriptIds);
		return getResultObject();
	}
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object addProjSqlScriptRelation(HttpServletRequest request, String json){
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = sqlScriptService.addProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelProjSqlScriptRelation(HttpServletRequest request, String json){
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = sqlScriptService.cancelProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDevloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的sql脚本id不能为空";
		}
		resultObject = sqlScriptService.publishSqlScript(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消发布sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDevloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的sql脚本id不能为空";
		}
		resultObject = sqlScriptService.cancelPublishSqlScript(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
