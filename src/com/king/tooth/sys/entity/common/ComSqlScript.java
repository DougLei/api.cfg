package com.king.tooth.sys.entity.common;

import gudusoft.gsqlparser.TGSqlParser;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.exception.gsp.EDBVendorIsNullException;
import com.king.tooth.exception.gsp.SqlScriptSyntaxException;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.common.sqlscript.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.common.sqlscript.ProcedureSqlScriptParameter;
import com.king.tooth.sys.entity.common.sqlscript.SqlQueryResultColumn;
import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameter;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.sqlparser.SqlParameterParserUtil;
import com.king.tooth.util.sqlparser.SqlStatementParserUtil;

/**
 * sql脚本资源对象
 * @author StoneKing
 */
@SuppressWarnings("serial")
public class ComSqlScript extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
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
	@JSONField(serialize = false)
	private List<SqlScriptParameter> sqlScriptParameterList;
	
	/**
	 * sql查询结果的列名对象集合(json串)
	 * <p>[该属性针对查询的sql语句]</p>
	 */
	private String sqlQueryResultColumns;
	@JSONField(serialize = false)
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
	@JSONField(serialize = false)
	private List<ProcedureSqlScriptParameter> procedureParameterList;
	
	/**
	 * 备注
	 */
	private String comments;
	
	//--------------------------------------------------------
	/**
	 * 解析对象
	 */
	@JSONField(serialize = false)
	private TGSqlParser gsqlParser;
	/**
	 * 关联的数据库id
	 * 该字段在发布的时候用到
	 * @see turnToPublish()
	 */
	@JSONField(serialize = false)
	private String refDatabaseId;
	
	/**
	 * 在调用sql资源时，保存被处理过的，可以执行的最终查询的sql脚本语句对象
	 */
	@JSONField(serialize = false)
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
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	
	
	private void doSetGsqlParser(){
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
		doSetGsqlParser();
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_SQL_SCRIPT", 0);
		table.setName("sql脚本资源对象表");
		table.setComments("sql脚本资源对象表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(22);
		
		ComColumndata sqlScriptCaptionColumn = new ComColumndata("sql_script_caption", DataTypeConstants.STRING, 50);
		sqlScriptCaptionColumn.setName("sql脚本的标题");
		sqlScriptCaptionColumn.setComments("sql脚本的标题");
		sqlScriptCaptionColumn.setOrderCode(1);
		columns.add(sqlScriptCaptionColumn);
		
		ComColumndata sqlScriptResourceNameColumn = new ComColumndata("sql_script_resource_name", DataTypeConstants.STRING, 60);
		sqlScriptResourceNameColumn.setName("sql脚本资源名称");
		sqlScriptResourceNameColumn.setComments("sql脚本资源名称(调用时用到)");
		sqlScriptResourceNameColumn.setIsNullabled(0);
		sqlScriptResourceNameColumn.setOrderCode(2);
		columns.add(sqlScriptResourceNameColumn);
		
		ComColumndata sqlScriptTypeColumn = new ComColumndata("sql_script_type", DataTypeConstants.STRING, 20);
		sqlScriptTypeColumn.setName("sql脚本类型");
		sqlScriptTypeColumn.setComments("sql脚本类型：如果有多个sql脚本，以第一个sql脚本的类型为准");
		sqlScriptTypeColumn.setOrderCode(3);
		columns.add(sqlScriptTypeColumn);
		
		ComColumndata sqlScriptContentColumn = new ComColumndata("sql_script_content", DataTypeConstants.CLOB, 0);
		sqlScriptContentColumn.setName("sql脚本内容");
		sqlScriptContentColumn.setComments("sql脚本内容");
		sqlScriptContentColumn.setIsNullabled(0);
		sqlScriptContentColumn.setOrderCode(4);
		columns.add(sqlScriptContentColumn);
		
		ComColumndata sqlScriptParametersColumn = new ComColumndata("sql_script_parameters", DataTypeConstants.STRING, 9999);
		sqlScriptParametersColumn.setName("sql脚本的参数对象集合");
		sqlScriptParametersColumn.setComments("sql脚本的参数(json串)");
		sqlScriptParametersColumn.setOrderCode(5);
		columns.add(sqlScriptParametersColumn);
		
		ComColumndata sqlQueryResultColumnsColumn = new ComColumndata("sql_query_result_columns", DataTypeConstants.STRING, 9999);
		sqlQueryResultColumnsColumn.setName("sql查询结果的列名对象集合");
		sqlQueryResultColumnsColumn.setComments("sql查询结果的列名对象集合(json串)[该属性针对查询的sql语句]");
		sqlQueryResultColumnsColumn.setOrderCode(6);
		columns.add(sqlQueryResultColumnsColumn);
		
		ComColumndata procedureNameColumn = new ComColumndata("procedure_name", DataTypeConstants.STRING, 80);
		procedureNameColumn.setName("存储过程名称");
		procedureNameColumn.setComments("存储过程名称");
		procedureNameColumn.setOrderCode(7);
		columns.add(procedureNameColumn);
		
		ComColumndata procedureParametersColumn = new ComColumndata("procedure_parameters", DataTypeConstants.STRING, 9999);
		procedureParametersColumn.setName("存储过程参数对象集合");
		procedureParametersColumn.setComments("存储过程参数对象集合(json串)");
		procedureParametersColumn.setOrderCode(8);
		columns.add(procedureParametersColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setOrderCode(9);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SQL_SCRIPT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComSqlScript";
	}

	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	public String validNotNullProps() {
		if(!isValidNotNullProps){
			isValidNotNullProps = true;
			if(StrUtils.isEmpty(sqlScriptResourceName)){
				validNotNullPropsResult = "sql脚本资源名称不能为空";
				return validNotNullPropsResult;
			}
			if(StrUtils.isEmpty(sqlScriptContent)){
				validNotNullPropsResult = "sql脚本内容不能为空";
				return validNotNullPropsResult;
			}
		}
		return validNotNullPropsResult;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			try {
				this.sqlScriptType = SqlStatementParserUtil.getSqlScriptType(getGsqlParser());
				setReqResourceMethod();
				
				// 如果是存储过程，则单独处理
				if(SqlStatementType.PROCEDURE.equals(this.sqlScriptType)){
					SqlStatementParserUtil.processProcedure(this);
				}else{
					// 读取内容去解析，获取sql语句中的参数集合 sqlScriptParameterList
					doSetSqlScriptParameterList(SqlParameterParserUtil.analysisMultiSqlScriptParam(getSqlScriptArr()));
					// 【针对select sql语句，处理其参数】读取内容去解析，获取selectsql脚本语句的查询结果的列名集合
					doSetSqlQueryResultColumns(SqlStatementParserUtil.getSelectSqlOfResultColumnsAndAnalysisSqlScriptParameters(this));
					// 将sql脚本的参数处理后，重新赋值给sqlScriptParameters属性，这时，processed=true，isUpdated=true
					doSetSqlScriptParameterList(this.sqlScriptParameterList);
				}
			} catch (Exception e) {
				result = ExceptionUtil.getErrMsg(e);
				Log4jUtil.debug("[ComSqlScript.analysisResourceProp]解析出现异常：{}", result);
			}
			
			// 每次解析sql脚本后，如果以下字段为空，则默认用空字符串展示，为了让hibernate在save的时候，更新这些字段的值
			if(StrUtils.isEmpty(sqlScriptParameters)){
				sqlScriptParameters = "";
			}
			if(StrUtils.isEmpty(sqlQueryResultColumns)){
				sqlQueryResultColumns = "";
			}
			if(StrUtils.isEmpty(procedureName)){
				procedureName = "";
			}
			if(StrUtils.isEmpty(procedureParameters)){
				procedureParameters = "";
			}
		}
		return result;
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
	@JSONField(serialize = false)
	private String[] getSqlScriptArr() {
		return SqlStatementParserUtil.getSqlScriptArr(getGsqlParser());
	}
	
	private void doSetSqlQueryResultColumns(List<SqlQueryResultColumn> queryResultColumns) {
		if(queryResultColumns != null && queryResultColumns.size() > 0){
			this.sqlQueryResultColumnList = queryResultColumns;
			this.sqlQueryResultColumns = JsonUtil.toJsonString(this.sqlQueryResultColumnList, false);
		}
	}
	
	private void doSetSqlScriptParameterList(List<SqlScriptParameter> sqlScriptParameterList) {
		this.sqlScriptParameterList = sqlScriptParameterList;
		if(sqlScriptParameterList != null && sqlScriptParameterList.size() > 0){
			this.sqlScriptParameters = JsonUtil.toJsonString(sqlScriptParameterList, false);
		}
	}
	
	public ComSysResource turnToResource() {
		ComSysResource resource = super.turnToResource();
		resource.setResourceType(SQLSCRIPT);
		resource.setResourceName(sqlScriptResourceName);
		return resource;
	}
	
	public ComSysResource turnToPublishResource(String projectId, String refResourceId) {
		ComSysResource resource = turnToResource();
		resource.setId(ResourceHandlerUtil.getIdentity());
		resource.setRefDataId(id);
		resource.setProjectId(projectId);
		resource.setRefResourceId(refResourceId);
		return resource;
	}
	
	@JSONField(serialize = false)
	public Integer getResourceType() {
		return SQLSCRIPT;
	}
	
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(projectId);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(sqlScriptResourceName);
		publish.setResourceType(SQLSCRIPT);
		return publish;
	}
}
