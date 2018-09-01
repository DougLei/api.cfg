package com.king.tooth.util.sqlparser;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.stmt.TCreateViewSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateProcedure;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.cfg.sql.ActParameter;
import com.king.tooth.sys.entity.cfg.sql.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.cfg.sql.SqlScriptParameterNameRecord;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.database.DBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql语句解析工具类
 * @author DougLei
 */
public class SqlStatementParserUtil {
	
	/**
	 * 根据sql脚本，获取gsql解析对象实例
	 * @param sqlScriptContent
	 * @return
	 */
	public static TGSqlParser getGsqlParser(String sqlScriptContent){
		return parserSqlScript(sqlScriptContent);
	}
	
	/**
	 * 解析sql脚本
	 * @param sqlScript sql脚本内容
	 * @return 返回TGSqlParser解析器对象实例
	 */
	private static TGSqlParser parserSqlScript(String sqlScript) {
		TGSqlParser sqlParser = preAnalysisSqlScript(sqlScript);
		int ret = sqlParser.parse();
		if(ret != 0){
			throw new IllegalArgumentException("解析的sql脚本内容出现语法错误：" + sqlParser.getErrormessage());
		}
		return sqlParser;
	}
	
	/**
	 * 预处理解析sql，获取TGSqlParser解析器实例
	 * @param sqlScript sql脚本内容
	 * @return
	 */
	private static TGSqlParser preAnalysisSqlScript(String sqlScript) {
		EDbVendor dbDialect = getSqlParserDbDialect();
		TGSqlParser sqlParser = new TGSqlParser(dbDialect);
		sqlParser.sqltext = sqlScript;
		return sqlParser;
	}
	
