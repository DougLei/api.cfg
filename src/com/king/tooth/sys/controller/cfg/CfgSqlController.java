package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本信息表Controller
 * @author DougLei
 */
@Controller
public class CfgSqlController extends AbstractPublishController{
	
	/**
	 * 添加sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComSqlScript> sqlScripts = getDataInstanceList(ijson, ComSqlScript.class, true);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			if(sqlScripts.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).saveSqlScript(sqlScripts.get(0));
				sqlScripts.get(0).clear();
			}else{
				for (ComSqlScript sqlScript : sqlScripts) {
					resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).saveSqlScript(sqlScript);
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
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComSqlScript> sqlScripts = getDataInstanceList(ijson, ComSqlScript.class, true);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			if(sqlScripts.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).updateSqlScript(sqlScripts.get(0));
				sqlScripts.get(0).clear();
			}else{
				for (ComSqlScript sqlScript : sqlScripts) {
					resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).updateSqlScript(sqlScript);
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
	public Object delete(HttpServletRequest request, IJson ijson){
		String sqlScriptIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(sqlScriptIds)){
			return "要删除的sql脚本id不能为空";
		}

		String[] sqlScriptIdArr = sqlScriptIds.split(",");
		for (String sqlScriptId : sqlScriptIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).deleteSqlScript(sqlScriptId);
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
	public Object immediateCreate(HttpServletRequest request, IJson ijson){
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).immediateCreate(ijson);
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
	public Object addProjSqlScriptRelation(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).addProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object cancelProjSqlScriptRelation(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).cancelProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object publish(HttpServletRequest request, IJson ijson){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要发布的sql脚本id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).publishSqlScript(jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object cancelPublish(HttpServletRequest request, IJson ijson){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要取消发布的sql脚本id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).cancelPublishSqlScript(jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
