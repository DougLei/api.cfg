package com.king.tooth.util.sqlparser;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TResultColumnList;
import gudusoft.gsqlparser.stmt.TCreateViewSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateProcedure;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.cfg.sql.ActParameter;
import com.king.tooth.sys.entity.cfg.sql.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.cfg.sql.SqlScriptParameterNameRecord;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql语句解析工具类
 * @author DougLei
 */
public class SqlStatementParserUtil {
	
	/**
	 * 根据sql脚本，获取gsql解析对象实例
	 * @param contents
	 * @return
	 */
	public static TGSqlParser getGsqlParser(String contents){
		return parserSqlScript(contents);
	}
	
	/**
	 * 解析sql脚本
	 * @param contents sql脚本内容
	 * @return 返回TGSqlParser解析器对象实例
	 */
	private static TGSqlParser parserSqlScript(String contents) {
		TGSqlParser sqlParser = preAnalysisSqlScript(contents);
		int ret = sqlParser.parse();
		if(ret != 0){
			throw new IllegalArgumentException("解析的sql脚本内容出现语法错误，请检查sql：" + sqlParser.getErrormessage());
		}
		return sqlParser;
	}
	
	/**
	 * 预处理解析sql，获取TGSqlParser解析器实例
	 * @param contents sql脚本内容
	 * @return
	 */
	private static TGSqlParser preAnalysisSqlScript(String contents) {
		EDbVendor dbDialect = getSqlParserDbDialect();
		TGSqlParser sqlParser = new TGSqlParser(dbDialect);
		sqlParser.sqltext = contents;
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
	public static String[] parseSqlScript(TGSqlParser gsqlParser, CfgSql sql) {
		TStatementList sqlList = gsqlParser.sqlstatements;
		int len = sqlList.size();
		
		Map<String, String> typeMap = getSqlScriptTypeMap(sqlList.get(0).sqlstatementtype);
		if(len > 1 &&  ("true".equals(typeMap.get("isUnique")))){
			throw new ArrayIndexOutOfBoundsException("目前系统只支持一次处理一条["+typeMap.get("type")+"]类型的sql脚本语句");
		}
		sql.setType(typeMap.get("type"));
		if(StrUtils.isEmpty(sql.getConfType())){
			sql.setConfType(typeMap.get("confType"));
		}
		if(StrUtils.isEmpty(sql.getRequestMethod())){
			sql.setRequestMethod(typeMap.get("reqMethod"));
		}
		
		String[] sqlScriptArr;
		if("true".equals(typeMap.get("isOtherSqlType"))){
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
		 Map<String, String> typeMap = new HashMap<String, String>(5);
		 typeMap.put("isOtherSqlType", "false");// 不在BuiltinDatabaseData常量中的，其他sql脚本类型
		 typeMap.put("isUnique", "false");
		 typeMap.put("reqMethod", ResourceInfoConstants.POST);
		 
		 switch(sqlStatementType){
	         case sstselect:
	        	 typeMap.put("type", SqlStatementTypeConstants.SELECT);
	        	 typeMap.put("isUnique", "true");
	        	 typeMap.put("reqMethod", ResourceInfoConstants.GET);
	        	 break;
	         case sstupdate:
	        	 typeMap.put("type", SqlStatementTypeConstants.UPDATE);
	        	 typeMap.put("confType", SqlStatementTypeConstants.UPDATE);
	        	 break;
	         case sstinsert:
	        	 typeMap.put("type", SqlStatementTypeConstants.INSERT);
	        	 typeMap.put("confType", SqlStatementTypeConstants.INSERT);
	        	 break;
	         case sstdelete:
	        	 typeMap.put("type", SqlStatementTypeConstants.DELETE);
	        	 typeMap.put("confType", SqlStatementTypeConstants.DELETE);
	        	 break;
	         case sstplsql_createprocedure:
	        	 typeMap.put("type", SqlStatementTypeConstants.PROCEDURE);
	        	 typeMap.put("isUnique", "true");
	        	 break;
	         case sstmssqlcreateprocedure:
	        	 typeMap.put("type", SqlStatementTypeConstants.PROCEDURE);
	        	 typeMap.put("isUnique", "true");
	        	 break;
	         case sstcreateview:
	        	 typeMap.put("type", SqlStatementTypeConstants.VIEW);
	        	 typeMap.put("isUnique", "true");
	        	 typeMap.put("reqMethod", ResourceInfoConstants.NONE);
	        	 break;
	         default:
	        	 Log4jUtil.warn("目前平台很可能不支持[{}]类型的sql脚本", sqlStatementType);
	        	 typeMap.put("type", sqlStatementType.toString());
	        	 typeMap.put("isOtherSqlType", "true");
	        	 break;
	     }
		 return typeMap;
	}
	
	/**
	 * 解析存储过程参数
	 * @param sqlScript
	 * @return
	 */
	public static void analysisProcedureSqlScriptParam(CfgSql sqlScript) {
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
	 * 保存存储过程中，输入结果集信息
	 * @param sql
	 */
	public static void saveProcedureTableTypeResultset(CfgSql sql) {
		if(SqlStatementTypeConstants.PROCEDURE.equals(sql.getType())){ 
			List<CfgSqlResultset> inSqlResultsets = sql.getInSqlResultsets();
			if(inSqlResultsets != null && inSqlResultsets.size() > 0){
				String sqlScriptId = sql.getId();
				for (CfgSqlResultset inSqlResultset : inSqlResultsets) {
					inSqlResultset.setSqlScriptId(sqlScriptId);
					HibernateUtil.saveObject(inSqlResultset, null);
				}
				inSqlResultsets.clear();
			}
		}
	}

	/**
	 * 解析oracle存储过程，获得存储过程名和参数集合
	 * @param procedureSqlStatement
	 * @param sqlScript
	 */
	private static void analysisOracleProcedure(TPlsqlCreateProcedure procedureSqlStatement, CfgSql sqlScript) {
		// 解析出存储过程名
		sqlScript.setObjectName(procedureSqlStatement.getProcedureName().toString());
		List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(1);
		SqlScriptParameterNameRecord parameterNameRecord = new SqlScriptParameterNameRecord(0);
		parameterNameRecordList.add(parameterNameRecord);
		
		// 解析参数
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
			
			List<CfgSqlParameter> sqlScriptParameterList = new ArrayList<CfgSqlParameter>(len);
			CfgSqlParameter parameter = null;
			
			TParameterDeclaration param = null;
			String defaultValue = null;
			String parameterName;
			String dataType;
			String[] lengthArr = null;
			String length;
			String precision;
			for(int i=0;i<len;i++){
				length = null;
				precision = null;
				
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				if(param.getDefaultValue() != null){
					defaultValue = param.getDefaultValue().toString();
					if(defaultValue.startsWith("'")){
						defaultValue = defaultValue.substring(1, defaultValue.length()-1);
					}
				}
				
				parameterName = param.getParameterName().toString();
				
				dataType = param.getDataType().toString().toLowerCase();
				if(dataType.indexOf("(") != -1){
					lengthArr = dataType.substring(dataType.indexOf("(")+1, dataType.indexOf(")")).split(",");
					length = lengthArr[0];
					if(lengthArr.length == 2){
						precision = lengthArr[1];
					}else if(lengthArr.length > 2){
						throw new IllegalArgumentException("系统在解析oracle存储过程参数时出现异常，请联系后台系统开发人员");
					}
					dataType = dataType.substring(0, dataType.indexOf("("));
				}
				
				parameter = new CfgSqlParameter(parameterName, dataType, true, param.getMode(), (i+1), true, true);
				parameter.setLengthStr(length);
				parameter.setPrecisionStr(precision);
				processProcedureTableParam(parameter, sqlScript);
				sqlScriptParameterList.add(parameter);
				parameterNameRecord.addParameterName(parameterName);
			}
			
			sqlScript.setSqlParams(sqlScriptParameterList);
			sqlScript.doSetParameterRecordList(parameterNameRecordList);
		}
	}
	
	/**
	 * 解析sqlserver存储过程，获得存储过程名和参数集合
	 * @param procedureSqlStatement
	 * @param sqlScript
	 */
	private static void analysisSqlServerProcedure(TMssqlCreateProcedure procedureSqlStatement, CfgSql sqlScript) {
		// 解析出存储过程名
		sqlScript.setObjectName(procedureSqlStatement.getProcedureName().toString());
		List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(1);
		SqlScriptParameterNameRecord parameterNameRecord = new SqlScriptParameterNameRecord(0);
		parameterNameRecordList.add(parameterNameRecord);
		
		// 解析参数
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
		
			List<CfgSqlParameter> sqlScriptParameterList = new ArrayList<CfgSqlParameter>(len);
			CfgSqlParameter parameter = null;
			
			TParameterDeclaration param = null;
			String defaultValue = null;
			String parameterName;
			String dataType;
			String[] lengthArr = null;
			String length;
			String precision;
			for(int i=0;i<len;i++){
				length = null;
				precision = null;
				
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				if(param.getDefaultValue() != null){
					defaultValue = param.getDefaultValue().toString();
					if(defaultValue.startsWith("'")){
						defaultValue = defaultValue.substring(1, defaultValue.length()-1);
					}
				}
				
				parameterName = param.getParameterName().toString();
				if(parameterName.startsWith("@")){
					parameterName = parameterName.substring(1);
				}
				
				dataType = param.getDataType().toString().toLowerCase();
				if(dataType.indexOf("(") != -1){
					lengthArr = dataType.substring(dataType.indexOf("(")+1, dataType.indexOf(")")).split(",");
					length = lengthArr[0];
					if(lengthArr.length == 2){
						precision = lengthArr[1];
					}else if(lengthArr.length > 2){
						throw new IllegalArgumentException("系统在解析sqlserver存储过程参数时出现异常，请联系后台系统开发人员");
					}
					dataType = dataType.substring(0, dataType.indexOf("("));
				}
				
				parameter = new CfgSqlParameter(parameterName , dataType, true, param.getMode(), (i+1), true, true);
				parameter.setLengthStr(length);
				parameter.setPrecisionStr(precision);
				parameter.setDefaultValue(defaultValue);
				processProcedureTableParam(parameter, sqlScript);
				sqlScriptParameterList.add(parameter);
				parameterNameRecord.addParameterName(parameterName);
			}
			sqlScript.setSqlParams(sqlScriptParameterList);
			sqlScript.doSetParameterRecordList(parameterNameRecordList);
		}
	}
	
	/**
	 * 处理存储过程的表类型参数，如果是表类型，则要在参数中标识出来
	 * @param parameter
	 * @param sqlScript 
	 */
	private static void processProcedureTableParam(CfgSqlParameter parameter, CfgSql sqlScript) {
		CfgTable table = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgTable.class, queryTableDataTypeOfTable, CfgTable.TABLE_DATATYPE, parameter.getDataType());
		if(table != null){
			if(table.getIsEnabled() == 0){
				throw new IllegalArgumentException("存储过程["+sqlScript.getObjectName()+"]，参数["+parameter.getName()+"]，引用的表类型被禁用，请联系系统管理员");
			}
			if(table.getIsCreated() == 0 || table.getIsBuildModel() == 0){
				throw new IllegalArgumentException("存储过程["+sqlScript.getObjectName()+"]，参数["+parameter.getName()+"]，引用的表类型还未建模，请联系系统管理员");
			}
			parameter.setIsTableType(1);
			
			CfgSqlResultset sqlResultSet = new CfgSqlResultset(sqlScript.getType(), null, 0, CfgSqlResultset.IN);
			sqlResultSet.setSqlParameterId(parameter.getId());
			sqlResultSet.setTableId(table.getId());
			sqlScript.addInSqlResultsets(sqlResultSet);
		}
	}
	/** 查询表数据类型的对应的表对象hql */
	private static final String queryTableDataTypeOfTable = "from CfgTable where type=? and tableName = ?";
	

