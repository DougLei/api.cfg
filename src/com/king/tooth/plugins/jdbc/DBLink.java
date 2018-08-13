package com.king.tooth.plugins.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;

/**
 * 数据库链接对象
 * @author DougLei
 */
public class DBLink {

	/**
	 * 数据库
	 */
	private CfgDatabase database;
	
	/**
	 * 根据动态数据库对象，获取dblink实例
	 * @param sessionFactory 
	 */
	public DBLink(CfgDatabase database){
		this.database = database;
	}
	
	/**
	 * 执行ddl语句
	 * ddl是数据库模式定义语言，是用于描述数据库中要存储的现实世界实体的语言
	 * @param ddlSqlArr ddl-sql语句数组
	 * @return 
	 */
	public String executeDDL(final String[] ddlSqlArr){
		if(ddlSqlArr == null || ddlSqlArr.length == 0){
			Log4jUtil.debug("没有要执行的ddl-sql语句");
			return "没有要执行的ddl-sql语句";
		}
		Log4jUtil.debug("执行{}数据库的ddl-sql语句为：{}", database.getType(), Arrays.toString(ddlSqlArr));
		
		Connection connection = null;
		Statement st = null;
		try {
			Class.forName(database.getDriverClass());
			connection = DriverManager.getConnection(database.getUrl(), database.getLoginUserName(), database.getLoginPassword());
			st = connection.createStatement();
			for (String ds : ddlSqlArr) {
				if(StrUtils.notEmpty(ds)){
					try {
						// 执行建库建表语句错误，也要继续下去
						st.executeUpdate(ds);
					} catch (Exception e) {
						String errMessage = "[DBLink.executeDDL]发生错误的ddl-sql语句为:["+ds+"]，错误信息为："+ExceptionUtil.getErrMsg("DBLink", "executeDDL", e);
						Log4jUtil.info(errMessage);
						throw new IllegalArgumentException(errMessage);
					}
				}
			}
		} catch (Exception e) {                                                                           
			Log4jUtil.debug("[DBLink.executeDDL]" + ExceptionUtil.getErrMsg("DBLink", "executeDDL", e));
		}finally{
			CloseUtil.closeDBConn(st, connection);
		}
		return null;
	}
	
	/**
	 * 执行update语句
	 * 即增删改的sql语句
	 * @param updateSqlArr update-sql语句数组
	 * @return 
	 */
	public String executeUpdate(final String[] updateSqlArr){
		if(updateSqlArr == null || updateSqlArr.length == 0){
			Log4jUtil.debug("没有要执行的update-sql语句");
			return "没有要执行的update-sql语句";
		}
		Log4jUtil.debug("执行{}数据库的update-sql语句为：{}", database.getType(), Arrays.toString(updateSqlArr));
		
		Connection connection = null;
		PreparedStatement pst = null;
		StringBuilder errSql = new StringBuilder();
		try {
			Class.forName(database.getDriverClass());
			connection = DriverManager.getConnection(database.getUrl(), database.getLoginUserName(), database.getLoginPassword());
			for (String us : updateSqlArr) {
				if(StrUtils.notEmpty(us)){
					pst = connection.prepareStatement(us);
					errSql.setLength(0);
					errSql.append(us);
					pst.executeUpdate();
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(" ====> 发生错误的ddl-sql语句为:["+errSql.toString()+"]，错误信息为："+ExceptionUtil.getErrMsg("DBLink", "executeUpdate", e));
		}finally{
			CloseUtil.closeDBConn(pst, connection);
		}
		return null;
	}
	
	/**
	 * 获取数据库类型
	 * @return
	 */
	public String getDBType(){
		return database.getType();
	}
	
	/**
	 * 获取目前dblink支持的数据库
	 * @return
	 */
	public String[] getDatabaseTypes() {
		return new String[]{BuiltinDatabaseData.DB_TYPE_ORACLE, BuiltinDatabaseData.DB_TYPE_SQLSERVER};
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName(database.getDriverClass());
		Connection connection = DriverManager.getConnection(database.getUrl(), database.getLoginUserName(), database.getLoginPassword());
		return connection;
	}
}
