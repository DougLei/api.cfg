package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinQueryParameters;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本参数信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
public class ComSqlScriptParameter extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis, Cloneable{
	/**
	 * 关联的sql脚本id
	 */
	private String sqlScriptId;
	/**
	 * 参数名称
	 */
	private String parameterName;
	/**
	 * 参数的值长度
	 * <p>默认值为32</p>
	 */
	private Integer length;
	/**
	 * 参数数据类型
	 * <p>默认值为string</p>
	 */
	private String parameterDataType;
	/**
	 * 是否是表类型
	 * <p>存储过程专用的属性，因为存储过程的参数，可以使用表类型</p>
	 * <p>默认值为0</p>
	 */
	private Integer isTableType;
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
	public ComSqlScriptParameter(String parameterName, String parameterDataType, int inOut, int orderCode, boolean isNeedAnalysisResourceProp) {
		this();
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
	public void analysisActualInValue() {
		if(parameterFrom == 0){
			if(actualInValue == null){
				actualInValue = defaultValue;
			}
			if(actualInValue == null){
				throw new NullPointerException("调用sql脚本时，参数["+parameterName+"]的值不能为空");
			}
			if(isPlaceholder == 1){
				if(BuiltinDataType.INTEGER.equals(parameterDataType)){
					actualInValue = Integer.valueOf(actualInValue.toString());
				}else if(BuiltinDataType.DOUBLE.equals(parameterDataType)){
					actualInValue = Double.valueOf(actualInValue.toString());
				}else if(BuiltinDataType.DATE.equals(parameterDataType)){
					actualInValue = DateUtil.parseSqlDate(actualInValue.toString());
				}else if(BuiltinDataType.BOOLEAN.equals(parameterDataType)){
					actualInValue = ("true".equals(actualInValue.toString()))? "1":"0";
				}else if(isTableType == 1){
					actualInValue = IJsonUtil.getIJson(actualInValue.toString());
				}else{
					actualInValue = actualInValue.toString();
				}
			}else{
				actualInValue = getSimpleSqlParameterValue(actualInValue);
			}
		}else if(parameterFrom == 1){
			actualInValue = BuiltinQueryParameters.getBuiltinQueryParamValue(parameterName);
			if(actualInValue == null){
				throw new NullPointerException("调用sql脚本时，内置参数["+parameterName+"]的值为空，请联系开发人员");
			}
			if(isPlaceholder == 0){
				actualInValue = getSimpleSqlParameterValue(actualInValue);
			}
		}else{
			throw new IllegalArgumentException("parameterFrom的值，仅限于：[0(用户输入)、1(系统内置)]");
		}
	}
	
	/**
	 * 获取简单的sql参数值
	 * <p>目前就是对值加上''</p>
	 * @param sqlParameterValue
	 * @return
	 */
	private String getSimpleSqlParameterValue(Object sqlParameterValue){
		if(sqlParameterValue == null){
			Log4jUtil.warn(ComSqlScriptParameter.class, "getSimpleSqlParameterValue", "在获取简单的sql参数值时，传入的sqlParameterValue参数值为null【目前就是对值加上''】");
			return "";
		}
		return "'"+sqlParameterValue.toString()+"'";
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
		// 如果是id，则每次的id值都要不一样
		if(parameterFrom == 1 && BuiltinQueryParameters.isBuiltinIdParameter(parameterName)){
			return ResourceHandlerUtil.getIdentity();
		}
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
	public void setLengthStr(String length) {
		if(StrUtils.notEmpty(length)){
			this.length = Integer.valueOf(length);
		}
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
	public Integer getIsTableType() {
		return isTableType;
	}
	public void setIsTableType(Integer isTableType) {
		this.isTableType = isTableType;
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

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_SQL_SCRIPT_PARAMETER", 0);
		table.setName("sql脚本参数信息表");
		table.setComments("sql脚本参数信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0); 
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata sqlScriptIdColumn = new ComColumndata("sql_script_id", BuiltinDataType.STRING, 32);
		sqlScriptIdColumn.setName("关联的sql脚本id");
		sqlScriptIdColumn.setComments("关联的sql脚本id");
		sqlScriptIdColumn.setOrderCode(10);
		columns.add(sqlScriptIdColumn);
		
		ComColumndata parameterNameColumn = new ComColumndata("parameter_name", BuiltinDataType.STRING, 50);
		parameterNameColumn.setName("参数名称");
		parameterNameColumn.setComments("参数名称");
		parameterNameColumn.setOrderCode(20);
		columns.add(parameterNameColumn);
		
		ComColumndata lengthColumn = new ComColumndata("length", BuiltinDataType.INTEGER, 4);
		lengthColumn.setName("参数的值长度");
		lengthColumn.setComments("默认值为32");
		lengthColumn.setDefaultValue("32");
		lengthColumn.setOrderCode(30);
		columns.add(lengthColumn);
		
		ComColumndata parameterDataTypeColumn = new ComColumndata("parameter_data_type", BuiltinDataType.STRING, 20);
		parameterDataTypeColumn.setName("参数数据类型");
		parameterDataTypeColumn.setComments("默认值为string");
		parameterDataTypeColumn.setDefaultValue(BuiltinDataType.STRING);
		parameterDataTypeColumn.setOrderCode(40);
		columns.add(parameterDataTypeColumn);
		
		ComColumndata isTableTypeColumn = new ComColumndata("is_table_type", BuiltinDataType.INTEGER, 1);
		isTableTypeColumn.setName("是否是表类型");
		isTableTypeColumn.setComments("存储过程专用的属性，因为存储过程的参数，可以使用表类型，默认值为0");
		isTableTypeColumn.setDefaultValue("0");
		columns.add(isTableTypeColumn);
		
		ComColumndata defaultValueColumn = new ComColumndata("default_value", BuiltinDataType.STRING, 100);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		defaultValueColumn.setOrderCode(50);
		columns.add(defaultValueColumn);
		
		ComColumndata parameterFromColumn = new ComColumndata("parameter_from", BuiltinDataType.INTEGER, 1);
		parameterFromColumn.setName("参数来源");
		parameterFromColumn.setComments("参数来源:0.用户输入、1.系统内置，默认值为0");
		parameterFromColumn.setDefaultValue("0");
		parameterFromColumn.setOrderCode(60);
		columns.add(parameterFromColumn);
		
		ComColumndata isPlaceholderColumn = new ComColumndata("is_placeholder", BuiltinDataType.INTEGER, 1);
		isPlaceholderColumn.setName("是否是需要占位符的参数");
		isPlaceholderColumn.setComments("是否是需要占位符的参数:即是否是需要用?代替的，目前全部都是1，默认值是1");
		isPlaceholderColumn.setDefaultValue("1");
		isPlaceholderColumn.setOrderCode(70);
		columns.add(isPlaceholderColumn);
		
		ComColumndata inOutColumn = new ComColumndata("in_out", BuiltinDataType.INTEGER, 1);
		inOutColumn.setName("参数的in/out类型");
		inOutColumn.setComments("参数的in/out类型:in=1、out=2、inOut=3，默认值是1");
		inOutColumn.setDefaultValue("1");
		inOutColumn.setOrderCode(80);
		columns.add(inOutColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 3);
		orderCodeColumn.setName("参数的顺序值");
		orderCodeColumn.setComments("参数的顺序值");
		orderCodeColumn.setOrderCode(90);
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
		if(parameterDataType == null){
			parameterDataType = BuiltinDataType.STRING;
		}
		
		if(BuiltinQueryParameters.isBuiltinQueryParams(parameterName)){
			parameterFrom = 1;
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
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
