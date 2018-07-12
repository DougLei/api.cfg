package com.king.tooth.sys.builtin.data;

import java.util.Date;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.util.DateUtil;

/**
 * 系统内置的数据对象
 * @author DougLei
 */
public class BuiltinDatas {
	
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
	
	/**
	 * 数据的有消息
	 */
	public transient static final Date validDate = DateUtil.parseDate("2099-12-31 23:59:59");
}
