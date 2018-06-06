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
	public transient static final ComDatabase currentSysDatabaseInstance = new ComDatabase(); 
	static{
		currentSysDatabaseInstance.setId(SysConfig.getSystemConfig("current.sys.database.id"));
		currentSysDatabaseInstance.setDbType(SysConfig.getSystemConfig("jdbc.dbType"));
		currentSysDatabaseInstance.setDbInstanceName(SysConfig.getSystemConfig("db.default.instancename"));
		currentSysDatabaseInstance.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
		currentSysDatabaseInstance.setLoginPassword(SysConfig.getSystemConfig("jdbc.password"));
		currentSysDatabaseInstance.setDbIp(SysConfig.getSystemConfig("db.default.ip"));
		currentSysDatabaseInstance.setDbPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
	}
	
	/**
	 * 当前系统项目对象实例
	 */
	public transient static final ComProject currentSysProjectInstance = new ComProject(); 
	static{
		currentSysProjectInstance.setId(SysConfig.getSystemConfig("current.sys.project.id"));
	}
}
