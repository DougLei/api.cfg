package com.king.tooth.sys.entity.common;

import gudusoft.gsqlparser.TGSqlParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.exception.gsp.AnalyzeSqlScriptException;
import com.king.tooth.exception.gsp.EDBVendorIsNullException;
import com.king.tooth.exception.gsp.SqlScriptSyntaxException;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.common.sqlscript.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.common.sqlscript.ProcedureSqlScriptParameter;
import com.king.tooth.sys.entity.common.sqlscript.SqlQueryResultColumn;
import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameter;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.sqlparser.SqlParameterParserUtil;
import com.king.tooth.util.sqlparser.SqlStatementParserUtil;

/**
 * [通用的]sql脚本资源对象
 * @author StoneKing
 */
@SuppressWarnings("serial")
public class ComSqlScript extends AbstractSysResource implements ITable, IEntity{
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
	 * sql脚本属于的数据库类型
	 */
	private String dbType;
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
	/**
	 * 是否内置
	 */
	private int isBuiltin;
	/**
	 * 是否创建内置资源
	 * <p>只有isBuiltin=1的时候，这个值才有效</p>
	 */
	private int isCreateBuiltinResource;
	
	//--------------------------------------------------------
	/**
	 * 解析对象
	 */
	private TGSqlParser gsqlParser;
	
	/**
	 * 在调用sql资源时，保存被处理过的，可以执行的最终查询的sql脚本语句对象
	 */
	private FinalSqlScriptStatement finalSqlScript;
	
	public ComSqlScript() {
	}
	public ComSqlScript(String sqlScriptCaption, String sqlScriptResourceName, String sqlScriptContent) throws SqlScriptSyntaxException, AnalyzeSqlScriptException, EDBVendorIsNullException {
		this();
		this.sqlScriptCaption = sqlScriptCaption;
		doSetSqlScriptResourceName(sqlScriptResourceName);
		doSetSqlScriptContent(sqlScriptContent);
	}

