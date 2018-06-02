package com.king.tooth.util.sqlparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameter;
import com.king.tooth.util.StrUtils;

/**
 * sql参数解析工具类
 * @author DougLei
 */
public class SqlParameterParserUtil {
	
	/**
	 * 转移符前缀
	 */
	private static final String escapeCharacterPrefix = "\\";
	/**
	 * sql参数的前缀
	 * [对内部使用]
	 */
	private static final String prefix = "$";
	/**
	 * sql参数的前缀
	 * [对外部使用]
	 */
	public static final String PREFIX = "$";
	/**
	 * sql参数的后缀
	 */
	private static final String suffix = "$";
	/**
	 * sql参数的占位符
	 */
	private static final char placeholder = '$';
	
	
	/**
	 * 匹配sql脚本中参数的正则表达式
	 */
	private static final Pattern sqlScriptParamPattern = 
			Pattern.compile(escapeCharacterPrefix + prefix + ".*" + escapeCharacterPrefix + suffix, 
							Pattern.MULTILINE);

	/**
	 * 读取多条sql语句，解析出里面的参数
	 * @param sqlArr
	 * @return
	 */
	public static List<SqlScriptParameter> analysisMultiSqlScriptParam(String[] sqlScriptArr){
		if(sqlScriptArr == null || sqlScriptArr.length == 0){
			return null;
		}
		
		int sqlScriptArrLength = sqlScriptArr.length;
		List<SqlScriptParameter> sqlScriptParameterList = new ArrayList<SqlScriptParameter>(sqlScriptArrLength*8);
		StringBuilder sb = new StringBuilder();// 记录每个sql语句所有包含参数的语句
		List<Integer> parameterPlaceholderIndex = new ArrayList<Integer>();// 记录每个$的下标
		String sql;
		int len;
		for(int i=0; i<sqlScriptArrLength; i++){
			Matcher matcher = sqlScriptParamPattern.matcher(sqlScriptArr[i]);
			while(matcher.find()){
				sb.append(matcher.group()).append(";");
			}
			
			len = sb.length();
			if(len == 0){
				continue;
			}
			for(int j = 0; j < len; j++){
				if(sb.charAt(j) == placeholder){
					parameterPlaceholderIndex.add(j);
				}
			}
			if(parameterPlaceholderIndex.size()%2!=0){
				throw new IllegalArgumentException("sql语句["+sqlScriptArr[i]+"]可能存在[$]符号，和系统内置的参数命名方式冲突，请检查修改，或联系管理员");
			}
			
			sql = sb.toString();
			len = parameterPlaceholderIndex.size();
			for (int j = 0; j < len; j++) {
				SqlScriptParameter sqlScriptParameter = new SqlScriptParameter((i+1), sql.substring(parameterPlaceholderIndex.get(j)+1,parameterPlaceholderIndex.get(++j)));
				sqlScriptParameterList.add(sqlScriptParameter);
			}
			
			sb.setLength(0);
			parameterPlaceholderIndex.clear();
		}
		return sqlScriptParameterList;
	}
	
	/**
	 * <pre>
	 * 	[单一替换]
	 * 	将sql语句中的指定参数替换为对应的值，并返回替换后的结果sql语句
	 * </pre>
	 * @param sql
	 * @param parameterName 参数名
	 * @param actualValue 参数对应的实际值
	 * @return
	 */
	public static String replaceSqlScriptParam(String sql, String parameterName, String actualValue){
		if(StrUtils.isEmpty(parameterName)){
			return sql;
		}
		return sql.replaceAll(escapeCharacterPrefix + prefix + parameterName + escapeCharacterPrefix + suffix, actualValue);
	}
	
	/**
	 * <pre>
	 * 	[多个替换]
	 * 	将sql脚本语句中的参数替换为对应的值，并返回替换后的结果sql脚本语句
	 * </pre>
	 * @param index
	 * @param sqlScript
	 * @param parameterName 参数名
	 * @param actualValue 参数对应的实际值
	 * @return
	 */
	public static String replaceSqlScriptParams(int index, String sqlScript, List<SqlScriptParameter> sqlScriptParameters){
		if(sqlScriptParameters == null || sqlScriptParameters.size() == 0){
			return sqlScript;
		}
		for (SqlScriptParameter ssp : sqlScriptParameters) {
			if(ssp.getIndex() == index){
				sqlScript = sqlScript.replaceAll(escapeCharacterPrefix + prefix + ssp.getParameterName() + escapeCharacterPrefix + suffix, ssp.getActualValue()+"");
			}
		}
		return sqlScript;
	}
	
	/**
	 * 指定的sql语句中，是否存在对应的参数名
	 * @param sql
	 * @param parameterName
	 * @return
	 */
	public static boolean sqlScriptContainsParameterName(String sql, String parameterName){
		return sql.contains(prefix + parameterName + suffix);
	}
}
