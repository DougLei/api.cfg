package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.service.common.ComModuleOperationService;
import com.king.tooth.util.StrUtils;

/**
 * 模块功能资源对象控制器
 * @author DougLei
 */
public class ComModuleOperationController extends AbstractPublishController{
	
	private ComModuleOperationService moduleOperationService = new ComModuleOperationService();
	
	/**
	 * 添加模块功能
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json) {
		List<ComModuleOperation> moduleOperations = getDataInstanceList(json, ComModuleOperation.class);
		analysisResourceProp(moduleOperations);
		if(analysisResult == null){
			if(moduleOperations.size() == 1){
				resultObject = moduleOperationService.saveModuleOperation(moduleOperations.get(0));
			}else{
				for (ComModuleOperation moduleOperation : moduleOperations) {
					resultObject = moduleOperationService.saveModuleOperation(moduleOperation);
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
	 * 修改模块功能
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json) {
		List<ComModuleOperation> moduleOperations = getDataInstanceList(json, ComModuleOperation.class);
		analysisResourceProp(moduleOperations);
		if(analysisResult == null){
			if(moduleOperations.size() == 1){
				resultObject = moduleOperationService.updateModuleOperation(moduleOperations.get(0));
			}else{
				for (ComModuleOperation moduleOperation : moduleOperations) {
					resultObject = moduleOperationService.updateModuleOperation(moduleOperation);
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
	 * 删除模块功能
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String moduleOperationIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(moduleOperationIds)){
			return "要删除的功能id不能为空";
		}
		
		String[] moduleOperationIdArr = moduleOperationIds.split(",");
		for (String moduleOperationId : moduleOperationIdArr) {
			resultObject = moduleOperationService.deleteModuleOperation(moduleOperationId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, moduleOperationIds);
		return getResultObject();
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布模块功能
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = JSONObject.parseObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的功能id不能为空";
		}
		resultObject = moduleOperationService.publishModuleOperation(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消发布模块功能
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = JSONObject.parseObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的功能id不能为空";
		}
		resultObject = moduleOperationService.cancelPublishModuleOperation(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
