package com.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * log4j工具类
 * ERROR  -->  WARN  -->  INFO  -->  DEBUG
 * @author DougLei
 */
public class Log4jUtil {
	private static final Logger logger = LoggerFactory.getLogger(Log4jUtil.class);
	
	/**
	 * debug
	 * @param content
	 */
	public static final void debug(String content){
		if(logger.isDebugEnabled())
			logger.debug("============================> " + content);
	}
	/**
	 * debug
	 * @param content
	 * @param params
	 */
	public static final void debug(String content, Object... params){
		if(logger.isDebugEnabled())
			logger.debug("============================> " + content, params);
	}
	/**
	 * debug
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 */
	@SuppressWarnings("rawtypes")
	public static final void debug(Class clz, String methodName, String content){
		if(logger.isDebugEnabled())
			logger.debug("============================> ["+clz.getName()+"."+methodName+"()]" + content);
	}
	/**
	 * debug
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public static final void debug(Class clz, String methodName, String content, Object... params){
		if(logger.isDebugEnabled())
			logger.debug("============================> ["+clz.getName()+"."+methodName+"()]" + content, params);
	}
	
	
	/**
	 * info
	 * @param content
	 */
	public static final void info(String content){
		if(logger.isInfoEnabled())
			logger.info("============================> " + content);
	}
	/**
	 * info
	 * @param content
	 * @param params
	 */
	public static final void info(String content, Object... params){
		if(logger.isInfoEnabled())
			logger.info("============================> " + content, params);
	}
	/**
	 * info
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 */
	@SuppressWarnings("rawtypes")
	public static final void info(Class clz, String methodName, String content){
		if(logger.isInfoEnabled())
			logger.info("============================> ["+clz.getName()+"."+methodName+"()]" + content);
	}
	/**
	 * info
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public static final void info(Class clz, String methodName, String content, Object... params){
		if(logger.isInfoEnabled())
			logger.info("============================> ["+clz.getName()+"."+methodName+"()]" + content, params);
	}
	
	
	/**
	 * warn
	 * @param content
	 */
	public static final void warn(String content){
		if(logger.isWarnEnabled())
			logger.warn("============================> " + content);
	}
	/**
	 * warn
	 * @param content
	 * @param params
	 */
	public static final void warn(String content, Object... params){
		if(logger.isWarnEnabled())
			logger.warn("============================> " + content, params);
	}
	/**
	 * warn
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 */
	@SuppressWarnings("rawtypes")
	public static final void warn(Class clz, String methodName, String content){
		if(logger.isWarnEnabled())
			logger.warn("============================> ["+clz.getName()+"."+methodName+"()]" + content);
	}
	/**
	 * warn
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public static final void warn(Class clz, String methodName, String content, Object... params){
		if(logger.isWarnEnabled())
			logger.warn("============================> ["+clz.getName()+"."+methodName+"()]" + content, params);
	}
	
	
	/**
	 * error
	 * @param content
	 */
	public static final void error(String content){
		if(logger.isErrorEnabled())
			logger.error("============================> " + content);
	}
	/**
	 * error
	 * @param content
	 * @param params
	 */
	public static final void error(String content, Object... params){
		if(logger.isErrorEnabled())
			logger.error("============================> " + content, params);
	}
	/**
	 * error
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 */
	@SuppressWarnings("rawtypes")
	public static final void error(Class clz, String methodName, String content){
		if(logger.isErrorEnabled())
			logger.error("============================> ["+clz.getName()+"."+methodName+"()]" + content);
	}
	/**
	 * error
	 * @param clz 调用的类，填写调用的类名.class即可，示例：Log4jUtil.class
	 * @param methodName 调用的方法名
	 * @param content
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public static final void error(Class clz, String methodName, String content, Object... params){
		if(logger.isErrorEnabled())
			logger.error("============================> ["+clz.getName()+"."+methodName+"()]" + content, params);
	}
}
