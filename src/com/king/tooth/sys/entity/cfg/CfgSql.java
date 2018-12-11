package com.king.tooth.sys.entity.cfg;

import gudusoft.gsqlparser.TGSqlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.sql.FinalSqlScriptStatement;
import com.king.tooth.sys.entity.cfg.sql.SqlScriptParameterNameRecord;
import com.king.tooth.util.ExceptionUtil;
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
@Table
public class CfgSql extends ACfgResource implements IEntityPropAnalysis, IEntity, Cloneable{

	/**
	 * 数据库类型
	 */
	private String dbType;
	/**
	 * sql脚本类型
	 * <p>如果有多个sql脚本，以第一个sql脚本的类型为准</p>
	 */
	private String type;
	/**
	 * 配置的sql脚本类型
	 * <p>默认和sql脚本类型一样，用户可以根据sql脚本的内容修改为insert/update/delete三个值之一，解决定位查询focuseid中id+_add/_edit/_delete</p>
	 */
	private String confType;
	/**
	 * sql脚本内容
	 */
	private String contents;
	/**
	 * sql对象名称
	 * <p>[存储过程、视图等]</p>
	 */
	private String objectName;
	/**
	 * sql参数名的记录
	 * <p>记录，第几个sql，都有哪些参数名，程序内部使用，不开放给用户</p>
	 */
	private String parameterNameRecords;
	@JSONField(serialize = false)
	private List<SqlScriptParameterNameRecord> parameterNameRecordList;
	
	//--------------------------------------------------------
	
	/**
	 * 是否覆盖sql对象
	 * <p>针对存储过程、视图，如果在创建时，已经出现同名的，是覆盖，还是提示已存在</p>
	 */
	@JSONField(serialize = false)
	private boolean isCoverSqlObject;
	
	/**
	 * 是否解析参数
	 * <p>在添加或修改的时候使用的判断标识，在修改sql内容时，不想修改参数的话，则这个字段要为0，否则为1</p>
	 */
	@JSONField(serialize = false)
	private int isAnalysisParameters;
	
	/**
	 * 是否立即创建
	 * <p>针对存储过程、视图等，需要在数据库中创建的对象</p>
	 * <p>为1时，会在立即在数据库中执行脚本，创建对应的存储过程、视图等对象</p>
	 */
	@JSONField(serialize = false)
	private int isImmediateCreate;
	
	/**
	 * 解析对象
	 */
	@JSONField(serialize = false)
	private TGSqlParser gsqlParser;
	
	/**
	 * 在调用sql资源时，保存被处理过的，可以执行的最终查询的sql脚本语句对象
	 */
	@JSONField(serialize = false)
	private List<FinalSqlScriptStatement> finalSqlScriptList;
	
	/**
	 * 脚本参数记录的map
	 * <p>
	 * 	key=sqlIndex
	 *  value=参数名集合
	 * </p>
	 */
	@JSONField(serialize = false)
	private Map<Integer, List<String>> parameterNameRecordMap;
	/**
	 * 是否解析脚本参数记录map
	 */
	@JSONField(serialize = false)
	private boolean isAnalysisParameterNameRecordMap;
	
	/**
	 * sql脚本的参数集合
	 */
	@JSONField(serialize = false)
	private List<CfgSqlParameter> sqlParams;
	// 同上，是为了实现批量执行同一个sql脚本的时候加入的属性
	@JSONField(serialize = false)
	private List<List<CfgSqlParameter>> sqlParamsList;
	
	/**
	 * sql结果集信息集合
	 * <p>传入</p>
	 */
	@JSONField(serialize = false)
	private List<CfgSqlResultset> inSqlResultsets;
	/**
	 * sql结果集信息集合
	 * <p>传出</p>
	 */
	@JSONField(serialize = false)
	private List<List<CfgSqlResultset>> outSqlResultsetsList;
	
