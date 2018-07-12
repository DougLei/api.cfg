package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.service.common.ComProjectService;
import com.king.tooth.util.StrUtils;

/**
 * 项目信息资源对象控制器
 * @author DougLei
 */
public class ComProjectController extends AbstractPublishController{
	
	private ComProjectService projectService = new ComProjectService();
	
	/**
	 * 添加项目
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json){
		List<ComProject> projects = getDataInstanceList(json, ComProject.class);
		analysisResourceProp(projects);
		if(analysisResult == null){
			if(projects.size() == 1){
				resultObject = projectService.saveProject(projects.get(0));
			}else{
				for (ComProject project : projects) {
					resultObject = projectService.saveProject(project);
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
	 * 修改项目
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json){
		List<ComProject> projects = getDataInstanceList(json, ComProject.class);
		analysisResourceProp(projects);
		if(analysisResult == null){
			if(projects.size() == 1){
				resultObject = projectService.updateProject(projects.get(0));
			}else{
				for (ComProject project : projects) {
					resultObject = projectService.updateProject(project);
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
	 * 删除项目
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String projectIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(projectIds)){
			return "要删除的项目id不能为空";
		}

		String[] projectIdArr = projectIds.split(",");
		for (String projectId : projectIdArr) {
			resultObject = projectService.deleteProject(projectId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, projectIds);
		return getResultObject();
	}
	
	/**
	 * 取消项目和[表/sql脚本]的关联信息
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelRelation(HttpServletRequest request, String json){
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消关联关系的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString("relationType"))){
			return "relationType不能为空,值目前包括：table、sql、all，必须传入一个";
		}
		resultObject = projectService.cancelRelation(jsonObject.getString(ResourceNameConstants.ID), jsonObject.getString("relationType"));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	} 
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目
	 * <p>【发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的项目id不能为空";
		}
		resultObject = projectService.publishProjectAll(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消发布项目
	 * <p>【取消发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的项目id不能为空";
		}
		resultObject = projectService.cancelPublishProjectAll(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
