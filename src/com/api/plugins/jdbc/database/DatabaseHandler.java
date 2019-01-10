package com.api.plugins.jdbc.database;

import java.util.Arrays;

import com.api.plugins.jdbc.DBLink;
import com.api.plugins.jdbc.database.impl.ADatabaseHandler;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.util.Log4jUtil;
import com.api.util.ReflectUtil;
import com.api.util.StrUtils;

/**
 * 对数据库的操作
 * 创建/删除
 * @author DougLei
 */
public class DatabaseHandler {
	
	/**
	 * 数据库连接对象
	 * 这个连接对象是数据库超级管理员的连接对象
	 * 配置在jdbc.properties中
	 */
	private DBLink dblink;
	/**
	 * 数据库的操作对象
	 * 创建/删除
	 */
	private ADatabaseHandler dbOper;
	
	/**
	 * 构造函数
	 * @param database 动态数据库对象 
	 */
	public DatabaseHandler(CfgDatabase database){
		dblink = new DBLink(database);
		newAbstractCreateDatabaseInstance();
	}
	
	/**
	 * 创建AbstractCreateDatabase的实例
	 * @return
	 */
	private void newAbstractCreateDatabaseInstance() {
		dbOper = ReflectUtil.newInstance(basePackage + dblink.getDBType() + implClassname);
		if(dbOper == null){
			Log4jUtil.debug("创建表时，配置文件配置的dbtype错误，请检查值是否与系统要求的值完全一致：[{}]", Arrays.toString(dblink.getDatabaseTypes()));
		}
	}
	private static final String basePackage = "com.api.plugins.jdbc.database.impl.";
	private static final String implClassname = ".DatabaseImpl";
	
	/**
	 * 执行操作数据库的ddlsql语句
	 * @param ddlSqlArr
	 */
	private void executeDDL(String[] ddlSqlArr, CfgDatabase database){
		String result = dblink.executeDDL(ddlSqlArr);
		if(result == null){
			Log4jUtil.debug("[DatabaseHandler.executeDDL]操作数据库成功：{}", database);
		}else{
			Log4jUtil.debug("[DatabaseHandler.executeDDL]操作数据库失败，异常信息为：{}", result);
		}
	}
	
	/**
	 * 创建数据库
	 * @param database
	 * @return
	 */
	public void createDatabase(CfgDatabase database){
		String tmpSql = getCreateDatabaseSql(database);
		if(StrUtils.notEmpty(tmpSql)){
			String[] ddlSqlArr = tmpSql.split(";");
			executeDDL(ddlSqlArr, database);
		}
	}
	
	/**
	 * 删除数据库
	 * @param database
	 */
	public void dropDatabase(CfgDatabase database){
		String tmpSql = getDropDatabaseSql(database);
		if(StrUtils.notEmpty(tmpSql)){
			String[] ddlSqlArr = tmpSql.split(";");
			executeDDL(ddlSqlArr, database);
		}
	}
	
	/**
	 * 获取创建数据库的sql
	 * @param database
	 * @return
	 */
	public String getCreateDatabaseSql(CfgDatabase database){
		if(dbOper == null){
			Log4jUtil.debug("[DatabaseHandler.createDatabase]操作数据库的对象AbstractDatabaseOper dbOper为null");
			return null;
		}
		return dbOper.installCreateDatabaseSql(database);
	}
	
	/**
	 * 获取删除数据库的sql
	 * @param database
	 * @return
	 */
	public String getDropDatabaseSql(CfgDatabase database){
		if(dbOper == null){
			Log4jUtil.debug("[DatabaseHandler.dropDatabase]操作数据库的对象AbstractDatabaseOper dbOper为null");
			return null;
		}
		return dbOper.installDropDatabaseSql(database);
	}
}
