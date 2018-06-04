package com.king.tooth.constants;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.common.ComDatabase;

/**
 * 系统数据库实例的常量
 * @author DougLei
 */
public class SysDatabaseInstanceConstants {
	
	/**
	 * 配置数据库
	 */
	public transient static final ComDatabase CFG_DATABASE = new ComDatabase(); 
	
	static{
		CFG_DATABASE.setDbType(SysConfig.getSystemConfig("jdbc.dbType"));
		CFG_DATABASE.setDbInstanceName(SysConfig.getSystemConfig("db.default.instancename"));
		CFG_DATABASE.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
		CFG_DATABASE.setLoginPassword(SysConfig.getSystemConfig("jdbc.password"));
		CFG_DATABASE.setDbIp(SysConfig.getSystemConfig("db.default.ip"));
		CFG_DATABASE.setDbPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
	}
}
