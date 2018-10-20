package com.king.tooth.constants;

/**
 * 资源属性名的名称常量
 * @author DougLei
 */
public class ResourcePropNameConstants {
	
	/**
	 * 主键的属性名称
	 */
	public static final String ID = "Id";
	
	/**
	 * 聚焦操作
	 */
	public static final String FOCUSED_OPER = "$focusedOper$";
	
	/**
	 * hql查询语句，返回的类型对象 $type$
	 * <p>hql查询语句，返回的$type$指明了查询的资源名，例如from SysAccount，则返回的$type$=SysAccount</p>
	 */
	public static final String HQL_QUERY_RETURN_TYPE_PROP = "$type$";
}
