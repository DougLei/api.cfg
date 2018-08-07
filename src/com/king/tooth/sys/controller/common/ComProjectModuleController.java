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
import com.king.tooth.sys.entity.cfg.ComProjectModule;
import com.king.tooth.util.StrUtils;

/**
 * 项目模块信息资源对象控制器
 * @author DougLei
 */
public class ComProjectModuleController extends AbstractPublishController{
	
	/**
	 * 添加项目模块
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams) {
		List<ComProjectModule> projectModules = getDataInstanceList(ijson, ComProjectModule.class);
		analysisResourceProp(projectModules);
		if(analysisResult == null){
			if(projectModules.size() == 1){
				resultObject = BuiltinInstance.projectModuleService.saveProjectModule(projectModules.get(0));
			}else{
				for (ComProjectModule projectModule : projectModules) {
					resultObject = BuiltinInstance.projectModuleService.saveProjectModule(projectModule);
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
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams) {
		List<ComProjectModule> projectModules = getDataInstanceList(ijson, ComProjectModule.class);
		analysisResourceProp(projectModules);
		if(analysisResult == null){
			if(projectModules.size() == 1){
				resultObject = BuiltinInstance.projectModuleService.updateProjectModule(projectModules.get(0));
			}else{
				for (ComProjectModule projectModule : projectModules) {
					resultObject = BuiltinInstance.projectModuleService.updateProjectModule(projectModule);
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
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String projectModuleIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(projectModuleIds)){
			return "要删除的项目模块id不能为空";
		}
		
		String[] projectModuleIdArr = projectModuleIds.split(",");
		for (String projectModuleId : projectModuleIdArr) {
			resultObject = BuiltinInstance.projectModuleService.deleteProjectModule(projectModuleId);
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
	public Object publish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的项目模块id不能为空";
		}
		resultObject = BuiltinInstance.projectModuleService.publishProjectModule(jsonObject.getString(ResourceNameConstants.ID));
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
	public Object cancelPublish(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的项目模块id不能为空";
		}
		resultObject = BuiltinInstance.projectModuleService.cancelPublishProjectModule(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
