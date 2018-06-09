package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;

/**
 * 字段数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComColumndata extends AbstractSysResource implements ITable, IEntity, IEntityPropAnalysis{
	/**
	 * 关联的表主键
	 */
	private String tableId;
	/**
	 * 显示的汉字名称
	 */
	private String name;
	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 属性名
	 */
	private String propName;
	/**
	 * 字段数据类型
	 */
	private String columnType;
	/**
	 * 字段长度
	 */
	private Integer length;
	/**
	 * 数据精度
	 */
	private Integer precision;
	/**
	 * 默认值
	 */
	private String defaultValue;
	/**
	 * 是否主键
	 */
	private Integer isPrimaryKey;
	/**
	 * 是否唯一
	 */
	private Integer isUnique;
	/**
	 * 是否可为空
	 */
	private Integer isNullabled;
	/**
	 * 是否数据字典
	 */
	private Integer isDataDictionary;
	/**
	 * 数据字典编码
	 */
	private String dataDictionaryCode;
	/**
	 * 排序
	 */
	private Integer orderCode;
	/**
	 * 注释
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	public ComColumndata() {
	}
	public ComColumndata(String columnName, String columnType, Integer length) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.length = length;
		analysisResourceProp();
	}
	
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = propName;
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getColumnType() {
		return columnType;
	}
	public String getDataDictionaryCode() {
		return dataDictionaryCode;
	}
	public void setDataDictionaryCode(String dataDictionaryCode) {
		this.dataDictionaryCode = dataDictionaryCode;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPropName() {
		return propName;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getPrecision() {
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	public Integer getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Integer isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public Integer getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(Integer isUnique) {
		this.isUnique = isUnique;
	}
	public Integer getIsNullabled() {
		return isNullabled;
	}
	public void setIsNullabled(Integer isNullabled) {
		this.isNullabled = isNullabled;
	}
	public Integer getIsDataDictionary() {
		return isDataDictionary;
	}
	public void setIsDataDictionary(Integer isDataDictionary) {
		this.isDataDictionary = isDataDictionary;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_COLUMNDATA", 0);
		table.setIsResource(1);
		table.setName("字段数据信息资源对象表");
		table.setComments("字段数据信息资源对象表");
		table.setVersion(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET+","+DELETE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(19);
		
		ComColumndata tableIdColumn = new ComColumndata("table_id", DataTypeConstants.STRING, 32);
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		tableIdColumn.setOrderCode(1);
		columns.add(tableIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 40);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name", DataTypeConstants.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columnNameColumn.setOrderCode(3);
		columns.add(columnNameColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name", DataTypeConstants.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		propNameColumn.setOrderCode(4);
		columns.add(propNameColumn);
		
		ComColumndata columnTypeColumn = new ComColumndata("column_type", DataTypeConstants.STRING, 10);
		columnTypeColumn.setName("字段数据类型");
		columnTypeColumn.setComments("字段数据类型");
		columnTypeColumn.setOrderCode(5);
		columns.add(columnTypeColumn);
		
		ComColumndata lengthColumn = new ComColumndata("length", DataTypeConstants.INTEGER, 4);
		lengthColumn.setName("字段长度");
		lengthColumn.setComments("字段长度");
		lengthColumn.setDefaultValue("32");
		lengthColumn.setOrderCode(6);
		columns.add(lengthColumn);
		
		ComColumndata precisionColumn = new ComColumndata("precision", DataTypeConstants.INTEGER, 4);
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度");
		precisionColumn.setDefaultValue("0");
		precisionColumn.setOrderCode(7);
		columns.add(precisionColumn);
		
		ComColumndata defaultValueColumn = new ComColumndata("default_value", DataTypeConstants.STRING, 50);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		defaultValueColumn.setOrderCode(8);
		columns.add(defaultValueColumn);
		
		ComColumndata isPrimaryKeyColumn = new ComColumndata("is_primary_key", DataTypeConstants.INTEGER, 1);
		isPrimaryKeyColumn.setName("是否主键");
		isPrimaryKeyColumn.setComments("是否主键");
		isPrimaryKeyColumn.setDefaultValue("0");
		isPrimaryKeyColumn.setOrderCode(9);
		columns.add(isPrimaryKeyColumn);
		
		ComColumndata isUniqueColumn = new ComColumndata("is_unique", DataTypeConstants.INTEGER, 1);
		isUniqueColumn.setName("是否唯一");
		isUniqueColumn.setComments("是否唯一");
		isUniqueColumn.setDefaultValue("0");
		isUniqueColumn.setOrderCode(10);
		columns.add(isUniqueColumn);
		
		ComColumndata isNullabledColumn = new ComColumndata("is_nullabled", DataTypeConstants.INTEGER, 1);
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空");
		isNullabledColumn.setDefaultValue("1");
		isNullabledColumn.setOrderCode(11);
		columns.add(isNullabledColumn);
		
		ComColumndata isDataDictionaryColumn = new ComColumndata("is_data_dictionary", DataTypeConstants.INTEGER, 1);
		isDataDictionaryColumn.setName("是否数据字典");
		isDataDictionaryColumn.setComments("是否数据字典");
		isDataDictionaryColumn.setDefaultValue("0");
		isDataDictionaryColumn.setOrderCode(12);
		columns.add(isDataDictionaryColumn);
		
		ComColumndata dataDictionaryCodeColumn = new ComColumndata("data_dictionary_code", DataTypeConstants.STRING, 50);
		dataDictionaryCodeColumn.setName("数据字典编码");
		dataDictionaryCodeColumn.setComments("数据字典编码");
		dataDictionaryCodeColumn.setOrderCode(13);
		columns.add(dataDictionaryCodeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(14);
		columns.add(orderCodeColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 400);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setOrderCode(15);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_COLUMNDATA";
	}

	public String getEntityName() {
		return "ComColumndata";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("length", length);
		entityJson.put("precision", precision);
		entityJson.put("isPrimaryKey", isPrimaryKey);
		entityJson.put("isUnique", isUnique);
		entityJson.put("isNullabled", isNullabled);
		entityJson.put("isDataDictionary", isDataDictionary);
		entityJson.put("orderCode", orderCode);
		entityJson.put("isEnabled", isEnabled);
		entityJson.put("isBuiltin", isBuiltin);
		entityJson.put("isNeedDeploy", isNeedDeploy);
		entityJson.put("isDeployed", isDeployed);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
	
	public String validNotNullProps() {
		if(!isValidNotNullProps){
			if(StrUtils.isEmpty(columnName)){
				validNotNullPropsResult = "字段名不能为空！";
			}
			if(StrUtils.isEmpty(columnType)){
				validNotNullPropsResult = "字段类型不能为空！";
			}
			if(DataTypeConstants.STRING.equals(columnType) && length < 1){
				validNotNullPropsResult = "字段长度不能为空！";
			}
			isValidNotNullProps = true;
		}
		return validNotNullPropsResult;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			this.columnName = columnName.trim();
			this.propName = NamingTurnUtil.columnNameTurnPropName(columnName);
		}
		return result;
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
}