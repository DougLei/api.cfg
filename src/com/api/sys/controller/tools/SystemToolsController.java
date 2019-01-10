package com.api.sys.controller.tools;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.service.tools.SystemToolsService;
import com.api.util.StrUtils;

/**
 * 系统工具类的Controller
 * @author DougLei
 */
@Controller
public class SystemToolsController extends AController{
	
	/**
	 * 监听hibernate类元数据
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping
	public Object monitorHibernateClassMetadata(HttpServletRequest request, IJson ijson){
		String[] resourceNameArr = null;
		
		String resourceNames = request.getParameter("resourceNames");
		if(StrUtils.notEmpty(resourceNames)){
			resourceNameArr = resourceNames.split(",");
		}
		
		resultObject = BuiltinResourceInstance.getInstance("SystemToolsService", SystemToolsService.class).monitorHibernateClassMetadata(resourceNameArr);
		return getResultObject(null, null);
	}
	
	/**
	 * 获取指定资源的信息
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping
	public Object getResourceInfo(HttpServletRequest request, IJson ijson){
		String name = request.getParameter("name");
		if(StrUtils.isEmpty(name)){
			return "获取指定资源信息接口的url参数[name]的值不能为空";
		}
		
		resultObject = BuiltinResourceInstance.getInstance("SystemToolsService", SystemToolsService.class).getResourceInfo(name);
		return getResultObject(null, null);
	}
}
