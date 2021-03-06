package com.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import com.api.cache.SysContext;
import com.api.constants.EncodingConstants;

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
	
	/**
	 * 解析出请求体
	 * @param request
	 * @return
	 */
	public static Object analysisFormData(HttpServletRequest request){
		if(request.getContentLength() <= 0){
			return null;
		}
		StringBuilder formData = new StringBuilder();
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(request.getInputStream(), EncodingConstants.UTF_8);
			br = new BufferedReader(reader);
			String tmp = null;
			while((tmp = br.readLine()) != null){
				formData.append(tmp);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("[HttpHelperUtil.analysisFormData]方法，在解析出请求体时，出现异常信息："+ ExceptionUtil.getErrMsg(e));
		}finally{
			CloseUtil.closeIO(br, reader);
		}
		return formData;
	}
	
	/**
	 * 获取请求的url
	 * @param request
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request){
		if(SysContext.WEB_SYSTEM_ROOT_WEBSITE == null){
			SysContext.WEB_SYSTEM_ROOT_WEBSITE = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}
		return SysContext.WEB_SYSTEM_ROOT_WEBSITE;
	}
	
	public static String getScheme(HttpServletRequest request){
		return request.getScheme();
	}
	public static String getServerName(HttpServletRequest request){
		return request.getServerName();
	}
	public static int getServerPort(HttpServletRequest request){
		return request.getServerPort();
	}
	public static String getContextPath(HttpServletRequest request){
		return request.getContextPath();
	}
}
