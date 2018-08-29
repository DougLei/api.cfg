package com.king.tooth.sys.controller.sys;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.util.StrUtils;

/**
 * 权限信息表Controller
 * @author DougLei
 */
@Controller
public class SysPermissionController extends AbstractController{
	
	/**
	 * 计算当前用户，指定code的功能权限以及子权限集合
	 * <p>请求方式：GET</p>
	 * <p>/permission</p>
	 * @return
	 */
	@RequestMapping
	public Object calcPermissionByCode(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String code = request.getParameter("objcode");
		if(StrUtils.isEmpty(code)){
			return "计算权限的code值不能为空";
		}
		
		boolean recursive = "true".equals(request.getParameter("recursive"));
		int deep = (request.getParameter("deep")== null)? 0:Integer.valueOf(request.getParameter("deep"));
		
		resultObject = BuiltinObjectInstance.permissionService.calcPermissionByCode(code, recursive, deep);
		return getResultObject();
	}
}
