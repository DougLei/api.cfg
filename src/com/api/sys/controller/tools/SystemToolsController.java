package com.api.sys.controller.tools;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.plugins.ijson.IJson;
import com.api.soap.OPCData;
import com.api.soap.SOAPClient;
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
	
	/**
	 * 
	 * @param request
	 * @param ijson
	 * @return
	 */
	@RequestMapping
	public Object soapRead(HttpServletRequest request, IJson ijson){
		String param = request.getParameter("param");
		if(StrUtils.isEmpty(param)){
			return "执行soap的url参数[param]的值不能为空";
		}
		
		String[] params = param.split(",");
		List<OPCData> list = new ArrayList<>(params.length);
		for (String p : params) {
			list.add(new OPCData(p));
		}
		try {
			return SOAPClient.instance().read(list);
		} catch (Exception e) {
			return "执行soap时出现异常:" + e;
		}
	}
}