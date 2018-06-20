package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.service.common.ComModuleOperationService;
import com.king.tooth.util.JsonUtil;
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
	public ResponseBody add(@RequestBody String moduleOperationJson) {
		ComModuleOperation moduleOperation = JsonUtil.parseObject(moduleOperationJson, ComModuleOperation.class);
		String result = moduleOperation.analysisResourceProp();
		if(result == null){
			result = moduleOperationService.saveModuleOperation(moduleOperation);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改模块功能
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public ResponseBody update(@RequestBody String moduleOperationJson) {
		ComModuleOperation moduleOperation = JsonUtil.parseObject(moduleOperationJson, ComModuleOperation.class);
		String result = moduleOperation.analysisResourceProp();
		if(result == null){
			result = moduleOperationService.updateModuleOperation(moduleOperation);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除模块功能
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public ResponseBody delete(HttpServletRequest request){
		String moduleOperationId = request.getParameter("moduleOperationId");
		if(StrUtils.isEmpty(moduleOperationId)){
			return installOperResponseBody("要删除的功能id不能为空", null);
		}
		String result = moduleOperationService.deleteModuleOperation(moduleOperationId);
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布模块功能
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody publish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String moduleOperationId = request.getParameter("moduleOperationId");
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
	public ResponseBody cancelPublish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String moduleOperationId = request.getParameter("moduleOperationId");
		if(StrUtils.isEmpty(moduleOperationId)){
			return installOperResponseBody("要取消发布的功能id不能为空", null);
		}
		String result = moduleOperationService.cancelPublishModuleOperation(moduleOperationId);
		return installOperResponseBody(result, null);
	}
}
