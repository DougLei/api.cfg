package com.api.util.datatype;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 数据类型验证工具类
 * @author DougLei
 */
public class DataTypeValidUtil {
	
	/**
	 * 是否是整型
	 * @param val
	 * @return
	 */
	public static boolean isInteger(Object val){
		if(val != null){
			if(val instanceof Integer){
				return true;
			}
			if(integerTypePattern.matcher(val.toString()).matches()){
				return true;
			}
		}
		return false;
	}
	/** 判断整型格式的正则表达式 */
	private static final Pattern integerTypePattern = Pattern.compile("(\\+|-)?[0-9]+");
	
	/**
	 * 是否是浮点型
	 * @param val
	 * @return
	 */
	public static boolean isBigDecimal(Object val){
		if(val != null){
			if(val instanceof BigDecimal){
				return true;
			}
			if(doubleTypePattern.matcher(val.toString()).matches()){
				return true;
			}
		}
		return false;
	}
	/** 判断浮点型格式的正则表达式 */
	private static final Pattern doubleTypePattern = Pattern.compile("(\\+|-)?[0-9]+.[0-9]+");
	
	/**
	 * 是否是数字类型
	 * <p>整型/浮点型</p>
	 * @param val
	 * @return
	 */
	public static boolean isNumber(Object val){
		return isInteger(val) || isBigDecimal(val);
	}
	
	/**
	 * 是否是boolean
	 * @param val
	 * @return
	 */
	public static boolean isBoolean(Object val){
		if(val != null){
			if(val instanceof Boolean){
				return true;
			}
			
			String valStr = val.toString();
			if("true".equals(valStr) || "false".equals(valStr)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是date
	 * @param val
	 * @return
	 */
	public static boolean isDate(Object val) {
		if(val != null){
			if(val instanceof Date || val instanceof java.sql.Date || val instanceof java.sql.Timestamp){
				return true;
			}
			
			String valStr = val.toString();
			if(dateTypePattern.matcher(valStr).matches()){
				return true;
			}
			if(timeZoneTypePattern.matcher(valStr).matches()){
				return true;
			}
			return false;
		}
		return false;
	}
	/** 判断日期格式的正则表达式 */
	private static final Pattern dateTypePattern = Pattern.compile("[0-9]{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1])( ([0-9]|0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[0-5][0-9]):(0[0-9]|[0-5][0-9]))?");
	private static final Pattern timeZoneTypePattern = Pattern.compile("[0-9]{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1])T([0-9]|0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[0-5][0-9]):(0[0-9]|[0-5][0-9]).[0-9][0-9][0-9]( )?Z");
}
