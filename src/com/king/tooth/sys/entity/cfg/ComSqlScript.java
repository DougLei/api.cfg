package com.king.tooth.sys.entity.cfg;

import gudusoft.gsqlparser.TGSqlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.ProcessStringTypeJsonExtend;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.sql.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.cfg.sql.SqlQueryResultColumn;
import com.king.tooth.sys.entity.cfg.sql.SqlScriptParameterNameRecord;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.sqlparser.SqlParameterParserUtil;
import com.king.tooth.util.sqlparser.SqlStatementParserUtil;

/**
 * sql脚本信息表
 * @author StoneKing
 */
@SuppressWarnings("serial")
public class ComSqlScript extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
	/**
	 * 数据库类型
	 */
	private String dbType;
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
	private List<ComSqlScriptParameter> sqlScriptParameterList;
	
	/**
	 * 存储过程名称
	 * <p>[该属性针对存储过程的sql语句]</p>
	 */
	private String procedureName;
	
	/**
	 * 备注
	 */
	private String comments;
	
	/**
	 * sql查询结果的列名对象集合(json串)
	 * <p>[该属性针对查询的sql语句，程序内部使用，不开放给用户]</p>
	 */
	private String sqlQueryResultColumns;
	@JSONField(serialize = false)
	private List<SqlQueryResultColumn> sqlQueryResultColumnList;
	
	/**
	 * sql参数名的记录对象集合
	 * <p>记录，第几个sql，都有哪些参数名，程序内部使用，不开放给用户</p>
	 */
	private String parameterNameRecords;
	@JSONField(serialize = false)
	private List<SqlScriptParameterNameRecord> parameterNameRecordList;
	
	//--------------------------------------------------------
	
	/**
	 * 是否解析参数
	 * <p>在添加或修改的时候使用的判断标识，在修改sql内容时，不想修改参数的话，则这个字段要为0，否则为1</p>
	 * <p>为1时，会自动解析sql脚本的参数，之前对参数的修改等操作，均被删除</p>
	 */
	@JSONField(serialize = false)
	private int isAnalysisParameters;
	
	/**
	 * 解析对象
	 */
	@JSONField(serialize = false)
	private TGSqlParser gsqlParser;
	
	/**
	 * 在调用sql资源时，保存被处理过的，可以执行的最终查询的sql脚本语句对象
	 */
	@JSONField(serialize = false)
	private FinalSqlScriptStatement finalSqlScript;
	
	/**
	 * 关联的数据库id
	 * 该字段在发布的时候用到
	 * @see turnToPublish()
	 */
	@JSONField(serialize = false)
	private String refDatabaseId;
	
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
	public void setSqlScriptCaption(String sqlScriptCaption) {
		this.sqlScriptCaption = sqlScriptCaption;
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
		this.sqlScriptParameterList = JsonUtil.parseArray(sqlScriptParameters, ComSqlScriptParameter.class);
	}
	@SuppressWarnings("unchecked")
	public void setParameterNameRecords(String parameterNameRecords) {
		this.parameterNameRecords = parameterNameRecords;
		if(StrUtils.isEmpty(parameterNameRecords)){
			return;
		}
		IJson ijson = ProcessStringTypeJsonExtend.getIJson(parameterNameRecords);
		int len = ijson.size();
		this.parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(len);
		
		SqlScriptParameterNameRecord spnr;
		JSONArray jsonArray;
		for(int i=0;i<len;i++){
			spnr = new SqlScriptParameterNameRecord(ijson.get(i).getIntValue("sqlIndex"));
			jsonArray = ijson.get(i).getJSONArray("parameterNames");
			if(jsonArray != null){
				spnr.addParameterNames(jsonArray.toJavaObject(List.class));
			}
			parameterNameRecordList.add(spnr);
		}
		ijson.clear();
	}
	public String getParameterNameRecords() {
		return parameterNameRecords;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public List<ComSqlScriptParameter> getSqlScriptParameterList() {
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
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public int getIsAnalysisParameters() {
		return isAnalysisParameters;
	}
	public void setIsAnalysisParameters(int isAnalysisParameters) {
		this.isAnalysisParameters = isAnalysisParameters;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public void initGsqlParser() {
		if(gsqlParser == null){
			gsqlParser = SqlStatementParserUtil.getGsqlParser(this.sqlScriptContent.replaceAll("\n", " ").replace("\t", " ").replaceAll("\r", " "));
		}
	}
	public TGSqlParser getGsqlParser() {
		initGsqlParser();
		return gsqlParser;
	}
	public String getSqlScriptType() {
		return sqlScriptType;
	}
	public void setSqlScriptType(String sqlScriptType) {
		this.sqlScriptType = sqlScriptType;
	}
	public void setSqlScriptParameterList(List<ComSqlScriptParameter> sqlScriptParameterList) {
		this.sqlScriptParameterList = sqlScriptParameterList;
	}
	public List<SqlScriptParameterNameRecord> getParameterNameRecordList() {
		return parameterNameRecordList;
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_SQL_SCRIPT", 0);
		table.setName("sql脚本信息表");
		table.setComments("sql脚本信息表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(24);
		
		ComColumndata dbTypeColumn = new ComColumndata("db_type", BuiltinCodeDataType.STRING, 16);
		dbTypeColumn.setName("数据库类型");
		dbTypeColumn.setComments("数据库类型");
		dbTypeColumn.setOrderCode(1);
		columns.add(dbTypeColumn);
		
		ComColumndata sqlScriptCaptionColumn = new ComColumndata("sql_script_caption", BuiltinCodeDataType.STRING, 50);
		sqlScriptCaptionColumn.setName("sql脚本的标题");
		sqlScriptCaptionColumn.setComments("sql脚本的标题");
		sqlScriptCaptionColumn.setOrderCode(2);
		columns.add(sqlScriptCaptionColumn);
		
		ComColumndata sqlScriptResourceNameColumn = new ComColumndata("sql_script_resource_name", BuiltinCodeDataType.STRING, 60);
		sqlScriptResourceNameColumn.setName("sql脚本资源名称");
		sqlScriptResourceNameColumn.setComments("sql脚本资源名称(调用时用到)");
		sqlScriptResourceNameColumn.setIsNullabled(0);
		sqlScriptResourceNameColumn.setOrderCode(3);
		columns.add(sqlScriptResourceNameColumn);
		
		ComColumndata sqlScriptTypeColumn = new ComColumndata("sql_script_type", BuiltinCodeDataType.STRING, 50);
		sqlScriptTypeColumn.setName("sql脚本类型");
		sqlScriptTypeColumn.setComments("sql脚本类型：如果有多个sql脚本，以第一个sql脚本的类型为准");
		sqlScriptTypeColumn.setOrderCode(4);
		columns.add(sqlScriptTypeColumn);
		
		ComColumndata sqlScriptContentColumn = new ComColumndata("sql_script_content", BuiltinCodeDataType.CLOB, 0);
		sqlScriptContentColumn.setName("sql脚本内容");
		sqlScriptContentColumn.setComments("sql脚本内容");
		sqlScriptContentColumn.setIsNullabled(0);
		sqlScriptContentColumn.setOrderCode(5);
		columns.add(sqlScriptContentColumn);
		
		ComColumndata sqlScriptParametersColumn = new ComColumndata("sql_script_parameters", BuiltinCodeDataType.STRING, 9999);
		sqlScriptParametersColumn.setName("sql脚本的参数对象集合");
		sqlScriptParametersColumn.setComments("sql脚本的参数(json串)");
		sqlScriptParametersColumn.setOrderCode(6);
		columns.add(sqlScriptParametersColumn);
		
		ComColumndata procedureNameColumn = new ComColumndata("procedure_name", BuiltinCodeDataType.STRING, 80);
		procedureNameColumn.setName("存储过程名称");
		procedureNameColumn.setComments("存储过程名称");
		procedureNameColumn.setOrderCode(7);
		columns.add(procedureNameColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinCodeDataType.STRING, 200);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setOrderCode(8);
		columns.add(commentsColumn);
		
		ComColumndata sqlQueryResultColumnsColumn = new ComColumndata("sql_query_result_columns", BuiltinCodeDataType.STRING, 9999);
		sqlQueryResultColumnsColumn.setName("sql查询结果的列名对象集合");
		sqlQueryResultColumnsColumn.setComments("sql查询结果的列名对象集合(json串)：该属性针对查询的sql语句，程序内部使用，不开放给用户");
		sqlQueryResultColumnsColumn.setOrderCode(9);
		columns.add(sqlQueryResultColumnsColumn);
		
		ComColumndata parameterNameRecordsColumn = new ComColumndata("parameter_name_records", BuiltinCodeDataType.STRING, 9999);
		parameterNameRecordsColumn.setName("sql参数名的记录对象集合");
		parameterNameRecordsColumn.setComments("sql参数名的记录(json串)：记录第几个sql，都有哪些参数名，程序内部使用，不开放给用户");
		parameterNameRecordsColumn.setOrderCode(10);
		columns.add(parameterNameRecordsColumn);
		
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

	public String validNotNullProps() {
		if(StrUtils.isEmpty(sqlScriptResourceName)){
			return "sql脚本资源名称不能为空";
		}
		if(StrUtils.isEmpty(sqlScriptContent)){
			return "sql脚本内容不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			// 获取数据库类型
			this.dbType = HibernateUtil.getCurrentDatabaseType();
			
			// 初始化sql脚本解析器对象
			initGsqlParser();
			
			// 解析sql脚本
			String[] sqlScriptArr = SqlStatementParserUtil.parseSqlScript(this.gsqlParser, this);
			
			if(isAnalysisParameters == 1){
				// 如果是存储过程，则用另一个方法处理，解析出参数
				if(BuiltinDatabaseData.PROCEDURE.equals(this.sqlScriptType)){ 
					SqlStatementParserUtil.analysisProcedureSqlScriptParam(this);
				} 
				// 否则是一般sql脚本，解析[$xxx$]的参数
				else{ 
					SqlParameterParserUtil.analysisMultiSqlScriptParam(sqlScriptArr, this);// 读取内容去解析，获取sql语句中的参数集合 sqlScriptParameterList
				}
			}
		}
		return result;
	}
	/**
	 * 设置select sql的查询结果字段名集合
	 * @param queryResultColumns
	 */
	public void doSetSqlQueryResultColumns(List<SqlQueryResultColumn> queryResultColumns) {
		if(queryResultColumns != null && queryResultColumns.size() > 0){
			this.sqlQueryResultColumnList = queryResultColumns;
			this.sqlQueryResultColumns = JsonUtil.toJsonString(this.sqlQueryResultColumnList, false);
		}
	}
	/**
	 * 设置sql脚本参数的集合
	 * @param sqlScriptParameterList
	 */
	public void doSetSqlScriptParameterList(List<ComSqlScriptParameter> sqlScriptParameterList) {
		this.sqlScriptParameterList = sqlScriptParameterList;
		if(sqlScriptParameterList != null && sqlScriptParameterList.size() > 0){
			this.sqlScriptParameters = JsonUtil.toJsonString(sqlScriptParameterList, false);
		}
	}
	/**
	 * 设置sql参数名的记录对象集合
	 * @param parameterRecordList
	 */
	public void doSetParameterRecordList(List<SqlScriptParameterNameRecord> parameterNameRecordList) {
		this.parameterNameRecordList = parameterNameRecordList;
		if(parameterNameRecordList != null && parameterNameRecordList.size() > 0){
			this.parameterNameRecords = JsonUtil.toJsonString(parameterNameRecordList, false);
		}
	}
	/**
	 * 解析出最终要操作的sql语句
	 * @param sqlScriptResource
	 * @param sqlParameterValues
	 */
	public void analysisFinalSqlScript(ComSqlScript sqlScriptResource, List<List<Object>> sqlParameterValues) {
		this.finalSqlScript = SqlStatementParserUtil.getFinalSqlScript(sqlScriptResource, sqlParameterValues);
	}
	
	public SysResource turnToResource() {
		SysResource resource = super.turnToResource();
		resource.setResourceType(SQLSCRIPT);
		resource.setResourceName(sqlScriptResourceName);
		return resource;
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		SysResource resource = turnToResource();
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
	
	public DmPublishInfo turnToPublish() {
		DmPublishInfo publish = new DmPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(projectId);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(sqlScriptResourceName);
		publish.setResourceType(SQLSCRIPT);
		return publish;
	}
	
	/**
	 * 将调用sql资源时，实际传过来的值存到参数集合中(sql参数集合/procedure sql参数集合)
	 * @param sqlScriptActualParameters
	 */
	public void setActualParams(List<ComSqlScriptParameter> sqlScriptActualParameters) {
		if(sqlScriptActualParameters == null || sqlScriptActualParameters.size() == 0){
			return;
		}
		
		if(this.sqlScriptParameterList == null || this.sqlScriptParameterList.size() == 0){
			throw new IllegalArgumentException("在调用sql资源时，传入的实际参数集合为：["+sqlScriptActualParameters+"]，但是被调用的sql资源["+this.sqlScriptResourceName+"]却不存在任何sql脚本的参数对象集合。请检查该sql资源是否确实有参数配置，并确认合法调用sql资源，或联系管理员");
		}
		
		int count = 0;
		for (ComSqlScriptParameter ssp : sqlScriptParameterList) {
			if(ssp.getParameterFrom() == 1){// 参数值来源为系统内置
				ssp.setActualInValue(ssp.analysisActualInValue());
				count++;
			}else if(ssp.getParameterFrom() == 0){// 参数值来源为用户输入
				for (ComSqlScriptParameter ssap : sqlScriptActualParameters) {
					if(ssp.getParameterName().equalsIgnoreCase(ssap.getParameterName())){
						ssp.setActualInValue(ssap.getActualInValue());
						ssp.setActualInValue(ssp.analysisActualInValue());
						count++;
						break;
					}
				}
			}
		}
		if(count != sqlScriptParameterList.size()){
			throw new IllegalArgumentException("调用sql脚本时，传入的参数值数量和配置的参数数量不匹配，请检查");
		}
	}
	
	/**
	 * 获取脚本参数记录的map
	 * <p>
	 * 	key=sqlIndex
	 *  value=参数名集合
	 * </p>
	 * @return
	 */
	@JSONField(serialize = false)
	public Map<Integer, List<String>> getParameterNameRecordMap() {
		if(parameterNameRecordList != null && parameterNameRecordList.size() > 0){
			Map<Integer, List<String>> parameterNameRecordMap = new HashMap<Integer, List<String>>(parameterNameRecordList.size());
			for (SqlScriptParameterNameRecord pnr : parameterNameRecordList) {
				parameterNameRecordMap.put(pnr.getSqlIndex(), pnr.getParameterNames());
			}
			parameterNameRecordList.clear();
			return parameterNameRecordMap;
		}
		return null;
	}
}
