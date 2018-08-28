package com.king.tooth.sys.controller.other;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.util.StrUtils;

/**
 * 系统工具类的Controller
 * @author DougLei
 */
public class SystemToolsController extends AbstractController{
	
	/**
	 * 监听hibernate类元数据
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public Object monitorHibernateClassMetadata(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String[] resourceNameArr = null;
		
		String resourceNames = request.getParameter("resourceNames");
		if(StrUtils.notEmpty(resourceNames)){
			resourceNameArr = resourceNames.split(",");
		}
		
		resultObject = BuiltinObjectInstance.systemToolsService.monitorHibernateClassMetadata(resourceNameArr);
		return getResultObject();
	}
}
