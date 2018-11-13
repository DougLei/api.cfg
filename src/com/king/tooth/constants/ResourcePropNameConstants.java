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
	
	/**
	 * 标识从前端传递的数据对象的操作类型
	 * @see OperDataTypeConstants
	 */
	public static final String OPER_DATA_TYPE = "$operDataType$";
	
	/**
	 * 标识从前端传递的数据对象，所属资源的资源名(的key)
	 */
	public static final String DATA_REF_RESOURCENAME = "$refResourceName$";
}
