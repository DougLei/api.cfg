package com.api.plugins.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库执行
 * @author DougLei
 */
public interface IExecute {
	
	/**
	 * 执行
	 * @param conn
	 * @throws SQLException
	 */
	public void execute(Connection conn) throws SQLException;
}
