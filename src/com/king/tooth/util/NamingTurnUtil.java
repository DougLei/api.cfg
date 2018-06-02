package com.king.tooth.util;


/**
 * 命名转换工具类
 * @author DougLei
 */
public class NamingTurnUtil {
	
	/**
	 * 数据库表名   转为   类名
	 * <p>数据库表名  ==> 多个单词，用_分割</p>
	 * <p>	 类命名  ==> 首字母大写、多个单词，后面每个单词首字母大写</p>
	 * @param tableName
	 * @return
	 */
	public static String tableNameTurnClassName(String tableName){
		return databaseNamingToCodeNaming(tableName, false);
	}
	
	/**
	 * 数据库字段名   转为   属性名
	 * <p>数据库字段名  ==> 多个单词，用_分割</p>
	 * <p>	     属性名  ==> 首字母小写、多个单词，后面每个单词首字母大写</p>
	 * @param columnName
	 * @return
	 */
	public static String columnNameTurnPropName(String columnName){
		return databaseNamingToCodeNaming(columnName, true);
	}
	
	/**
	 * 将数据库命名规范的字符串，转换为代码命名规范的字符串
	 * @param databaseNaming
	 * @param turnToPropname 是否是转换为属性名：如果是转换为属性名，还要根据系统配置决定属性名的首字母是大写还是小写
	 * @return
	 */
	private static String databaseNamingToCodeNaming(String databaseNaming, boolean turnToPropname){
		StringBuilder sb = new StringBuilder();
		databaseNaming = databaseNaming.trim().toLowerCase();
		String firstWord = databaseNaming.substring(0,1);// 首字母
		
		String[] words = databaseNaming.split("_");
		sb.append(words[0]);
		if(!turnToPropname){ // 是要转换表名
			sb.replace(0, 1, firstWord.toUpperCase());
		}
		
		int len = words.length;
		if(len > 1){
			for(int i = 1; i<len; i++){
				firstWord = words[i].substring(0,1);
				sb.append(words[i].replaceFirst(firstWord, firstWord.toUpperCase()));
			}
		}
		return sb.toString();
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * 类名   转为   数据库表名
	 * <p>数据库表名  ==> 多个单词，用_分割</p>
	 * @param className
	 * @return
	 */
	public static String classNameTurnTableName(String className){
		return codeNamingToDatabaseNaming(className);
	}
	
	/**
	 * 属性名   转为   数据库字段名
	 * <p>数据库字段名  ==> 多个单词，用_分割</p>
	 * @param propName
	 * @return
	 */
	public static String propNameTurnColumnName(String propName){
		return codeNamingToDatabaseNaming(propName);
	}
	
	/**
	 * 将代码命名规范的字符串，转换为数据库命名规范的字符串
	 * @param codeNaming
	 * @return
	 */
	private static String codeNamingToDatabaseNaming(String codeNaming){
		StringBuilder sb = new StringBuilder();
		sb.append(codeNaming.charAt(0));
		int len = codeNaming.length();
		char tmp;
		for(int i=1;i<len;i++){
			tmp = codeNaming.charAt(i);
			if((tmp+0) >= 65 && (tmp+0) <= 90){
				sb.append("_");
			}
			sb.append(tmp);
		}
		return sb.toString().toUpperCase();
	}
}
