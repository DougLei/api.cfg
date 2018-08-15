package com.king.tooth.listener;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.util.Assert;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.util.PropertiesUtil;
import com.king.tooth.util.StrUtils;

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
		loadProperties("api.platform.basic.properties", SysConfig.getSystemProperties());
		String importProperties = SysConfig.getSystemConfig("imports");
		if(StrUtils.notEmpty(importProperties)){
			String[] propertiesArr = importProperties.split(",");
			for (int i = 0; i < propertiesArr.length; i++) {
				loadProperties(propertiesArr[i], SysConfig.getSystemProperties());
				propertiesArr[i] = null;
			}
			propertiesArr = null;
		}
	}
	
	public void contextDestroyed(ServletContextEvent sc) {
		SysConfig.getSystemProperties().clear();
	}
}
