package com.king.tooth.constants;

import com.king.tooth.sys.entity.cfg.CfgColumn;

/**
 * 资源信息常量
 * @author DougLei
 */
public class ResourceInfoConstants {

	/**
	 * 1：表资源类型
	 */
	public static final Integer TABLE = 1;
	/**
	 * 2：sql脚本资源类型
	 */
	public static final Integer SQL = 2;
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
	 * 7：项目模块操作资源类型
	 */
	public static final Integer PROJECT_MODULE_OPERATION = 7;
	
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
	
	/**
	 * hql查询语句，返回的类型对象 $type$
	 * <p>hql查询语句，返回的$type$指明了查询的资源名，例如from SysAccount，则返回的$type$=SysAccount</p>
	 */
	public static final String HQL_QUERY_RETURN_TYPE_PROP = "$type$";
	
	// ----------------------------------------------------------------------------
	/**
	 * 查询表资源元数据信息集合的hql语句头
	 */
	public static final String queryTableMetadataInfosHqlHead = "select new map(columnName as columnName,propName as propName,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName) from CfgColumn where tableId=? and isEnabled=1 and operStatus="+CfgColumn.CREATED;
}
