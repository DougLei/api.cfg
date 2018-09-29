package com.king.tooth.util;


/**
 * 命名处理工具类
 * @author DougLei
 */
public class NamingProcessUtil {
	
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
		if(StrUtils.isEmpty(databaseNaming)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		databaseNaming = databaseNaming.trim().toLowerCase();
		if(databaseNaming.startsWith("_")){
			throw new IllegalArgumentException("数据库字段命名不能以[_]开头");
		}
		String firstWord = databaseNaming.substring(0,1);// 首字母
		
		String[] words = databaseNaming.split("_");
		sb.append(words[0]);
		if(!turnToPropname){ // 是要转换表名
			sb.replace(0, 1, firstWord.toUpperCase());
		}
		
		int len = words.length;
		if(len > 1){
			for(int i = 1; i<len; i++){
				if(StrUtils.notEmpty(words[i])){
					firstWord = words[i].substring(0,1);
					sb.append(words[i].replaceFirst(firstWord, firstWord.toUpperCase()));
				}
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
		if(StrUtils.isEmpty(codeNaming)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		try {
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
		} finally{
			sb.setLength(0);
		}
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * 提取数据库对象名称
	 * <p>第一个下划线前的所有字母加_，再加后续每个单词的首字母，然后加_，加后续每个单词的尾字母，最后加_，再加总数量，均大写</p>
	 * <p>例如: SYS_USER_LINE，提取结果为SYS_UL</p>
	 * @param dbObjectName
	 * @return
	 */
	public static String extractDbObjName(String dbObjectName){
		if(StrUtils.isEmpty(dbObjectName)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder suffix = new StringBuilder();
		try {
			String[] nameArr = dbObjectName.split("_");
			int length = nameArr.length;
			for(int i=0;i<length;i++){
				if(i == 0){
					sb.append(nameArr[i]).append("_");
				}else{
					sb.append(nameArr[i].substring(0, 1));
					suffix.append(nameArr[i].substring(nameArr[i].length()-1));
				}
			}
			sb.append("_").append(suffix).append("_").append(dbObjectName.length());
			return sb.toString().toUpperCase();
		} finally{
			sb.setLength(0);
			suffix.setLength(0);
		}
	}
}
