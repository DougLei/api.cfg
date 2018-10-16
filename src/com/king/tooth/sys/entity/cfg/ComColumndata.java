package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;

/**
 * 字段数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
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
	/**
	 * 操作状态:0:待创建、1:已创建、2:被删除、3:被修改
	 * <p>默认为0</p>
	 */
	private Integer operStatus;
	/**
	 * 旧的信息json串
	 * <p>如果列信息被修改，记录之前的列信息，在重新建模的时候，进行相应的删除操作</p>
	 * <p>例如：旧列名，旧默认值等</p>
	 */
	private String oldInfoJson;
	@JSONField(serialize = false)
	private JSONObject oldColumnInfo;
	
	/**
	 * 是否excel导入
	 * <p>默认为1，标识都导入</p>
	 */
	private Integer isImportExcel;
	/**
	 * excel导入排序
	 * <p>默认和order_code的值一致</p>
	 */
	private Integer importExcelOrderCode;
	
	/**
	 * 是否excel导出
	 * <p>默认为1，标识都导出</p>
	 */
	private Integer isExportExcel;
	/**
	 * excel导出排序
	 * <p>默认和order_code的值一致</p>
	 */
	private Integer exportExcelOrderCode;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 是否忽略检查内置的列
	 */
	@JSONField(serialize = false)
	private boolean isIgnoreCheckBuiltinColumnName;
	
	public ComColumndata() {
	}
	public ComColumndata(String columnName, String columnType, Integer length) {
		this(columnName, columnType, length, 1, 1);
	}
	private ComColumndata(String columnName, String columnType, Integer length, Integer isImportExcel, Integer isExportExcel) {
		this.columnName = columnName.trim().toUpperCase();
		this.propName = NamingProcessUtil.columnNameTurnPropName(columnName);
		this.columnType = columnType;
		this.length = length;
		this.isImportExcel = isImportExcel;
		this.isExportExcel = isExportExcel;
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
	public Integer getOperStatus() {
		return operStatus;
	}
	public void setOperStatus(Integer operStatus) {
		this.operStatus = operStatus;
	}
	public String getOldInfoJson() {
		return oldInfoJson;
	}
	public void setOldInfoJson(String oldInfoJson) {
		this.oldInfoJson = oldInfoJson;
		this.oldColumnInfo = JsonUtil.parseJsonObject(oldInfoJson);
	}
	public JSONObject getOldColumnInfo() {
		return oldColumnInfo;
	}
	public Integer getIsImportExcel() {
		return isImportExcel;
	}
	public void setIsImportExcel(Integer isImportExcel) {
		this.isImportExcel = isImportExcel;
	}
	public Integer getImportExcelOrderCode() {
		return importExcelOrderCode;
	}
	public void setImportExcelOrderCode(Integer importExcelOrderCode) {
		this.importExcelOrderCode = importExcelOrderCode;
	}
	public Integer getIsExportExcel() {
		return isExportExcel;
	}
	public void setIsExportExcel(Integer isExportExcel) {
		this.isExportExcel = isExportExcel;
	}
	public Integer getExportExcelOrderCode() {
		return exportExcelOrderCode;
	}
	public void setExportExcelOrderCode(Integer exportExcelOrderCode) {
		this.exportExcelOrderCode = exportExcelOrderCode;
	}
	
	/**
	 * 解析出旧的列信息
	 * <p>为重新建模做准备</p>
	 * @param oldColumn
	 * @param newColumn
	 */
	public void analysisOldColumnInfo(ComColumndata oldColumn, ComColumndata newColumn) {
		if(oldColumn.getOperStatus() == ComColumndata.CREATED){
			this.oldColumnInfo = new JSONObject(10);
			// 1.记录 (旧的)列名
			if(!oldColumn.getColumnName().equals(newColumn.getColumnName())){
				this.oldColumnInfo.put("columnName", oldColumn.getColumnName());
			}
			// 2.记录 (旧的)字段数据类型
			if(!oldColumn.getColumnType().equals(newColumn.getColumnType())){
				this.oldColumnInfo.put("columnType", oldColumn.getColumnType());
			}
			// 3.记录 (旧的)字段长度
			if(oldColumn.getLength() != newColumn.getLength()){
				this.oldColumnInfo.put("length", oldColumn.getLength());
			}
			// 4.记录 (旧的)数据精度
			if(oldColumn.getPrecision() != newColumn.getPrecision()){
				this.oldColumnInfo.put("precision", oldColumn.getPrecision());
			}
			// 5.记录 (旧的)默认值
			if((oldColumn.getDefaultValue() != null && !oldColumn.getDefaultValue().equals(newColumn.getDefaultValue())) 
					|| (newColumn.getDefaultValue() != null && !newColumn.getDefaultValue().equals(oldColumn.getDefaultValue()))){
				this.oldColumnInfo.put("havaOldDefaultValue", true);
				this.oldColumnInfo.put("defaultValue", oldColumn.getDefaultValue());
			}
			// 6.记录 (旧的)是否唯一
			if(oldColumn.getIsUnique() != newColumn.getIsUnique()){
				this.oldColumnInfo.put("isUnique", oldColumn.getIsUnique());
			}
			// 7.记录 (旧的)是否可为空
			if(oldColumn.getIsNullabled() != newColumn.getIsNullabled()){
				this.oldColumnInfo.put("isNullabled", oldColumn.getIsNullabled());
			}
			// 8.记录 (旧的)备注信息
			if((oldColumn.getComments() != null && !oldColumn.getComments().equals(newColumn.getComments())) 
					|| (newColumn.getComments() != null && !newColumn.getComments().equals(oldColumn.getComments()))){
				this.oldColumnInfo.put("havaComments", true);
				this.oldColumnInfo.put("comments", oldColumn.getComments());
			}
			this.oldInfoJson = JsonUtil.toJsonString(oldColumnInfo, false);
		}
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(22+7);
		
		ComColumndata tableIdColumn = new ComColumndata("table_id", DataTypeConstants.STRING, 32);
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		columns.add(tableIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 60);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		columns.add(nameColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name", DataTypeConstants.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columnNameColumn.setIsNullabled(0);
		columns.add(columnNameColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name", DataTypeConstants.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		propNameColumn.setIsNullabled(0);
		columns.add(propNameColumn);
		
		ComColumndata columnTypeColumn = new ComColumndata("column_type", DataTypeConstants.STRING, 10);
		columnTypeColumn.setName("字段数据类型");
		columnTypeColumn.setComments("字段数据类型：默认为string");
		columnTypeColumn.setDefaultValue(DataTypeConstants.STRING);
		columnTypeColumn.setIsNullabled(0);
		columns.add(columnTypeColumn);
		
		ComColumndata lengthColumn = new ComColumndata("length", DataTypeConstants.INTEGER, 4);
		lengthColumn.setName("字段长度");
		lengthColumn.setComments("字段长度:默认长度为32");
		lengthColumn.setDefaultValue("32");
		lengthColumn.setIsNullabled(0);
		columns.add(lengthColumn);
		
		ComColumndata precisionColumn = new ComColumndata("precision", DataTypeConstants.INTEGER, 2);
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度:默认为0");
		precisionColumn.setDefaultValue("0");
		columns.add(precisionColumn);
		
		ComColumndata defaultValueColumn = new ComColumndata("default_value", DataTypeConstants.STRING, 50);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		columns.add(defaultValueColumn);
		
		ComColumndata isPrimaryKeyColumn = new ComColumndata("is_primary_key", DataTypeConstants.INTEGER, 1);
		isPrimaryKeyColumn.setName("是否主键");
		isPrimaryKeyColumn.setComments("是否主键:默认为0");
		isPrimaryKeyColumn.setDefaultValue("0");
		columns.add(isPrimaryKeyColumn);
		
		ComColumndata isUniqueColumn = new ComColumndata("is_unique", DataTypeConstants.INTEGER, 1);
		isUniqueColumn.setName("是否唯一");
		isUniqueColumn.setComments("是否唯一:默认为0");
		isUniqueColumn.setDefaultValue("0");
		columns.add(isUniqueColumn);
		
		ComColumndata isNullabledColumn = new ComColumndata("is_nullabled", DataTypeConstants.INTEGER, 1);
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空:默认为1");
		isNullabledColumn.setDefaultValue("1");
		columns.add(isNullabledColumn);
		
		ComColumndata isDataDictionaryColumn = new ComColumndata("is_data_dictionary", DataTypeConstants.INTEGER, 1);
		isDataDictionaryColumn.setName("是否数据字典");
		isDataDictionaryColumn.setComments("是否数据字典:默认为0");
		isDataDictionaryColumn.setDefaultValue("0");
		columns.add(isDataDictionaryColumn);
		
		ComColumndata dataDictionaryCodeColumn = new ComColumndata("data_dictionary_code", DataTypeConstants.STRING, 50);
		dataDictionaryCodeColumn.setName("数据字典编码");
		dataDictionaryCodeColumn.setComments("数据字典编码");
		columns.add(dataDictionaryCodeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效:默认为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 650);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		columns.add(commentsColumn);
		
		ComColumndata operStatusColumn = new ComColumndata("oper_status", DataTypeConstants.INTEGER, 1);
		operStatusColumn.setName("操作状态");
		operStatusColumn.setComments("0:待创建、1:已创建、2:被删除、3:被修改，默认为0");
		operStatusColumn.setDefaultValue("0");
		columns.add(operStatusColumn);

		ComColumndata oldInfoJsonColumn = new ComColumndata("old_info_json", DataTypeConstants.STRING, 1000);
		oldInfoJsonColumn.setName("旧的信息json串");
		oldInfoJsonColumn.setComments("如果列信息被修改，记录之前的列信息，在重新建模的时候，进行相应的删除操作；例如：旧列名，旧默认值等");
		columns.add(oldInfoJsonColumn);
		
		ComColumndata isImportExcelColumn = new ComColumndata("is_import_excel", DataTypeConstants.INTEGER, 1);
		isImportExcelColumn.setName("是否excel导入");
		isImportExcelColumn.setComments("默认为1，标识都导入");
		isImportExcelColumn.setDefaultValue("1");
		columns.add(isImportExcelColumn);
		
		ComColumndata importExcelOrderCodeColumn = new ComColumndata("import_excel_order_code", DataTypeConstants.INTEGER, 4);
		importExcelOrderCodeColumn.setName("excel导入排序");
		importExcelOrderCodeColumn.setComments("默认和order_code的值一致");
		importExcelOrderCodeColumn.setDefaultValue("0");
		columns.add(importExcelOrderCodeColumn);
		
		ComColumndata isExportExcelColumn = new ComColumndata("is_export_excel", DataTypeConstants.INTEGER, 1);
		isExportExcelColumn.setName("是否excel导出");
		isExportExcelColumn.setComments("默认为1，标识都导出");
		isExportExcelColumn.setDefaultValue("1");
		columns.add(isExportExcelColumn);
		
		ComColumndata exportExcelOrderCodeColumn = new ComColumndata("export_excel_order_code", DataTypeConstants.INTEGER, 4);
		exportExcelOrderCodeColumn.setName("excel导出排序");
		exportExcelOrderCodeColumn.setComments("默认和order_code的值一致");
		exportExcelOrderCodeColumn.setDefaultValue("0");
		columns.add(exportExcelOrderCodeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("字段数据信息资源对象表");
		table.setComments("字段数据信息资源对象表");
		
		table.setColumns(getColumnList());
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
		if(DataTypeConstants.STRING.equals(columnType) && (length == null || length < 1)){
			return "字段长度不能为空！";
		}
		if(orderCode == null || orderCode < 1){
			return "字段排序值不能为空，且必须大于0！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(!isIgnoreCheckBuiltinColumnName){
			if(result == null){
				for(String builtinColumnName: BUILTIN_COLUMNNAMES){
					if(columnName.equalsIgnoreCase(builtinColumnName)){
						return "不能添加系统内置的列名:"+columnName;
					}
				}
			}
		}
		if(result == null){
			if((DataTypeConstants.CLOB.equals(columnType) || DataTypeConstants.BLOB.equals(columnType)) && (isUnique != null && isUnique == 1)){
				return "列["+columnName+"]，属于大字段类型，禁止添加唯一约束";
			}
			if(isPrimaryKey != null && isPrimaryKey == 1){
				return "列["+columnName+"]，无法设置为主键，系统已内置名为id的主键，如确实需要创建主键，请联系后端系统开发人员";
			}
			if((isUnique != null && isUnique == 1)){
				if(isNullabled != null && isNullabled == 1){
					return "列["+columnName+"]，在唯一约束下，不能为空";
				}
				
				if(isPrimaryKey != null && isPrimaryKey == 1){
					return "列["+columnName+"]，唯一约束和主键约束，只能指定一个";
				}
			}
			if(!DataTypeConstants.INTEGER.equals(columnType) || !DataTypeConstants.DOUBLE.equals(columnType)){
				if(precision != null && precision != 0){
					return "列["+columnName+"]，非数字类型，禁止添加精度";
				}
			}
		}
		if(result == null){
			this.columnName = columnName.trim().toUpperCase();
			this.propName = NamingProcessUtil.columnNameTurnPropName(columnName);
			if(this.importExcelOrderCode == null){
				this.importExcelOrderCode = this.orderCode;
			}
			if(this.exportExcelOrderCode == null){
				this.exportExcelOrderCode = this.orderCode;
			}
		}
		return result;
	}
	
	/**
	 * 系统内置的列名
	 */
	private static final String[] BUILTIN_COLUMNNAMES = {"id", "customer_id", "project_id", "create_date", "last_update_date", "create_user_id", "last_update_user_id"};
	
	// --------------------------------------------------------------------------------------
	/**
	 * 0:待创建
	 */
	public static final Integer UN_CREATED = 0;
	/**
	 * 1:已创建
	 */
	public static final Integer CREATED = 1;
	/**
	 * 2:被删除
	 */
	public static final Integer DELETED = 2;
	/**
	 * 3:被修改
	 */
	public static final Integer MODIFIED = 3;
}
