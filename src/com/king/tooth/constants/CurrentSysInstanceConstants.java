package com.king.tooth.constants;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComProject;

/**
 * 当前系统实例常量
 * @author DougLei
 */
public class CurrentSysInstanceConstants {
	
	/**
	 * 当前系统的数据库对象实例
	 */
	public transient static final ComDatabase currentSysBuiltinDatabaseInstance = new ComDatabase(); 
	static{
		currentSysBuiltinDatabaseInstance.setId(SysConfig.getSystemConfig("current.sys.database.id"));
		currentSysBuiltinDatabaseInstance.setDbType(SysConfig.getSystemConfig("jdbc.dbType"));
		currentSysBuiltinDatabaseInstance.setDbInstanceName(SysConfig.getSystemConfig("db.default.instancename"));
		currentSysBuiltinDatabaseInstance.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
		currentSysBuiltinDatabaseInstance.setLoginPassword(SysConfig.getSystemConfig("jdbc.password"));
		currentSysBuiltinDatabaseInstance.setDbIp(SysConfig.getSystemConfig("db.default.ip"));
		currentSysBuiltinDatabaseInstance.setDbPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
	}
	
	/**
	 * 当前系统项目对象实例
	 */
	public transient static final ComProject currentSysBuiltinProjectInstance = new ComProject(); 
	static{
		currentSysBuiltinProjectInstance.setId(SysConfig.getSystemConfig("current.sys.project.id"));
	}
}
