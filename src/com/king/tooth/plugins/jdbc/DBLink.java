package com.king.tooth.plugins.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 数据库链接对象
 * @author DougLei
 */
public class DBLink {

	/**
	 * 数据库类型
	 */
	private String dbType;
	/**
	 * session
	 */
	private Session session;
	
	/**
	 * 根据动态数据库对象，获取dblink实例
	 * @param sessionFactory 
	 */
	public DBLink(){
		this.dbType = HibernateUtil.getCurrentDatabaseType();
		this.session = HibernateUtil.getCurrentThreadSession();
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
		Log4jUtil.debug("执行{}数据库的ddl-sql语句为：{}", dbType, Arrays.toString(ddlSqlArr));
		
		session.doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				Statement st = null;
				StringBuilder errSql = new StringBuilder();
				try {
					st = connection.createStatement();
					for (String ds : ddlSqlArr) {
						if(StrUtils.notEmpty(ds)){
							errSql.setLength(0);
							errSql.append(ds);
							st.executeUpdate(ds);
						}
					}
				} catch (SQLException e) {
					throw new SQLException(ExceptionUtil.getErrMsg(e) + " ====> 发生错误的ddl-sql语句为:["+errSql.toString()+"]");
				}finally{
					CloseUtil.closeDBConn(st);
				}
			}
		});
		return null;
	}
	
	/**
	 * 获取数据库类型
	 * @return
	 */
	public String getDBType(){
		return dbType;
	}
	
	/**
	 * 获取目前dblink支持的数据库
	 * @return
	 */
	public String[] getDatabaseTypes() {
		return new String[]{DynamicDataConstants.DB_TYPE_ORACLE, DynamicDataConstants.DB_TYPE_SQLSERVER};
	}
}
