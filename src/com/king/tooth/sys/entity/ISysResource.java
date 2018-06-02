package com.king.tooth.sys.entity;

/**
 * 系统资源接口
 * @author DougLei
 */
public interface ISysResource {
	/**
	 * 资源类型
	 * 1：表资源类型
	 */
	public static final int TABLE_RESOURCE_TYPE = 1;
	/**
	 * 资源类型
	 * 2：sql脚本资源类型
	 */
	public static final int SQLSCRIPT_RESOURCE_TYPE = 2;
	/**
	 * 资源类型
	 * 3：代码资源类型
	 */
	public static final int CODE_RESOURCE_TYPE = 3;
	/**
	 * 资源类型
	 * 4：数据库资源类型
	 */
	public static final int DATABASE_RESOURCE_TYPE = 4;
	
	/**
	 * 资源状态
	 * 1:启用
	 */
	public static final int ENABLED_RESOURCE_STATUS = 1;
	/**
	 * 资源状态
	 * 2:禁用
	 */
	public static final int UNENABLED_RESOURCE_STATUS = 2;
	
	/**
	 * 获取资源类型
	 * @return
	 */
	public int getResourceType();
	
	/**
	 * 获取资源的状态
	 * @return
	 */
	public int getIsEnabled();
	
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
}
