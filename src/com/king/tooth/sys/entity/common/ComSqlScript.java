package com.king.tooth.sys.entity.common;

import gudusoft.gsqlparser.TGSqlParser;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.exception.gsp.EDBVendorIsNullException;
import com.king.tooth.exception.gsp.SqlScriptSyntaxException;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.common.sqlscript.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.common.sqlscript.ProcedureSqlScriptParameter;
import com.king.tooth.sys.entity.common.sqlscript.SqlQueryResultColumn;
import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameter;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.sqlparser.SqlParameterParserUtil;
import com.king.tooth.util.sqlparser.SqlStatementParserUtil;

/**
 * sql脚本资源对象
 * @author StoneKing
 */
@SuppressWarnings("serial")
public class ComSqlScript extends AbstractSysResource implements ITable, IEntity, IEntityPropAnalysis{
	/**
	 * sql脚本的标题
	 */
	private String sqlScriptCaption;
	/**
	 * sql脚本资源名称
	 * (调用时用到)
	 */
	private String sqlScriptResourceName;
	/**
	 * sql脚本类型
	 * <p>如果有多个sql脚本，以第一个sql脚本的类型为准</p>
	 */
	private String sqlScriptType;
	/**
	 * sql脚本内容
	 */
	private String sqlScriptContent;
	/**
	 * sql脚本的参数集合(json串)
	 */
	private String sqlScriptParameters;
	private List<SqlScriptParameter> sqlScriptParameterList;
	
	/**
	 * sql查询结果的列名对象集合(json串)
	 * <p>[该属性针对查询的sql语句]</p>
	 */
	private String sqlQueryResultColumns;
	private List<SqlQueryResultColumn> sqlQueryResultColumnList;
	
	/**
	 * 存储过程名称
	 * <p>[该属性针对存储过程的sql语句]</p>
	 */
	private String procedureName;
	/**
	 * 存储过程参数对象集合(json串)
	 * <p>[该属性针对存储过程的sql语句]</p>
	 */
	private String procedureParameters;
	private List<ProcedureSqlScriptParameter> procedureParameterList;
	
	/**
	 * 备注
	 */
	private String comments;
	
	//--------------------------------------------------------
	/**
	 * 解析对象
	 */
	private TGSqlParser gsqlParser;
	
	/**
	 * 在调用sql资源时，保存被处理过的，可以执行的最终查询的sql脚本语句对象
	 */
	private FinalSqlScriptStatement finalSqlScript;
	
