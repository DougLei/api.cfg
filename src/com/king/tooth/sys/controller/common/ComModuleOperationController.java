package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.service.common.ComModuleOperationService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

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
	public ResponseBody add(HttpServletRequest request, String json) {
		List<ComModuleOperation> moduleOperations = getDataInstanceList(json, ComModuleOperation.class);
		String result = analysisResourceProp(moduleOperations);
		if(result == null){
			for (ComModuleOperation moduleOperation : moduleOperations) {
				result = moduleOperationService.saveModuleOperation(moduleOperation);
				if(result != null){
					throw new IllegalArgumentException(result);
				}
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改模块功能
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public ResponseBody update(HttpServletRequest request, String json) {
		List<ComModuleOperation> moduleOperations = getDataInstanceList(json, ComModuleOperation.class);
		String result = analysisResourceProp(moduleOperations);
		if(result == null){
			for (ComModuleOperation moduleOperation : moduleOperations) {
				result = moduleOperationService.updateModuleOperation(moduleOperation);
				if(result != null){
					throw new IllegalArgumentException(result);
				}
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除模块功能
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public ResponseBody delete(HttpServletRequest request, String json){
		String moduleOperationIds = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(moduleOperationIds)){
			return installOperResponseBody("要删除的功能id不能为空", null);
		}
		String result = null;
		String[] moduleOperationIdArr = moduleOperationIds.split(",");
		for (String moduleOperationId : moduleOperationIdArr) {
			result = moduleOperationService.deleteModuleOperation(moduleOperationId);
			if(result != null){
				throw new IllegalArgumentException(result);
			}
		}
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布模块功能
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String moduleOperationId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(moduleOperationId)){
			return installOperResponseBody("要发布的功能id不能为空", null);
		}
		String result = moduleOperationService.publishModuleOperation(moduleOperationId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布模块功能
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isAdministrator()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String moduleOperationId = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(moduleOperationId)){
			return installOperResponseBody("要取消发布的功能id不能为空", null);
		}
		String result = moduleOperationService.cancelPublishModuleOperation(moduleOperationId);
		return installOperResponseBody(result, null);
	}
}
