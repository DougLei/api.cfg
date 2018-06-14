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
	public static final Integer TABLE = 1;
	/**
	 * 2：sql脚本资源类型
	 */
	public static final Integer SQLSCRIPT = 2;
	/**
	 * 3：代码资源类型
	 */
	public static final Integer CODE = 3;
	/**
	 * 4：数据库资源类型
	 */
	public static final Integer DATABASE = 4;
	/**
	 * 5：项目资源类型
	 */
	public static final Integer PROJECT = 5;
	/**
	 * 6：项目模块资源类型
	 */
	public static final Integer PROJECT_MODULE = 6;
	/**
	 * 7：基础数据资源类型
	 */
	public static final Integer BASIC_DATA = 7;
	
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
	 * 所属的平台类型
	 * <p>1：配置平台</p>
	 */
	public static final int CONFIG_PLATFORM = 1;
	/**
	 * 所属的平台类型
	 * <p>2：运行平台</p>
	 */
	public static final int APP_PLATFORM = 2;
	/**
	 * 所属的平台类型
	 * <p>3：通用(这个类型由后端开发者控制)</p>
	 */
	public static final int COMMON_PLATFORM = 3;
	
	
	/**
	 * 转换为资源对象
	 */
	public ComSysResource turnToResource();
	/**
	 * 转换为要发布的资源对象
	 */
	public ComSysResource turnToPublishResource();
	
	/**
	 * 获取资源类型
	 * @return
	 */
	public Integer getResourceType();
	
	/**
	 * 获的批量发布时的消息
	 * @return
	 */
	public String getBatchPublishMsg();
}
