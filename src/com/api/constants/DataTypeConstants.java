package com.api.constants;

import com.api.util.StrUtils;

/**
 * 内置的数据类型
 * @author DougLei
 */
public class DataTypeConstants {
	
	/**
	 * char
	 */
	public static final String CHAR = "char";
	
	/**
	 * string
	 */
	public static final String STRING = "string";
	
	/**
	 * boolean
	 */
	public static final String BOOLEAN = "boolean";
	
	/**
	 * integer
	 */
	public static final String INTEGER = "integer";
	
	/**
	 * double
	 */
	public static final String DOUBLE = "double";
	
	/**
	 * date
	 */
	public static final String DATE = "date";
	
	/**
	 * clob
	 */
	public static final String CLOB = "clob";
	
	/**
	 * blob
	 */
	public static final String BLOB = "blob";
	
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * oracle的游标类型
	 * <p>即表类型</p>
	 */
	public static final String ORACLE_CURSOR_TYPE = "sys_refcursor";
	
	// -----------------------------------------------------------------------------------------------------------------
	/**
	 * 是否是合法的数据类型，即是否是系统支持的数据类型
	 * @param dataType
	 * @return
	 */
	public static boolean isLegalDataType(String dataType){
		if(StrUtils.isEmpty(dataType)){
			return false;
		}
		dataType = dataType.trim().toLowerCase();
		if(CHAR.equals(dataType) 
				|| STRING.equals(dataType)
				|| BOOLEAN.equals(dataType)
				|| INTEGER.equals(dataType)
				|| DOUBLE.equals(dataType)
				|| DATE.equals(dataType)
				|| CLOB.equals(dataType)
				|| BLOB.equals(dataType)
				|| ORACLE_CURSOR_TYPE.equals(dataType)){
			return true;
		}
		return false;
	}
}
