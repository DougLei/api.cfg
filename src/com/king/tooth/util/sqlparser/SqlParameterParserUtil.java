package com.king.tooth.util.sqlparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.sys.entity.cfg.sql.ActParameter;
import com.king.tooth.sys.entity.cfg.sql.SqlScriptParameterNameRecord;
import com.king.tooth.util.hibernate.HibernateUtil;

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
	 * <p>同时还有一个功能：验证存储过程中是否有$xxx$的变量，如果存在则返回解析出来的第一个变量，并提示存储过程中不能出现这种方式的变量声明</p>
	 * @param sqlArr
	 * @param sqlScript
	 * @param isProcedure 是否是存储过程，如果是存储过程，则要去看，存储过程中是否出现$param$的变量，如果出现了这种变量，是会出现异常的
	 * @return 解析出来的第一个参数名，目前主要是针对存储过程中不能有$param$变量处理的。返回null表示一切正常
	 */
	public static String analysisMultiSqlScriptParam(String[] sqlScriptArr, CfgSql sqlScript, boolean isProcedure){
		if(sqlScriptArr == null || sqlScriptArr.length == 0){
			return null;
		}
		List<CfgSqlParameter> sqlScriptParameterList = new ArrayList<CfgSqlParameter>();
		List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>();
		
		List<String> parameterNames = new ArrayList<String>();// 记录参数名
		List<Integer> parameterPlaceholderIndex = new ArrayList<Integer>();// 记录每个$的下标
		
		int sqlScriptArrLength = sqlScriptArr.length;
		Matcher matcher;
		StringBuilder sb = new StringBuilder();// 记录每个sql语句所有包含参数的语句
		int len;
		String sql;
		String parameterName;
		SqlScriptParameterNameRecord parameterNameRecord;
		CfgSqlParameter sqlScriptParameter;
		int orderCode =1;
		for(int i=0; i<sqlScriptArrLength; i++){
			parameterNameRecord = new SqlScriptParameterNameRecord(i);
			parameterNameRecordList.add(parameterNameRecord);
			
			matcher = sqlScriptParamPattern.matcher(sqlScriptArr[i]);
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
			
			len = parameterPlaceholderIndex.size();
			if(len%2!=0){
				throw new IllegalArgumentException("sql语句["+sqlScriptArr[i]+"]可能存在[$]符号，和系统内置的参数命名方式冲突，请检查修改，或联系管理员");
			}
			
			sql = sb.toString();
			sb.setLength(0);
			if(len > 0){
				if(isProcedure){
					return sql.substring(parameterPlaceholderIndex.get(0),parameterPlaceholderIndex.get(1)+1);
				}
				for (int j = 0; j < len; j++) {
					parameterName = sql.substring(parameterPlaceholderIndex.get(j)+1,parameterPlaceholderIndex.get(++j));
					parameterNameRecord.addParameterName(parameterName);
					if(parameterNames.contains(parameterName)){
						continue;
					}
					parameterNames.add(parameterName);
					
					sqlScriptParameter = new CfgSqlParameter(parameterName, DataTypeConstants.STRING, false, 0, orderCode++, true, true);
					sqlScriptParameterList.add(sqlScriptParameter);
				}
			}
			parameterPlaceholderIndex.clear();
		}
		parameterNames.clear();
		
		if(!isProcedure){
			sqlScript.doSetParameterRecordList(parameterNameRecordList);
			
			// 保存参数
			if(sqlScriptParameterList != null && sqlScriptParameterList.size() > 0){
				String sqlScriptId = sqlScript.getId();
				for (CfgSqlParameter sqlParam : sqlScriptParameterList) {
					sqlParam.setSqlId(sqlScriptId);
					HibernateUtil.saveObject(sqlParam, null);
				}
				sqlScriptParameterList.clear();
			}
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 	[多个替换]
	 * 	将sql脚本语句中的参数替换为对应的值，并返回替换后的结果sql脚本语句
	 * </pre>
	 * @param sqlScript
	 * @param actParameters 实际的参数集合
	 * @return
	 */
	public static String replaceSqlScriptParams(String sqlScript, List<ActParameter> actParameters){
		if(actParameters == null || actParameters.size() == 0){
			return sqlScript;
		}
		for (ActParameter ac : actParameters) {
			sqlScript = sqlScript.replaceAll(escapeCharacterPrefix + prefix + ac.getParameterName() + escapeCharacterPrefix + suffix, ac.getActualValue()+"");
		}
		actParameters.clear();
		return sqlScript;
	}
}
