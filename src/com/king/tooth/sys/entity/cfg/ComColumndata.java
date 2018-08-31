package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;

/**
 * 字段数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
public class ComColumndata extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
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
	 * <p>默认类型为string</p>
	 */
	private String columnType;
	/**
	 * 字段长度
	 * <p>默认长度为32</p>
	 */
	private Integer length;
	/**
	 * 数据精度
	 * <p>默认为0</p>
	 */
	private Integer precision;
	/**
	 * 默认值
	 */
	private String defaultValue;
	/**
	 * 是否主键
	 * <p>默认为0</p>
	 */
	private Integer isPrimaryKey;
	/**
	 * 是否唯一
	 * <p>默认为0</p>
	 */
	private Integer isUnique;
	/**
	 * 是否可为空
	 * <p>默认为1</p>
	 */
	private Integer isNullabled;
	/**
	 * 是否数据字典
	 * <p>默认为0</p>
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
	 * 是否有效
	 * <p>默认为1</p>
	 */
	private String isEnabled;
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
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_COLUMNDATA", 0);
		table.setName("字段数据信息资源对象表");
		table.setComments("字段数据信息资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0); 
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(23);
		
		ComColumndata tableIdColumn = new ComColumndata("table_id", BuiltinDataType.STRING, 32);
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		tableIdColumn.setOrderCode(1);
		columns.add(tableIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinDataType.STRING, 60);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name", BuiltinDataType.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columnNameColumn.setIsNullabled(0);
		columnNameColumn.setOrderCode(3);
		columns.add(columnNameColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name", BuiltinDataType.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		propNameColumn.setOrderCode(4);
		columns.add(propNameColumn);
		
		ComColumndata columnTypeColumn = new ComColumndata("column_type", BuiltinDataType.STRING, 10);
		columnTypeColumn.setName("字段数据类型");
		columnTypeColumn.setComments("字段数据类型：默认为string");
		columnTypeColumn.setDefaultValue(BuiltinDataType.STRING);
		columnTypeColumn.setIsNullabled(0);
		columnTypeColumn.setOrderCode(5);
		columns.add(columnTypeColumn);
		
		ComColumndata lengthColumn = new ComColumndata("length", BuiltinDataType.INTEGER, 4);
		lengthColumn.setName("字段长度");
		lengthColumn.setComments("字段长度:默认长度为32");
		lengthColumn.setDefaultValue("32");
		lengthColumn.setIsNullabled(0);
		lengthColumn.setOrderCode(6);
		columns.add(lengthColumn);
		
		ComColumndata precisionColumn = new ComColumndata("precision", BuiltinDataType.INTEGER, 4);
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度:默认为0");
		precisionColumn.setDefaultValue("0");
		precisionColumn.setOrderCode(7);
		columns.add(precisionColumn);
		
		ComColumndata defaultValueColumn = new ComColumndata("default_value", BuiltinDataType.STRING, 50);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		defaultValueColumn.setOrderCode(8);
		columns.add(defaultValueColumn);
		
		ComColumndata isPrimaryKeyColumn = new ComColumndata("is_primary_key", BuiltinDataType.INTEGER, 1);
		isPrimaryKeyColumn.setName("是否主键");
		isPrimaryKeyColumn.setComments("是否主键:默认为0");
		isPrimaryKeyColumn.setDefaultValue("0");
		isPrimaryKeyColumn.setOrderCode(9);
		columns.add(isPrimaryKeyColumn);
		
		ComColumndata isUniqueColumn = new ComColumndata("is_unique", BuiltinDataType.INTEGER, 1);
		isUniqueColumn.setName("是否唯一");
		isUniqueColumn.setComments("是否唯一:默认为0");
		isUniqueColumn.setDefaultValue("0");
		isUniqueColumn.setOrderCode(10);
		columns.add(isUniqueColumn);
		
		ComColumndata isNullabledColumn = new ComColumndata("is_nullabled", BuiltinDataType.INTEGER, 1);
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空:默认为1");
		isNullabledColumn.setDefaultValue("1");
		isNullabledColumn.setOrderCode(11);
		columns.add(isNullabledColumn);
		
		ComColumndata isDataDictionaryColumn = new ComColumndata("is_data_dictionary", BuiltinDataType.INTEGER, 1);
		isDataDictionaryColumn.setName("是否数据字典");
		isDataDictionaryColumn.setComments("是否数据字典:默认为0");
		isDataDictionaryColumn.setDefaultValue("0");
		isDataDictionaryColumn.setOrderCode(12);
		columns.add(isDataDictionaryColumn);
		
		ComColumndata dataDictionaryCodeColumn = new ComColumndata("data_dictionary_code", BuiltinDataType.STRING, 50);
		dataDictionaryCodeColumn.setName("数据字典编码");
		dataDictionaryCodeColumn.setComments("数据字典编码");
		dataDictionaryCodeColumn.setOrderCode(13);
		columns.add(dataDictionaryCodeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 4);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(14);
		columns.add(orderCodeColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", BuiltinDataType.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效:默认为1");
		isEnabledColumn.setDefaultValue("1");
		isEnabledColumn.setOrderCode(15);
		columns.add(isEnabledColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinDataType.STRING, 650);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setOrderCode(16);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_COLUMNDATA";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComColumndata";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(columnName)){
			return "字段名不能为空！";
		}
		if(StrUtils.isEmpty(columnType)){
			return "字段类型不能为空！";
		}
		if(BuiltinDataType.STRING.equals(columnType) && (length == null || length < 1)){
			return "字段长度不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			this.columnName = columnName.trim().toUpperCase();
			this.propName = NamingTurnUtil.columnNameTurnPropName(columnName);
		}
		return result;
	}
	
	public SysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
}
