package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.service.common.ComProjectService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]项目信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComProject")
public class ComProjectController extends AbstractResourceController{
	
	private ComProjectService projectService = new ComProjectService();
	
	/**
	 * 发布项目
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/deploying/{id}", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody deployingProject(@PathVariable String id){
		if(StrUtils.isEmpty(id)){
			return installResponseBody("要发布的项目id不能为空", null);
		}
		projectService.deployingProject(id);
		return installResponseBody("发布成功", null);
	}
	
	/**
	 * 取消发布项目
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/cancelDeploy/{id}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelDeployingProject(@PathVariable String id){
		if(StrUtils.isEmpty(id)){
			return installResponseBody("要取消发布的项目id不能为空", null);
		}
		projectService.cancelDeployingProject(id);
		return installResponseBody("取消发布成功", null);
	}
	
}
