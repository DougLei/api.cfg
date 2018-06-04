package com.king.tooth.web.builtin.method.common.util.querycondfunc.entity;

import java.util.regex.Pattern;

/**
 * 查询函数参数实体接口
 * @author DougLei
 */
public interface IQueryCondFuncEntity {

	/**
	 * 值的正则表达式编译器对象
	 */
	public static final Pattern VALUES_PATTERN = Pattern.compile("(?<=\\().*(?=\\))");
	/**
	 * 注释的正则表达式编译器对象
	 */
	public static final Pattern COMMENTS_PATTERN = Pattern.compile("(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\r\n])))*\\*+/)|(//.*)");
	/**
	 * 方法的正则表达式编译器对象
	 */
	public static final Pattern METHOD_PATTERN = Pattern.compile("(?<=!?)\\w+(?=\\()");
	
	/**
	 * 方法名
	 * @return
	 */
	public String getMethodName();
	
	/**
	 * 值数组
	 * @return
	 */
	public Object[] getValues();
	
	/**
	 * 是否取反
	 * @return
	 */
	public boolean isInversion();
	
	/**
	 * 请求的属性名
	 * @return
	 */
	public String getPropName();
}
