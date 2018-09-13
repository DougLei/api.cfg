package com.king.tooth.sys.controller.cfg;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本信息表Controller
 * @author DougLei
 */
@Controller
public class ComSqlScriptController extends AbstractPublishController{
	
	/**
	 * 添加sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComSqlScript> sqlScripts = getDataInstanceList(ijson, ComSqlScript.class, true);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			if(sqlScripts.size() == 1){
				resultObject = BuiltinObjectInstance.sqlScriptService.saveSqlScript(sqlScripts.get(0));
				sqlScripts.get(0).clear();
			}else{
				for (ComSqlScript sqlScript : sqlScripts) {
					resultObject = BuiltinObjectInstance.sqlScriptService.saveSqlScript(sqlScript);
					sqlScript.clear();
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			sqlScripts.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改sql脚本
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComSqlScript> sqlScripts = getDataInstanceList(ijson, ComSqlScript.class, true);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			if(sqlScripts.size() == 1){
				resultObject = BuiltinObjectInstance.sqlScriptService.updateSqlScript(sqlScripts.get(0));
				sqlScripts.get(0).clear();
			}else{
				for (ComSqlScript sqlScript : sqlScripts) {
					resultObject = BuiltinObjectInstance.sqlScriptService.updateSqlScript(sqlScript);
					sqlScript.clear();
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			sqlScripts.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 删除sql脚本
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String sqlScriptIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(sqlScriptIds)){
			return "要删除的sql脚本id不能为空";
		}

		String[] sqlScriptIdArr = sqlScriptIds.split(",");
		for (String sqlScriptId : sqlScriptIdArr) {
			resultObject = BuiltinObjectInstance.sqlScriptService.deleteSqlScript(sqlScriptId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, sqlScriptIds);
		return getResultObject();
	}
	
	/**
	 * 创建sql脚本对象
	 * <p>存储过程、视图等</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object immediateCreate(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		resultObject = BuiltinObjectInstance.sqlScriptService.immediateCreate(ijson);
		if(resultObject == null){
			resultObject = ijson.getJson();
		}
		return getResultObject();
	}
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object addProjSqlScriptRelation(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = BuiltinObjectInstance.sqlScriptService.addProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
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
	@RequestMapping
	public Object cancelProjSqlScriptRelation(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = BuiltinObjectInstance.sqlScriptService.cancelProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object publish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要发布的sql脚本id不能为空";
		}
		resultObject = BuiltinObjectInstance.sqlScriptService.publishSqlScript(jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object cancelPublish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要取消发布的sql脚本id不能为空";
		}
		resultObject = BuiltinObjectInstance.sqlScriptService.cancelPublishSqlScript(jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