	public void setProcedureParameterList(List<ProcedureSqlScriptParameter> procedureParameterList) {
		this.procedureParameterList = procedureParameterList;
		if(procedureParameterList != null && procedureParameterList.size() > 0){
			this.procedureParameters = JsonUtil.toJsonString(procedureParameterList, false);
		}
	}
	public void setSqlScriptResourceName(String sqlScriptResourceName) {
		this.sqlScriptResourceName = sqlScriptResourceName;
	}
	public void setSqlScriptContent(String sqlScriptContent) {
		this.sqlScriptContent = sqlScriptContent;
	}
	public String getSqlScriptCaption() {
		if(StrUtils.isEmpty(sqlScriptCaption)){
			sqlScriptCaption = sqlScriptResourceName;
		}
		return sqlScriptCaption;
	}
	public List<ProcedureSqlScriptParameter> getProcedureParameterList() {
		return procedureParameterList;
	}
	public void setSqlScriptCaption(String sqlScriptCaption) {
		this.sqlScriptCaption = sqlScriptCaption;
	}
	public String getSqlScriptType() {
		return sqlScriptType;
	}
	public void setSqlScriptType(String sqlScriptType) {
		this.sqlScriptType = sqlScriptType;
	}
	public String getSqlScriptResourceName() {
		return sqlScriptResourceName;
	}
	public String getSqlScriptContent() {
		return sqlScriptContent;
	}
	public String getSqlScriptParameters() {
		return sqlScriptParameters;
	}
	public List<SqlQueryResultColumn> getSqlQueryResultColumnList() {
		return sqlQueryResultColumnList;
	}
	public String getSqlQueryResultColumns() {
		return sqlQueryResultColumns;
	}
	public void setSqlQueryResultColumns(String sqlQueryResultColumns) {
		this.sqlQueryResultColumns = sqlQueryResultColumns;
		this.sqlQueryResultColumnList = JsonUtil.parseArray(sqlQueryResultColumns, SqlQueryResultColumn.class);
	}
	public void setSqlScriptParameters(String sqlScriptParameters) {
		this.sqlScriptParameters = sqlScriptParameters;
		this.sqlScriptParameterList = JsonUtil.parseArray(sqlScriptParameters, SqlScriptParameter.class);
	}
	public void setProcedureParameters(String procedureParameters) {
		this.procedureParameters = procedureParameters;
		this.procedureParameterList = JsonUtil.parseArray(procedureParameters, ProcedureSqlScriptParameter.class);
	}
	public String getProcedureParameters() {
		return procedureParameters;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public List<SqlScriptParameter> getSqlScriptParameterList() {
		return sqlScriptParameterList;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public FinalSqlScriptStatement getFinalSqlScript() {
		return finalSqlScript;
	}
	public void setFinalSqlScript(FinalSqlScriptStatement finalSqlScript) {
		this.finalSqlScript = finalSqlScript;
	}
	
	
	private void setGsqlParser(){
		if(this.gsqlParser == null){
			try {
				this.gsqlParser = SqlStatementParserUtil.getGsqlParser(sqlScriptContent);
			} catch (SqlScriptSyntaxException e) {
				throw new ParserException(ExceptionUtil.getErrMsg(e));
			} catch (EDBVendorIsNullException e) {
				throw new ParserException(ExceptionUtil.getErrMsg(e));
			}
		}
	}
	
	/**
	 * 获取sql脚本的解析器gsql对象实例
	 * @return
	 */
	public TGSqlParser getGsqlParser() {
		setGsqlParser();
		return gsqlParser;
	}
	
	/**
	 * 将调用sql资源时，实际传过来的值存到参数集合中(sql参数集合/procedure sql参数集合)
	 * @param sqlScriptActualParameters
	 */
	public void setActualParams(List<SqlScriptParameter> sqlScriptActualParameters) {
		if(sqlScriptActualParameters == null || sqlScriptActualParameters.size() == 0){
			return;
		}
		if(this.sqlScriptType.equals(SqlStatementType.PROCEDURE)){
			if(StrUtils.isEmpty(this.procedureParameters) || this.procedureParameterList == null || this.procedureParameterList.size() == 0){
				throw new NullPointerException("在调用procedure sql资源时，传入的实际参数集合为：["+sqlScriptActualParameters+"]，但是被调用的procedure sql资源["+this.sqlScriptResourceName+"("+this.procedureName+")]却不存在任何参数对象集合。请检查该procedure sql资源是否确实有参数配置，并确认合法调用sql资源，或联系管理员");
			}
			for (SqlScriptParameter ssap : sqlScriptActualParameters) {
				for (ProcedureSqlScriptParameter pssp : procedureParameterList) {
					if(ssap.getIndex() == 1 && ssap.getParameterName().equalsIgnoreCase(pssp.getParameterName())){
						ssap.setIsPlaceholderParameter(true);
						pssp.setActualValue(ssap.getActualValue());
					}
				}
			}
		}else{
			if(StrUtils.isEmpty(this.sqlScriptParameters) || this.sqlScriptParameterList == null || this.sqlScriptParameterList.size() == 0){
				throw new NullPointerException("在调用sql资源时，传入的实际参数集合为：["+sqlScriptActualParameters+"]，但是被调用的sql资源["+this.sqlScriptResourceName+"]却不存在任何sql脚本的参数对象集合。请检查该sql资源是否确实有参数配置，并确认合法调用sql资源，或联系管理员");
			}
			for (SqlScriptParameter ssap : sqlScriptActualParameters) {
				for (SqlScriptParameter ssp : sqlScriptParameterList) {
					if(ssap.getIndex() == ssp.getIndex() && ssap.getParameterName().equalsIgnoreCase(ssp.getParameterName())){
						ssap.setIsPlaceholderParameter(ssp.getIsPlaceholderParameter());
						ssp.setActualValue(ssap.getActualValue());
						break;
					}
				}
			}
		}
	}
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_SQL_SCRIPT", 0);
		table.setIsResource(1);
		table.setName("sql脚本资源对象表");
		table.setComments("sql脚本资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET+","+DELETE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(20);
		
		ComColumndata sqlScriptCaptionColumn = new ComColumndata("sql_script_caption");
		sqlScriptCaptionColumn.setName("sql脚本的标题");
		sqlScriptCaptionColumn.setComments("sql脚本的标题");
		sqlScriptCaptionColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptCaptionColumn.setLength(50);
		sqlScriptCaptionColumn.setOrderCode(1);
		columns.add(sqlScriptCaptionColumn);
		
		ComColumndata sqlScriptResourceNameColumn = new ComColumndata("sql_script_resource_name");
		sqlScriptResourceNameColumn.setName("sql脚本资源名称");
		sqlScriptResourceNameColumn.setComments("sql脚本资源名称(调用时用到)");
		sqlScriptResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptResourceNameColumn.setLength(60);
		sqlScriptResourceNameColumn.setOrderCode(2);
		columns.add(sqlScriptResourceNameColumn);
		
		ComColumndata sqlScriptTypeColumn = new ComColumndata("sql_script_type");
		sqlScriptTypeColumn.setName("sql脚本类型");
		sqlScriptTypeColumn.setComments("sql脚本类型：如果有多个sql脚本，以第一个sql脚本的类型为准");
		sqlScriptTypeColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptTypeColumn.setLength(40);
		sqlScriptTypeColumn.setOrderCode(3);
		columns.add(sqlScriptTypeColumn);
		
		ComColumndata sqlScriptContentColumn = new ComColumndata("sql_script_content");
		sqlScriptContentColumn.setName("sql脚本内容");
		sqlScriptContentColumn.setComments("sql脚本内容");
		sqlScriptContentColumn.setColumnType(DataTypeConstants.CLOB);
		sqlScriptContentColumn.setOrderCode(4);
		columns.add(sqlScriptContentColumn);
		
		ComColumndata sqlScriptParametersColumn = new ComColumndata("sql_script_parameters");
		sqlScriptParametersColumn.setName("sql脚本的参数对象集合");
		sqlScriptParametersColumn.setComments("sql脚本的参数(json串)");
		sqlScriptParametersColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptParametersColumn.setLength(2000);
		sqlScriptParametersColumn.setOrderCode(5);
		columns.add(sqlScriptParametersColumn);
		
		ComColumndata sqlQueryResultColumnsColumn = new ComColumndata("sql_query_result_columns");
		sqlQueryResultColumnsColumn.setName("sql查询结果的列名对象集合");
		sqlQueryResultColumnsColumn.setComments("sql查询结果的列名对象集合(json串)[该属性针对查询的sql语句]");
		sqlQueryResultColumnsColumn.setColumnType(DataTypeConstants.STRING);
		sqlQueryResultColumnsColumn.setLength(1000);
		sqlQueryResultColumnsColumn.setOrderCode(6);
		columns.add(sqlQueryResultColumnsColumn);
		
		ComColumndata procedureNameColumn = new ComColumndata("procedure_name");
		procedureNameColumn.setName("存储过程名称");
		procedureNameColumn.setComments("存储过程名称");
		procedureNameColumn.setColumnType(DataTypeConstants.STRING);
		procedureNameColumn.setLength(80);
		procedureNameColumn.setOrderCode(7);
		columns.add(procedureNameColumn);
		
		ComColumndata procedureParametersColumn = new ComColumndata("procedure_parameters");
		procedureParametersColumn.setName("存储过程参数对象集合");
		procedureParametersColumn.setComments("存储过程参数对象集合(json串)");
		procedureParametersColumn.setColumnType(DataTypeConstants.STRING);
		procedureParametersColumn.setLength(1000);
		procedureParametersColumn.setOrderCode(8);
		columns.add(procedureParametersColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments");
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setColumnType(DataTypeConstants.STRING);
		commentsColumn.setLength(200);
		commentsColumn.setOrderCode(9);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SQL_SCRIPT";
	}
	
	public String getEntityName() {
		return "ComSqlScript";
	}

	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("isEnabled", isEnabled+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isDeployed", isDeployed+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
	
	public void validNotNullProps() {
		if(!isValidNotNullProps){
			if(StrUtils.isEmpty(sqlScriptResourceName)){
				throw new NullPointerException("sql脚本资源名称不能为空");
			}
			if(StrUtils.isEmpty(sqlScriptContent)){
				throw new NullPointerException("sql脚本内容不能为空");
			}
			isValidNotNullProps = true;
		}
	}
	
	public void analysisResourceProp() {
		validNotNullProps();
		try {
			this.sqlScriptType = SqlStatementParserUtil.getSqlScriptType(getGsqlParser());
			setReqResourceMethod();
			
			// 如果是存储过程，则单独处理
			if(SqlStatementType.PROCEDURE.equals(this.sqlScriptType)){
				SqlStatementParserUtil.processProcedure(this);
				return;
			}
			
			// 读取内容去解析，获取sql语句中的参数集合 sqlScriptParameterList
			setSqlScriptParameterList(SqlParameterParserUtil.analysisMultiSqlScriptParam(getSqlScriptArr()));
			// 【针对select sql语句，处理其参数】读取内容去解析，获取selectsql脚本语句的查询结果的列名集合
			setSqlQueryResultColumns(SqlStatementParserUtil.getSelectSqlOfResultColumnsAndAnalysisSqlScriptParameters(this));
			// 将sql脚本的参数处理后，重新赋值给sqlScriptParameters属性，这时，processed=true，isUpdated=true
			setSqlScriptParameterList(this.sqlScriptParameterList);
		} catch (Exception e) {
			Log4jUtil.debug("[ComSqlScript.analysisResourceProp]解析出现异常：{}", ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 设置请求资源的方式
	 */
	private void setReqResourceMethod() {
		if(SqlStatementType.SELECT.equals(sqlScriptType)){
			this.reqResourceMethod = GET;
		}else if(SqlStatementType.UPDATE.equals(sqlScriptType)){
			this.reqResourceMethod = PUT;
		}else if(SqlStatementType.INSERT.equals(sqlScriptType)){
			this.reqResourceMethod = POST;
		}else if(SqlStatementType.DELETE.equals(sqlScriptType)){
			this.reqResourceMethod = DELETE;
		}else if(SqlStatementType.PROCEDURE.equals(sqlScriptType)){
			this.reqResourceMethod = POST;
		}else{
			this.reqResourceMethod = NONE;
		}
	}
	
	/**
	 * 获取sql脚本的语句数组
	 * @return
	 */
	private String[] getSqlScriptArr() {
		return SqlStatementParserUtil.getSqlScriptArr(getGsqlParser());
	}
	
	private void setSqlQueryResultColumns(List<SqlQueryResultColumn> queryResultColumns) {
		if(queryResultColumns != null && queryResultColumns.size() > 0){
			this.sqlQueryResultColumnList = queryResultColumns;
			this.sqlQueryResultColumns = JsonUtil.toJsonString(this.sqlQueryResultColumnList, false);
		}
	}
	
	private void setSqlScriptParameterList(List<SqlScriptParameter> sqlScriptParameterList) {
		this.sqlScriptParameterList = sqlScriptParameterList;
		if(sqlScriptParameterList != null && sqlScriptParameterList.size() > 0){
			this.sqlScriptParameters = JsonUtil.toJsonString(sqlScriptParameterList, false);
		}
	}
	
	public ComSysResource turnToResource() {
		analysisResourceProp();
		ComSysResource resource = super.turnToResource();
		resource.setRefResourceId(id);
		resource.setResourceType(SQLSCRIPT);
		resource.setResourceName(sqlScriptResourceName);
		if(isBuiltin == 1){
			resource.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		}
		return resource;
	}
}
