package com.king.tooth.constants;

/**
 * 资源的名称常量
 * @author DougLei
 */
public class ResourceNameConstants {
	
	/**
	 * 主资源的别名
	 */
	public static final String ALIAS_PARENT_RESOURCE="p_";
	/**
	 * 子资源的别名
	 */
	public static final String ALIAS_RESOURCE="s_";
	/**
	 * 数据关联资源的别名
	 */
	public static final String ALIAS_DATA_LINK_RESOURCE="d_";
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * 关联的项目主键的属性名称
	 */
	public static final String PROJECT_ID = "projectId";
	/**
	 * 主键的属性名称
	 */
	public static final String ID = "Id";
	/**
	 * 创建时间的属性名称
	 */
	public static final String CREATE_TIME = "createTime";
	/**
	 * 创建人主键的属性名称
	 */
	public static final String CREATE_USER_ID = "createUserId";
	/**
	 * 最后修改人主键的属性名称
	 */
	public static final String LAST_UPDATED_USER_ID = "lastUpdatedUserId";
	/**
	 * 最后修改时间的属性名称
	 */
	public static final String LAST_UPDATE_TIME = "lastUpdateTime";
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * 父级字段的属性名称
	 */
	public static final String PARENT_ID = "parentId";
	
	/**
	 * 子节点集合的属性名称
	 */
	public static final String CHILDREN = "children";
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * 左ID的属性名称
	 */
	public static final String LEFT_ID = "leftId";
	
	/**
	 * 右ID的属性名称
	 */
	public static final String RIGHT_ID = "rightId";
	
	/**
	 * 排序的属性名称
	 */
	public static final String ORDER_CODE = "orderCode";
	
	/**
	 * 左资源名的属性名称
	 */
	public static final String LEFT_RESOURCE_NAME = "leftResourceName";
	
	/**
	 * 右资源名的属性名称
	 */
	public static final String RIGHT_RESOURCE_NAME = "rightResourceName";
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * 父子资源关联关系 的 关系表名的前缀
	 */
	public static final String DATALINK_TABLENAME_PREFIX = "DL_";
	/**
	 * 父子资源关联关系 的 关系表名的后缀
	 */
	public static final String DATALINK_TABLENAME_SUFFIX = "_LINKS";
	/**
	 * 父子资源关联关系 的 关系资源名的后缀
	 */
	public static final String DATALINK_RESOURCENAME_SUFFIX = "Links";
	/**
	 * 通用关联关系资源名
	 */
	public static final String COMMON_DATALINK_RESOURCENAME = "ComDataLinks";
	/**
	 * 通用关联关系表名
	 */
	public static final String COMMON_DATALINK_TABLENAME = "COM_DATA_LINKS";
	
	//-----------------------------------------------------------------------------------------------
	/**
	 * 请求日志记录对象实例，在request中存储的key名称
	 */
	public static final String REQ_LOG_KEY = "reqLog";
}
