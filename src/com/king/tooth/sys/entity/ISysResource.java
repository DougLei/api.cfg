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
	 * 获取资源类型
	 * @return
	 */
	public int getResourceType();
	/**
	 * 获取资源名
	 * @return
	 */
	public String getResourceName();
}