	/**
	 * 该对象中，是否包含所有信息
	 * <p>包括参数集合、传入传出结果集信息集合</p>
	 */
	@JSONField(serialize = false)
	private boolean includeAllInfo;
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getContents() {
		return contents;
	}
	@SuppressWarnings("unchecked")
	public void setParameterNameRecords(String parameterNameRecords) {
		this.parameterNameRecords = parameterNameRecords;
		if(StrUtils.isEmpty(parameterNameRecords)){
			return;
		}
		IJson ijson = IJsonUtil.getIJson(parameterNameRecords);
		int len = ijson.size();
		this.parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(len);
		
		SqlScriptParameterNameRecord spnr = null;
		JSONArray jsonArray = null;
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
	public List<CfgSqlParameter> getSqlParams() {
		return sqlParams;
	}
	public void setSqlParams(List<CfgSqlParameter> sqlParams) {
		this.sqlParams = sqlParams;
	}
	public List<FinalSqlScriptStatement> getFinalSqlScriptList() {
		return finalSqlScriptList;
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
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		if(objectName == null){
			this.objectName = null;
		}else{
			this.objectName = objectName.toUpperCase();
		}
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public void initGsqlParser() {
		if(gsqlParser == null){
			gsqlParser = SqlStatementParserUtil.getGsqlParser(this.contents.replaceAll("\n", " ").replace("\t", " ").replaceAll("\r", " "));
		}
	}
	public TGSqlParser getGsqlParser() {
		initGsqlParser();
		return gsqlParser;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean getIsCoverSqlObject() {
		return isCoverSqlObject;
	}
	public void setIsCoverSqlObject(boolean isCoverSqlObject) {
		this.isCoverSqlObject = isCoverSqlObject;
	}
	public List<SqlScriptParameterNameRecord> getParameterNameRecordList() {
		return parameterNameRecordList;
	}
	public int getIsImmediateCreate() {
		return isImmediateCreate;
	}
	public void setIsImmediateCreate(int isImmediateCreate) {
		this.isImmediateCreate = isImmediateCreate;
	}
	public List<List<CfgSqlParameter>> getSqlParamsList() {
		return sqlParamsList;
	}
	public void setSqlParamsList(List<List<CfgSqlParameter>> sqlParamsList) {
		this.sqlParamsList = sqlParamsList;
	}
	public String getConfType() {
		return confType;
	}
	public void setConfType(String confType) {
		this.confType = confType;
	}
	public List<CfgSqlResultset> getInSqlResultsets() {
		return inSqlResultsets;
	}
	public void setInSqlResultsets(List<CfgSqlResultset> inSqlResultsets) {
		this.inSqlResultsets = inSqlResultsets;
	}
	public boolean getIncludeAllInfo() {
		return includeAllInfo;
	}
	public void setIncludeAllInfo(boolean includeAllInfo) {
		this.includeAllInfo = includeAllInfo;
	}
	public List<List<CfgSqlResultset>> getOutSqlResultsetsList() {
		return outSqlResultsetsList;
	}
	public void setOutSqlResultsetsList(List<List<CfgSqlResultset>> outSqlResultsetsList) {
		this.outSqlResultsetsList = outSqlResultsetsList;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(11+7);
		
		CfgColumn dbTypeColumn = new CfgColumn("db_type", DataTypeConstants.STRING, 16);
		dbTypeColumn.setName("数据库类型");
		dbTypeColumn.setComments("数据库类型");
		columns.add(dbTypeColumn);
		
		columns.add(BuiltinObjectInstance.nameColumn);
		columns.add(BuiltinObjectInstance.resourceNameColumn);
		
		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.STRING, 80);
		typeColumn.setName("sql脚本类型");
		typeColumn.setComments("sql脚本类型：如果有多个sql脚本，以第一个sql脚本的类型为准");
		columns.add(typeColumn);
		
		CfgColumn confTypeColumn = new CfgColumn("conf_type", DataTypeConstants.STRING, 80);
		confTypeColumn.setName("配置的sql脚本类型");
		confTypeColumn.setComments("默认和sql脚本类型一样，用户可以根据sql脚本的内容修改为insert/update/delete三个值之一，解决定位查询focuseid中id+_add/_edit/_delete");
		columns.add(confTypeColumn);
		
		CfgColumn contentsColumn = new CfgColumn("contents", DataTypeConstants.CLOB, 0);
		contentsColumn.setName("sql脚本内容");
		contentsColumn.setComments("sql脚本内容");
		contentsColumn.setIsNullabled(0);
		columns.add(contentsColumn);
		
		CfgColumn objectNameColumn = new CfgColumn("object_name", DataTypeConstants.STRING, 80);
		objectNameColumn.setName("sql对象名称");
		objectNameColumn.setComments("存储过程、视图等");
		columns.add(objectNameColumn);
		
		CfgColumn parameterNameRecordsColumn = new CfgColumn("parameter_name_records", DataTypeConstants.STRING, 8000);
		parameterNameRecordsColumn.setName("sql参数名的记录");
		parameterNameRecordsColumn.setComments("sql参数名的记录(json串)：记录第几个sql，都有哪些参数名，程序内部使用，不开放给用户");
		columns.add(parameterNameRecordsColumn);
		
		columns.add(BuiltinObjectInstance.remarkColumn);
		columns.add(BuiltinObjectInstance.isCreatedColumn);
		columns.add(BuiltinObjectInstance.isEnabledColumn);
		columns.add(BuiltinObjectInstance.requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("sql脚本信息表");
		table.setRemark("sql脚本信息表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_SQL";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgSql";
	}

	public String validNotNullProps() {
		if(StrUtils.isEmpty(resourceName)){
			return "sql脚本资源名称不能为空";
		}
		if(StrUtils.isEmpty(contents)){
			return "sql脚本内容不能为空";
		}
		if(StrUtils.isEmpty(confType)){
			return "业务操作类型(confType)的值不能为空";
		}
		if(isEnabled == null){
			isEnabled = 1;
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			if(StrUtils.isEmpty(id)){
				this.id = ResourceHandlerUtil.getIdentity();
			}
			
			// 获取数据库类型
			this.dbType = HibernateUtil.getCurrentDatabaseType();
			
			// 初始化sql脚本解析器对象
			initGsqlParser();
			
			// 解析sql脚本
			String[] sqlScriptArr = SqlStatementParserUtil.parseSqlScript(this.gsqlParser, this);
			
			// 如果是存储过程，则用另一个方法处理，解析出参数
			if(SqlStatementTypeConstants.PROCEDURE.equals(this.type)){ 
				result = SqlParameterParserUtil.analysisMultiSqlScriptParam(sqlScriptArr, this, true);
				if(result != null){
					return "["+result+"]错误！存储过程中不能使用[$xxx$]的变量声明方式，请将需要传入的变量写到存储过程的参数列表中";
				}
				SqlStatementParserUtil.analysisProcedureSqlScriptParam(this);
			}
			// 如果是视图，则不用解析参数，只要解析出视图名即可
			else if(SqlStatementTypeConstants.VIEW.equals(this.type)){ 
				SqlStatementParserUtil.analysisViewName(this);
			}
			// 否则是一般sql脚本，解析[$xxx$]的参数
			else{ 
				SqlParameterParserUtil.analysisMultiSqlScriptParam(sqlScriptArr, this, false);// 读取内容去解析，获取sql语句中的参数集合 sqlScriptParameterList
			}
			
			// 尝试删除该sql关联的所有结果集信息
			if(SqlStatementTypeConstants.SELECT.equals(type) || SqlStatementTypeConstants.PROCEDURE.equals(type) || SqlStatementTypeConstants.VIEW.equals(type)){
				HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgSqlResultset where sqlScriptId = ?", id);// 删除之前的所有结果集信息(select/存储过程/视图)
			}
			
			// 如果是select语句，还要解析出查询结果字段集合，如果select语句查询的是*，则抛出异常，不能这样写，这样写不规范
			SqlStatementParserUtil.analysisSelectSqlResultSetList(this);
			
			// 如果是存储过程，可能还要保存输入结果集信息
			SqlStatementParserUtil.saveProcedureTableTypeResultset(this);
			
			// 处理sql参数
			processSqlParameters();
		}
		return result;
	}
	
	/**
	 * 处理sql参数
	 */
	private void processSqlParameters() {
		if(isAnalysisParameters == 0){
			if(this.sqlParams != null && this.sqlParams.size() > 0){
				this.sqlParams.clear();
			}
		}else{
			List<CfgSqlParameter> oldSqlParams = HibernateUtil.extendExecuteListQueryByHqlArr(CfgSqlParameter.class, null, null, "from CfgSqlParameter where sqlScriptId = ?", id);
			if(oldSqlParams == null || oldSqlParams.size() == 0){
				if(sqlParams != null && sqlParams.size() > 0){
					for (CfgSqlParameter sqlParam : sqlParams) {
						sqlParam.setSqlScriptId(id);
						HibernateUtil.saveObject(sqlParam, null);
					}
					sqlParams.clear();
				}
			}else{
				if(sqlParams == null || sqlParams.size() == 0){
					HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgSqlParameter where sqlScriptId = ?", id);// 删除之前的参数
				}else{
					CfgSqlParameter sp = null;
					for (int i = 0; i < sqlParams.size(); i++) {
						sp = sqlParams.get(i);
						for (int j = 0; j < oldSqlParams.size(); j++) {
							if(sp.getName().equals(oldSqlParams.get(j).getName())){
								sp = oldSqlParams.remove(j--);
								sp.setOrderCode(sqlParams.remove(i--).getOrderCode());
								HibernateUtil.updateEntityObject(sp, null);
								break;
							}
						}
					}
					
					if(sqlParams.size()>0){
						for (CfgSqlParameter sqlParam : sqlParams) {
							sqlParam.setSqlScriptId(id);
							HibernateUtil.saveObject(sqlParam, null);
						}
						sqlParams.clear();
					}
					if(oldSqlParams.size()>0){
						for (CfgSqlParameter oldSqlParam : oldSqlParams) {
							HibernateUtil.deleteObject(oldSqlParam, null);
						}
						oldSqlParams.clear();
					}
				}
			}
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
	public void analysisFinalSqlScript(CfgSql sqlScriptResource, List<List<Object>> sqlParameterValues) {
		this.finalSqlScriptList = SqlStatementParserUtil.getFinalSqlScriptList(sqlScriptResource, sqlParameterValues);
	}
	
	public Map<Integer, List<String>> getParameterNameRecordMap() {
		analysisParameterNameRecordMap();
		return parameterNameRecordMap;
	}
	
	/**
	 * 解析脚本参数记录的map
	 */
	private void analysisParameterNameRecordMap(){
		if(!isAnalysisParameterNameRecordMap){
			isAnalysisParameterNameRecordMap = true;
			
			if(parameterNameRecordList != null && parameterNameRecordList.size() > 0){
				parameterNameRecordMap = new HashMap<Integer, List<String>>(parameterNameRecordList.size());
				for (SqlScriptParameterNameRecord pnr : parameterNameRecordList) {
					parameterNameRecordMap.put(pnr.getSqlIndex(), pnr.getParameterNames());
				}
				parameterNameRecordList.clear();
			}
		}
	}
	
	public void clear(){
		if(inSqlResultsets != null && inSqlResultsets.size() > 0){
			inSqlResultsets.clear();
		}
		if(outSqlResultsetsList != null && outSqlResultsetsList.size() > 0){
			for (List<CfgSqlResultset> sqlResultSets : outSqlResultsetsList) {
				if(sqlResultSets != null && sqlResultSets.size() > 0){
					sqlResultSets.clear();
				}
			}
			outSqlResultsetsList.clear();
		}
		
		if(finalSqlScriptList != null && finalSqlScriptList.size() > 0){
			finalSqlScriptList.clear();
		}
		if(parameterNameRecordMap != null && parameterNameRecordMap.size() > 0){
			parameterNameRecordMap.clear();
		}
		if(sqlParams != null && sqlParams.size() > 0){
			sqlParams.clear();
		}
		if(sqlParamsList != null && sqlParamsList.size() > 0){
			for (List<CfgSqlParameter> sqlParams : sqlParamsList) {
				if(sqlParams != null && sqlParams.size() > 0){
					sqlParams.clear();
				}
			}
			sqlParamsList.clear();
		}
	}
	
	/**
	 * 解析sql时，添加一个输入的结果集信息
	 * @param sqlResultSet
	 */
	public void addInSqlResultsets(CfgSqlResultset sqlResultSet) {
		if(inSqlResultsets == null){
			inSqlResultsets = new ArrayList<CfgSqlResultset>();
		}
		inSqlResultsets.add(sqlResultSet);
	}
	
	public CfgResource turnToResource() {
		CfgResource resource = super.turnToResource();
		resource.setResourceType(ResourceInfoConstants.SQL);
		return resource;
	}
	
	/**
	 * 克隆sql参数集合
	 * @return
	 */
	public List<CfgSqlParameter> cloneSqlParams() {
		List<CfgSqlParameter> targetSqlParams = new ArrayList<CfgSqlParameter>(sqlParams.size());
		try {
			for (CfgSqlParameter sqlParam : sqlParams) {
				targetSqlParams.add((CfgSqlParameter)sqlParam.clone());
			}
		} catch (CloneNotSupportedException e) {
			throw new IllegalArgumentException("克隆CfgSqlParameter对象时出现异常："+ExceptionUtil.getErrMsg(e));
		}
		return targetSqlParams;
	}

	public Object clone() throws CloneNotSupportedException {
		CfgSql sql = (CfgSql) super.clone();
		sql.setIncludeAllInfo(false);
		sql.setSqlParams(cloneSqlParams());
		
		// 克隆parameterNameRecordList
		if(parameterNameRecordList != null && parameterNameRecordList.size() > 0){
			sql.parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(parameterNameRecordList.size());
			sql.parameterNameRecordList.addAll(parameterNameRecordList);
		}
		return sql;
	}
	
	/**
	 * 获取sql参数集合列表的json对象
	 * @return
	 */
	public Object getSqlParamsListJson() {
		if(sqlParamsList != null && sqlParamsList.size() > 0){
			if(sqlParamsList.size() == 1){
				return analyzeToJson(sqlParamsList.get(0));
			}else{
				JSONArray jsonArray = new JSONArray(sqlParamsList.size());
				for (List<CfgSqlParameter> sqlParams : sqlParamsList) {
					jsonArray.add(analyzeToJson(sqlParams));
				}
				return jsonArray;
			}
		}
		return null;
	}
	private JSONObject analyzeToJson(List<CfgSqlParameter> sqlParams){
		if(sqlParams == null || sqlParams.size() == 0){
			return null;
		}
		JSONObject json = new JSONObject(sqlParams.size());
		for (CfgSqlParameter sqlParam : sqlParams) {
			json.put(sqlParam.getName(), sqlParam.getActualInValue());
		}
		return json;
	}
	
	/**
	 * 是否是select语句
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isSelectSql(){
		return SqlStatementTypeConstants.SELECT.equals(confType);
	}
	/**
	 * 是否是insert语句
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isInsertSql(){
		return SqlStatementTypeConstants.INSERT.equals(confType);
	}
	/**
	 * 是否是update语句
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isUpdateSql(){
		return SqlStatementTypeConstants.UPDATE.equals(confType);
	}
	/**
	 * 是否是delete语句
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isDeleteSql(){
		return SqlStatementTypeConstants.DELETE.equals(confType);
	}
}
