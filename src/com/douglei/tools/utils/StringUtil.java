package com.douglei.tools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author StoneKing
 */
public class StringUtil {
	
	/**
	 * <pre>
	 * 	字符串是否为空
	 * 	trim()后的空字符串, 也会被判定为空
	 * </pre>
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string){
		if(string == null || string.trim().length() == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * object类型的字符串是否为空
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object){
		if(object == null || object.toString().trim().length() == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 字符串是否不为空
	 * @param string
	 * @return
	 */
	public static boolean notEmpty(String string){
		if(string == null || string.trim().length() == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * object类型的字符串是否不为空
	 * @param object
	 * @return
	 */
	public static boolean notEmpty(Object object){
		if(object == null || object.toString().trim().length() == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 	去掉前后指定的字符
	 * @param str
	 * @param c
	 * @return
	 */
	public static String trim(String str, char c) {
		int topIndex = 0;
		int bottomIndex = str.length();
		
		while(topIndex < bottomIndex && str.charAt(topIndex) == c) {
			topIndex++;
		}
		while(topIndex < bottomIndex && str.charAt(bottomIndex-1) == c) {
			bottomIndex--;
		}
		return (topIndex > 0 || bottomIndex < str.length())?str.substring(topIndex, bottomIndex):str;
	}
	
	/**
	 * <pre>
	 * 	去掉前后指定的字符
	 * 	[0]=要去掉的前面的字符, 如果没有返回null
	 * 	[1]=去掉前后字符的string
	 * 	[2]=要去掉的后面的字符, 如果没有返回null
	 * </pre>
	 * @param str
	 * @param c
	 * @return
	 */
	public static String[] trim_(String str, char c) {
		String[] result = new String[3];
		
		int topIndex = 0;
		int bottomIndex = str.length();
		
		while(topIndex < bottomIndex && str.charAt(topIndex) == c) {
			topIndex++;
		}
		while(topIndex < bottomIndex && str.charAt(bottomIndex-1) == c) {
			bottomIndex--;
		}
		
		boolean flag = false;
		if(topIndex > 0) {
			result[0] = str.substring(0, topIndex);
			flag = true;
		}
		if(bottomIndex < str.length()) {
			result[2] = str.substring(bottomIndex);
			flag = true;
		}
		result[1] = flag?str.substring(topIndex, bottomIndex):str;
		return result;
	}
	
	/**
	 * 计算字符串的长度
	 * <p>如果存在汉字，将对应的长度+1</p>
	 * @param string
	 * @return
	 */
	public static int computeStringLength(String string) {
		if(string == null){
			return 0;
		}
		int length = string.length();
		Matcher matcher = chineseCharacterPattern.matcher(string);
		while(matcher.find()){
			length++;
		}
		return length;
	}
	// 匹配汉字
	private static final Pattern chineseCharacterPattern = Pattern.compile("[\u4e00-\u9fa5]");
}
