package com.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties操作工具类
 * @author DougLei
 */
public class PropertiesUtil {
	
	/**
	 * 根据properties文件的类相对路径，获取properties实例
	 * @param propertiesPath
	 * @return
	 */
	public static Properties loadPropertiesFile(String propertiesPath){
		InputStream propertiesInputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesPath);
		Properties properties = new Properties();
		try {
			properties.load(propertiesInputStream);
		} catch (IOException e) {
			Log4jUtil.debug("加载{}配置文件，出现异常！异常信息为：{}", propertiesPath, ExceptionUtil.getErrMsg(e));
			return null;
		}finally{
			CloseUtil.closeIO(propertiesInputStream);
		}
		return properties;
	}
}
