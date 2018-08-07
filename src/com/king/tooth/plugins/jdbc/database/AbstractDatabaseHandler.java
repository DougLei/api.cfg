package com.king.tooth.plugins.jdbc.database;

import com.king.tooth.sys.entity.cfg.CfgDatabase;


/**
 * 数据库操作的抽象类(创建/删除)
 * @author DougLei
 */
public abstract class AbstractDatabaseHandler {

	/**
	 * 操作数据库的sql
	 * 创建或删除
	 */
	protected final StringBuilder operDatabaseSql = new StringBuilder();
	
	/**
	 * 组装创建数据库的sql语句
	 * @param database
	 * @return
	 */
	public abstract String installCreateDatabaseSql(CfgDatabase database);
	
	/**
	 * 组装删除数据库的sql语句
	 * @param database
	 * @return
	 */
	public abstract String installDropDatabaseSql(CfgDatabase database);
}
