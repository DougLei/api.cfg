package com.king.tooth.util.database;

import java.sql.Types;

import oracle.jdbc.OracleTypes;

import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
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
	
	/**
	 * 是否是sqlserver数据库
	 * @return
	 */
	public static boolean isSqlserver(){
		String dbType = HibernateUtil.getCurrentDatabaseType();
		if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是oracle数据库
	 * @return
	 */
	public static boolean isOracle(){
		String dbType = HibernateUtil.getCurrentDatabaseType();
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			return true;
		}
		return false;
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
		if(isSqlServer){
			if("varchar".equals(dataType)){
				return Types.VARCHAR;
			}else if("char".equals(dataType)){
				return Types.CHAR;
			}else if("int".equals(dataType)){
				return Types.INTEGER;
			}else if("decimal".equals(dataType)){
				return Types.DECIMAL;
			}else if("datetime".equals(dataType)){
				return Types.TIMESTAMP;
			}
			throw new IllegalArgumentException("系统目前不支持[sqlserver]数据库的["+dataType+"]数据类型转换，请联系管理员");
		}else if(isOracle){
			if("varchar2".equals(dataType)){
				return OracleTypes.VARCHAR;
			}else if("char".equals(dataType)){
				return OracleTypes.CHAR;
			}else if("number".equals(dataType)){
				return OracleTypes.NUMBER;
			}else if("date".equals(dataType)){
				return OracleTypes.TIMESTAMP;
			}else if(isTableType == 1){
				return OracleTypes.CURSOR;
			}
			throw new IllegalArgumentException("系统目前不支持[oracle]数据库的["+dataType+"]数据类型转换，请联系管理员，目前支持的数据类型为：[varchar2、char、number、date]");
		}
		throw new IllegalArgumentException("系统目前只支持[oracle和sqlserver]数据库的数据类型转换，请联系管理员");
	}
}
