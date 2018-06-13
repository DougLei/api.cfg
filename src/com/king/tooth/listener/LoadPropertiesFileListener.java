package com.king.tooth.listener;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.util.Assert;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.sys.service.init.app.InitAppSystemService;
import com.king.tooth.sys.service.init.cfg.InitSystemService;
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
		// 初始化资源处理器配置
		ProcesserConfig.initResourceProcesserConfig();
		// 初始化路由解析规则配置
		RouteBodyAnalysis.initRouteRuleConfig();
		// 初始化系统内置查询条件函数配置
		BuiltinQueryCondFuncUtil.initBuiltinQueryCondFuncConfig();
		// 初始化系统核心表信息
		initSysCoreTableInfos();
		// 因为gsql第一次加载很慢，所以放到系统启动时，进行初次加载
		initGSqlParser();
	}
	
	/**
	 * 初始化系统核心表信息
	 */
	private void initSysCoreTableInfos() {
		if("1".equals(SysConfig.getSystemConfig("current.sys.type"))){// 是配置系统
			if("true".equals(SysConfig.getSystemConfig("is.init.baisc.data"))){
				new InitSystemService().loadSysBasicDatasBySysFirstStart();
			}else{
				new InitSystemService().loadSysBasicDatasByStart();
			}
		}else if("2".equals(SysConfig.getSystemConfig("current.sys.type"))){// 是运行系统
			// 系统启动时，初始化配置数据库的表和所有基础数据
			new InitAppSystemService().loadSysBasicDatasBySysStart();
		}
	}

	/**
	 * 因为gsql第一次加载很慢，所以放到系统启动时，进行初次加载
	 */
	private void initGSqlParser() {
		String dbType = SysConfig.getSystemConfig("jdbc.dbType");
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)){
			new TGSqlParser(EDbVendor.dbvoracle);
		}else if(DynamicDataConstants.DB_TYPE_SQLSERVER.equals(dbType)){
			new TGSqlParser(EDbVendor.dbvmssql);
		}else{
			throw new IllegalArgumentException("目前系统不支持 ["+dbType+"]类型的数据库sql脚本解析");
		}
	}

	public void contextDestroyed(ServletContextEvent sc) {
		SysConfig.getSystemProperties().clear();
	}
}
