package com.king.tooth.util;

import javax.servlet.http.HttpServletRequest;

/**
 * http工具类
 * @author DougLei
 */
public class HttpHelperUtil {
	
	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request){
		String xForwardedFor = request.getHeader("x-forwarded-for");
		if(xForwardedFor == null){
			return request.getRemoteAddr();
		}
		// 如果是通过代理访问，会在request请求header中，用x-forwarded-for存储真实客户端的ip
		return xForwardedFor;
	}
}
