package com.api.plugins.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.api.sys.builtin.data.BuiltinDatabaseData;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.CloseUtil;
import com.api.util.ExceptionUtil;
import com.api.util.Log4jUtil;
import com.api.util.StrUtils;

/**
 * 数据库链接对象
 * @author DougLei
 */
public class DBLink {

	/**
	 * 数据库
	 */
	private CfgDatabase database;
	private boolean connectionFromCurrentThread;
	
	private boolean isOracle;
	private boolean isSqlServer;
	
	/**
	 * 根据动态数据库对象，获取dblink实例
	 * <p>连接从当前线程中获取</p>
	 * @param sessionFactory 
	 */
	public DBLink(CfgDatabase database){
		this(database, true);
	}
	
	/**
	 * 根据动态数据库对象，获取dblink实例
	 * @param sessionFactory 
	 * @param connectionFromCurrentThread 连接是否从当前线程中获取 
	 */
	public DBLink(CfgDatabase database, boolean connectionFromCurrentThread){
		if(database == null){
			throw new NullPointerException("创建DBLink实例时，传入的database对象不能为空");
		}
		this.database = database;
		this.connectionFromCurrentThread = connectionFromCurrentThread;
		
		String dbType = database.getType();
		isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType);
		isSqlServer = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType);
		if(!isOracle && !isSqlServer){
			throw new IllegalArgumentException("系统目前不支持["+dbType+"]类型的数据库操作");
		}
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
		String errorDDLSql = null;
		try {
			connection = getConnection();
			st = connection.createStatement();
			for (String ds : ddlSqlArr) {
				if(StrUtils.notEmpty(ds)){
					// 日志记录发出的ddl语句
					CurrentThreadContext.toReqLogDataAddOperSqlLog(ds, null);
					errorDDLSql = ds;
					st.executeUpdate(ds);
				}
			}
		} catch (Exception e) {     
			String errMessage = "[DBLink.executeDDL]发生错误的ddl-sql语句为:["+errorDDLSql+"]，错误信息为："+ExceptionUtil.getErrMsg(e);
			Log4jUtil.info(errMessage);
			throw new IllegalArgumentException(errMessage);
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
	public String executeUpdate(final String... updateSqlArr){
		if(updateSqlArr == null || updateSqlArr.length == 0){
			Log4jUtil.debug("没有要执行的update-sql语句");
			return "没有要执行的update-sql语句";
		}
		Log4jUtil.debug("执行{}数据库的update-sql语句为：{}", database.getType(), Arrays.toString(updateSqlArr));
		
		Connection connection = null;
		Statement st = null;
		StringBuilder errSql = new StringBuilder();
		try {
			connection = getConnection();
			for (String us : updateSqlArr) {
				if(StrUtils.notEmpty(us)){
					st = connection.createStatement();
					errSql.setLength(0);
					errSql.append(us);
					// 日志记录发出的update语句
					CurrentThreadContext.toReqLogDataAddOperSqlLog(us, null);
					st.executeUpdate(us);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(" ====> 发生错误的ddl-sql语句为:["+errSql.toString()+"]，错误信息为："+ExceptionUtil.getErrMsg(e));
		}finally{
			CloseUtil.closeDBConn(st, connection);
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
	 * 是否是oracle数据库
	 * @return
	 */
	public boolean isOracle() {
		return isOracle;
	}

	/**
	 * 是否是sqlserver数据库
	 * @return
	 */
	public boolean isSqlServer() {
		return isSqlServer;
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
		Connection connection = null;
		if(connectionFromCurrentThread){
			connection = CurrentThreadContext.getConnectionInstance();
		}else{
			Class.forName(database.getDriverClass());
			connection = DriverManager.getConnection(database.getUrl(), database.getLoginUserName(), database.getLoginPassword());
		}
		return connection;
	}
	
	/**
	 * 执行原生数据库操作
	 * @param execute
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void doExecute(IExecute execute){
		try {
			execute.execute(getConnection());
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
