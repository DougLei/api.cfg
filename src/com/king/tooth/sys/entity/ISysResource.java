package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.common.ComSysResource;

/**
 * 系统资源接口
 * @author DougLei
 */
public interface ISysResource {
	
	/**
	 * 1：表资源类型
	 */
	public static final int TABLE = 1;
	/**
	 * 2：sql脚本资源类型
	 */
	public static final int SQLSCRIPT = 2;
	/**
	 * 3：代码资源类型
	 */
	public static final int CODE = 3;
	/**
	 * 4：数据库资源类型
	 */
	public static final int DATABASE = 4;
	/**
	 * 5：项目资源类型
	 */
	public static final int PROJECT = 5;
	
	/**
	 * 请求资源的方式：GET
	 */
	public static final String GET = "get";
	/**
	 * 请求资源的方式：POST
	 */
	public static final String POST = "post";
	/**
	 * 请求资源的方式：PUT
	 */
	public static final String PUT = "put";
	/**
	 * 请求资源的方式：DELETE
	 */
	public static final String DELETE = "delete";
	/**
	 * 请求资源的方式：ALL
	 * 支持get/post/put/delete
	 */
	public static final String ALL = "all";
	/**
	 * 请求资源的方式：NONE
	 * 都不支持
	 */
	public static final String NONE = "none";

	/**
	 * 转换为资源对象
	 */
	public ComSysResource turnToResource();
}
