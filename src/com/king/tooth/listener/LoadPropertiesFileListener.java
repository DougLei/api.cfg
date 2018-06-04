package com.king.tooth.listener;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.util.Assert;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.service.common.ComBasicDataProcessService;
import com.king.tooth.util.PropertiesUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.king.tooth.web.processer.ProcesserConfig;
import com.king.tooth.web.servlet.route.RouteBodyAnalysis;

/**
 * 加载系统配置文件
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
		// 获取项目在磁盘的根目录
		SysConfig.WEB_SYSTEM_CONTEXT_REALPATH = sc.getServletContext().getRealPath(File.separator);
		
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
		
		// 初始化系统配置
		initSystemConfig();
	}
	
	/**
	 * 初始化系统配置
	 */
	private void initSystemConfig() {
		// 初始化配置系统的项目id和数据库id映射
		ProjectIdRefDatabaseIdMapping.initBasicProjectIdRefDatabaseIdMapping();
		// 初始化资源处理器配置
		ProcesserConfig.initResourceProcesserConfig();
		// 初始化路由解析规则配置
		RouteBodyAnalysis.initRouteRuleConfig();
		// 初始化系统内置查询条件函数配置
		BuiltinQueryCondFuncUtil.initBuiltinQueryCondFuncConfig();
		// 系统启动时，初始化配置数据库的表和所有基础数据
		if("true".equals(SysConfig.getSystemConfig("is.init.baisc.data"))){
			new ComBasicDataProcessService().loadSysBasicDatasBySysFirstStart();
		}else{
			new ComBasicDataProcessService().loadSysConfDatasBySysStart();
		}
	}

	public void contextDestroyed(ServletContextEvent sc) {
		SysConfig.getSystemProperties().clear();
	}
}
