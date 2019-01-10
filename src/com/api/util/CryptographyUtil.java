package com.api.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密、解密工具类
 * @author DougLei
 */
public class CryptographyUtil {
	
	/**
	 * base64加密算法
	 * @param str
	 * @return
	 */
	public static String encodeBase64(String str){
		return Base64.encodeToString(str.getBytes());
	}
	
	/**
	 * base64解密算法
	 * @param str
	 * @return
	 */
	public static String decodeBase64(String str){
		return Base64.decodeToString(str);
	}
	
	/**
	 * md5加密算法【加盐】
	 * 将原密码和指定的salt组合加密，防止[md5加密后，可能通过暴力碰撞破解]
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String encodeMd5(String str, String salt){
		return new Md5Hash(str, salt).toString();
	}
	
	/**
	 * md5加密算法
	 * @param str
	 * @return
	 */
	public static String encodeMd5(String str){
		return new Md5Hash(str).toString();
	}
}
