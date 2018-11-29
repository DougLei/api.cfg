package com.king.tooth.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.king.tooth.constants.EncodingConstants;

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
	
	/**
	 * 计算字符串的长度
	 * <p>如果是汉字等，长度解析为2</p>
	 * @param string
	 * @return
	 */
	public static int calcStrLength(String str) {
		if(isEmpty(str)){
			return 0;
		}
		int len = str.length();
		Matcher matcher = doubleCharPattern.matcher(str);
		while(matcher.find()){
			len++;
		}
		return len;
	}
	/** 匹配双字节字符的正则表达式，包括汉字 */
	private static final Pattern doubleCharPattern = Pattern.compile("[^x00-xff]");
	// [\u4e00-\u9fa5]  匹配汉字的正则表达式
	
	
	// --------------------------------------------------------------------------------------------------
	/**
	 * 根据byte数组，获取字符串内容
	 * <p>默认为utf-8编码格式</p>
	 * @param byteArray
	 * @return
	 */
	public static String getStringByByteArray(byte[] byteArray){
		return getStringByByteArray(byteArray, null);
	}
	
	/**
	 * 根据byte数组，获取字符串内容
	 * @param byteArray
	 * @param encodingConstants
	 * @return
	 */
	public static String getStringByByteArray(byte[] byteArray, String encodingConstants){
		if(byteArray != null && byteArray.length > 0){
			try {
				if(StrUtils.isEmpty(encodingConstants)){
					encodingConstants = EncodingConstants.UTF_8;
				}
				return new String(byteArray, encodingConstants);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("在将byte数组转换成"+encodingConstants+"编码格式的字符串时出现异常：" + ExceptionUtil.getErrMsg(e));
			}
		}
		return null;
	}
	
}
