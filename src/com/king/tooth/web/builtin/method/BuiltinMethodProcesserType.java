package com.king.tooth.web.builtin.method;

/**
 * 内置函数处理器类型
 * @author DougLei
 */
public final class BuiltinMethodProcesserType {
	
	/**
	 * 聚焦函数处理器
	 */
	public static final int FOCUSED_ID = 1;
	
	/**
	 * 分页函数处理器
	 */
	public static final int PAGER_QUERY = 2;
	
	/**
	 * 查询函数处理器
	 */
	public static final int QUERY = 3;
	
	/**
	 * 查询条件函数处理器
	 */
	public static final int QUERY_COND = 4;
	
	/**
	 * 递归函数处理器
	 */
	public static final int RECURSIVE = 5;
	
	/**
	 * 排序函数处理器
	 */
	public static final int SORT = 6;
	
	/**
	 * 父子资源链接查询函数处理器
	 */
	public static final int PARENT_SUB_QUERY = 7;
	
	/**
	 * sql脚本处理器
	 */
	public static final int SQL_SCRIPT = 8;
}
