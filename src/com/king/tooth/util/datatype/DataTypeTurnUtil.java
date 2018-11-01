package com.king.tooth.util.datatype;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.util.DateUtil;

/**
 * 数据类型转换的工具类
 * @author DougLei
 */
public class DataTypeTurnUtil {

	/**
	 * 转换值数据类型
	 * @param value
	 * @param targetDataCodeType @see DataTypeConstants
	 * @param isNullStr 如果value为null，是否返回空字符串
	 * @param isCodeDateType 是否是代码时间类型，不是代码时间类型，就是sql时间类型，主要区别在于日期的转换，一个是java.util.Date，一个是java.sql.Date
	 * @param dateIsDetail 日期类型是否是详细，详细包括时分秒
	 * @return
	 */
	public static Object turnValueDataType(Object value, String targetDataCodeType, boolean isNullStr, boolean isCodeDateType, boolean dateIsDetail){
		if(value == null){
			if(isNullStr){
				return "";
			}else{
				return null;
			}
		}
		
		String valueStr = value.toString();
		if(DataTypeConstants.STRING.equals(targetDataCodeType) || DataTypeConstants.CHAR.equals(targetDataCodeType)){
			value = valueStr;
		}else if(DataTypeConstants.BOOLEAN.equals(targetDataCodeType)){
			if(DataTypeValidUtil.isBoolean(value)){
				if("true".equals(valueStr)){
					value = true;
				}else{
					value = false;
				}
			}else{
				throw new IllegalArgumentException("在转换值的数据类型时，传入的value值["+value+"]，无法转换为boolean类型");
			}
		}else if(DataTypeConstants.INTEGER.equals(targetDataCodeType)){
			if(DataTypeValidUtil.isInteger(value)){
				value = Integer.valueOf(valueStr);
			}else{
				throw new IllegalArgumentException("在转换值的数据类型时，传入的value值["+value+"]，无法转换为integer类型");
			}
		}else if(DataTypeConstants.DOUBLE.equals(targetDataCodeType)){
			if(DataTypeValidUtil.isNumber(value)){
				value = Double.valueOf(valueStr);
			}else{
				throw new IllegalArgumentException("在转换值的数据类型时，传入的value值["+value+"]，无法转换为浮点类型");
			}
		}else if(DataTypeConstants.DATE.equals(targetDataCodeType)){
			if(DataTypeValidUtil.isDate(value)){
				if(isCodeDateType){
					value = DateUtil.parseDate(valueStr);
				}else{
					if(dateIsDetail){
						value = DateUtil.parseTimestamp(valueStr);
					}else{
						value = DateUtil.parseDate(valueStr);
					}
				}
			}else{
				throw new IllegalArgumentException("在转换值的数据类型时，传入的value值["+value+"]，无法转换为date类型");
			}
		}else{
			throw new IllegalArgumentException("在转换值的数据类型时，系统目前不支持要转换的目标数据类型["+targetDataCodeType+"]，请联系后端系统开发人员");
		}
		return value;
	}
}
