package com.king.tooth.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统上下文
 * <p>后期考虑加入到redis之类的缓存中，目前还不确定，先放到这里</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysContext implements Serializable{
	
	/**
	 * web系统在磁盘中的根目录
	 * <p>例如：C:\devlopment\MyTomcat\apache-tomcat-7.0.63\webapps\项目名\</p>
	 */
	public transient static String WEB_SYSTEM_CONTEXT_REALPATH;
	/**
	 * web系统的根地址
	 * <p>例如：http://localhost:8080/api.cfg/</p>
	 */
	public transient static String WEB_SYSTEM_ROOT_WEBSITE;
	
	/**
	 * 存储系统的参数配置
	 */
	private transient static final Map<String, String> SYSTEM_PROPERTIES = new HashMap<String, String>();
	
	/**
	 * 获取系统的配置信息集合
	 * @param key
	 * @return
	 */
	public static Map<String, String> getSystemProperties(){
		return SYSTEM_PROPERTIES;
	}
	
	/**
	 * 获取系统的配置信息
	 * @param key
	 * @return
	 */
	public static String getSystemConfig(String key){
		return SYSTEM_PROPERTIES.get(key);
	}
	
	/**
	 * 存储系统的配置信息
	 * @param key
	 * @return
	 */
	public static void setSystemConfig(String key, String value){
		SYSTEM_PROPERTIES.put(key, value);
	}
}
