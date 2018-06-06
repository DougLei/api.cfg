package com.king.tooth.sys.entity;

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
	 * 6：表映射资源类型
	 */
	public static final int TABLE_HBM = 6;
	
	/**
	 * GET
	 */
	public static final String GET = "get";
	/**
	 * POST
	 */
	public static final String POST = "post";
	/**
	 * PUT
	 */
	public static final String PUT = "put";
	/**
	 * DELETE
	 */
	public static final String DELETE = "delete";
	/**
	 * ALL
	 */
	public static final String ALL = "all";
	/**
	 * NONE
	 */
	public static final String NONE = "none";
	
	/**
	 * 1:配置平台
	 */
	public static final int IS_CFG_PLATFORM_TYPE = 1;
	/**
	 * 2:运行平台
	 */
	public static final int IS_APP_PLATFORM_TYPE = 2;
	/**
	 * 3:平台通用/公用
	 */
	public static final int IS_COMMON_PLATFORM_TYPE = 3;
	
	/**
	 * 获取资源类型
	 * @return
	 */
	public int getResourceType();
	/**
	 * 获取资源名
	 * @return
	 */
	public String getResourceName();
	/**
	 * 获取资源主键
	 * @return
	 */
	public String getResourceId();
	
	/**
	 * 请求资源的方式
	 * @return
	 */
	public String getReqResourceMethod();
}
