package com.king.tooth.listener;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.service.InitCfgSystemService;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.king.tooth.web.processer.ProcesserConfig;
import com.king.tooth.web.servlet.route.RouteBodyAnalysis;

/**
 * 初始化系统数据Listener
 * @author DougLei
 */
public class InitSysDataListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sc) {
		// 获取项目在磁盘的根目录
		SysConfig.WEB_SYSTEM_CONTEXT_REALPATH = sc.getServletContext().getRealPath(File.separator);
		
		// 获取项目类型，配置系统(1)/运行系统(2)
		SysConfig.isConfSys = "1".equals(sc.getServletContext().getInitParameter("currentSysType"));
		SysConfig.isAppSys = !SysConfig.isConfSys;
		
		// 初始化资源处理器配置
		ProcesserConfig.initResourceProcesserConfig();
		
		// 初始化路由解析规则配置
		RouteBodyAnalysis.initRouteRuleConfig();
		
		// 初始化系统内置查询条件函数配置
		BuiltinQueryCondFuncUtil.initBuiltinQueryCondFuncConfig();
		
		// 初始化系统代码资源映射
		CodeResourceMapping.initBuiltinQueryCondFuncConfig();
		
		// 初始化系统核心数据信息
		initSysCoreDataInfos();
		
		// 因为gsql第一次加载很慢，所以放到系统启动时，进行初次加载
		initGSqlParser();
	}
	
	/**
	 * 初始化是配置系统核心数据信息
	 */
	private void initSysCoreDataInfos() {
		if("true".equals(SysConfig.getSystemConfig("is.init.baisc.data"))){
			new InitCfgSystemService().loadSysBasicDatasBySysFirstStart();
		}else{
			new InitCfgSystemService().loadHbmsByStart();
		}
	}

	/**
	 * 因为gsql第一次加载很慢，所以放到系统启动时，进行初次加载
	 */
	private void initGSqlParser() {
		String dbType = SysConfig.getSystemConfig("jdbc.dbType");
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			new TGSqlParser(EDbVendor.dbvoracle);
		}else if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType)){
			new TGSqlParser(EDbVendor.dbvmssql);
		}else{
			throw new IllegalArgumentException("目前系统不支持 ["+dbType+"]类型的数据库sql脚本解析");
		}
	}

	public void contextDestroyed(ServletContextEvent sc) {
	}
}
