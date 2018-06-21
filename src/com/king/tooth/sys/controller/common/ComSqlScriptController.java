package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.JsonUtil;
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
		ComSqlScript sqlScript = JsonUtil.parseObject(json, ComSqlScript.class);
		String result = sqlScript.validNotNullProps();
		if(result == null){
			result = sqlScriptService.saveSqlScript(sqlScript);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改sql脚本
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public ResponseBody update(HttpServletRequest request, String json){
		ComSqlScript sqlScript = JsonUtil.parseObject(json, ComSqlScript.class);
		String result = sqlScript.validNotNullProps();
		if(result == null){
			result = sqlScriptService.updateSqlScript(sqlScript);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除sql脚本
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public ResponseBody delete(HttpServletRequest request, String json){
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要删除的sql脚本id不能为空", null);
		}
		String projectId = request.getParameter("projectId");
		String result = sqlScriptService.deleteSqlScript(sqlScriptId, projectId);
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
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要取消发布的sql脚本关联的项目id不能为空", null);
		}
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要发布的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.publishSqlScript(projectId, sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布sql脚本
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要取消发布的sql脚本关联的项目id不能为空", null);
		}
		String sqlScriptId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要取消发布的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.cancelPublishSqlScript(projectId, sqlScriptId);
		return installOperResponseBody(result, null);
	}
}
