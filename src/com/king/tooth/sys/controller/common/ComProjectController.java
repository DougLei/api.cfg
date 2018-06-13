package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.service.common.ComProjectService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 项目信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComProject")
public class ComProjectController extends AbstractPublishController{
	
	private ComProjectService projectService = new ComProjectService();
	
	/**
	 * 添加项目
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComProject project){
		String result = project.analysisResourceProp();
		if(result == null){
			result = projectService.saveProject(project);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改项目
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComProject project){
		String result = project.analysisResourceProp();
		if(result == null){
			result = projectService.updateProject(project);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除项目
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(HttpServletRequest request){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要删除的项目id不能为空", null);
		}
		String result = projectService.deleteProject(projectId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消项目和[表/sql脚本]的关联信息
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/cancelRelation", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelRelation(HttpServletRequest request){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要取消关联关系的项目id不能为空", null);
		}
		String relationType = request.getParameter("relationType");
		if(StrUtils.isEmpty(relationType)){
			return installOperResponseBody("relationType不能为空,值目前包括：table、sql、all", null);
		}
		String result = projectService.cancelRelation(projectId, relationType);
		return installOperResponseBody(result, null);
	} 
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目
	 * <p>【发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * <p>【单独发布项目信息】</p>
	 * @return
	 */
	@RequestMapping(value="/publish", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody publish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要发布的项目id不能为空", null);
		}
		String result = projectService.publishProjectAll(projectId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布项目
	 * <p>【取消发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/cancelPublish", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelPublish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要取消发布的项目id不能为空", null);
		}
		String result = projectService.cancelPublishProjectAll(projectId);
		return installOperResponseBody(result, null);
	}
}
