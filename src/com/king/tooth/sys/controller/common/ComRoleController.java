package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComRole;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]角色资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComRole")
public class ComRoleController extends AbstractResourceController{
	
	/**
	 * 分配权限
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/grantpermission", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody grantPermission(@RequestBody ComRole role){
		return null;
	}
}
