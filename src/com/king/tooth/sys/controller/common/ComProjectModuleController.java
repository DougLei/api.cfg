package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.service.common.ComProjectModuleService;
import com.king.tooth.util.StrUtils;

/**
 * 项目模块信息资源对象控制器
 * @author DougLei
 */
public class ComProjectModuleController extends AbstractPublishController{
	
	private ComProjectModuleService projectModuleService = new ComProjectModuleService();
	
	/**
	 * 添加项目模块
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json) {
		List<ComProjectModule> projectModules = getDataInstanceList(json, ComProjectModule.class);
		analysisResourceProp(projectModules);
		if(analysisResult == null){
			if(projectModules.size() == 1){
				resultObject = projectModuleService.saveProjectModule(projectModules.get(0));
			}else{
				for (ComProjectModule projectModule : projectModules) {
					resultObject = projectModuleService.saveProjectModule(projectModule);
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
	 * 修改项目模块
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json) {
		List<ComProjectModule> projectModules = getDataInstanceList(json, ComProjectModule.class);
		analysisResourceProp(projectModules);
		if(analysisResult == null){
			if(projectModules.size() == 1){
				resultObject = projectModuleService.updateProjectModule(projectModules.get(0));
			}else{
				for (ComProjectModule projectModule : projectModules) {
					resultObject = projectModuleService.updateProjectModule(projectModule);
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
	 * 删除项目模块
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String projectModuleIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(projectModuleIds)){
			return "要删除的项目模块id不能为空";
		}
		
		String[] projectModuleIdArr = projectModuleIds.split(",");
		for (String projectModuleId : projectModuleIdArr) {
			resultObject = projectModuleService.deleteProjectModule(projectModuleId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, projectModuleIds);
		return getResultObject();
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目模块
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = JSONObject.parseObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的项目模块id不能为空";
		}
		resultObject = projectModuleService.publishProjectModule(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消发布项目模块
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = JSONObject.parseObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的项目模块id不能为空";
		}
		resultObject = projectModuleService.cancelPublishProjectModule(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
