package com.api.constants;



/**
 * 资源信息常量
 * @author DougLei
 */
public class ResourceInfoConstants {

	/**
	 * 内置资源标识
	 * <p>系统内置资源的refResourceId的值为内置资源标识</p>
	 */
	public static final String BUILTIN_RESOURCE = "builtinResource";
	/**
	 * 1：[表]资源类型
	 */
	public static final Integer TABLE = 1;
	/**
	 * 2：[sql脚本]资源类型
	 */
	public static final Integer SQL = 2;
	/**
	 * 3：[代码]资源类型
	 */
	public static final Integer CODE = 3;
	/**
	 * 4：[业务模型]资源类型
	 */
	public static final Integer BUSINESS_MODEL = 4;
	
	// ----------------------------------------------------------------------------
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
	
	// ----------------------------------------------------------------------------
	/**
	 * 系统内置的列名
	 */
	public static final String[] BUILTIN_COLUMN_NAMES = {"id", "customer_id", "project_id", "create_date", "last_update_date", "create_user_id", "last_update_user_id"};
	/**
	 * 系统内置的属性名
	 */
	public static final String[] BUILTIN_PROP_NAMES = {ResourcePropNameConstants.ID, "customerId", "projectId", "createDate", "lastUpdateDate", "createUserId", "lastUpdateUserId"};
	
	// ----------------------------------------------------------------------------
	/**
	 * 1:文件导入
	 */
	public static final Integer FILE_IMPORT = 1;
	/**
	 * 1:文件导出
	 */
	public static final Integer FILE_EXPORT = 2;
}
