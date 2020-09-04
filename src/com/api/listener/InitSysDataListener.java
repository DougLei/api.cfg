package com.api.listener;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.cache.FaceEngineContext;
import com.api.cache.SysContext;
import com.api.plugins.code.code.resource.PluginCodeResourceMapping;
import com.api.sys.builtin.data.BuiltinDatabaseData;
import com.api.sys.code.resource.CodeResourceMapping;
import com.api.sys.service.tools.InitSystemService;
import com.api.util.hibernate.HibernateUtil;
import com.api.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.api.web.processer.ProcesserConfig;
import com.api.web.servlet.route.RouteBodyAnalysis;
import com.arcsoft.face.FaceEngine;

/**
 * 初始化系统数据Listener
 * @author DougLei
 */
public class InitSysDataListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(InitSysDataListener.class);

	public void contextInitialized(ServletContextEvent sc) {
		// 获取项目在磁盘的根目录
		SysContext.WEB_SYSTEM_CONTEXT_REALPATH = sc.getServletContext().getRealPath(File.separator);
		
		// 初始化资源处理器配置
		ProcesserConfig.initResourceProcesserConfig();
		
		// 初始化路由解析规则配置
		RouteBodyAnalysis.initRouteRuleConfig();
		
		// 初始化系统内置查询条件函数配置
		BuiltinQueryCondFuncUtil.initBuiltinQueryCondFuncConfig();
		
		// 初始化系统代码资源映射
		CodeResourceMapping.initCodeResourceMapping();
		
		// ****** 初始化系统插件代码资源映射 ****** //
		PluginCodeResourceMapping.initPluginCodeResourceMapping();
		
		// 初始化系统核心数据信息
		initSysCoreDataInfos();
		
		// 因为gsql第一次加载很慢，所以放到系统启动时，进行初次加载
		initGSqlParser();
		
		// 初始化刷脸登录的引擎
		initFaceEngine();
	}
	
	/**
	 * 初始化是配置系统核心数据信息
	 */
	private void initSysCoreDataInfos() {
		if("true".equals(SysContext.getSystemConfig("is.init.baisc.data"))){
			new InitSystemService().firstStart();
		}else{
			new InitSystemService().start();
		}
	}

	/**
	 * 因为gsql第一次加载很慢，所以放到系统启动时，进行初次加载
	 */
	private void initGSqlParser() {
		String dbType = SysContext.getSystemConfig("jdbc.dbType");
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			new TGSqlParser(EDbVendor.dbvoracle);
		}else if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType)){
			new TGSqlParser(EDbVendor.dbvmssql);
		}else{
			throw new IllegalArgumentException("目前系统不支持 ["+dbType+"]类型的数据库sql脚本解析");
		}
	}
	
	/**
	 * 初始化刷脸登录的引擎
	 */
	private void initFaceEngine() {
		try {
			logger.info("先从tomcat的目录上, 加载FaceEngine: {}", SysContext.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar + "dll" + File.separatorChar + "face");
			FaceEngineContext.setFaceEngine(new FaceEngine(SysContext.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar + "dll" + File.separatorChar + "face"));
		} catch (Throwable e) {
			logger.info("从tomcat路径加载FaceEngine出现异常: {}", getExceptionDetailMessage(e));
			try {
				FaceEngineContext.setFaceEngine(new FaceEngine("D:\\workspace3\\api.cfg\\resources\\dll\\face"));
			} catch (Throwable e1) {
				logger.info("从eclipse中测试用, 使用project路径加载FaceEngine: D:\\workspace3\\api.cfg\\resources\\dll\\face, 仍然出现异常, 所以目前系统不支持使用人脸认证功能");
			}
		} finally {
			HibernateUtil.closeCurrentThreadSession();
		}
	}

	public void contextDestroyed(ServletContextEvent sc) {
	}
	
	/**
	 * 获取异常的详细信息
	 * <p>错在哪个类，哪一行</p>
	 * @param t
	 * @return
	 */
	public static String getExceptionDetailMessage(Throwable t){
		PrintWriter pw = null;
		try {
			StringWriter sw = new StringWriter();
			pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			pw.close();
			pw = null;
		}
	}
}
