package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.TableResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ie.IETableResourceMetadataInfo;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

/**
 * 字段信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgColumn extends BasicEntity implements IEntity, IEntityPropAnalysis{
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
	 * <p>默认为0，1表示表级别唯一，2表示项目级别唯一，3标识客户级别唯一</p>
	 */
	private Integer isUnique;
	/**
	 * 是否可为空
	 * <p>默认为1</p>
	 */
	private Integer isNullabled;
	/**
	 * 排序
	 */
	private Integer orderCode;
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
	 * 是否导入
	 * <p>默认为1，标识都导入</p>
	 */
	private Integer isImport;
	/**
	 * 导入排序
	 * <p>默认和order_code的值一致</p>
	 */
	private Integer importOrderCode;
	
	/**
	 * 是否导出
	 * <p>默认为1，标识都导出</p>
	 */
	private Integer isExport;
	/**
	 * 导出排序
	 * <p>默认和order_code的值一致</p>
	 */
	private Integer exportOrderCode;
	/**
	 * 是否忽略验证
	 * <p>1是0否，默认值为0</p>
	 */
	private Integer isIgnoreValid=0;
	
	//-------------------------------------------------------------------------
	/**
	 * 旧的信息json串
	 * <p>如果列信息被修改，记录之前的列信息，在重新建模的时候，进行相应的删除操作</p>
	 * <p>例如：旧列名，旧默认值等</p>
	 */
	@JSONField(serialize = false)
	private JSONObject oldColumnInfo;
	/**
	 * 导入导出的扩展配置对象
	 */
	@JSONField(serialize = false)
	private CfgPropExtendConf ieConfExtend;
	
	public CfgColumn() {
	}
	public CfgColumn(String columnName) {
		this.columnName = columnName;
	}
	public CfgColumn(String columnName, String columnType, Integer length) {
		this(columnName, columnType, length, 1, 1);
	}
	private CfgColumn(String columnName, String columnType, Integer length, Integer isImport, Integer isExport) {
		this.tableId = ResourceInfoConstants.BUILTIN_RESOURCE;
		this.columnName = columnName.trim().toUpperCase();
		this.propName = NamingProcessUtil.columnNameTurnPropName(columnName);
		this.columnType = columnType;
		this.length = length;
		this.isImport = isImport;
		this.isExport = isExport;
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
		if(precision == null){
			precision = 0;
		}
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	public CfgPropExtendConf getIeConfExtend() {
		return ieConfExtend;
	}
	public void setIeConfExtend(CfgPropExtendConf ieConfExtend) {
		this.ieConfExtend = ieConfExtend;
	}
	public Integer getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Integer isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public Integer getIsUnique() {
		if(isUnique == null){
			isUnique = 0;
		}
		return isUnique;
	}
	public void setIsUnique(Integer isUnique) {
		this.isUnique = isUnique;
	}
	public Integer getIsNullabled() {
		if(isNullabled == null){
			isNullabled = 1;
		}
		return isNullabled;
	}
	public void setIsNullabled(Integer isNullabled) {
		this.isNullabled = isNullabled;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getIsIgnoreValid() {
		return isIgnoreValid;
	}
	public void setIsIgnoreValid(Integer isIgnoreValid) {
		this.isIgnoreValid = isIgnoreValid;
	}
	public Integer getOperStatus() {
		return operStatus;
	}
	public void setOperStatus(Integer operStatus) {
		this.operStatus = operStatus;
	}
	public JSONObject getOldColumnInfo() {
		return oldColumnInfo;
	}
	public Integer getIsImport() {
		return isImport;
	}
	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}
	public Integer getImportOrderCode() {
		return importOrderCode;
	}
	public void setImportOrderCode(Integer importOrderCode) {
		this.importOrderCode = importOrderCode;
	}
	public Integer getIsExport() {
		return isExport;
	}
	public void setIsExport(Integer isExport) {
		this.isExport = isExport;
	}
	public Integer getExportOrderCode() {
		return exportOrderCode;
	}
	public void setExportOrderCode(Integer exportOrderCode) {
		this.exportOrderCode = exportOrderCode;
	}
	
	/**
	 * 解析出旧的列信息
	 * <p>为重新建模做准备</p>
	 * @param oldColumn
	 * @param newColumn
	 * @return 是否需要重新建模
	 */
	public boolean analysisOldColumnInfo(CfgColumn oldColumn) {
		if(oldColumn.getOperStatus() == CfgColumn.CREATED){
			this.oldColumnInfo = new JSONObject(10);
			// 1.记录 (旧的)列名
			if(!oldColumn.getColumnName().equals(this.getColumnName())){
				this.oldColumnInfo.put("columnName", oldColumn.getColumnName());
			}
			// 2.记录 (旧的)字段数据类型
			if(!oldColumn.getColumnType().equals(this.getColumnType())){
				this.oldColumnInfo.put("columnType", oldColumn.getColumnType());
			}
			// 3.记录 (旧的)字段长度
			if(oldColumn.getLength() != this.getLength()){
				this.oldColumnInfo.put("length", oldColumn.getLength());
			}
			// 4.记录 (旧的)数据精度
			if(oldColumn.getPrecision() != this.getPrecision()){
				this.oldColumnInfo.put("precision", oldColumn.getPrecision());
			}
			// 5.记录 (旧的)默认值
			if((oldColumn.getDefaultValue() != null && !oldColumn.getDefaultValue().equals(this.getDefaultValue())) 
					|| (this.getDefaultValue() != null && !this.getDefaultValue().equals(oldColumn.getDefaultValue()))){
				this.oldColumnInfo.put("updateDefaultValue", true);
				this.oldColumnInfo.put("oldDefaultValue", oldColumn.getDefaultValue());
			}
			// 6.记录 (旧的)是否唯一
			if(oldColumn.getIsUnique() != this.getIsUnique()){
				this.oldColumnInfo.put("isUnique", oldColumn.getIsUnique());
			}
			// 7.记录 (旧的)是否可为空
			if(oldColumn.getIsNullabled() != this.getIsNullabled()){
				this.oldColumnInfo.put("isNullabled", oldColumn.getIsNullabled());
			}
			// 8.记录 (旧的)备注信息
			if((StrUtils.notEmpty(oldColumn.getComments()) && !oldColumn.getComments().equals(this.getComments())) 
					|| (StrUtils.notEmpty(this.getComments()) && !this.getComments().equals(oldColumn.getComments()))){
				this.oldColumnInfo.put("updateComments", true);
				this.oldColumnInfo.put("oldComments", oldColumn.getComments());
			}
			if(oldColumnInfo.size() > 0){// 如果有修改记录了，才会标识列的状态被修改，需要用户重新建模；修改了列的备注等信息，是不用重新建模的
				this.setOperStatus(CfgColumn.MODIFIED);
				return true;
			}
		}
		return false;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(19+7);
		
		CfgColumn tableIdColumn = new CfgColumn("table_id", DataTypeConstants.STRING, 32);
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		columns.add(tableIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 60);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		columns.add(nameColumn);
		
		CfgColumn columnNameColumn = new CfgColumn("column_name", DataTypeConstants.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columnNameColumn.setIsNullabled(0);
		columns.add(columnNameColumn);
		
		CfgColumn propNameColumn = new CfgColumn("prop_name", DataTypeConstants.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		propNameColumn.setIsNullabled(0);
		columns.add(propNameColumn);
		
		CfgColumn columnTypeColumn = new CfgColumn("column_type", DataTypeConstants.STRING, 10);
		columnTypeColumn.setName("字段数据类型");
		columnTypeColumn.setComments("字段数据类型：默认为string");
		columnTypeColumn.setDefaultValue(DataTypeConstants.STRING);
		columnTypeColumn.setIsNullabled(0);
		columns.add(columnTypeColumn);
		
		CfgColumn lengthColumn = new CfgColumn("length", DataTypeConstants.INTEGER, 4);
		lengthColumn.setName("字段长度");
		lengthColumn.setComments("字段长度:默认长度为32");
		lengthColumn.setDefaultValue("32");
		lengthColumn.setIsNullabled(0);
		columns.add(lengthColumn);
		
		CfgColumn precisionColumn = new CfgColumn("precision", DataTypeConstants.INTEGER, 2);
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度:默认为0");
		precisionColumn.setDefaultValue("0");
		columns.add(precisionColumn);
		
		CfgColumn defaultValueColumn = new CfgColumn("default_value", DataTypeConstants.STRING, 50);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		columns.add(defaultValueColumn);
		
		CfgColumn isPrimaryKeyColumn = new CfgColumn("is_primary_key", DataTypeConstants.INTEGER, 1);
		isPrimaryKeyColumn.setName("是否主键");
		isPrimaryKeyColumn.setComments("是否主键:默认为0");
		isPrimaryKeyColumn.setDefaultValue("0");
		columns.add(isPrimaryKeyColumn);
		
		CfgColumn isUniqueColumn = new CfgColumn("is_unique", DataTypeConstants.INTEGER, 1);
		isUniqueColumn.setName("是否唯一");
		isUniqueColumn.setComments("默认为0，1表示表级别唯一，2表示项目级别唯一，3标识客户级别唯一");
		isUniqueColumn.setDefaultValue("0");
		columns.add(isUniqueColumn);
		
		CfgColumn isNullabledColumn = new CfgColumn("is_nullabled", DataTypeConstants.INTEGER, 1);
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空:默认为1");
		isNullabledColumn.setDefaultValue("1");
		columns.add(isNullabledColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		CfgColumn commentsColumn = new CfgColumn("comments", DataTypeConstants.STRING, 650);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		columns.add(commentsColumn);
		
		CfgColumn operStatusColumn = new CfgColumn("oper_status", DataTypeConstants.INTEGER, 1);
		operStatusColumn.setName("操作状态");
		operStatusColumn.setComments("0:待创建、1:已创建、2:被删除、3:被修改，默认为0");
		operStatusColumn.setDefaultValue("0");
		columns.add(operStatusColumn);

		CfgColumn isImportColumn = new CfgColumn("is_import", DataTypeConstants.INTEGER, 1);
		isImportColumn.setName("是否导入");
		isImportColumn.setComments("默认为1，标识都导入");
		isImportColumn.setDefaultValue("1");
		columns.add(isImportColumn);
		
		CfgColumn importOrderCodeColumn = new CfgColumn("import_order_code", DataTypeConstants.INTEGER, 3);
		importOrderCodeColumn.setName("导入排序");
		importOrderCodeColumn.setComments("默认和order_code的值一致");
		importOrderCodeColumn.setDefaultValue("0");
		columns.add(importOrderCodeColumn);
		
		CfgColumn isExportColumn = new CfgColumn("is_export", DataTypeConstants.INTEGER, 1);
		isExportColumn.setName("是否导出");
		isExportColumn.setComments("默认为1，标识都导出");
		isExportColumn.setDefaultValue("1");
		columns.add(isExportColumn);
		
		CfgColumn exportOrderCodeColumn = new CfgColumn("export_order_code", DataTypeConstants.INTEGER, 3);
		exportOrderCodeColumn.setName("导出排序");
		exportOrderCodeColumn.setComments("默认和order_code的值一致");
		exportOrderCodeColumn.setDefaultValue("0");
		columns.add(exportOrderCodeColumn);

		CfgColumn isIgnoreValidColumn = new CfgColumn("is_ignore_valid", DataTypeConstants.INTEGER, 1);
		isIgnoreValidColumn.setName("是否忽略验证");
		isIgnoreValidColumn.setComments("1是0否，默认值为0");
		isIgnoreValidColumn.setDefaultValue("0");
		columns.add(isIgnoreValidColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("字段数据信息资源对象表");
		table.setRemark("字段数据信息资源对象表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_COLUMN";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgColumn";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(tableId)){
			return "字段关联的表id不能为空！";
		}
		if(StrUtils.isEmpty(columnName)){
			return "字段名不能为空！";
		}
		if(StrUtils.isEmpty(columnType)){
			return "字段类型不能为空！";
		}
		if(!DataTypeConstants.isLegalDataType(columnType)){
			return "数据类型不合法，目前系统支持的数据类型值包括:[char]、[string]、[boolean]、[integer]、[double]、[date]、[clob]、[blob]";
		}
		if((DataTypeConstants.STRING.equals(columnType) 
				|| DataTypeConstants.CHAR.equals(columnType) 
				|| DataTypeConstants.DOUBLE.equals(columnType)
				|| DataTypeConstants.INTEGER.equals(columnType)) 
					&& (length == null || length < 1)){
			return "字段长度不能为空，且必须大于0！";
		}
		if(orderCode == null || orderCode < 1){
			return "字段排序值不能为空，且必须大于0！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			if(ResourceHandlerUtil.isBuildInColumns(columnName)){
				return "不能添加系统内置的列名:"+columnName;
			}
			
			if(StrUtils.isEmpty(defaultValue)){
				defaultValue = null;
			}else{
				if(isSqlKeyWords(defaultValue)){
					return "列["+columnName+"]，不能添加sql关键字[ "+defaultValue+" ]，作为默认值";
				}
				if(DataTypeConstants.DATE.equals(columnType)){
					return "列["+columnName+"]，属于日期类型，禁止添加默认值";
				}
			}
			if(DataTypeConstants.CLOB.equals(columnType) || DataTypeConstants.BLOB.equals(columnType)){
				if(isUnique != null && isUnique == 1){
					return "列["+columnName+"]，属于大字段类型，禁止添加唯一约束";
				}
				this.isImport = 0;
				this.isExport = 0;
			}
			if(isPrimaryKey != null && isPrimaryKey == 1){
				return "列["+columnName+"]，无法设置为主键，系统已内置名为id的主键，如确实需要创建主键，请联系后端系统开发人员";
			}
			if((isUnique != null && isUnique == 1)){
				if(isNullabled == null || isNullabled == 1){
					return "列["+columnName+"]，在唯一约束下，不能为空";
				}
				if(isPrimaryKey != null && isPrimaryKey == 1){
					return "列["+columnName+"]，唯一约束和主键约束，只能指定一个";
				}
			}
			if(!DataTypeConstants.INTEGER.equals(columnType) && !DataTypeConstants.DOUBLE.equals(columnType)){
				if(precision != null && precision != 0){
					return "列["+columnName+"]，非数字类型，禁止添加精度";
				}
			}
		}
		if(result == null){
			this.columnName = columnName.trim().toUpperCase();
			this.propName = NamingProcessUtil.columnNameTurnPropName(columnName);
			if(this.importOrderCode == null){
				this.importOrderCode = this.orderCode;
			}
			if(this.exportOrderCode == null){
				this.exportOrderCode = this.orderCode;
			}
		}
		return result;
	}
	
	/**
	 * 是否是sql关键字
	 * @param defaultValue
	 * @return
	 */
	private boolean isSqlKeyWords(String defaultValue) {
		for (String sqlKeyWord : SQL_KEY_WORDS) {
			if(sqlKeyWord.equals(defaultValue)){
				return true;
			}
		}
		return false;
	}
	private static final String[] SQL_KEY_WORDS = {"'", "\""};
	
	public TableResourceMetadataInfo toTableResourceMetadataInfo(){
		return new TableResourceMetadataInfo(columnName, columnType, length, precision, isUnique, isNullabled, isIgnoreValid, propName, comments);
	}
	public IETableResourceMetadataInfo toIETableResourceMetadataInfo(){
		return new IETableResourceMetadataInfo(columnName, columnType, length, precision, isUnique, isNullabled, isIgnoreValid, ResourceInfoConstants.BUILTIN_RESOURCE, ieConfExtend, propName, comments);
	}
	
	/**
	 * 是否没有唯一约束
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isUnUnique(){
		return isUnique == UN_UNIQUE; 
	}
	/**
	 * 是否是表级别唯一约束
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isTableUnique(){
		return isUnique == TABLE_UNIQUE; 
	}
	/**
	 * 是否是项目级别唯一约束
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isProjectUnique(){
		return isUnique == PROJECT_UNIQUE; 
	}
	/**
	 * 是否是客户级别唯一约束
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isCustomerUnique(){
		return isUnique == CUSTOMER_UNIQUE; 
	}
	
	/** 没有唯一约束 */
	public static final Integer UN_UNIQUE = 0;
	/** 表级别的唯一约束 */
	public static final Integer TABLE_UNIQUE = 1;
	/** 项目级别的唯一约束 */
	public static final Integer PROJECT_UNIQUE = 2;
	/** 客户级别的唯一约束 */
	public static final Integer CUSTOMER_UNIQUE = 3;
	
	// --------------------------------------------------------------------------------------
	/**
	 * 列的操作状态：
	 * 0:待创建
	 */
	public static final Integer UN_CREATED = 0;
	/**
	 * 列的操作状态：
	 * 1:已创建
	 */
	public static final Integer CREATED = 1;
	/**
	 * 列的操作状态：
	 * 2:被删除
	 */
	public static final Integer DELETED = 2;
	/**
	 * 列的操作状态：
	 * 3:被修改
	 */
	public static final Integer MODIFIED = 3;
}
