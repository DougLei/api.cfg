package com.king.tooth.util.sqlparser;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TCTEList;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TJoinItemList;
import gudusoft.gsqlparser.nodes.TJoinList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateProcedure;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.exception.gsp.AnalyzeSqlScriptException;
import com.king.tooth.exception.gsp.EDBVendorIsNullException;
import com.king.tooth.exception.gsp.SqlScriptSyntaxException;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.sqlscript.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.common.sqlscript.ProcedureSqlScriptParameter;
import com.king.tooth.sys.entity.common.sqlscript.SqlQueryResultColumn;
import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameter;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql语句解析工具类
 * @author DougLei
 */
public class SqlStatementParserUtil {
	
	/**
	 * 根据数据库类型，获取sql解析器的数据库方言
	 * @return
	 * @throws EDBVendorIsNullException 
	 */
	private static EDbVendor getSqlParserDbDialect() throws EDBVendorIsNullException{
		String dbType = HibernateUtil.getCurrentDatabaseType();
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)){
			return EDbVendor.dbvoracle;
		}else if(DynamicDataConstants.DB_TYPE_SQLSERVER.equals(dbType)){
			return EDbVendor.dbvmssql;
		}
		throw new EDBVendorIsNullException("根据数据库类型，获取sql解析器的数据库方言时，无法获取 ["+dbType+"] 数据库类型的方言");
	}
	
	/**
	 * 预处理解析sql，获取TGSqlParser解析器实例
	 * @param sqlScript sql脚本内容
	 * @return
	 * @throws EDBVendorIsNullException 
	 */
	private static TGSqlParser preAnalysisSqlScript(String sqlScript) throws EDBVendorIsNullException {
		if(StrUtils.isEmpty(sqlScript)){
			throw new NullPointerException("要解析的sql脚本语句不能为空");
		}
		EDbVendor dbDialect = getSqlParserDbDialect();
		
		TGSqlParser sqlParser = new TGSqlParser(dbDialect);
		sqlParser.sqltext = sqlScript;
		return sqlParser;
	}

	/**
	 * 解析sql脚本
	 * @param sqlScript sql脚本内容
	 * @return 返回TGSqlParser解析器对象实例
	 * @throws SqlScriptSyntaxException
	 * @throws EDBVendorIsNullException 
	 */
	private static TGSqlParser parserSqlScript(String sqlScript) throws SqlScriptSyntaxException, EDBVendorIsNullException{
		TGSqlParser sqlParser = preAnalysisSqlScript(sqlScript);
		int ret = sqlParser.parse();
		if(ret != 0){
			throw new SqlScriptSyntaxException("解析的sql脚本内容出现语法错误：" + sqlParser.getErrormessage());
		}
		return sqlParser;
	}
	
	/**
	 * 根据sql脚本，获取gsql解析对象实例
	 * @param sqlScriptContent
	 * @return
	 * 
	 * @throws EDBVendorIsNullException 
	 * @throws SqlScriptSyntaxException 
	 */
	public static TGSqlParser getGsqlParser(String sqlScriptContent) throws SqlScriptSyntaxException, EDBVendorIsNullException {
		return parserSqlScript(sqlScriptContent);
	}
	
	/**
	 * 获取sql脚本的类型
	 * <p>如果有多个sql脚本，以第一个sql脚本的类型为准</p>
	 * @param gsqlParser
	 * @return
	 */
	public static String getSqlScriptType(TGSqlParser gsqlParser) {
		return getSqlScriptType(gsqlParser.sqlstatements.get(0).sqlstatementtype);
	}
	private static String getSqlScriptType(ESqlStatementType sqlStatementType) {
		 switch(sqlStatementType){
	         case sstselect:
	        	 return SqlStatementType.SELECT;
	         case sstupdate:
	        	 return SqlStatementType.UPDATE;
	         case sstinsert:
	        	 return SqlStatementType.INSERT;
	         case sstdelete:
	        	 return SqlStatementType.DELETE;
	         case sstplsql_createprocedure:
	        	 return SqlStatementType.PROCEDURE;
	         case sstmssqlcreateprocedure:
	        	 return SqlStatementType.PROCEDURE;
	     }
		 throw new IllegalArgumentException("目前平台不支持["+sqlStatementType+"]类型的sql脚本");
	}
	
	/**
	 * 处理存储过程，包括：
	 * 	<p>1、验证存储过程只有一条，同select sql语句一样，一次只支持解析一条存储过程</p>
	 * 	<p>2、解析出存储过程的名称和参数对象集合</p>
	 * @param comSqlScript
	 * 
	 * @throws AnalyzeSqlScriptException 
	 */
	public static void processProcedure(ComSqlScript comSqlScript) throws AnalyzeSqlScriptException {
		TGSqlParser sqlParser = comSqlScript.getGsqlParser();
		if(sqlParser.sqlstatements.size() > 1){
			throw new AnalyzeSqlScriptException("目前只支持一次解析一条procedure sql脚本语句");
		}
		
		TCustomSqlStatement sqlStatement = sqlParser.sqlstatements.get(0);
		switch(sqlStatement.sqlstatementtype){
		    case sstplsql_createprocedure:
		    	analysisOracleProcedure(comSqlScript, (TPlsqlCreateProcedure)sqlStatement);
		    	break;
		    case sstmssqlcreateprocedure:
		    	analysisSqlserverProcedure(comSqlScript, (TMssqlCreateProcedure)sqlStatement);
		    	break;
		}
	}

	/**
	 * 解析oracle存储过程，获得存储过程名和参数集合
	 * @param comSqlScript
	 * @param procedureSqlStatement
	 */
	private static void analysisOracleProcedure(ComSqlScript comSqlScript, TPlsqlCreateProcedure procedureSqlStatement) {
		comSqlScript.setProcedureName(procedureSqlStatement.getProcedureName().toString());

		// 解析参数
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
			List<ProcedureSqlScriptParameter> procedureParameterList = new ArrayList<ProcedureSqlScriptParameter>(len);
			ProcedureSqlScriptParameter pssp = null;
			TParameterDeclaration param = null;
			String parameterName;
			for(int i=0;i<len;i++){
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				parameterName = param.getParameterName().toString();
				if(parameterName.indexOf("(") != -1){
					parameterName = parameterName.substring(0, parameterName.indexOf("("));
				}
				pssp = new ProcedureSqlScriptParameter(DynamicDataConstants.DB_TYPE_ORACLE, (i+1), parameterName, param.getDataType().toString(), param.getMode());
				procedureParameterList.add(pssp);
			}
			comSqlScript.setProcedureParameterList(procedureParameterList);
		}
	}

	/**
	 * 解析sqlserver存储过程，获得存储过程名和参数集合
	 * @param comSqlScript
	 * @param procedureSqlStatement
	 */
	private static void analysisSqlserverProcedure(ComSqlScript comSqlScript, TMssqlCreateProcedure procedureSqlStatement) {
		comSqlScript.setProcedureName(procedureSqlStatement.getProcedureName().toString());

		// 解析参数
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
			List<ProcedureSqlScriptParameter> procedureParameterList = new ArrayList<ProcedureSqlScriptParameter>(len);
			ProcedureSqlScriptParameter pssp = null;
			TParameterDeclaration param = null;
			String parameterName;
			for(int i=0;i<len;i++){
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				parameterName = param.getParameterName().toString();
				if(parameterName.startsWith("@")){
					parameterName = parameterName.substring(1);
				}
				if(parameterName.indexOf("(") != -1){
					parameterName = parameterName.substring(0, parameterName.indexOf("("));
				}
				pssp = new ProcedureSqlScriptParameter(DynamicDataConstants.DB_TYPE_SQLSERVER, (i+1), parameterName , param.getDataType().toString(), param.getMode());
				procedureParameterList.add(pssp);
			}
			comSqlScript.setProcedureParameterList(procedureParameterList);
		}
	}

	/**
	 * 获取sql脚本的语句数组
	 * @param gsqlParser
	 * @return
	 */
	public static String[] getSqlScriptArr(TGSqlParser gsqlParser) {
		TStatementList sqlList = gsqlParser.sqlstatements;
		int len = sqlList.size();
		if(len == 0){
			return null;
		}
		
		String[] sqlScriptArr = new String[len];
		for(int i=0;i<len;i++){
			sqlScriptArr[i] = sqlList.get(i).toString();
		}
		return sqlScriptArr;
	}
	
	/**
	 * 获取select的sql语句，执行后的查询字段名集合
	 * <p>同时，处理sql脚本的参数，分析出哪些参数是需要通过占位符使用的。后续可以提高调用时的效率</p>
	 * @param sqlParser
	 * @param sqlScriptParameterList
	 * @return select sql语句查询的结果列集合
	 * 
	 * @throws SqlScriptSyntaxException 
	 * @throws AnalyzeSqlScriptException 
	 * @throws EDBVendorIsNullException 
	 */
	public static List<SqlQueryResultColumn> getSelectSqlOfResultColumnsAndAnalysisSqlScriptParameters(ComSqlScript comSqlScript) throws SqlScriptSyntaxException, AnalyzeSqlScriptException, EDBVendorIsNullException {
		TGSqlParser sqlParser = comSqlScript.getGsqlParser();
		TStatementList selectSqlStatementList = sqlParser.sqlstatements;
		if(selectSqlStatementList != null && selectSqlStatementList.size() > 0){
			
			List<SqlScriptParameter> sqlScriptParameters = comSqlScript.getSqlScriptParameterList();
			if(sqlScriptParameters != null && sqlScriptParameters.size() > 0){
				for (SqlScriptParameter ssp : sqlScriptParameters) {
					ssp.setProcessed(true);
					ssp.setUpdated(true);
				}
			}
			
			TCustomSqlStatement sqlStatement = selectSqlStatementList.get(0);
			switch(sqlStatement.sqlstatementtype){
				case sstselect:
					if(selectSqlStatementList.size() > 1){
						throw new AnalyzeSqlScriptException("目前只支持一次解析一条select sql脚本语句");
					}
					return getSelectSqlOfResultColumns((TSelectSqlStatement)sqlStatement, sqlScriptParameters);
				case sstinsert:
					analysisInsertSqlScriptParameters(selectSqlStatementList, sqlScriptParameters);
					return null;
				case sstupdate:
					analysisUpdateSqlScriptParameters(selectSqlStatementList, sqlScriptParameters);
					return null;
				case sstdelete:
					analysisDeleteSqlScriptParameters(selectSqlStatementList, sqlScriptParameters);
					return null;
				default:
					throw new IllegalAccessError("目前对sql脚本参数的解析，还不支持该sql脚本类型：["+sqlStatement.sqlstatementtype+"]");
			}
		}
		return null;
	}
	
	/**
	 * 解析添加sql脚本的参数
	 * <p>目前都是占位符</p>
	 * @param deleteSqlStatement
	 * @param sqlScriptParameters
	 */
	private static void analysisInsertSqlScriptParameters(TStatementList selectSqlStatementList, List<SqlScriptParameter> sqlScriptParameters) {
		for (SqlScriptParameter sqlScriptParameter : sqlScriptParameters) {
			sqlScriptParameter.setIsPlaceholderParameter(true);
		}
	}
	
	/**
	 * 解析修改sql脚本的参数
	 * <p>目前都是占位符</p>
	 * @param deleteSqlStatement
	 * @param sqlScriptParameters
	 */
	private static void analysisUpdateSqlScriptParameters(TStatementList selectSqlStatementList, List<SqlScriptParameter> sqlScriptParameters) {
		for (SqlScriptParameter sqlScriptParameter : sqlScriptParameters) {
			sqlScriptParameter.setIsPlaceholderParameter(true);
		}
	}
	
	/**
	 * 解析删除sql脚本的参数
	 * <p>目前都是占位符</p>
	 * @param deleteSqlStatement
	 * @param sqlScriptParameters
	 */
	private static void analysisDeleteSqlScriptParameters(TStatementList selectSqlStatementList, List<SqlScriptParameter> sqlScriptParameters) {
		for (SqlScriptParameter sqlScriptParameter : sqlScriptParameters) {
			sqlScriptParameter.setIsPlaceholderParameter(true);
		}
	}

	/**
	 * 获取select sql执行后的结果列对象集合
	 * @param selectSqlStatement
	 * @param sqlScriptParameters
	 * @return
	 */
	private static List<SqlQueryResultColumn> getSelectSqlOfResultColumns(TSelectSqlStatement selectSqlStatement, List<SqlScriptParameter> sqlScriptParameters) {
		selectSqlStatement.addCondition("1=2");// 加入 1=2 的条件，结果不会查询出任何数据，因为主要要列名
		
		List<Object> queryCondParameters = new ArrayList<Object>();// 记录测试时，查询条件的值集合
		if(sqlScriptParameters != null && sqlScriptParameters.size() > 0){
			// 修改标识[被处理过，被修改过]，再进行相应的处理
			for (SqlScriptParameter ssp : sqlScriptParameters) { // 循环每个对象，设置isProcessed、isUpdated属性值都为true
				ssp.setProcessed(true);
				ssp.setUpdated(true);
			}
			// 查询语句只支持处理一条，所以这里index参数传递为1
			processSqlCondClauseParameter(1, selectSqlStatement, sqlScriptParameters, queryCondParameters);
		}
		
		String finalSelectSqlScript = SqlParameterParserUtil.replaceSqlScriptParams(1, selectSqlStatement.toString(), sqlScriptParameters);
		List<SqlQueryResultColumn> resultColumns = HibernateUtil.getQueryResultColumns(finalSelectSqlScript, queryCondParameters);// 记录select sql语句，最终查询的字段名集合.如果有别名，则记录别名，不记录实际的字段名
		return resultColumns;
	}

	/**
	 * 处理sql中所有条件子句语的参数
	 * <p>如果是条件子句中的参数，将对应的SqlScriptParameter对象的actualValue的值存储到queryCondParameters集合中</p>
	 * <p>				 再将其actualValue的值改为?，后续直接调用SqlParameterParserUtil.replaceSqlScriptParams的替换方法，完成select sql语句的参数替换</p>
	 * <p>				最后，根据结果sql语句，和queryCondParameters集合，进行查询</p>
	 * @param index
	 * @param selectSqlStatement
	 * @param sqlScriptParameterList
	 * @param queryCondParameters
	 */
	private static void processSqlCondClauseParameter(int index, TSelectSqlStatement selectSqlStatement, List<SqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
		// 如果是组合查询，比如是用到了union、union all、intersect、intersect all、minus、 minus all、except、except all这些关键字，将多个查询结果集组合起来
		if (selectSqlStatement.isCombinedQuery()) {
			processSqlCondClauseParameter(index, selectSqlStatement.getLeftStmt(), sqlScriptParameterList, queryCondParameters);
			return;
		}
		// 否则就是单一查询，包括left join连接查询等，都属于单一查询，最后都是查询出一个结果集
		else {
			// 获取每一个独立的sql语句，看看里面有没有条件子句，如果有，看看子句中有没有参数，如果有，将参数替换为?，实际值add到queryCondParameters中
			processCteClauseSqlCondClauseParameter(index, selectSqlStatement.getCteList(), sqlScriptParameterList, queryCondParameters);
			processJoinClauseSqlOnCondClauseParameter(index, selectSqlStatement.getJoins(), sqlScriptParameterList, queryCondParameters);
			if(selectSqlStatement.getWhereClause() != null){
				processCondClauseSqlParameter(index, selectSqlStatement.getWhereClause().toString(), sqlScriptParameterList, queryCondParameters);
			}
		}
	}

	/**
	 * 处理cte子句中所有条件子句语的参数
	 * <p>cte子句，就是with子句</p>
	 * @param index
	 * @param cteList
	 * @param sqlScriptParameterList
	 * @param queryCondParameters
	 */
	private static void processCteClauseSqlCondClauseParameter(int index, TCTEList cteList, List<SqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
		if(cteList != null && cteList.size() > 0){
			int len = cteList.size();
			for(int i=0;i<len;i++){
				processSqlCondClauseParameter(index, cteList.getCTE(i).getSubquery(), sqlScriptParameterList, queryCondParameters);
			}
		}
	}

	/**
	 * 处理join on中所有条件子句语的参数
	 * @param index
	 * @param joinSqls
	 * @param sqlScriptParameterList
	 * @param queryCondParameters
	 */
	private static void processJoinClauseSqlOnCondClauseParameter(int index, TJoinList joinSqls, List<SqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
		if(joinSqls != null && joinSqls.size() > 0){
			 TJoinItemList joinItemList = null;
			 TJoinItem joinItem = null;
			 int joinLen = joinSqls.size();
			 int joinItemLen = 0;
			 for(int i = 0;i < joinLen;i++){
				 joinItemList = joinSqls.getJoin(i).getJoinItems();
				 joinItemLen = joinItemList.size();
				 for(int j = 0;j < joinItemLen; j++){
					 joinItem = joinItemList.getJoinItem(j);
					 if (joinItem.getOnCondition() != null){
						 processCondClauseSqlParameter(index, joinItem.getOnCondition().toString(), sqlScriptParameterList, queryCondParameters);
					 }
				 }
			 }
		}
	}
	
	/**
	 * 处理条件子句所有条件子句语的参数
	 * @param index
	 * @param conditionSql
	 * @param sqlScriptParameterList
	 * @param queryCondParameters
	 */
	private static void processCondClauseSqlParameter(int index, String conditionSql, List<SqlScriptParameter> sqlScriptParameterList, List<Object> queryCondParameters) {
		if(conditionSql.contains(SqlParameterParserUtil.PREFIX)){// 这个是判断sql语句中是否有$字符，如果则证明有配置参数，但是可能用户定义了一个条件字段，包含$，如果是这样，这个判断去掉即可
			for (SqlScriptParameter ssp : sqlScriptParameterList) {
				if(index == ssp.getIndex() && SqlParameterParserUtil.sqlScriptContainsParameterName(conditionSql, ssp.getParameterName())){
					queryCondParameters.add(ssp.getActualValue());
					ssp.setActualValue("?");
					ssp.setIsPlaceholderParameter(true);
				}
			}
		}
	}
	
	
	
	
	/**
	 * 根据sql脚本参数，获取最终的sql脚本对象
	 * @param sqlScript
	 * @param sqlParameterValues 
	 * @param 
	 * 
	 * @throws EDBVendorIsNullException 
	 * @throws SqlScriptSyntaxException 
	 * @throws AnalyzeSqlScriptException 
	 */
	public static FinalSqlScriptStatement getFinalSqlScript(ComSqlScript sqlScript, List<List<Object>> sqlParameterValues) throws EDBVendorIsNullException, SqlScriptSyntaxException, AnalyzeSqlScriptException{
		if(sqlScript.getSqlScriptType().equals(SqlStatementType.PROCEDURE)){// 是存储过程，不解析最终的sql语句
			return null;
		}
		
		TStatementList sqlStatementList = sqlScript.getGsqlParser().sqlstatements;
		if(sqlStatementList.size() == 0){
			throw new AnalyzeSqlScriptException("没有找到可以处理的sql脚本语句");
		}
		FinalSqlScriptStatement finalSqlScript = new FinalSqlScriptStatement();
		
		List<SqlScriptParameter> sqlScriptParameters = sqlScript.getSqlScriptParameterList();
		TCustomSqlStatement sqlStatement = sqlStatementList.get(0);
		switch(sqlStatement.sqlstatementtype){
			case sstselect:
				if(sqlStatementList.size() > 1){
					throw new AnalyzeSqlScriptException("平台目前只支持一次处理一条select sql脚本语句");
				}
				finalSqlScript.setIsSelectSqlScript(true);
				setFinalSelectSqlHandler(sqlScriptParameters, finalSqlScript, sqlStatement, sqlParameterValues);
				break;
			case sstinsert:
				finalSqlScript.setIsInsertSqlScript(true);
				setFinalModifySqlHandler(sqlScriptParameters, finalSqlScript, sqlStatementList, sqlParameterValues);
				break;
			case sstupdate:
				finalSqlScript.setIsUpdateSqlScript(true);
				setFinalModifySqlHandler(sqlScriptParameters, finalSqlScript, sqlStatementList, sqlParameterValues);
				break;
			case sstdelete:
				finalSqlScript.setIsDeleteSqlScript(true);
				setFinalModifySqlHandler(sqlScriptParameters, finalSqlScript, sqlStatementList, sqlParameterValues);
				break;
		}
		if(sqlScriptParameters != null && sqlScriptParameters.size() > 0){
			sqlScriptParameters.clear();
		}
		return finalSqlScript;
	}
	
	/**
	 * 最终的insert/update/delete sql语句处理
	 * @param sqlScriptParameters
	 * @param finalSelect
	 * @param sqlStatement
	 * @param sqlParameterValues 
	 */
	private static void setFinalModifySqlHandler(List<SqlScriptParameter> sqlScriptParameters, FinalSqlScriptStatement finalSqlScript, TStatementList sqlStatementList, List<List<Object>> sqlParameterValues) {
		int sqlLen = sqlStatementList.size();
		String[] modifySqlArr = new String[sqlLen];
		
		int index;
		List<Object> sqlParamValues;
		for(int i = 0; i < sqlLen; i++){
			index = i+1;
			sqlParamValues = new ArrayList<Object>(8);
			for (SqlScriptParameter ssp : sqlScriptParameters) {
				if(ssp.getIndex() == index){
					sqlParamValues.add(ssp.getActualValue());
					ssp.setActualValue("?");
				}
			}
			sqlParameterValues.add(sqlParamValues);
			
			modifySqlArr[i] = SqlParameterParserUtil.replaceSqlScriptParams(index, sqlStatementList.get(i).toString(), sqlScriptParameters);
		}
		finalSqlScript.setFinalModifySqlArr(modifySqlArr);
	}

	/**
	 * 最终的select sql语句处理
	 * @param sqlScriptParameters
	 * @param finalSelect
	 * @param sqlStatement
	 * @param sqlParameterValues 
	 * 
	 * @throws AnalyzeSqlScriptException 
	 * @throws EDBVendorIsNullException 
	 * @throws SqlScriptSyntaxException 
	 */
	private static void setFinalSelectSqlHandler(List<SqlScriptParameter> sqlScriptParameters, FinalSqlScriptStatement finalSelect, TCustomSqlStatement sqlStatement, List<List<Object>> sqlParameterValues) throws SqlScriptSyntaxException, EDBVendorIsNullException, AnalyzeSqlScriptException {
		if(sqlScriptParameters != null && sqlScriptParameters.size() > 0){
			List<Object> queryCondParameters = new ArrayList<Object>();
			// 处理参数，将实际值存储到queryCondParameters集合中，再转换成?，替换到select sql语句中
			for (SqlScriptParameter ssp : sqlScriptParameters) {
				if(ssp.getIndex() == 1 && ssp.getIsPlaceholderParameter()){//如果是条件参数，将值加入到queryCondParameters中，并将实际值改为?
					queryCondParameters.add(ssp.getActualValue());
					ssp.setActualValue("?");
				}
			}
			// 将该条select sql的条件值对象加到最终的参数集合中
			sqlParameterValues.add(queryCondParameters);
			
			analysisSelectSqlScript(SqlParameterParserUtil.replaceSqlScriptParams(1, sqlStatement.toString(), sqlScriptParameters), finalSelect);
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
	 * 
	 * @throws EDBVendorIsNullException 
	 * @throws SqlScriptSyntaxException 
	 * @throws AnalyzeSqlScriptException 
	 */
	private static void analysisSelectSqlScript(String sqlScript, FinalSqlScriptStatement sqlScriptFinalStatement) throws SqlScriptSyntaxException, EDBVendorIsNullException, AnalyzeSqlScriptException{
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
}
