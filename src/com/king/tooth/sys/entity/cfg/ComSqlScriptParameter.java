package com.king.tooth.sys.entity.cfg;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.plugins.builtin.params.BuiltinQueryParameters;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * sql脚本的参数资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSqlScriptParameter extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
	/**
	 * 关联的sql脚本id
	 */
	private String sqlScriptId;
	/**
	 * 记录是第几个sql语句的参数
	 * <p>默认值是1</p>
	 */
	private Integer sqlIndex;
	/**
	 * 参数名称
	 */
	private String parameterName;
	/**
	 * 参数长度
	 * <p>默认值为32</p>
	 */
	private Integer length;
	/**
	 * 参数数据类型
	 * <p>默认值为string</p>
	 */
	private String parameterDataType;
	/**
	 * 默认值
	 */
	private String defaultValue;
	/**
	 * 参数来源
	 * <p>0.用户输入、1.系统内置</p>
	 * <p>默认值是0</p>
	 */
	private Integer parameterFrom = 0;
	/**
	 * 是否是需要占位符的参数
	 * <p>即是否是需要用?代替的</p>
	 * <p>目前全部都是1</p>
	 * <p>默认值是1</p>
	 */
	private Integer isPlaceholder = 1;
	/**
	 * 参数的in/out类型
	 * <p>in=1、out=2、inOut=3</p>
	 * <p>增删改查sql语句都是in类型</p>
	 * <p>默认值是1</p>
	 */
	private Integer inOut;
	/**
	 * 参数的顺序值
	 * <p>递增，必须按顺序，且不能为空，或重复</p>
	 */
	private Integer orderCode;
	
	//------------------------------------------------------------------------------
	
	/**
	 * 实际在使用时，传递in的值
	 */
	@JSONField(serialize = false)
	private Object actualInValue;
	
	/**
	 * 实际在使用中，返回out的值
	 */
	@JSONField(serialize = false)
	private Object acutalOutValue;
	
	public ComSqlScriptParameter() {
		this.id = ResourceHandlerUtil.getIdentity();
	}
	public ComSqlScriptParameter(int sqlIndex, String parameterName, String parameterDataType, int inOut, int orderCode, boolean isNeedAnalysisResourceProp) {
		this();
		this.sqlIndex = sqlIndex;
		this.parameterName = parameterName;
		this.parameterDataType = parameterDataType;
		this.inOut = inOut;
		this.orderCode = orderCode;
		if(isNeedAnalysisResourceProp){ // 在调用sql脚本时，不需要解析
			analysisResourceProp();
		}
	}
	
	/**
	 * 解析实际传入的参数值
	 * @return
	 */
	public Object analysisActualInValue() {
		if(parameterFrom == 0){
			if(actualInValue == null){
				actualInValue = defaultValue;
			}
			if(actualInValue == null){
				throw new NullPointerException("调用sql脚本时，参数["+parameterName+"]的值不能为空");
			}
			if(isPlaceholder == 1){
				if(DataTypeConstants.INTEGER.equals(parameterDataType)){
					return Integer.valueOf(actualInValue+"");
				}else if(DataTypeConstants.DOUBLE.equals(parameterDataType)){
					return Double.valueOf(actualInValue+"");
				}else if(DataTypeConstants.DATE.equals(parameterDataType)){
					return DateUtil.parseDate(actualInValue+"");
				}else{
					return actualInValue+"";
				}
			}else{
				return "'"+actualInValue+"'";
			}
		}else if(parameterFrom == 1){
			actualInValue = BuiltinQueryParameters.getBuiltinQueryParamValue(parameterName);
			if(actualInValue == null){
				throw new NullPointerException("调用sql脚本时，内置参数["+parameterName+"]的值为空，请检查配置，或程序代码");
			}
			return actualInValue;
		}else{
			throw new IllegalArgumentException("parameterFrom的值，仅限于：[0(用户输入)、1(系统内置)]");
		}
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterDataType() {
		return parameterDataType;
	}
	public void setParameterDataType(String parameterDataType) {
		this.parameterDataType = parameterDataType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Object getActualInValue() {
		return actualInValue;
	}
	public void setActualInValue(Object actualInValue) {
		this.actualInValue = actualInValue;
	}
	public Object getAcutalOutValue() {
		return acutalOutValue;
	}
	public void setAcutalOutValue(Object acutalOutValue) {
		this.acutalOutValue = acutalOutValue;
	}
	public String getSqlScriptId() {
		return sqlScriptId;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getParameterFrom() {
		return parameterFrom;
	}
	public void setParameterFrom(Integer parameterFrom) {
		this.parameterFrom = parameterFrom;
	}
	public Integer getIsPlaceholder() {
		return isPlaceholder;
	}
	public void setIsPlaceholder(Integer isPlaceholder) {
		this.isPlaceholder = isPlaceholder;
	}
	public Integer getInOut() {
		return inOut;
	}
	public void setInOut(Integer inOut) {
		this.inOut = inOut;
	}
	public Integer getSqlIndex() {
		return sqlIndex;
	}
	public void setSqlIndex(Integer sqlIndex) {
		this.sqlIndex = sqlIndex;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public void setSqlScriptId(String sqlScriptId) {
		this.sqlScriptId = sqlScriptId;
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComSqlScriptParameter";
	}

	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("sqlIndex", sqlIndex);
		entityJson.put("length", length);
		entityJson.put("parameterFrom", parameterFrom);
		entityJson.put("isPlaceholder", isPlaceholder);
		entityJson.put("inOut", inOut);
		entityJson.put("orderCode", orderCode);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_SQL_SCRIPT_PARAMETER", 0);
		table.setName("sql脚本的参数资源对象表");
		table.setComments("sql脚本的参数资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0); 
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.CONFIG_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata sqlScriptIdColumn = new ComColumndata("sql_script_id", DataTypeConstants.STRING, 32);
		sqlScriptIdColumn.setName("关联的sql脚本id");
		sqlScriptIdColumn.setComments("关联的sql脚本id");
		sqlScriptIdColumn.setOrderCode(10);
		columns.add(sqlScriptIdColumn);
		
		ComColumndata sqlIndexColumn = new ComColumndata("sql_index", DataTypeConstants.INTEGER, 2);
		sqlIndexColumn.setName("记录是第几个sql语句的参数");
		sqlIndexColumn.setComments("记录是第几个sql语句的参数:默认值是1");
		sqlIndexColumn.setDefaultValue("1");
		sqlIndexColumn.setOrderCode(20);
		columns.add(sqlIndexColumn);
		
		ComColumndata parameterNameColumn = new ComColumndata("parameter_name", DataTypeConstants.STRING, 50);
		parameterNameColumn.setName("参数名称");
		parameterNameColumn.setComments("参数名称");
		parameterNameColumn.setOrderCode(30);
		columns.add(parameterNameColumn);
		
		ComColumndata lengthColumn = new ComColumndata("length", DataTypeConstants.INTEGER, 4);
		lengthColumn.setName("参数长度");
		lengthColumn.setComments("参数长度：默认值为32");
		lengthColumn.setDefaultValue("32");
		lengthColumn.setOrderCode(40);
		columns.add(lengthColumn);
		
		ComColumndata parameterDataTypeColumn = new ComColumndata("parameter_data_type", DataTypeConstants.STRING, 10);
		parameterDataTypeColumn.setName("参数数据类型");
		parameterDataTypeColumn.setComments("参数数据类型:默认值为string");
		parameterDataTypeColumn.setDefaultValue(DataTypeConstants.STRING);
		parameterDataTypeColumn.setOrderCode(50);
		columns.add(parameterDataTypeColumn);
		
		ComColumndata defaultValueColumn = new ComColumndata("default_value", DataTypeConstants.STRING, 100);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		defaultValueColumn.setOrderCode(60);
		columns.add(defaultValueColumn);
		
		ComColumndata parameterFromColumn = new ComColumndata("parameter_from", DataTypeConstants.INTEGER, 1);
		parameterFromColumn.setName("参数来源");
		parameterFromColumn.setComments("参数来源:0.用户输入、1.系统内置，默认值为0");
		parameterFromColumn.setDefaultValue("0");
		parameterFromColumn.setOrderCode(70);
		columns.add(parameterFromColumn);
		
		ComColumndata isPlaceholderColumn = new ComColumndata("is_placeholder", DataTypeConstants.INTEGER, 1);
		isPlaceholderColumn.setName("是否是需要占位符的参数");
		isPlaceholderColumn.setComments("是否是需要占位符的参数:即是否是需要用?代替的，目前全部都是1，默认值是1");
		isPlaceholderColumn.setDefaultValue("1");
		isPlaceholderColumn.setOrderCode(80);
		columns.add(isPlaceholderColumn);
		
		ComColumndata inOutColumn = new ComColumndata("in_out", DataTypeConstants.STRING, 32);
		inOutColumn.setName("参数的in/out类型");
		inOutColumn.setComments("参数的in/out类型:in=1、out=2、inOut=3，默认值是1");
		inOutColumn.setDefaultValue("1");
		inOutColumn.setOrderCode(90);
		columns.add(inOutColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("参数的顺序值");
		orderCodeColumn.setComments("参数的顺序值：递增，必须按顺序，且不能为空，或重复");
		orderCodeColumn.setOrderCode(100);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_SQL_SCRIPT_PARAMETER";
	}
	
	public String validNotNullProps() {
		return null;
	}
	
	public String analysisResourceProp() {
		if(BuiltinQueryParameters.isBuiltinQueryParams(parameterName)){
			parameterFrom = 1;
		}
		
		if(parameterDataType == null){
			parameterDataType = DataTypeConstants.STRING;
		}
		
		if(inOut == null){
			inOut = 0;
		}
		if(inOut == 0 || inOut == 1){// in
			inOut = 1;
		}else if(inOut == 2 || inOut == 4){// out
			inOut = 2;
		}else{// in out
			inOut = 3;
		}
		return null;
	}
	
	/**
	 * 获取存储过程参数对应的数据类型
	 * <p>在调用存储过程时使用到该方法：将数据库类型，转换为对应的代码类型</p>
	 * @param dbType
	 * @return
	 */
	@JSONField(serialize = false)
	public int getProcedureParamsMappingDataTypes(String dbType){
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)){
			if("varchar2".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.VARCHAR;
			}else if("char".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.CHAR;
			}else if("number".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.NUMBER;
			}else if("date".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.DATE;
			}
			throw new IllegalArgumentException("系统目前不支持[oracle]数据库的["+parameterDataType+"]数据类型转换，请联系管理员，目前支持的数据类型为：[varchar2、char、number、date]");
		}else if(DynamicDataConstants.DB_TYPE_SQLSERVER.equals(dbType)){
			if("varchar".equalsIgnoreCase(parameterDataType)){
				return Types.VARCHAR;
			}else if("char".equalsIgnoreCase(parameterDataType)){
				return Types.CHAR;
			}else if("int".equalsIgnoreCase(parameterDataType)){
				return Types.INTEGER;
			}else if("decimal".equalsIgnoreCase(parameterDataType)){
				return Types.DECIMAL;
			}else if("datetime".equalsIgnoreCase(parameterDataType)){
				return Types.DATE;
			}
			throw new IllegalArgumentException("系统目前不支持[sqlserver]数据库的["+parameterDataType+"]数据类型转换，请联系管理员");
		}
		throw new IllegalArgumentException("系统目前不支持["+dbType+"]数据库的数据类型转换，请联系管理员");
	}
}
