package com.king.tooth.sys.controller.sys;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.service.sys.SysPermissionService;
import com.king.tooth.util.StrUtils;

/**
 * 权限信息表Controller
 * @author DougLei
 */
@Controller
public class SysPermissionController extends AController{
	
	/**
	 * 计算当前用户，指定code的功能权限以及子权限集合
	 * <p>请求方式：GET</p>
	 * <p>/permission</p>
	 * @return
	 */
	@RequestMapping
	public Object calcPermissionByCode(HttpServletRequest request, IJson ijson){
		String code = request.getParameter("objcode");
		if(StrUtils.isEmpty(code)){
			return "计算权限的code值不能为空";
		}
		
		boolean recursive = "true".equals(request.getParameter("recursive"));
		int deep = (request.getParameter("deep")== null)? 0:Integer.valueOf(request.getParameter("deep"));
		
		resultObject = BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).calcPermissionByCode(code, recursive, deep);
		return getResultObject();
	}
}