	/**
	 * 根据数据库类型，获取sql解析器的数据库方言
	 * @return
	 */
	private static EDbVendor getSqlParserDbDialect() {
		String dbType = HibernateUtil.getCurrentDatabaseType();
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			return EDbVendor.dbvoracle;
		}else if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(dbType)){
			return EDbVendor.dbvmssql;
		}
		throw new IllegalArgumentException("目前系统不支持 ["+dbType+"]类型的数据库sql脚本解析");
	}
	
	/**
	 * 解析sql脚本
	 * @param gsqlParser
	 * @param comSqlScript
	 * @return 返回解析的sql脚本内容数组
	 */
	public static String[] parseSqlScript(TGSqlParser gsqlParser, ComSqlScript sql) {
		TStatementList sqlList = gsqlParser.sqlstatements;
		int len = sqlList.size();
		
		Map<String, String> typeMap = getSqlScriptTypeMap(sqlList.get(0).sqlstatementtype);
		if(len > 1 &&  ("true".equals(typeMap.get("isUnique")))){
			throw new ArrayIndexOutOfBoundsException("目前系统只支持一次处理一条["+typeMap.get("type")+"]类型的sql脚本语句");
		}
		sql.setSqlScriptType(typeMap.get("type"));
		sql.setReqResourceMethod(typeMap.get("reqMethod"));
		
		String[] sqlScriptArr;
		if("true".equals(typeMap.get("other"))){
			sqlScriptArr = new String[1];
			sqlScriptArr[0] =  gsqlParser.sqltext;
		}else{
			sqlScriptArr = new String[len];
			for(int i=0;i<len;i++){
				sqlScriptArr[i] = sqlList.get(i).toString();
			}
		}
		
		typeMap.clear();
		return sqlScriptArr;
	}
	
	/**
	 * 获取sql脚本类型
	 * @param sqlStatementType
	 * @return
	 */
	private static Map<String, String> getSqlScriptTypeMap(ESqlStatementType sqlStatementType) {
		 Map<String, String> typeMap = new HashMap<String, String>(4);
		 typeMap.put("other", "false");
		 typeMap.put("isUnique", "false");
		 
		 switch(sqlStatementType){
	         case sstselect:
	        	 typeMap.put("type", BuiltinDatabaseData.SELECT);
	        	 typeMap.put("isUnique", "true");
	        	 typeMap.put("reqMethod", "get");
	        	 break;
	         case sstupdate:
	        	 typeMap.put("type", BuiltinDatabaseData.UPDATE);
	        	 typeMap.put("reqMethod", "put");
	        	 break;
	         case sstinsert:
	        	 typeMap.put("type", BuiltinDatabaseData.INSERT);
	        	 typeMap.put("reqMethod", "post");
	        	 break;
	         case sstdelete:
	        	 typeMap.put("type", BuiltinDatabaseData.DELETE);
	        	 typeMap.put("reqMethod", "delete");
	        	 break;
	         case sstplsql_createprocedure:
	        	 typeMap.put("type", BuiltinDatabaseData.PROCEDURE);
	        	 typeMap.put("isUnique", "true");
	        	 typeMap.put("reqMethod", "post");
	        	 break;
	         case sstmssqlcreateprocedure:
	        	 typeMap.put("type", BuiltinDatabaseData.PROCEDURE);
	        	 typeMap.put("isUnique", "true");
	        	 typeMap.put("reqMethod", "post");
	        	 break;
	         case sstcreateview:
	        	 typeMap.put("type", BuiltinDatabaseData.VIEW);
	        	 typeMap.put("isUnique", "true");
	        	 typeMap.put("reqMethod", "get");
	        	 break;
	         default:
	        	 Log4jUtil.warn("目前平台很可能不支持[{}]类型的sql脚本", sqlStatementType);
	        	 typeMap.put("type", sqlStatementType.toString());
	        	 typeMap.put("other", "true");
	        	 typeMap.put("reqMethod", "post");
	        	 break;
	     }
		 return typeMap;
	}
	
	/**
	 * 解析存储过程参数
	 * @param sqlScript
	 * @return
	 */
	public static void analysisProcedureSqlScriptParam(ComSqlScript sqlScript) {
		TCustomSqlStatement sqlStatement = sqlScript.getGsqlParser().sqlstatements.get(0);
		switch(sqlStatement.sqlstatementtype){
		    case sstplsql_createprocedure:
		    	analysisOracleProcedure((TPlsqlCreateProcedure)sqlStatement, sqlScript);
		    	break;
		    case sstmssqlcreateprocedure:
		    	analysisSqlServerProcedure((TMssqlCreateProcedure)sqlStatement, sqlScript);
		    	break;
		    default:
		    	throw new IllegalArgumentException("目前不支持["+sqlStatement.sqlstatementtype+"]类型的存储过程");
		}
	}

	/**
	 * 解析oracle存储过程，获得存储过程名和参数集合
	 * @param procedureSqlStatement
	 * @param sqlScript
	 */
	private static void analysisOracleProcedure(TPlsqlCreateProcedure procedureSqlStatement, ComSqlScript sqlScript) {
		// 解析出存储过程名
		sqlScript.setObjectName(procedureSqlStatement.getProcedureName().toString());

		List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(1);
		SqlScriptParameterNameRecord parameterNameRecord = new SqlScriptParameterNameRecord(0);
		parameterNameRecordList.add(parameterNameRecord);
		
		// 解析参数
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
			
			List<ComSqlScriptParameter> sqlScriptParameterList = new ArrayList<ComSqlScriptParameter>(len);
			ComSqlScriptParameter parameter = null;
			
			TParameterDeclaration param = null;
			String parameterName;
			String dataType;
			String length = null;
			for(int i=0;i<len;i++){
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				parameterName = param.getParameterName().toString();
				
				dataType = param.getDataType().toString().toLowerCase();
				if(dataType.indexOf("(") != -1){
					length = dataType.substring(dataType.indexOf("(")+1, dataType.indexOf(")"));
					dataType = dataType.substring(0, dataType.indexOf("("));
				}
				
				parameter = new ComSqlScriptParameter(parameterName, dataType, param.getMode(), (i+1), true);
				parameter.setLengthStr(length);
				processOracleProcCursorParam(parameter, sqlScript);
				sqlScriptParameterList.add(parameter);
				parameterNameRecord.addParameterName(parameterName);
			}
			
			sqlScript.setSqlParams(sqlScriptParameterList);
			sqlScript.doSetParameterRecordList(parameterNameRecordList);
		}
	}
	/**
	 * 处理oracle存储过程的游标类型参数，如果是游标类型，则要在参数中标识出来
	 * @param parameter
	 * @param sqlScript 
	 */
	private static void processOracleProcCursorParam(ComSqlScriptParameter parameter, ComSqlScript sqlScript) {
		if(BuiltinDataType.ORACLE_CURSOR_TYPE.equals(parameter.getParameterDataType())){
			parameter.setIsTableType(1);
		}
	}
	
	/**
	 * 解析sqlserver存储过程，获得存储过程名和参数集合
	 * @param procedureSqlStatement
	 * @param sqlScript
	 */
	private static void analysisSqlServerProcedure(TMssqlCreateProcedure procedureSqlStatement, ComSqlScript sqlScript) {
		// 解析出存储过程名
		sqlScript.setObjectName(procedureSqlStatement.getProcedureName().toString());

		List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(1);
		SqlScriptParameterNameRecord parameterNameRecord = new SqlScriptParameterNameRecord(0);
		parameterNameRecordList.add(parameterNameRecord);
		
		// 解析参数
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
		
			List<ComSqlScriptParameter> sqlScriptParameterList = new ArrayList<ComSqlScriptParameter>(len);
			ComSqlScriptParameter parameter = null;
			
			TParameterDeclaration param = null;
			String parameterName;
			String dataType;
			String length = null;
			for(int i=0;i<len;i++){
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				parameterName = param.getParameterName().toString();
				if(parameterName.startsWith("@")){
					parameterName = parameterName.substring(1);
				}
				
				dataType = param.getDataType().toString().toLowerCase();
				if(dataType.indexOf("(") != -1){
					length = dataType.substring(dataType.indexOf("(")+1, dataType.indexOf(")"));
					dataType = dataType.substring(0, dataType.indexOf("("));
				}
				
				parameter = new ComSqlScriptParameter(parameterName , dataType, param.getMode(), (i+1), true);
				parameter.setLengthStr(length);
				processSqlServerProcTableParam(parameter, sqlScript);
				sqlScriptParameterList.add(parameter);
				parameterNameRecord.addParameterName(parameterName);
			}
			sqlScript.setSqlParams(sqlScriptParameterList);
			sqlScript.doSetParameterRecordList(parameterNameRecordList);
		}
	}
	/**
	 * 处理sqlserver存储过程的表类型参数，如果是表类型，则要在参数中标识出来
	 * @param parameter
	 * @param sqlScript 
	 */
	@SuppressWarnings("unchecked")
	private static void processSqlServerProcTableParam(ComSqlScriptParameter parameter, ComSqlScript sqlScript) {
		int count = (int) HibernateUtil.executeUniqueQueryBySqlArr(sqlserver_queryDefinedTableTypeIsExistsSql, parameter.getParameterDataType());
		if(count == 1){
			parameter.setIsTableType(1);
			
			List<Object[]> columnList = HibernateUtil.executeListQueryBySqlArr(sqlserver_queryDefinedTableTypeColumnInfoSql, "tt_"+parameter.getParameterDataType()+"_%");
			if(columnList == null || columnList.size() == 0){
				throw new NullPointerException("sqlserver中，自定义的表类型["+parameter.getParameterDataType()+"]，不存在任何列信息，请检查");
			}
			
			List<CfgSqlResultset> sqlResultSets = new ArrayList<CfgSqlResultset>(columnList.size());
			int index = 0;
			CfgSqlResultset sqlResultSet;
			for (Object[] objArr : columnList) {
				sqlResultSet = new CfgSqlResultset(objArr[0].toString(), index++, 1);
				sqlResultSets.add(sqlResultSet);
				
				sqlResultSet.setSqlParameterId(parameter.getId());
				sqlResultSet.setDataType(DBUtil.getSqlServerDataType(Integer.valueOf(objArr[1].toString())));
			}
			columnList.clear();
			sqlScript.addInSqlResultsets(sqlResultSets);
		}
	}
	// sqlserver查询自定义的表类型是否存在
	private static final String sqlserver_queryDefinedTableTypeIsExistsSql = "select count(1) from sys.types where is_user_defined=1 and name = ? and is_table_type=1";
	// sqlserver查询自定义的表类型的列信息
	private static final String sqlserver_queryDefinedTableTypeColumnInfoSql = "select cast(c.name as varchar) name, cast(c.xtype as varchar) xtype from syscolumns c left join sysobjects o on (c.id = o.id) where o.name like ? and o.xtype = 'TT' order by c.colorder asc";
	

	/**
	 * 解析出视图名
	 * @param sqlScript
	 */
	public static void analysisViewSqlScriptParam(ComSqlScript sqlScript) {
		TCustomSqlStatement sqlStatement = sqlScript.getGsqlParser().sqlstatements.get(0);
		sqlScript.setObjectName(((TCreateViewSqlStatement) sqlStatement).getViewName().toString());
	}
	
	/**
	 * 根据sql脚本参数，获取最终的sql脚本对象集合
	 * @param sqlScript
	 * @param sqlParameterValues 
	 * @param 
	 */
	public static List<FinalSqlScriptStatement> getFinalSqlScriptList(ComSqlScript sqlScript, List<List<Object>> sqlParameterValues) {
		if(BuiltinDatabaseData.PROCEDURE.equals(sqlScript.getSqlScriptType())){// 存储过程，不解析最终的sql语句
			return null;
		}
		
		// 获取sql脚本参数集合
		List<List<ComSqlScriptParameter>> sqlParamsList = sqlScript.getSqlParamsList();
		boolean sqlScriptHavaParams = (sqlParamsList != null && sqlParamsList.size() > 0);
		
		// 初始化最终的sql脚本对象集合
		List<FinalSqlScriptStatement> finalSqlScriptList = new ArrayList<FinalSqlScriptStatement>(sqlScriptHavaParams?sqlParamsList.size():1);
		FinalSqlScriptStatement finalSqlScript;
		
		// 获取sql脚本对象，以及sql类型
		TStatementList sqlStatementList = sqlScript.getGsqlParser().sqlstatements;
		ESqlStatementType sqlStatementType = sqlStatementList.get(0).sqlstatementtype;
		
		if(sqlScriptHavaParams){
			for (List<ComSqlScriptParameter> sqlParams : sqlParamsList) {
				finalSqlScript = new FinalSqlScriptStatement();
				finalSqlScriptList.add(finalSqlScript);
				setFinalSqlHandler(sqlStatementType, finalSqlScript, sqlScript, sqlParams, sqlStatementList, sqlParameterValues);
			}
		}else{
			finalSqlScript = new FinalSqlScriptStatement();
			finalSqlScriptList.add(finalSqlScript);
			setFinalSqlHandler(sqlStatementType, finalSqlScript, sqlScript, null, sqlStatementList, sqlParameterValues);
		}
		return finalSqlScriptList;
	}
	
	/**
	 * 最终的sql语句处理
	 * @param sqlStatementType
	 * @param finalSqlScript
	 * @param sqlScript
	 * @param sqlParams
	 * @param sqlStatementList
	 * @param sqlParameterValues
	 */
	private static void setFinalSqlHandler(ESqlStatementType sqlStatementType, FinalSqlScriptStatement finalSqlScript, ComSqlScript sqlScript, List<ComSqlScriptParameter> sqlParams, TStatementList sqlStatementList, List<List<Object>> sqlParameterValues){
		switch(sqlStatementType){
			case sstselect:
				finalSqlScript.setIsSelectSqlScript(true);
				setFinalSelectSqlHandler(sqlScript.getParameterNameRecordMap(), sqlParams, finalSqlScript, sqlStatementList.get(0), sqlParameterValues);
				break;
			case sstinsert:
				finalSqlScript.setIsInsertSqlScript(true);
				setFinalModifySqlHandler(sqlScript.getParameterNameRecordMap(), sqlParams, finalSqlScript, sqlStatementList, sqlParameterValues);
				break;
			case sstupdate:
				finalSqlScript.setIsUpdateSqlScript(true);
				setFinalModifySqlHandler(sqlScript.getParameterNameRecordMap(), sqlParams, finalSqlScript, sqlStatementList, sqlParameterValues);
				break;
			case sstdelete:
				finalSqlScript.setIsDeleteSqlScript(true);
				setFinalModifySqlHandler(sqlScript.getParameterNameRecordMap(), sqlParams, finalSqlScript, sqlStatementList, sqlParameterValues);
				break;
			default:
				finalSqlScript.setIsOther(true);
				setFinalOtherSqlHandler(sqlScript.getParameterNameRecordMap(), sqlParams, finalSqlScript, sqlScript.getGsqlParser().sqltext, sqlParameterValues);
				break;
		}
	}
	
	/**
	 * 最终的select sql语句处理
	 * @param parameterNameRecordMap 
	 * @param sqlParams
	 * @param finalSelect
	 * @param sqlStatement
	 * @param sqlParameterValues 
	 */
	private static void setFinalSelectSqlHandler(Map<Integer, List<String>> parameterNameRecordMap, List<ComSqlScriptParameter> sqlParams, FinalSqlScriptStatement finalSelect, TCustomSqlStatement sqlStatement, List<List<Object>> sqlParameterValues) {
		List<String> parameterNames = parameterNameRecordMap.get(0);
		if(parameterNames!=null && parameterNames.size()>0){
			List<Object> queryCondParameters = new ArrayList<Object>(parameterNames.size());
			
			List<ActParameter> actParams = new ArrayList<ActParameter>(parameterNames.size());
			ActParameter actParam;
			
			if(sqlParams != null && sqlParams.size()>0){
				// 记录系统内置参数和值
				StringBuilder placeHolder = new StringBuilder();
				for (String parameterName : parameterNames) {
					for (ComSqlScriptParameter ssp : sqlParams) {
						if(parameterName.equalsIgnoreCase(ssp.getParameterName())){
							actParam = new ActParameter();
							actParam.setParameterName(parameterName);
							actParams.add(actParam);
							
							processActualParameter(parameterName, ssp, placeHolder, queryCondParameters, actParam);
							break;
						}
					}
				}
			}
			
			// 将该条select sql的条件值对象加到最终的参数集合中
			sqlParameterValues.add(queryCondParameters);
			analysisSelectSqlScript(SqlParameterParserUtil.replaceSqlScriptParams(sqlStatement.toString(), actParams), finalSelect);
			return;
		}
		// 如果sql脚本中没有参数，则直接返回sql语句，这个就是最终的查询语句
		analysisSelectSqlScript(sqlStatement.toString(), finalSelect);
	}
	/**
	 * 解析select sql脚本语句
	 * <p>针对select sql语句</p>
	 * @see FinalSqlScriptStatement.setFinalSqlScript()的注释
	 * @param sqlScript
	 * @param sqlScriptFinalStatement
	 */
	private static void analysisSelectSqlScript(String sqlScript, FinalSqlScriptStatement sqlScriptFinalStatement) {
		TGSqlParser sqlParser = parserSqlScript(sqlScript);
		TStatementList sqlStatementList = sqlParser.sqlstatements;
		if(sqlStatementList != null){
			TSelectSqlStatement selectSqlStatement = (TSelectSqlStatement) sqlStatementList.get(0);
			if(selectSqlStatement.getCteList() == null){
				sqlScriptFinalStatement.setFinalSelectSqlScript(sqlScript);
				return;
			}
			String finalCteSql = "with "+selectSqlStatement.getCteList().toString() + " ";
			sqlScriptFinalStatement.setFinalCteSql(finalCteSql);
			sqlScriptFinalStatement.setFinalSelectSqlScript(sqlScript.substring(sqlScript.indexOf(finalCteSql) + finalCteSql.length()));
		}
	}
	
	/**
	 * 最终的insert/update/delete sql语句处理
	 * @param parameterNameRecordMap 
	 * @param sqlParams
	 * @param finalSelect
	 * @param sqlStatement
	 * @param sqlParameterValues 
	 */
	private static void setFinalModifySqlHandler(Map<Integer, List<String>> parameterNameRecordMap, List<ComSqlScriptParameter> sqlParams, FinalSqlScriptStatement finalSqlScript, TStatementList sqlStatementList, List<List<Object>> sqlParameterValues) {
		int sqlLen = sqlStatementList.size();
		String[] modifySqlArr = new String[sqlLen];
		
		List<String> parameterNames;
		List<Object> sqlParamValues;
		List<ActParameter> actParams;
		ActParameter actParam;
		int size;
		for(int i = 0; i < sqlLen; i++){
			parameterNames = parameterNameRecordMap.get(i);
			if(parameterNames != null && parameterNames.size() > 0){
				size = parameterNames.size();
				sqlParamValues = new ArrayList<Object>(size);
				actParams = new ArrayList<ActParameter>(size);
				
				if(sqlParams != null && sqlParams.size()>0){
					for (String parameterName : parameterNames) {
						for (ComSqlScriptParameter ssp : sqlParams) {
							if(parameterName.equalsIgnoreCase(ssp.getParameterName())){
								actParam = new ActParameter();
								actParam.setParameterName(parameterName);
								actParams.add(actParam);
								
								// 如果是条件参数，将值加入到queryCondParameters中，并将实际值改为?
								if(ssp.getIsPlaceholder() == 1){
									actParam.setActualValue("?");
									sqlParamValues.add(ssp.getActualInValue());
								}else{
									actParam.setActualValue(ssp.getActualInValue());
								}
								break;
							}
						}
					}
				}
				modifySqlArr[i] = SqlParameterParserUtil.replaceSqlScriptParams(sqlStatementList.get(i).toString(), actParams);
			}else{
				sqlParamValues = null;
				modifySqlArr[i] = sqlStatementList.get(i).toString();
			}
			sqlParameterValues.add(sqlParamValues);
		}
		finalSqlScript.setFinalModifySqlArr(modifySqlArr);
	}
	
	/**
	 * 最终的other类型的 sql语句处理
	 * @param parameterNameRecordMap
	 * @param sqlParams
	 * @param finalSqlScript
	 * @param otherSql
	 * @param sqlParameterValues
	 */
	private static void setFinalOtherSqlHandler(Map<Integer, List<String>> parameterNameRecordMap, List<ComSqlScriptParameter> sqlParams, FinalSqlScriptStatement finalSqlScript, String otherSql, List<List<Object>> sqlParameterValues) {
		List<String> parameterNames = parameterNameRecordMap.get(0);
		String[] otherSqlArr = new String[1];
		
		List<Object> sqlParamValues = null;
		
		if(parameterNames!=null && parameterNames.size()>0){
			int size = parameterNames.size();
			sqlParamValues = new ArrayList<Object>(size);
			
			List<ActParameter> actParams = new ArrayList<ActParameter>(size);
			ActParameter actParam;

			if(sqlParams != null && sqlParams.size()>0){
				// 记录系统内置参数和值
				StringBuilder placeHolder = new StringBuilder();
				for (String parameterName : parameterNames) {
					for (ComSqlScriptParameter ssp : sqlParams) {
						if(parameterName.equalsIgnoreCase(ssp.getParameterName())){
							actParam = new ActParameter();
							actParam.setParameterName(parameterName);
							actParams.add(actParam);
							
							processActualParameter(parameterName, ssp, placeHolder, sqlParamValues, actParam);
							break;
						}
					}
				}
			}
			otherSqlArr[0] = SqlParameterParserUtil.replaceSqlScriptParams(otherSql, actParams);
		}else{
			otherSqlArr[0] = otherSql;
		}
		sqlParameterValues.add(sqlParamValues);
		finalSqlScript.setFinalModifySqlArr(otherSqlArr);
	}
	
	/**
	 * 处理实际的sql脚本参数
	 * <p>目前主要是在[setFinalSelectSqlHandler]和[setFinalOtherSqlHandler]方法中用到</p>
	 * @param parameterName
	 * @param ssp
	 * @param placeHolder
	 * @param sqlParamValues
	 * @param actParam
	 */
	private static void processActualParameter(String parameterName, ComSqlScriptParameter ssp, StringBuilder placeHolder, List<Object> sqlParamValues, ActParameter actParam) {
		Object actualInValue = ssp.getActualInValue();
		if(ssp.getParameterFrom() == 1){ // 如果参数是内置的
			
			// 如果是条件参数，将值加入到sqlParamValues中，并将实际值改为?
			if(ssp.getIsPlaceholder() == 1){
				
				// 如果是String类型，则证明是id，则要用,分割，可能会出现多个id
				if(actualInValue instanceof String){
					String[] bqpvTmp = ((String)actualInValue).split(",");
					for (String bt : bqpvTmp) {
						placeHolder.append("?,");
						sqlParamValues.add(bt);
					}
					placeHolder.setLength(placeHolder.length()-1);
					actParam.setActualValue(placeHolder.toString());
					placeHolder.setLength(0);
				}
				// 否则就是一个值，则直接处理
				else{
					actParam.setActualValue("?");
					sqlParamValues.add(actualInValue);
				}
			}
			// 否则直接添加值
			else{
				actParam.setActualValue(actualInValue);
			}
		}else{
			// 如果是条件参数，将值加入到sqlParamValues中，并将实际值改为?
			if(ssp.getIsPlaceholder() == 1){
				actParam.setActualValue("?");
				sqlParamValues.add(actualInValue);
			}
			// 否则直接添加值
			else{
				actParam.setActualValue(actualInValue);
			}
		}		
	}


