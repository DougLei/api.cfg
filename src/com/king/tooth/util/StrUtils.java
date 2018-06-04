package com.king.tooth.util;

import java.io.UnsupportedEncodingException;

/**
 * 字符串工具类
 * @author DougLei
 */
public class StrUtils {
	
	/**
	 * 转换字符串编码
	 * @param str
	 * @param fromEncoding 原来的编码格式
	 * @param toEncoding 要转换的编码格式
	 * @return
	 */
	public static String turnStrEncoding(String str, String fromEncoding, String toEncoding){
		try {
			return new String(str.getBytes(fromEncoding), toEncoding);
		} catch (UnsupportedEncodingException e) {
			Log4jUtil.debug("[StrUtils.encodingString]方法在转换字符串编码时出现异常：{}", ExceptionUtil.getErrMsg(e));
		}
		return null;
	}
	
	/**
	 * 是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否为空
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object){
		if(object == null || "".equals(object.toString().trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否不为空
	 * @param str
	 * @return
	 */
	public static boolean notEmpty(String str){
		if(str != null && !"".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否不为空
	 * @param object
	 * @return
	 */
	public static boolean notEmpty(Object object){
		if(object != null && !"".equals(object.toString().trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 比较两个字符串是否相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareIsSame(String str1, String str2){
		if(isEmpty(str1) || isEmpty(str2)){
			return false;
		}
		return str1.trim().equals(str2.trim());
	}
	
	/**
	 * 比较两个字符串是否不相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareUnSame(String str1, String str2){
		return !compareIsSame(str1, str2);
	}
	
	/**
	 * 忽略大小写的比较两个字符串是否相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareIsSameIgnoreCase(String str1, String str2){
		if(isEmpty(str1) || isEmpty(str2)){
			return false;
		}
		return str1.trim().equalsIgnoreCase(str2.trim());
	}
	
	/**
	 * 忽略大小写的比较两个字符串是否不相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareUnSameIgnoreCase(String str1, String str2){
		return !compareIsSameIgnoreCase(str1, str2);
	}
	
	/**
	 * 判断字符串的内容是否是null字符串
	 * <p>即String a = "null";</p>
	 * @param str
	 * @return
	 */
	public static boolean isNullStr(String str){
		if(NULL.equalsIgnoreCase(str)){
			return true;
		}
		return false;
	}
	private static final String NULL = "null";
}
