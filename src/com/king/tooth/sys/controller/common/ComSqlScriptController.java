package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

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
	public ResponseBody add(HttpServletRequest request, String json){
		List<ComSqlScript> sqlScripts = getDataInstanceList(json, ComSqlScript.class);
		String result = analysisResourceProp(sqlScripts);
		if(result == null){
			for (ComSqlScript sqlScript : sqlScripts) {
				result = sqlScriptService.saveSqlScript(sqlScript);
				if(result != null){
					throw new IllegalArgumentException(result);
				}
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改sql脚本
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public ResponseBody update(HttpServletRequest request, String json){
		List<ComSqlScript> sqlScripts = getDataInstanceList(json, ComSqlScript.class);
		String result = analysisResourceProp(sqlScripts);
		if(result == null){
			for (ComSqlScript sqlScript : sqlScripts) {
				result = sqlScriptService.updateSqlScript(sqlScript);
				if(result != null){
					throw new IllegalArgumentException(result);
				}
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除sql脚本
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public ResponseBody delete(HttpServletRequest request, String json){
		String sqlScriptIds = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptIds)){
			return installOperResponseBody("要删除的sql脚本id不能为空", null);
		}
		String result = null;
		String[] sqlScriptIdArr = sqlScriptIds.split(",");
		for (String sqlScriptId : sqlScriptIdArr) {
			result = sqlScriptService.deleteSqlScript(sqlScriptId);
			if(result != null){
				throw new IllegalArgumentException(result);
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody addProjSqlScriptRelation(HttpServletRequest request, String json){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要操作的项目id不能为空", null);
		}
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要操作的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.addProjSqlScriptRelation(projectId, sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody cancelProjSqlScriptRelation(HttpServletRequest request, String json){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要操作的项目id不能为空", null);
		}
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要操作的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.cancelProjSqlScriptRelation(projectId, sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布sql脚本
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要发布的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.publishSqlScript(sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布sql脚本
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要取消发布的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.cancelPublishSqlScript(sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 添加sql脚本参数
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public ResponseBody addSqlScriptParameter(HttpServletRequest request, String json){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(json, ComSqlScriptParameter.class);
		String result = analysisResourceProp(sqlScriptParameters);
		if(result == null){
			result = sqlScriptService.saveSqlScriptParameter(sqlScriptParameters);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改sql脚本参数
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public ResponseBody updateSqlScriptParameter(HttpServletRequest request, String json){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(json, ComSqlScriptParameter.class);
		String result = analysisResourceProp(sqlScriptParameters);
		if(result == null){
			result = sqlScriptService.updateSqlScriptParameter(sqlScriptParameters);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除sql脚本参数
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public ResponseBody deleteSqlScriptParameter(HttpServletRequest request, String json){
		String sqlScriptParameterIds = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptParameterIds)){
			return installOperResponseBody("要删除的sql脚本参数id不能为空", null);
		}
		String result = sqlScriptService.deleteSqlScriptParameter(sqlScriptParameterIds);
		return installOperResponseBody(result, null);
	}
}
