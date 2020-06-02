package com.douglei.tools.utils;

/**
 * 
 * @author DougLei
 */
public class RegularExpressionUtil {
	private static final char[] regular_expression_keys = {'$', '(', ')', '*', '+', '.', '[', '?', '\\', '^', '{', '|'};
	
	/**
	 * 对字符串中正则表达式关键字的字符进行转义, 前加\
	 * @param str
	 * @return
	 */
	public static String transferRegularExpressionKey(String str) {
		StringBuilder sp = new StringBuilder(str.length()*2);
		char s;
		for(byte i=0;i<str.length();i++) {
			s = str.charAt(i);
			for(char k : regular_expression_keys) {
				if(s == k) {
					sp.append('\\');
					break;
				}
			}
			sp.append(s);
		}
		if(sp.length() == str.length()) {
			return str;
		}
		return sp.toString();
	}
	
	/**
	 * 指定字符, 是否是正则表达式关键字
	 * @param c
	 * @return
	 */
	public static boolean isRegularExpressionKey(char c) {
		for(char k : regular_expression_keys) {
			if(k == c) 
				return true;
		}
		return false;
	}
	
	/**
	 * 指定字符串, 是否包含正则表达式关键字
	 * @param str
	 * @return
	 */
	public static boolean includeRegularExpressionKey(String str) {
		char s;
		for(byte i=0;i<str.length();i++) {
			s = str.charAt(i);
			for(char k : regular_expression_keys) {
				if(s == k) 
					return true;
			}
		}
		return false;
	}
	
}
