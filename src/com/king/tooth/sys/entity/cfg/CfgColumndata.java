package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;

/**
 * [配置系统]字段数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CfgColumndata extends BasicEntity implements ITable{
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
	private int isKey;
	/**
	 * 是否唯一
	 */
	private int isUnique;
	/**
	 * 是否可为空
	 */
	private int isNullabled;
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
	private int dataDictionaryCode;
	/**
	 * 排序
	 */
	private int orderCode;
	/**
	 * 注释
	 */
	private String comments;
	/**
	 * 是否有效
	 */
	private int isEnabled;
	
	/**
	 * 解析出属性名
	 * @param columnName
	 */
	private void analysisPropName(String columnName) {
		if(StrUtils.notEmpty(columnName)){
			this.propName = NamingTurnUtil.columnNameTurnPropName(columnName);
		}
	}
	
	public CfgColumndata() {
	}
	public CfgColumndata(String columnName) {
		doSetColumnName(columnName);
	}
	private void doSetColumnName(String columnName) {
		this.isEnabled = 1;
		this.columnName = columnName.trim();
		analysisPropName(this.columnName);
	}

	//-------------------------------------------------------------------------
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
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public int getIsDataDictionary() {
		return isDataDictionary;
	}
	public void setIsDataDictionary(int isDataDictionary) {
		this.isDataDictionary = isDataDictionary;
	}
	public int getDataDictionaryCode() {
		return dataDictionaryCode;
	}
	public void setDataDictionaryCode(int dataDictionaryCode) {
		this.dataDictionaryCode = dataDictionaryCode;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getDefaultValue() {
		return defaultValue;
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
	public int getIsKey() {
		return isKey;
	}
	public void setIsKey(int isKey) {
		this.isKey = isKey;
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
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "CFG_COLUMNDATA");
		table.setName("[配置系统]字段数据信息资源对象表");
		table.setComments("[配置系统]字段数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(22);
		
		CfgColumndata tableIdColumn = new CfgColumndata("table_id");
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		tableIdColumn.setColumnType(DataTypeConstants.STRING);
		tableIdColumn.setLength(32);
		tableIdColumn.setOrderCode(1);
		columns.add(tableIdColumn);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(50);
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		CfgColumndata columnNameColumn = new CfgColumndata("column_name");
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columnNameColumn.setColumnType(DataTypeConstants.STRING);
		columnNameColumn.setLength(50);
		columnNameColumn.setOrderCode(3);
		columns.add(columnNameColumn);
		
		CfgColumndata propNameColumn = new CfgColumndata("prop_name");
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		propNameColumn.setColumnType(DataTypeConstants.STRING);
		propNameColumn.setLength(50);
		propNameColumn.setOrderCode(4);
		columns.add(propNameColumn);
		
		CfgColumndata columnTypeColumn = new CfgColumndata("column_type");
		columnTypeColumn.setName("字段数据类型");
		columnTypeColumn.setComments("字段数据类型");
		columnTypeColumn.setColumnType(DataTypeConstants.STRING);
		columnTypeColumn.setLength(10);
		columnTypeColumn.setOrderCode(5);
		columns.add(columnTypeColumn);
		
		CfgColumndata lengthColumn = new CfgColumndata("length");
		lengthColumn.setName("字段长度");
		lengthColumn.setComments("字段长度");
		lengthColumn.setColumnType(DataTypeConstants.INTEGER);
		lengthColumn.setLength(4);
		lengthColumn.setOrderCode(6);
		columns.add(lengthColumn);
		
		CfgColumndata precisionColumn = new CfgColumndata("precision");
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度");
		precisionColumn.setColumnType(DataTypeConstants.INTEGER);
		precisionColumn.setLength(4);
		precisionColumn.setOrderCode(7);
		columns.add(precisionColumn);
		
		CfgColumndata defaultValueColumn = new CfgColumndata("default_value");
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		defaultValueColumn.setColumnType(DataTypeConstants.STRING);
		defaultValueColumn.setLength(50);
		defaultValueColumn.setOrderCode(8);
		columns.add(defaultValueColumn);
		
		CfgColumndata isKeyColumn = new CfgColumndata("is_key");
		isKeyColumn.setName("是否主键");
		isKeyColumn.setComments("是否主键");
		isKeyColumn.setColumnType(DataTypeConstants.INTEGER);
		isKeyColumn.setLength(1);
		isKeyColumn.setOrderCode(9);
		columns.add(isKeyColumn);
		
		CfgColumndata isUniqueColumn = new CfgColumndata("is_unique");
		isUniqueColumn.setName("是否唯一");
		isUniqueColumn.setComments("是否唯一");
		isUniqueColumn.setColumnType(DataTypeConstants.INTEGER);
		isUniqueColumn.setLength(1);
		isUniqueColumn.setOrderCode(10);
		columns.add(isUniqueColumn);
		
		CfgColumndata isNullabledColumn = new CfgColumndata("is_nullabled");
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空");
		isNullabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isNullabledColumn.setLength(1);
		isNullabledColumn.setOrderCode(11);
		columns.add(isNullabledColumn);
		
		CfgColumndata isRequireColumn = new CfgColumndata("is_require");
		isRequireColumn.setName("是否必填");
		isRequireColumn.setComments("是否必填");
		isRequireColumn.setColumnType(DataTypeConstants.INTEGER);
		isRequireColumn.setLength(1);
		isRequireColumn.setOrderCode(12);
		columns.add(isRequireColumn);
		
		CfgColumndata isDataDictionaryColumn = new CfgColumndata("is_data_dictionary");
		isDataDictionaryColumn.setName("是否数据字典");
		isDataDictionaryColumn.setComments("是否数据字典");
		isDataDictionaryColumn.setColumnType(DataTypeConstants.INTEGER);
		isDataDictionaryColumn.setLength(1);
		isDataDictionaryColumn.setOrderCode(13);
		columns.add(isDataDictionaryColumn);
		
		CfgColumndata dataDictionaryCodeColumn = new CfgColumndata("data_dictionary_code");
		dataDictionaryCodeColumn.setName("数据字典编码");
		dataDictionaryCodeColumn.setComments("数据字典编码");
		dataDictionaryCodeColumn.setColumnType(DataTypeConstants.STRING);
		dataDictionaryCodeColumn.setLength(70);
		dataDictionaryCodeColumn.setOrderCode(14);
		columns.add(dataDictionaryCodeColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(15);
		columns.add(orderCodeColumn);
		
		CfgColumndata commentsColumn = new CfgColumndata("comments");
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setColumnType(DataTypeConstants.STRING);
		commentsColumn.setLength(200);
		commentsColumn.setOrderCode(16);
		columns.add(commentsColumn);
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(17);
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "CFG_COLUMNDATA";
	}
}
