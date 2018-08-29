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
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * 项目信息表Controller
 * @author DougLei
 */
@Controller
public class ComProjectController extends AbstractPublishController{
	
	/**
	 * 添加项目
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComProject> projects = getDataInstanceList(ijson, ComProject.class);
		analysisResourceProp(projects);
		if(analysisResult == null){
			if(projects.size() == 1){
				resultObject = BuiltinObjectInstance.projectService.saveProject(projects.get(0));
			}else{
				for (ComProject project : projects) {
					resultObject = BuiltinObjectInstance.projectService.saveProject(project);
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
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComProject> projects = getDataInstanceList(ijson, ComProject.class);
		analysisResourceProp(projects);
		if(analysisResult == null){
			if(projects.size() == 1){
				resultObject = BuiltinObjectInstance.projectService.updateProject(projects.get(0));
			}else{
				for (ComProject project : projects) {
					resultObject = BuiltinObjectInstance.projectService.updateProject(project);
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
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String projectIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(projectIds)){
			return "要删除的项目id不能为空";
		}

		String[] projectIdArr = projectIds.split(",");
		for (String projectId : projectIdArr) {
			resultObject = BuiltinObjectInstance.projectService.deleteProject(projectId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, projectIds);
		return getResultObject();
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目
	 * <p>【发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要发布的项目id不能为空";
		}
		resultObject = BuiltinObjectInstance.projectService.publishProjectAll(jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object cancelPublish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要取消发布的项目id不能为空";
		}
		resultObject = BuiltinObjectInstance.projectService.cancelPublishProjectAll(jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
