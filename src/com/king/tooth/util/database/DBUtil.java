package com.king.tooth.util.database;

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
}