	private void doSetSqlScriptResourceName(String sqlScriptResourceName) {
		this.sqlScriptResourceName = sqlScriptResourceName;
		this.sqlScriptCaption = sqlScriptResourceName;
	}
	private void doSetSqlScriptContent(String sqlScriptContent) throws SqlScriptSyntaxException, AnalyzeSqlScriptException, EDBVendorIsNullException {
		if(StrUtils.isEmpty(sqlScriptContent)){
			throw new NullPointerException("sql脚本内容不能为空");
		}
		this.sqlScriptContent = sqlScriptContent;
		this.dbType = HibernateUtil.getCurrentDatabaseType();
		this.sqlScriptType = SqlStatementParserUtil.getSqlScriptType(getGsqlParser());
		setReqResourceMethod();
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
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getSqlScriptResourceName() {
		return sqlScriptResourceName;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
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
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
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
	public int getIsDeploymentRun() {
		return isDeploymentRun;
	}
	public void setIsDeploymentRun(int isDeploymentRun) {
		this.isDeploymentRun = isDeploymentRun;
	}
	public int getIsBuiltin() {
		return isBuiltin;
	}
	public void setIsBuiltin(int isBuiltin) {
		this.isBuiltin = isBuiltin;
	}
	public int getIsCreateBuiltinResource() {
		return isCreateBuiltinResource;
	}
	public void setIsCreateBuiltinResource(int isCreateBuiltinResource) {
		this.isCreateBuiltinResource = isCreateBuiltinResource;
	}
	public void setReqResourceMethod() {
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
	
	
	public void setGsqlParser(TGSqlParser gsqlParser) {
		this.gsqlParser = gsqlParser;
	}
	private void setGsqlParser(){
		if(this.gsqlParser == null){
			if(StrUtils.notEmpty(this.sqlScriptContent)){
				try {
					this.gsqlParser = SqlStatementParserUtil.getGsqlParser(sqlScriptContent);
				} catch (SqlScriptSyntaxException e) {
					throw new ParserException(ExceptionUtil.getErrMsg(e));
				} catch (EDBVendorIsNullException e) {
					throw new ParserException(ExceptionUtil.getErrMsg(e));
				}
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
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_SQL_SCRIPT");
		table.setName("[通用的]sql脚本资源对象表");
		table.setComments("[通用的]sql脚本资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(18);
		
		CfgColumndata sqlScriptCaptionColumn = new CfgColumndata("sql_script_caption");
		sqlScriptCaptionColumn.setName("sql脚本的标题");
		sqlScriptCaptionColumn.setComments("sql脚本的标题");
		sqlScriptCaptionColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptCaptionColumn.setLength(50);
		sqlScriptCaptionColumn.setOrderCode(1);
		columns.add(sqlScriptCaptionColumn);
		
		CfgColumndata sqlScriptResourceNameColumn = new CfgColumndata("sql_script_resource_name");
		sqlScriptResourceNameColumn.setName("sql脚本资源名称");
		sqlScriptResourceNameColumn.setComments("sql脚本资源名称(调用时用到)");
		sqlScriptResourceNameColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptResourceNameColumn.setLength(100);
		sqlScriptResourceNameColumn.setOrderCode(2);
		columns.add(sqlScriptResourceNameColumn);
		
		CfgColumndata dbTypeColumn = new CfgColumndata("db_type");
		dbTypeColumn.setName("sql脚本属于的数据库类型");
		dbTypeColumn.setComments("sql脚本属于的数据库类型");
		dbTypeColumn.setColumnType(DataTypeConstants.STRING);
		dbTypeColumn.setLength(10);
		dbTypeColumn.setOrderCode(3);
		columns.add(dbTypeColumn);
		
		CfgColumndata sqlScriptTypeColumn = new CfgColumndata("sql_script_type");
		sqlScriptTypeColumn.setName("sql脚本类型");
		sqlScriptTypeColumn.setComments("sql脚本类型：如果有多个sql脚本，以第一个sql脚本的类型为准");
		sqlScriptTypeColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptTypeColumn.setLength(40);
		sqlScriptTypeColumn.setOrderCode(4);
		columns.add(sqlScriptTypeColumn);
		
		CfgColumndata sqlScriptContentColumn = new CfgColumndata("sql_script_content");
		sqlScriptContentColumn.setName("sql脚本内容");
		sqlScriptContentColumn.setComments("sql脚本内容");
		sqlScriptContentColumn.setColumnType(DataTypeConstants.CLOB);
		sqlScriptContentColumn.setOrderCode(5);
		columns.add(sqlScriptContentColumn);
		
		CfgColumndata sqlScriptParametersColumn = new CfgColumndata("sql_script_parameters");
		sqlScriptParametersColumn.setName("sql脚本的参数对象集合");
		sqlScriptParametersColumn.setComments("sql脚本的参数(json串)");
		sqlScriptParametersColumn.setColumnType(DataTypeConstants.STRING);
		sqlScriptParametersColumn.setLength(2000);
		sqlScriptParametersColumn.setOrderCode(6);
		columns.add(sqlScriptParametersColumn);
		
		CfgColumndata sqlQueryResultColumnsColumn = new CfgColumndata("sql_query_result_columns");
		sqlQueryResultColumnsColumn.setName("sql查询结果的列名对象集合");
		sqlQueryResultColumnsColumn.setComments("sql查询结果的列名对象集合(json串)[该属性针对查询的sql语句]");
		sqlQueryResultColumnsColumn.setColumnType(DataTypeConstants.STRING);
		sqlQueryResultColumnsColumn.setLength(1000);
		sqlQueryResultColumnsColumn.setOrderCode(7);
		columns.add(sqlQueryResultColumnsColumn);
		
		CfgColumndata procedureNameColumn = new CfgColumndata("procedure_name");
		procedureNameColumn.setName("存储过程名称");
		procedureNameColumn.setComments("存储过程名称");
		procedureNameColumn.setColumnType(DataTypeConstants.STRING);
		procedureNameColumn.setLength(80);
		procedureNameColumn.setOrderCode(8);
		columns.add(procedureNameColumn);
		
		CfgColumndata procedureParametersColumn = new CfgColumndata("procedure_parameters");
		procedureParametersColumn.setName("存储过程参数对象集合");
		procedureParametersColumn.setComments("存储过程参数对象集合(json串)");
		procedureParametersColumn.setColumnType(DataTypeConstants.STRING);
		procedureParametersColumn.setLength(1000);
		procedureParametersColumn.setOrderCode(9);
		columns.add(procedureParametersColumn);
		
		CfgColumndata commentsColumn = new CfgColumndata("comments");
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setColumnType(DataTypeConstants.STRING);
		commentsColumn.setLength(200);
		commentsColumn.setOrderCode(10);
		columns.add(commentsColumn);
		
		CfgColumndata isDeploymentRunColumn = new CfgColumndata("is_deployment_run");
		isDeploymentRunColumn.setName("是否部署到正式环境");
		isDeploymentRunColumn.setComments("是否部署到正式环境");
		isDeploymentRunColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentRunColumn.setLength(1);
		isDeploymentRunColumn.setOrderCode(11);
		columns.add(isDeploymentRunColumn);
		
		CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
		isBuiltinColumn.setName("是否内置");
		isBuiltinColumn.setComments("是否内置");
		isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
		isBuiltinColumn.setLength(1);
		isBuiltinColumn.setOrderCode(12);
		columns.add(isBuiltinColumn);
		
		CfgColumndata isCreateBuiltinResourceColumn = new CfgColumndata("is_create_builtin_resource");
		isCreateBuiltinResourceColumn.setName("是否创建内置资源");
		isCreateBuiltinResourceColumn.setComments("是否创建内置资源：只有isBuiltin=1的时候，这个值才有效");
		isCreateBuiltinResourceColumn.setColumnType(DataTypeConstants.INTEGER);
		isCreateBuiltinResourceColumn.setLength(1);
		isCreateBuiltinResourceColumn.setOrderCode(13);
		columns.add(isCreateBuiltinResourceColumn);
		
		table.setColumns(columns);
		table.setReqResourceMethod(ISysResource.GET);
		table.setIsBuiltin(1);
		return table;
	}

	public String toDropTable() {
		return "COM_SQL_SCRIPT";
	}
	
	public int getResourceType() {
		return SQLSCRIPT;
	}
	public String getResourceName() {
		return sqlScriptResourceName;
	}	
	public String getResourceId() {
		return getId();
	}
	public String getReqResourceMethod() {
		if(reqResourceMethod == null){
			return ALL;
		}
		return reqResourceMethod;
	}
	
	public String getEntityName() {
		return "ComSqlScript";
	}
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("isBuiltin", isBuiltin+"");
		json.put("isCreateBuiltinResource", isCreateBuiltinResource+"");
		json.put("isDeploymentRun", isDeploymentRun+"");
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
