package com.king.tooth.sys.builtin.data;


/**
 * 系统内置的参数key
 * @author DougLei
 */
public class BuiltinParameterKeys {
	
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
	
	//------------------------------------------------------------------------------------
	/**
	 * 存储在request中的RequestBody实例，方便在各个地方调用到
	 */
	public static final String _REQUEST_BODY_KEY = "_requestBody";
	/**
	 * 存储在request中的ResponseBody实例，方便在各个地方调用到
	 */
	public static final String _RESPONSE_BODY_KEY = "_responseBody";
	/**
	 * 存储在request中，标识是否需要打印出responseBody
	 * <p>目前只有在下载文件的时候，是不需要打印responseBody</p>
	 */
	public static final String _IS_PRINT_RESPONSEBODY = "_isPrintResponseBody";
}
