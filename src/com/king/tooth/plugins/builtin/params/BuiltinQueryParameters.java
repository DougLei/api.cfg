package com.king.tooth.plugins.builtin.params;

/**
 * 系统内置的查询参数
 * @author DougLei
 */
public class BuiltinQueryParameters {
	
	/**
	 * url:_ids
	 */
	public static final String _IDS = "_ids";
	/**
	 * url:_resource_name
	 */
	public static final String RESOURCE_NAME = "_resource_name";
	/**
	 * url:_resource_id
	 */
	public static final String RESOURCE_ID = "_resource_id";
	/**
	 * url:_parent_resource_name
	 */
	public static final String PARENT_RESOURCE_NAME = "_parent_resource_name";
	/**
	 * url:_parent_resource_id
	 */
	public static final String PARENT_RESOURCE_ID = "_parent_resource_id";

	
	
	
	/**
	 * 判断参数是否是系统内置参数
	 * @param parameterName
	 * @return
	 */
	public static boolean isBuiltinQueryParams(String parameterName) {
		return false;
	}
	/**
	 * 获取内置参数对应的值
	 * @param parameterName
	 * @return
	 */
	public static Object getBuiltinQueryParamValue(String parameterName) {
		return null;
	}
}
