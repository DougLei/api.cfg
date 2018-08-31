package com.king.tooth.sys.builtin.data;

/**
 * 内置的数据库数据
 * @author DougLei
 */
public class BuiltinDatabaseData {
	
	// -----------------------------------------------------------------------------
	/**
	 * 表类型：1:单表
	 */
	public static final int SINGLE_TABLE = 1;
	/**
	 * 表类型：2:树表
	 */
	public static final int TREE_TABLE = 2;
	/**
	 * 表类型：3:父子关系表
	 */
	public static final int PARENT_SUB_TABLE = 3;
	
	// -----------------------------------------------------------------------------
	/**
	 * 数据库类型：oracle
	 */
	public static final String DB_TYPE_ORACLE = "oracle";
	/**
	 * 数据库类型：sqlserver
	 */
	public static final String DB_TYPE_SQLSERVER = "sqlserver";
	
	// -----------------------------------------------------------------------------
	/**
	 * oracle数据库方言
	 */
	private static final String DIALECT_ORACLE = "org.hibernate.dialect.OracleDialect";
	/**
	 * sqlserver数据库方言
	 */
	private static final String DIALECT_SQLSERVER = "org.hibernate.dialect.SQLServerDialect";
	/**
	 * 获取对应数据库的方言
	 * @param dbType
	 * @return
	 */
	public static final String getDataBaseDialect(String dbType){
		if(DB_TYPE_ORACLE.equals(dbType)){
			return DIALECT_ORACLE;
		}else if(DB_TYPE_SQLSERVER.equals(dbType)){
			return DIALECT_SQLSERVER;
		}
		throw new IllegalArgumentException("没有找到["+dbType+"]类型的数据库方言");
	}
	
	// -----------------------------------------------------------------------------
	/**
	 * oracle数据库驱动
	 */
	private static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	/**
	 * sqlserver数据库驱动
	 */
	private static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/**
	 * 获取对应数据库的连接驱动
	 * @param dbType
	 * @return
	 */
	public static final String getDataBaseDriver(String dbType){
		if(DB_TYPE_ORACLE.equals(dbType)){
			return DRIVER_ORACLE;
		}else if(DB_TYPE_SQLSERVER.equals(dbType)){
			return DRIVER_SQLSERVER;
		}
		throw new IllegalArgumentException("没有找到["+dbType+"]类型的数据库连接驱动");
	}
	
	// -----------------------------------------------------------------------------
	/**
	 * 获取对应数据库的连接字符串
	 * @param dbType
	 * @param ip
	 * @param port
	 * @param instanceName
	 * @return
	 */
	public static final String getDataBaseLinkUrl(String dbType, String ip, int port, String instanceName){
		if(DB_TYPE_ORACLE.equals(dbType)){
			return "jdbc:oracle:thin:@"+ip+":"+port+":"+instanceName;
		}else if(DB_TYPE_SQLSERVER.equals(dbType)){
			return "jdbc:sqlserver://"+ip+":"+port+";DatabaseName="+instanceName;
		}
		throw new IllegalArgumentException("没有找到["+dbType+"]类型的数据库连接字符串");
	}
	
	// -----------------------------------------------------------------------------
	// sql语句的类型
	/**
	 * select
	 */
	public static final String SELECT = "select";
	/**
	 * insert
	 */
	public static final String INSERT = "insert";
	/**
	 * update
	 */
	public static final String UPDATE = "update";
	/**
	 * delete
	 */
	public static final String DELETE = "delete";
	/**
	 * view
	 */
	public static final String VIEW = "view";
	/**
	 * procedure
	 */
	public static final String PROCEDURE = "procedure";
	
	// -----------------------------------------------------------------------------
	// 查询数据是否存在的sql语句
	/**
	 * sqlserver查询对象是否存在
	 * <p>目前主要是查询表、存储过程、视图</p>
	 */
	public static final String sqlserver_queryObjectIsExistsSql = "select count(1) from  sysobjects where id = object_id(?) and type = ?";
	
	/**
	 * oracle查询对象是否存在
	 * <p>目前主要是查询表、存储过程、视图</p>
	 */
	public static final String oracle_queryObjectIsExistsSql = "select count(1) from user_objects where object_name = ? and object_type = ?";
	
	// -----------------------------------------------------------------------------
}
