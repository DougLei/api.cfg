package com.king.tooth.util.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.jdbc.Work;

import oracle.jdbc.OracleTypes;

import com.king.tooth.constants.database.OracleDataTypeConstants;
import com.king.tooth.constants.database.SQLServerDataTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 数据库工具类
 * @author DougLei
 */
public class DBUtil {
	
	/**
	 * 获取数据库字符串连接符
	 * @return
	 */
	public static String getStrAppendCharacter(){
		String dbType = HibernateUtil.getCurrentDatabaseType();
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			return "||";
		}else if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType)){
			return "+";
		}
		throw new IllegalArgumentException("根据数据库类型，获取数据库字符串连接符时，无法获取 ["+dbType+"] 数据库类型的字符串连接符");
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 获取约束名
	 * @param tableName
	 * @param columnName
	 * @param constraintType
	 * @return
	 */
	public static String getConstraintName(String tableName, String columnName, String constraintType){
		String dbType = HibernateUtil.getCurrentDatabaseType();
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			String constraintName = tableName+"_"+columnName;
			if(constraintName.length() >= 27){
				// oracle的约束名长度不能超过30个字符，所以这里对约束名做处理：
				Log4jUtil.debug("在oracle数据库中，因约束名长度超过30个字符，系统自动处理",  constraintName);
				constraintName = NamingProcessUtil.extractDbObjName(constraintName);
				Log4jUtil.debug("自动处理的新约束名为：{}",  constraintName);
			}
			return constraintType+"_"+constraintName;
		}else if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType)){
			return constraintType+"_"+tableName+"_"+columnName;
		}
		throw new IllegalArgumentException("获取约束名时，无法获取 ["+dbType+"] 数据库类型的约束名");
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 创建数据库对象
	 * <p>存储过程、视图等</p>
	 * @param sqls
	 */
	public static void createObjects(List<ComSqlScript> sqls) {
		final boolean isSqlServler = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqls.get(0).getDbType());
		final boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqls.get(0).getDbType());
		if(!isSqlServler && !isOracle){
			throw new IllegalArgumentException("系统目前不支持["+sqls.get(0).getDbType()+"]类型的数据库操作");
		}
		processDBObjects(sqls, true, isSqlServler, isOracle);
	}
	
	/**
	 * 删除数据库对象
	 * <p>存储过程、视图等</p>
	 * @param sqls
	 */
	public static void dropObject(ComSqlScript sql){
		List<ComSqlScript> sqls = new ArrayList<ComSqlScript>(1);
		sqls.add(sql);
		dropObjects(sqls);
		sqls.clear();
	}
	
	/**
	 * 删除数据库对象
	 * <p>存储过程、视图等</p>
	 * @param sqls
	 */
	public static void dropObjects(List<ComSqlScript> sqls){
		final boolean isSqlServler = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqls.get(0).getDbType());
		final boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqls.get(0).getDbType());
		if(!isSqlServler && !isOracle){
			throw new IllegalArgumentException("系统目前不支持["+sqls.get(0).getDbType()+"]类型的数据库操作");
		}
		processDBObjects(sqls, false, isSqlServler, isOracle);
	}
	
	/**
	 * 处理数据库对象，如果存在则删除，再根据参数(isCreate)决定是否重新创建
	 * <p>存储过程、视图等</p>
	 */
	private static void processDBObjects(final List<ComSqlScript> sqls, final boolean isCreate, final boolean isSqlServler, final boolean isOracle) {
		HibernateUtil.getCurrentThreadSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				Statement st = null;
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					ComSqlScript tmpSql = sqls.get(0);
					if(isSqlServler){
						pst = conn.prepareStatement(BuiltinDatabaseData.sqlserver_queryObjectIsExistsSql);
						if(BuiltinDatabaseData.PROCEDURE.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "P");
						}else if(BuiltinDatabaseData.VIEW.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "V");
						}else{
							throw new IllegalArgumentException("系统目前不支持在sqlserver数据库中创建["+tmpSql.getSqlScriptType()+"]类型的sql对象");
						}
					}else if(isOracle){
						pst = conn.prepareStatement(BuiltinDatabaseData.oracle_queryObjectIsExistsSql);
						if(BuiltinDatabaseData.PROCEDURE.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "PROCEDURE");
						}else if(BuiltinDatabaseData.VIEW.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "VIEW");
						}else{
							throw new IllegalArgumentException("系统目前不支持在oracle数据库中创建["+tmpSql.getSqlScriptType()+"]类型的sql对象");
						}
					}
					
					st = conn.createStatement();
					for (ComSqlScript sql : sqls) {
						pst.setString(1, sql.getObjectName());
						
						// 如果已经存在对象，则删除
						rs = pst.executeQuery();
						if(rs.next() && (rs.getInt(1) > 0)){
							st.executeUpdate("drop " + sql.getSqlScriptType() + " " + sql.getObjectName());
						}
						
						if(isCreate){
							st.executeUpdate(sql.getSqlScriptContent());// 创建对象
						}
					}
				} finally{
					CloseUtil.closeDBConn(rs, st, pst);
				}
			}
		});
	}
	
	//----------------------------------------------------------------------------
	/**
	 * 获取数据库数据类型对应的编码
	 * <p>目前在调用存储过程的时候用到</p>
	 * @param dataType
	 * @param isTableType
	 * @param isOracle
	 * @param isSqlServer
	 * @return
	 */
	public static int getDatabaseDataTypeCode(String dataType, Integer isTableType, boolean isOracle, boolean isSqlServer){
		if(isOracle){
			if(OracleDataTypeConstants.VARCHAR2.equals(dataType)){
				return OracleTypes.VARCHAR;
			}else if(OracleDataTypeConstants.CHAR.equals(dataType)){
				return OracleTypes.CHAR;
			}else if(OracleDataTypeConstants.NUMBER.equals(dataType)){
				return OracleTypes.NUMBER;
			}else if(OracleDataTypeConstants.DATE.equals(dataType)){
				return OracleTypes.TIMESTAMP;
			}else if(isTableType == 1){
				return OracleTypes.CURSOR;
			}
			throw new IllegalArgumentException("系统目前不支持[oracle]数据库的["+dataType+"]数据类型转换，请联系管理员，目前支持的数据类型为：[varchar2、char、number、date]");
		}else if(isSqlServer){
			if(SQLServerDataTypeConstants.VARCHAR.equals(dataType)){
				return Types.VARCHAR;
			}else if(SQLServerDataTypeConstants.CHAR.equals(dataType)){
				return Types.CHAR;
			}else if(SQLServerDataTypeConstants.INT.equals(dataType)){
				return Types.INTEGER;
			}else if(SQLServerDataTypeConstants.DECIMAL.equals(dataType)){
				return Types.DECIMAL;
			}else if(SQLServerDataTypeConstants.DATETIME.equals(dataType)){
				return Types.TIMESTAMP;
			}
			throw new IllegalArgumentException("系统目前不支持[sqlserver]数据库的["+dataType+"]数据类型转换，请联系管理员");
		}
		throw new IllegalArgumentException("系统目前只支持[oracle和sqlserver]数据库的数据类型转换，请联系管理员");
	}
	
	/**
	 * 根据xtype，获得sqlserver对应的数据类型
	 * <p>select name, xtype from systypes where name in('varchar','char','int','decimal','datetime')</p>
	 * @param xtype
	 * @return
	 */
	public static String getSqlServerDataType(int xtype){
		switch(xtype){
			case 56:
				return SQLServerDataTypeConstants.INT;
			case 61:
				return SQLServerDataTypeConstants.DATETIME;
			case 106:
				return SQLServerDataTypeConstants.DECIMAL;
			case 167:
				return SQLServerDataTypeConstants.VARCHAR;
			case 175:
				return SQLServerDataTypeConstants.CHAR;
			default:
				throw new IllegalArgumentException("系统目前不支持[sqlserver]数据库[xtype="+xtype+"]的类型转换，请联系管理员");
		}
	}
}