	/**
	 * 解析出视图名
	 * @param sqlScript
	 */
	public static void analysisViewName(CfgSql sqlScript) {
		TCustomSqlStatement sqlStatement = sqlScript.getGsqlParser().sqlstatements.get(0);
		sqlScript.setObjectName(((TCreateViewSqlStatement) sqlStatement).getViewName().toString());
	}
	
	/**
	 * 根据sql脚本参数，获取最终的sql脚本对象集合
	 * @param sqlScript
	 * @param sqlParameterValues 
	 * @param 
	 */
	public static List<FinalSqlScriptStatement> getFinalSqlScriptList(CfgSql sqlScript, List<List<Object>> sqlParameterValues) {
		if(SqlStatementTypeConstants.PROCEDURE.equals(sqlScript.getType())){// 存储过程，不解析最终的sql语句
			return null;
		}
		
		// 获取sql脚本参数集合
		List<List<CfgSqlParameter>> sqlParamsList = sqlScript.getSqlParamsList();
		boolean sqlScriptHaveParams = (sqlParamsList != null && sqlParamsList.size() > 0);
		
		// 初始化最终的sql脚本对象集合
		List<FinalSqlScriptStatement> finalSqlScriptList = new ArrayList<FinalSqlScriptStatement>(sqlScriptHaveParams?sqlParamsList.size():1);
		FinalSqlScriptStatement finalSqlScript;
		
		// 获取sql脚本对象，以及sql类型
		TStatementList sqlStatementList = sqlScript.getGsqlParser().sqlstatements;
		ESqlStatementType sqlStatementType = sqlStatementList.get(0).sqlstatementtype;
		
		if(sqlScriptHaveParams){
			for (List<CfgSqlParameter> sqlParams : sqlParamsList) {
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
	private static void setFinalSqlHandler(ESqlStatementType sqlStatementType, FinalSqlScriptStatement finalSqlScript, CfgSql sqlScript, List<CfgSqlParameter> sqlParams, TStatementList sqlStatementList, List<List<Object>> sqlParameterValues){
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
	private static void setFinalSelectSqlHandler(Map<Integer, List<String>> parameterNameRecordMap, List<CfgSqlParameter> sqlParams, FinalSqlScriptStatement finalSelect, TCustomSqlStatement sqlStatement, List<List<Object>> sqlParameterValues) {
		List<String> parameterNames = parameterNameRecordMap.get(0);
		if(parameterNames!=null && parameterNames.size()>0){
			List<Object> queryCondParameters = new ArrayList<Object>(parameterNames.size());
			
			List<ActParameter> actParams = new ArrayList<ActParameter>(parameterNames.size());
			ActParameter actParam;
			
			if(sqlParams != null && sqlParams.size()>0){
				// 记录系统内置参数和值
				StringBuilder placeHolder = new StringBuilder();
				for (String parameterName : parameterNames) {
					for (CfgSqlParameter ssp : sqlParams) {
						if(parameterName.equalsIgnoreCase(ssp.getName())){
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
	private static void setFinalModifySqlHandler(Map<Integer, List<String>> parameterNameRecordMap, List<CfgSqlParameter> sqlParams, FinalSqlScriptStatement finalSqlScript, TStatementList sqlStatementList, List<List<Object>> sqlParameterValues) {
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
						for (CfgSqlParameter ssp : sqlParams) {
							if(parameterName.equalsIgnoreCase(ssp.getName())){
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
	private static void setFinalOtherSqlHandler(Map<Integer, List<String>> parameterNameRecordMap, List<CfgSqlParameter> sqlParams, FinalSqlScriptStatement finalSqlScript, String otherSql, List<List<Object>> sqlParameterValues) {
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
					for (CfgSqlParameter ssp : sqlParams) {
						if(parameterName.equalsIgnoreCase(ssp.getName())){
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
	private static void processActualParameter(String parameterName, CfgSqlParameter ssp, StringBuilder placeHolder, List<Object> sqlParamValues, ActParameter actParam) {
		Object actualInValue = ssp.getActualInValue();
		if(ssp.getValueFrom() == CfgSqlParameter.SYSTEM_BUILTIN){ // 如果参数是内置的
			
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

	/**
	 * 解析select sql语句查询的结果集信息
	 * <p>如果select语句查询的是*，则抛出异常，不能这样写，这样写不规范</p>
	 * @param sql
	 */
	public static void analysisSelectSqlResultSetList(CfgSql sql) {
		if(SqlStatementTypeConstants.SELECT.equals(sql.getType())){ 
			TGSqlParser sqlParser = sql.getGsqlParser();
			TSelectSqlStatement selectSqlStatement = (TSelectSqlStatement) sqlParser.sqlstatements.get(0);
			TResultColumnList columns = analysisResultColumnList(selectSqlStatement);
			if(columns == null || columns.size() == 0){
				throw new NullPointerException("资源名为["+sql.getResourceName()+"]的select sql资源，没有解析到任何查询结果字段，请联系后台系统开发人员");
			}
			
			int columnSize = columns.size();
			List<CfgSqlResultset> sqlResultSets = new ArrayList<CfgSqlResultset>(columnSize);
			
			boolean includeIdColumn = false;// 是否包含id字段，如果不包括，则系统抛出异常
			String columnName = null;
			TResultColumn column = null;
			CfgSqlResultset csr = null;
			for(int i=0;i<columnSize ;i++){
				column = columns.getResultColumn(i);
				columnName = column.getAliasClause() == null ? column.getColumnNameOnly() : column.getColumnAlias();
				
				if(StrUtils.isEmpty(columnName)){
					throw new IllegalArgumentException("在资源名为["+sql.getResourceName()+"]的select sql资源中，请给查询结果列["+column.toString()+"]指定合法的列名");
				}
				if("*".equals(columnName)){
					throw new IllegalArgumentException("在资源名为["+sql.getResourceName()+"]的select sql资源中，请指定具体查询的列名，禁止使用["+column.toString()+"]查询");
				}
				csr = new CfgSqlResultset(sql.getType(), columnName, (i+1), CfgSqlResultset.OUT);
				csr.setSqlScriptId(sql.getId());
				sqlResultSets.add(csr);
				
				if(!includeIdColumn && csr.getPropName() == ResourcePropNameConstants.ID){
					includeIdColumn = true;
				}
			}
			if(!includeIdColumn){
				throw new IllegalArgumentException("资源名为["+sql.getResourceName()+"]的select sql资源，其查询结果字段，不包含id列，系统无法处理，请修改该sql脚本，增加查询id字段的语句");
			}
			
			for(int i=0;i<columnSize-1;i++){
				columnName = sqlResultSets.get(i).getColumnName();
				for(int j=i+1;j<columnSize;j++){
					if(columnName.equals(sqlResultSets.get(j).getColumnName())){
						throw new IllegalArgumentException("资源名为["+sql.getResourceName()+"]的select sql资源，其查询结果字段，出现重复列名["+columnName+"]，位置分别位于["+(i+1)+","+(j+1)+"]，请检查");
					}
				}
			}
			
			String sqlScriptId = sql.getId();
			for (CfgSqlResultset sqlResultSet : sqlResultSets) {
				sqlResultSet.setSqlScriptId(sqlScriptId);
				HibernateUtil.saveObject(sqlResultSet, null);// 将每条结果集信息保存到数据库
			}
			sqlResultSets.clear();
		}
	}
	
	/**
	 * 解析出查询的结果列
	 * @param selectSqlStatement
	 * @return
	 */
	private static TResultColumnList analysisResultColumnList(TSelectSqlStatement selectSqlStatement) {
		if(selectSqlStatement.isCombinedQuery()){
			return analysisResultColumnList(selectSqlStatement.getLeftStmt());
		}else{
			return selectSqlStatement.getResultColumnList();
		}
	}
}
