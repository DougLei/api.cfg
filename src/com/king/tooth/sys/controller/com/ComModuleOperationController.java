package com.king.tooth.sys.controller.com;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.service.com.ComModuleOperationService;
import com.king.tooth.util.StrUtils;

/**
 * 模块功能资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComModuleOperation")
public class ComModuleOperationController extends AbstractPublishController{
	
	private ComModuleOperationService moduleOperationService = new ComModuleOperationService();
	
	/**
	 * 添加模块功能
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String add(@RequestBody ComModuleOperation moduleOperation) {
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
	@RequestMapping(value="/update", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String update(@RequestBody ComModuleOperation moduleOperation) {
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
	@RequestMapping(value="/delete", method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String delete(HttpServletRequest request){
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
	@RequestMapping(value="/publish", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String publish(HttpServletRequest request){
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
	@RequestMapping(value="/cancelPublish", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String cancelPublish(HttpServletRequest request){
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
