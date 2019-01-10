package com.api.listener;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.util.Assert;

import com.api.cache.SysContext;
import com.api.util.PropertiesUtil;
import com.api.util.StrUtils;

/**
 * 加载系统配置文件Listener
 * @author DougLei
 */
public class LoadPropertiesFileListener implements ServletContextListener {

	private void loadProperties(String propertiesPath, Map<String, String> param){
		boolean result = false;
		Properties prop = PropertiesUtil.loadPropertiesFile(propertiesPath);
		if(prop != null){
			Set<Object> keys = prop.keySet();
			for (Object key : keys) {
				param.put(key+"", prop.get(key)+"");
			}
			result = true;
		}
		Assert.isTrue(result, "load system properties file [" + propertiesPath + "], result is " + result);
	}
	
	public void contextInitialized(ServletContextEvent sc) {
		loadProperties("api.platform.basic.properties", SysContext.getSystemProperties());
		String importProperties = SysContext.getSystemConfig("imports");
		if(StrUtils.notEmpty(importProperties)){
			String[] propertiesArr = importProperties.split(",");
			for (int i = 0; i < propertiesArr.length; i++) {
				loadProperties(propertiesArr[i], SysContext.getSystemProperties());
				propertiesArr[i] = null;
			}
			propertiesArr = null;
		}
	}
	
	public void contextDestroyed(ServletContextEvent sc) {
		SysContext.getSystemProperties().clear();
	}
}
