package com.king.tooth.constants;

/**
 * 数据类型常量
 * @author DougLei
 */
public class DataTypeConstants {
	
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
	
	/**
	 * hibernate的timestamp数据类型
	 */
	public static final String HIBERNATE_TIMESTAMP="timestamp";

	/**
	 * 获取数据类型
	 * @param dataType
	 * @return
	 */
	public static String getDataType(String dataType) {
		if(STRING.equalsIgnoreCase(dataType)){
			return STRING;
		}else if(BOOLEAN.equalsIgnoreCase(dataType)){
			return BOOLEAN;
		}else if(INTEGER.equalsIgnoreCase(dataType)){
			return INTEGER;
		}else if(DOUBLE.equalsIgnoreCase(dataType)){
			return DOUBLE;
		}else if(DATE.equalsIgnoreCase(dataType)){
			return DATE;
		}else if(CLOB.equalsIgnoreCase(dataType)){
			return CLOB;
		}else if(BLOB.equalsIgnoreCase(dataType)){
			return BLOB;
		}
		throw new IllegalArgumentException("填写的数据类型格式不正确，请检查。系统内置的数据类型值有：[" + toDataTypeDesc() + "]");
	}
	
	private static String toDataTypeDesc(){
		return STRING + "," + BOOLEAN + "," + INTEGER + "," + DOUBLE + "," + DATE + "," + CLOB + "," + BLOB;
	}
	
}
