package com.king.tooth.constants;

import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 权限常量
 * @author DougLei
 */
public class PermissionConstants {

	/**
	 * 权限关联的主体类型: user
	 */
	public static final String OBJ_TYPE_USER = "user";
	/**
	 * 权限关联的主体类型: account
	 */
	public static final String OBJ_TYPE_ACCOUNT = "account";
	/**
	 * 权限关联的主体类型: role
	 */
	public static final String OBJ_TYPE_ROLE = "role";
	/**
	 * 权限关联的主体类型: dept
	 */
	public static final String OBJ_TYPE_DEPT = "dept";
	/**
	 * 权限关联的主体类型: position
	 */
	public static final String OBJ_TYPE_POSITION = "position";
	/**
	 * 权限关联的主体类型: userGroup
	 */
	public static final String OBJ_TYPE_USERGROUP = "userGroup";
	
	// -----------------------------------------------------------
	/**
	 * 权限关联的资源类型: 模块module
	 */
	public static final String RT_MODULE = "module";
	/**
	 * 权限关联的资源类型: 页签tab
	 */
	public static final String RT_TAB = "tab";
	/**
	 * 权限关联的资源类型: 功能oper
	 */
	public static final String RT_OPER = "oper";
	/**
	 * 权限关联的资源类型: 字段field
	 */
	public static final String RT_FIELD = "field";
	
	// -----------------------------------------------------------
	/**
	 * 是否开启权限优先级
	 * <p>true可以体现出优先级，例如A角色和B角色都对同一个功能C有权限控制，A角色可以操作C，B角色不可以操作C。如果A的优先级高，则最后可以操作功能C；如果B的优先级高，则最终权限不能操作功能C</p>
	 * <p>alse是只要配置了相应的权限，就能访问和操作，没有体现出优先级</p>
	 * <p>默认值为true</p>
	 * <p>@see SysPermissionService.doMerge()</p>
	 */
	public static final boolean PERMISSION_PRIORITY_IS_OPEN;
	static{
		 PERMISSION_PRIORITY_IS_OPEN = Boolean.valueOf(ResourceHandlerUtil.initConfValue("permission.priority.is.open", "true"));
	}
}
