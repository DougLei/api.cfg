package com.king.tooth.sys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.service.ComProjectModuleService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 项目模块信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComProjectModule")
public class ComProjectModuleController extends AbstractPublishController{
	
	private ComProjectModuleService projectModuleService = new ComProjectModuleService();
	
	/**
	 * 添加项目模块
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComProjectModule projectModule) {
		String result = projectModule.analysisResourceProp();
		if(result == null){
			result = projectModuleService.saveProjectModule(projectModule);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改项目模块
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComProjectModule projectModule) {
		String result = projectModule.analysisResourceProp();
		if(result == null){
			result = projectModuleService.updateProjectModule(projectModule);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除项目模块
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(HttpServletRequest request){
		String projectModuleId = request.getParameter("projectModuleId");
		if(StrUtils.isEmpty(projectModuleId)){
			return installOperResponseBody("要删除的项目模块id不能为空", null);
		}
		String result = projectModuleService.deleteProjectModule(projectModuleId);
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目模块
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/publish", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody publish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectModuleId = request.getParameter("projectModuleId");
		if(StrUtils.isEmpty(projectModuleId)){
			return installOperResponseBody("要发布的项目模块id不能为空", null);
		}
		String result = projectModuleService.publishProjectModule(projectModuleId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布项目模块
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/cancelPublish", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelPublish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectModuleId = request.getParameter("projectModuleId");
		if(StrUtils.isEmpty(projectModuleId)){
			return installOperResponseBody("要取消发布的项目模块id不能为空", null);
		}
		String result = projectModuleService.cancelPublishProjectModule(projectModuleId);
		return installOperResponseBody(result, null);
	}
}
