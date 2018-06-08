package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
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
	private int length;
	/**
	 * 数据精度
	 */
	private int precision;
	/**
	 * 默认值
	 */
	private String defaultValue;
	/**
	 * 是否主键
	 */
	private int isPrimaryKey;
	/**
	 * 是否唯一
	 */
	private int isUnique;
	/**
	 * 是否可为空
	 */
	private int isNullabled = 1;
	/**
	 * 是否必填
	 */
	private int isRequire;
	/**
	 * 是否数据字典
	 */
	private int isDataDictionary;
	/**
	 * 数据字典编码
	 */
	private String dataDictionaryCode;
	/**
	 * 排序
	 */
	private int orderCode;
	/**
	 * 注释
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	public ComColumndata() {
	}
	public ComColumndata(String columnName) {
		this.columnName = columnName;
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
	public int getIsDataDictionary() {
		return isDataDictionary;
	}
	public void setIsDataDictionary(int isDataDictionary) {
		this.isDataDictionary = isDataDictionary;
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
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(int isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public int getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(int isUnique) {
		this.isUnique = isUnique;
	}
	public int getIsNullabled() {
		return isNullabled;
	}
	public void setIsNullabled(int isNullabled) {
		this.isNullabled = isNullabled;
	}
	public int getIsRequire() {
		return isRequire;
	}
	public void setIsRequire(int isRequire) {
		this.isRequire = isRequire;
	}
	public int getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
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

	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_COLUMNDATA", 0);
		table.setIsResource(1);
		table.setName("字段数据信息资源对象表");
		table.setComments("字段数据信息资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET+","+DELETE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(21);
		
		ComColumndata tableIdColumn = new ComColumndata("table_id");
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		tableIdColumn.setColumnType(DataTypeConstants.STRING);
		tableIdColumn.setLength(32);
		tableIdColumn.setOrderCode(1);
		columns.add(tableIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name");
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(50);
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name");
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columnNameColumn.setColumnType(DataTypeConstants.STRING);
		columnNameColumn.setLength(50);
		columnNameColumn.setOrderCode(3);
		columns.add(columnNameColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name");
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		propNameColumn.setColumnType(DataTypeConstants.STRING);
		propNameColumn.setLength(50);
		propNameColumn.setOrderCode(4);
		columns.add(propNameColumn);
		
		ComColumndata columnTypeColumn = new ComColumndata("column_type");
		columnTypeColumn.setName("字段数据类型");
		columnTypeColumn.setComments("字段数据类型");
		columnTypeColumn.setColumnType(DataTypeConstants.STRING);
		columnTypeColumn.setLength(10);
		columnTypeColumn.setOrderCode(5);
		columns.add(columnTypeColumn);
		
		ComColumndata lengthColumn = new ComColumndata("length");
		lengthColumn.setName("字段长度");
		lengthColumn.setComments("字段长度");
		lengthColumn.setColumnType(DataTypeConstants.INTEGER);
		lengthColumn.setLength(4);
		lengthColumn.setOrderCode(6);
		columns.add(lengthColumn);
		
		ComColumndata precisionColumn = new ComColumndata("precision");
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度");
		precisionColumn.setColumnType(DataTypeConstants.INTEGER);
		precisionColumn.setLength(4);
		precisionColumn.setOrderCode(7);
		columns.add(precisionColumn);
		
		ComColumndata defaultValueColumn = new ComColumndata("default_value");
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		defaultValueColumn.setColumnType(DataTypeConstants.STRING);
		defaultValueColumn.setLength(50);
		defaultValueColumn.setOrderCode(8);
		columns.add(defaultValueColumn);
		
		ComColumndata isPrimaryKeyColumn = new ComColumndata("is_primary_key");
		isPrimaryKeyColumn.setName("是否主键");
		isPrimaryKeyColumn.setComments("是否主键");
		isPrimaryKeyColumn.setColumnType(DataTypeConstants.INTEGER);
		isPrimaryKeyColumn.setLength(1);
		isPrimaryKeyColumn.setOrderCode(9);
		columns.add(isPrimaryKeyColumn);
		
		ComColumndata isUniqueColumn = new ComColumndata("is_unique");
		isUniqueColumn.setName("是否唯一");
		isUniqueColumn.setComments("是否唯一");
		isUniqueColumn.setColumnType(DataTypeConstants.INTEGER);
		isUniqueColumn.setLength(1);
		isUniqueColumn.setOrderCode(10);
		columns.add(isUniqueColumn);
		
		ComColumndata isNullabledColumn = new ComColumndata("is_nullabled");
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空");
		isNullabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isNullabledColumn.setLength(1);
		isNullabledColumn.setOrderCode(11);
		columns.add(isNullabledColumn);
		
		ComColumndata isRequireColumn = new ComColumndata("is_require");
		isRequireColumn.setName("是否必填");
		isRequireColumn.setComments("是否必填");
		isRequireColumn.setColumnType(DataTypeConstants.INTEGER);
		isRequireColumn.setLength(1);
		isRequireColumn.setOrderCode(12);
		columns.add(isRequireColumn);
		
		ComColumndata isDataDictionaryColumn = new ComColumndata("is_data_dictionary");
		isDataDictionaryColumn.setName("是否数据字典");
		isDataDictionaryColumn.setComments("是否数据字典");
		isDataDictionaryColumn.setColumnType(DataTypeConstants.INTEGER);
		isDataDictionaryColumn.setLength(1);
		isDataDictionaryColumn.setOrderCode(13);
		columns.add(isDataDictionaryColumn);
		
		ComColumndata dataDictionaryCodeColumn = new ComColumndata("data_dictionary_code");
		dataDictionaryCodeColumn.setName("数据字典编码");
		dataDictionaryCodeColumn.setComments("数据字典编码");
		dataDictionaryCodeColumn.setColumnType(DataTypeConstants.STRING);
		dataDictionaryCodeColumn.setLength(70);
		dataDictionaryCodeColumn.setOrderCode(14);
		columns.add(dataDictionaryCodeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code");
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(15);
		columns.add(orderCodeColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments");
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setColumnType(DataTypeConstants.STRING);
		commentsColumn.setLength(360);
		commentsColumn.setOrderCode(16);
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
	
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("length", length+"");
		json.put("precision", precision+"");
		json.put("isPrimaryKey", isPrimaryKey+"");
		json.put("isUnique", isUnique+"");
		json.put("isNullabled", isNullabled+"");
		json.put("isRequire", isRequire+"");
		json.put("isDataDictionary", isDataDictionary+"");
		json.put("orderCode", orderCode+"");
		json.put("isEnabled", isEnabled+"");
		json.put("validDate", validDate);
		json.put("isBuiltin", isBuiltin+"");
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isDeployed", isDeployed+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
	
	public void validNotNullProps() {
		if(!isValidNotNullProps){
			if(StrUtils.isEmpty(columnName)){
				throw new NullPointerException("列名不能为空！");
			}
			isValidNotNullProps = true;
		}
	}
	
	public void analysisResourceProp() {
		validNotNullProps();
		this.columnName = columnName.trim();
		this.propName = NamingTurnUtil.columnNameTurnPropName(columnName);
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
}