//	下面的代码，是为了解析sql语句，分析出参数中，哪些是在调用的时候，通过?方式传值的，以及获取select语句，最终查询的结果列名集合
//	/**
//	 * 获取select的sql语句，执行后的查询字段名集合
//	 * <p>同时，处理sql脚本的参数，分析出哪些参数是需要通过占位符使用的。后续可以提高调用时的效率</p>
//	 * @param sqlParser
//	 * @param sqlScriptParameterList
//	 * @return select sql语句查询的结果列集合
//	 * 
//	 * @throws SqlScriptSyntaxException 
//	 * @throws AnalyzeSqlScriptException 
//	 * @throws EDBVendorIsNullException 
//	 */
//	public static List<SqlQueryResultColumn> getSelectSqlOfResultColumnsAndAnalysisSqlScriptParameters(ComSqlScript comSqlScript) throws SqlScriptSyntaxException, AnalyzeSqlScriptException, EDBVendorIsNullException {
//		TGSqlParser sqlParser = comSqlScript.getGsqlParser();
//		TStatementList selectSqlStatementList = sqlParser.sqlstatements;
//		if(selectSqlStatementList != null && selectSqlStatementList.size() > 0){
//			
//			List<ComSqlScriptParameter> sqlScriptParameters = comSqlScript.getSqlScriptParameterList();
//			
//			TCustomSqlStatement sqlStatement = selectSqlStatementList.get(0);
//			switch(sqlStatement.sqlstatementtype){
//				case sstselect:
//					if(selectSqlStatementList.size() > 1){
//						throw new AnalyzeSqlScriptException("目前只支持一次解析一条select sql脚本语句");
//					}
//					return getSelectSqlOfResultColumns((TSelectSqlStatement)sqlStatement, sqlScriptParameters);
//				case sstinsert:
//					analysisInsertSqlScriptParameters(selectSqlStatementList, sqlScriptParameters);
//					return null;
//				case sstupdate:
//					analysisUpdateSqlScriptParameters(selectSqlStatementList, sqlScriptParameters);
//					return null;
//				case sstdelete:
//					analysisDeleteSqlScriptParameters(selectSqlStatementList, sqlScriptParameters);
//					return null;
//				default:
//					throw new IllegalArgumentException("目前对sql脚本参数的解析，还不支持该sql脚本类型：["+sqlStatement.sqlstatementtype+"]");
//			}
//		}
//		return null;
//	}
//	
//	/**
//	 * 解析添加sql脚本的参数
//	 * <p>目前都是占位符</p>
//	 * @param deleteSqlStatement
//	 * @param sqlScriptParameters
//	 */
//	private static void analysisInsertSqlScriptParameters(TStatementList selectSqlStatementList, List<ComSqlScriptParameter> sqlScriptParameters) {
//		for (ComSqlScriptParameter sqlScriptParameter : sqlScriptParameters) {
//			sqlScriptParameter.setIsPlaceholderParameter(1);
//		}
//	}
//	
//	/**
//	 * 解析修改sql脚本的参数
//	 * <p>目前都是占位符</p>
//	 * @param deleteSqlStatement
//	 * @param sqlScriptParameters
//	 */
//	private static void analysisUpdateSqlScriptParameters(TStatementList selectSqlStatementList, List<ComSqlScriptParameter> sqlScriptParameters) {
//		for (ComSqlScriptParameter sqlScriptParameter : sqlScriptParameters) {
//			sqlScriptParameter.setIsPlaceholderParameter(1);
//		}
//	}
//	
//	/**
//	 * 解析删除sql脚本的参数
//	 * <p>目前都是占位符</p>
//	 * @param deleteSqlStatement
//	 * @param sqlScriptParameters
//	 */
//	private static void analysisDeleteSqlScriptParameters(TStatementList selectSqlStatementList, List<ComSqlScriptParameter> sqlScriptParameters) {
//		for (ComSqlScriptParameter sqlScriptParameter : sqlScriptParameters) {
//			sqlScriptParameter.setIsPlaceholderParameter(1);
//		}
//	}
//
//	/**
//	 * 获取select sql执行后的结果列对象集合
//	 * @param selectSqlStatement
//	 * @param sqlScriptParameters
//	 * @return
//	 */
//	private static List<SqlQueryResultColumn> getSelectSqlOfResultColumns(TSelectSqlStatement selectSqlStatement, List<ComSqlScriptParameter> sqlScriptParameters) {
//		selectSqlStatement.addCondition("1=2");// 加入 1=2 的条件，结果不会查询出任何数据，因为主要要列名
//		
//		List<Object> queryCondParameters = new ArrayList<Object>();// 记录测试时，查询条件的值集合
//		if(sqlScriptParameters != null && sqlScriptParameters.size() > 0){
//			// 查询语句只支持处理一条，所以这里index参数传递为1
//			processSqlCondClauseParameter(1, selectSqlStatement, sqlScriptParameters, queryCondParameters);
//		}
//		
//		String finalSelectSqlScript = SqlParameterParserUtil.replaceSqlScriptParams(1, selectSqlStatement.toString(), sqlScriptParameters);
//		List<SqlQueryResultColumn> resultColumns = HibernateUtil.getQueryResultColumns(finalSelectSqlScript, queryCondParameters);// 记录select sql语句，最终查询的字段名集合.如果有别名，则记录别名，不记录实际的字段名
//		return resultColumns;
//	}
//
//	/**
//	 * 处理sql中所有条件子句语的参数
//	 * <p>如果是条件子句中的参数，将对应的SqlScriptParameter对象的actualValue的值存储到queryCondParameters集合中</p>
//	 * <p>				 再将其actualValue的值改为?，后续直接调用SqlParameterParserUtil.replaceSqlScriptParams的替换方法，完成select sql语句的参数替换</p>
//	 * <p>				最后，根据结果sql语句，和queryCondParameters集合，进行查询</p>
//	 * @param index
//	 * @param selectSqlStatement
//	 * @param sqlScriptParameterList
//	 * @param queryCondParameters
//	 */
//	private static void processSqlCondClauseParameter(int index, TSelectSqlStatement selectSqlStatement, List<ComSqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
//		// 如果是组合查询，比如是用到了union、union all、intersect、intersect all、minus、 minus all、except、except all这些关键字，将多个查询结果集组合起来
//		if (selectSqlStatement.isCombinedQuery()) {
//			processSqlCondClauseParameter(index, selectSqlStatement.getLeftStmt(), sqlScriptParameterList, queryCondParameters);
//			return;
//		}
//		// 否则就是单一查询，包括left join连接查询等，都属于单一查询，最后都是查询出一个结果集
//		else {
//			// 获取每一个独立的sql语句，看看里面有没有条件子句，如果有，看看子句中有没有参数，如果有，将参数替换为?，实际值add到queryCondParameters中
//			processCteClauseSqlCondClauseParameter(index, selectSqlStatement.getCteList(), sqlScriptParameterList, queryCondParameters);
//			processJoinClauseSqlOnCondClauseParameter(index, selectSqlStatement.getJoins(), sqlScriptParameterList, queryCondParameters);
//			if(selectSqlStatement.getWhereClause() != null){
//				processCondClauseSqlParameter(index, selectSqlStatement.getWhereClause().toString(), sqlScriptParameterList, queryCondParameters);
//			}
//		}
//	}
//
//	/**
//	 * 处理cte子句中所有条件子句语的参数
//	 * <p>cte子句，就是with子句</p>
//	 * @param index
//	 * @param cteList
//	 * @param sqlScriptParameterList
//	 * @param queryCondParameters
//	 */
//	private static void processCteClauseSqlCondClauseParameter(int index, TCTEList cteList, List<ComSqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
//		if(cteList != null && cteList.size() > 0){
//			int len = cteList.size();
//			for(int i=0;i<len;i++){
//				processSqlCondClauseParameter(index, cteList.getCTE(i).getSubquery(), sqlScriptParameterList, queryCondParameters);
//			}
//		}
//	}
//
//	/**
//	 * 处理join on中所有条件子句语的参数
//	 * @param index
//	 * @param joinSqls
//	 * @param sqlScriptParameterList
//	 * @param queryCondParameters
//	 */
//	private static void processJoinClauseSqlOnCondClauseParameter(int index, TJoinList joinSqls, List<ComSqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
//		if(joinSqls != null && joinSqls.size() > 0){
//			 TJoinItemList joinItemList = null;
//			 TJoinItem joinItem = null;
//			 int joinLen = joinSqls.size();
//			 int joinItemLen = 0;
//			 for(int i = 0;i < joinLen;i++){
//				 joinItemList = joinSqls.getJoin(i).getJoinItems();
//				 joinItemLen = joinItemList.size();
//				 for(int j = 0;j < joinItemLen; j++){
//					 joinItem = joinItemList.getJoinItem(j);
//					 if (joinItem.getOnCondition() != null){
//						 processCondClauseSqlParameter(index, joinItem.getOnCondition().toString(), sqlScriptParameterList, queryCondParameters);
//					 }
//				 }
//			 }
//		}
//	}
//	
//	/**
//	 * 处理条件子句所有条件子句语的参数
//	 * @param index
//	 * @param conditionSql
//	 * @param sqlScriptParameterList
//	 * @param queryCondParameters
//	 */
//	private static void processCondClauseSqlParameter(int index, String conditionSql, List<ComSqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
//		if(conditionSql.contains(SqlParameterParserUtil.PREFIX)){// 这个是判断sql语句中是否有$字符，如果有，则证明有配置参数
//			for (ComSqlScriptParameter ssp : sqlScriptParameterList) {
//				if(index == ssp.getIndex() && SqlParameterParserUtil.sqlScriptContainsParameterName(conditionSql, ssp.getParameterName())){
//					queryCondParameters.add(ssp.getActualValue());
//					ssp.setActualValue("?");
//					ssp.setIsPlaceholderParameter(1);
//				}
//			}
//		}
//	}
}
