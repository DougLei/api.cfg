package com.api.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.constants.ResourceInfoConstants;
import com.api.constants.database.OracleDataTypeConstants;
import com.api.constants.database.SQLServerDataTypeConstants;
import com.api.sys.builtin.data.BuiltinParameters;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.IEntityPropAnalysis;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;

/**
 * sql脚本参数信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgSqlParameter extends BasicEntity implements IEntity, IEntityPropAnalysis, Cloneable{
	/**
	 * 关联的sql脚本id
	 */
	private String sqlScriptId;
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 参数的值长度
	 * <p>默认值为32</p>
	 */
	private Integer length;
	/**
	 * 数据精度
	 * <p>默认为0</p>
	 */
	private Integer precision;
	/**
	 * 参数数据类型
	 * <p>默认值为string</p>
	 */
	private String dataType;
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
	 * 参数值来源
	 * <p>0.用户输入、1.系统内置、2.自动编码</p>
	 * <p>默认值是0</p>
	 */
	private Integer valueFrom = 0;
	
	/**
	 * 是否是需要占位符的参数
	 * <p>即是否是需要用?代替的</p>
	 * <p>目前全部都是1</p>
	 * <p>默认值是1</p>
	 */
	private Integer isPlaceholder = 1;
	/**
	 * 值包裹起始字符
	 * <p>当isPlaceholder=0时，需要用指定的字符包裹值，这个存储的是值前要追加的字符</p>
	 * <p>默认值为'</p>
	 */
	private String valuePackStart;
	/**
	 * 值包裹结束字符
	 * <p>当isPlaceholder=0时，需要用指定的字符包裹值，这个存储的是值后要追加的字符</p>
	 * <p>默认值为'</p>
	 */
	private String valuePackEnd;
	/**
	 * 是否可为空
	 * <p>默认为0</p>
	 */
	private Integer isNullabled;
	/**
	 * 参数的in/out类型
	 * <p>in=1、out=2、inOut=3</p>
	 * <p>增删改查sql语句都是in类型</p>
	 * <p>默认值是1</p>
	 */
	private Integer inOut;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 备注
	 */
	private String remark;
	
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
	
	/**
	 * 构造对象时，是否忽略验证不能为空的属性
	 */
	@JSONField(serialize = false)
	private boolean isIgnoreValidNotNullProps;
	
	public CfgSqlParameter() {
	}
	public CfgSqlParameter(String name, String dataType, boolean isDbDataType, int inOut, int orderCode, boolean isNeedAnalysisResourceProp, boolean isIgnoreValidNotNullProps) {
		this.id = ResourceHandlerUtil.getIdentity();
		this.name = name;
		this.remark = name;
		if(isDbDataType){
			this.dataType = turnDbDataTypeToCodeDataType(dataType.toLowerCase());
		}else{
			this.dataType = dataType;
		}
		this.inOut = inOut;
		this.orderCode = orderCode;
		this.isIgnoreValidNotNullProps = isIgnoreValidNotNullProps;
		if(isNeedAnalysisResourceProp){ // 在调用sql脚本时，不需要解析
			analysisResourceProp();
		}
	}
	
	/**
	 * 将数据库的数据类型转换为代码的数据类型
	 * @param dbDataType
	 * @return
	 */
	private String turnDbDataTypeToCodeDataType(String dbDataType) {
		dbDataType = dbDataType.trim();
		if(OracleDataTypeConstants.VARCHAR2.equals(dbDataType)){
			return DataTypeConstants.STRING;
		}else if(OracleDataTypeConstants.CHAR.equals(dbDataType)){
			return DataTypeConstants.CHAR;
		}else if(OracleDataTypeConstants.NUMBER.equals(dbDataType)){
			return DataTypeConstants.INTEGER;
		}else if(OracleDataTypeConstants.DATE.equals(dbDataType)){
			return DataTypeConstants.DATE;
		}
		else if(SQLServerDataTypeConstants.VARCHAR.equals(dbDataType)){
			return DataTypeConstants.STRING;
		}else if(SQLServerDataTypeConstants.CHAR.equals(dbDataType)){
			return DataTypeConstants.CHAR;
		}else if(SQLServerDataTypeConstants.BIT.equals(dbDataType)){
			return DataTypeConstants.BOOLEAN;
		}else if(SQLServerDataTypeConstants.INT.equals(dbDataType)){
			return DataTypeConstants.INTEGER;
		}else if(SQLServerDataTypeConstants.DECIMAL.equals(dbDataType)){
			return DataTypeConstants.DOUBLE;
		}else if(SQLServerDataTypeConstants.DATETIME.equals(dbDataType)){
			return DataTypeConstants.DATE;
		}
		return dbDataType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Object getActualInValue() {
		// 如果是id，则每次的id值都要不一样
		if(valueFrom == 1 && BuiltinParameters.isBuiltinIdParameter(name)){
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
	public String getValuePackStart() {
		if(valuePackStart == null){
			valuePackStart = "'";
		}
		return valuePackStart;
	}
	public void setValuePackStart(String valuePackStart) {
		this.valuePackStart = valuePackStart;
	}
	public String getValuePackEnd() {
		if(valuePackEnd == null){
			valuePackEnd = "'";
		}
		return valuePackEnd;
	}
	public void setValuePackEnd(String valuePackEnd) {
		this.valuePackEnd = valuePackEnd;
	}
	public Integer getIsNullabled() {
		return isNullabled;
	}
	public void setIsNullabled(Integer isNullabled) {
		this.isNullabled = isNullabled;
	}
	public Integer getPrecision() {
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	public void setLengthStr(String length) {
		if(StrUtils.notEmpty(length)){
			this.length = Integer.valueOf(length.trim());
		}
	}
	public void setPrecisionStr(String precision) {
		if(StrUtils.notEmpty(precision)){
			this.precision = Integer.valueOf(precision.trim());
			this.dataType = DataTypeConstants.DOUBLE;
		}
	}
	public Integer getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(Integer valueFrom) {
		this.valueFrom = valueFrom;
	}
	public Integer getIsPlaceholder() {
		return isPlaceholder;
	}
	public Integer getIsTableType() {
		if(isTableType == null){
			return 0;
		}
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgSqlParameter";
	}

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(15+7);
		
		CfgColumn sqlScriptIdColumn = new CfgColumn("sql_script_id", DataTypeConstants.STRING, 32);
		sqlScriptIdColumn.setName("关联的sql脚本id");
		sqlScriptIdColumn.setComments("关联的sql脚本id");
		columns.add(sqlScriptIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 50);
		nameColumn.setName("参数名称");
		nameColumn.setComments("参数名称");
		columns.add(nameColumn);
		
		CfgColumn lengthColumn = new CfgColumn("length", DataTypeConstants.INTEGER, 4);
		lengthColumn.setName("参数的值长度");
		lengthColumn.setComments("默认值为32");
		lengthColumn.setDefaultValue("32");
		columns.add(lengthColumn);
		
		CfgColumn precisionColumn = new CfgColumn("precision", DataTypeConstants.INTEGER, 2);
		precisionColumn.setName("数据精度");
		precisionColumn.setComments("数据精度:默认为0");
		precisionColumn.setDefaultValue("0");
		columns.add(precisionColumn);
		
		CfgColumn dataTypeColumn = new CfgColumn("data_type", DataTypeConstants.STRING, 20);
		dataTypeColumn.setName("参数数据类型");
		dataTypeColumn.setComments("默认值为string");
		dataTypeColumn.setDefaultValue(DataTypeConstants.STRING);
		columns.add(dataTypeColumn);
		
		CfgColumn isTableTypeColumn = new CfgColumn("is_table_type", DataTypeConstants.INTEGER, 1);
		isTableTypeColumn.setName("是否是表类型");
		isTableTypeColumn.setComments("存储过程专用的属性，因为存储过程的参数，可以使用表类型，默认值为0");
		isTableTypeColumn.setDefaultValue("0");
		columns.add(isTableTypeColumn);
		
		CfgColumn defaultValueColumn = new CfgColumn("default_value", DataTypeConstants.STRING, 100);
		defaultValueColumn.setName("默认值");
		defaultValueColumn.setComments("默认值");
		columns.add(defaultValueColumn);
		
		CfgColumn valueFromColumn = new CfgColumn("value_from", DataTypeConstants.INTEGER, 1);
		valueFromColumn.setName("参数值来源");
		valueFromColumn.setComments("0.用户输入、1.系统内置、2.自动编码，默认值为0");
		valueFromColumn.setDefaultValue("0");
		columns.add(valueFromColumn);
		
		CfgColumn isPlaceholderColumn = new CfgColumn("is_placeholder", DataTypeConstants.INTEGER, 1);
		isPlaceholderColumn.setName("是否是需要占位符的参数");
		isPlaceholderColumn.setComments("是否是需要占位符的参数:即是否是需要用?代替的，目前全部都是1，默认值是1");
		isPlaceholderColumn.setDefaultValue("1");
		columns.add(isPlaceholderColumn);
		
		CfgColumn valuePackStartColumn = new CfgColumn("value_pack_start", DataTypeConstants.STRING, 100);
		valuePackStartColumn.setName("值包裹起始字符");
		valuePackStartColumn.setComments("当isPlaceholder=0时，需要用指定的符号包裹值，这个存储的是值前要追加的字符，默认值为英文输入法下的单引号");
		columns.add(valuePackStartColumn);
		
		CfgColumn valuePackEndColumn = new CfgColumn("value_pack_end", DataTypeConstants.STRING, 100);
		valuePackEndColumn.setName("值包裹结束字符");
		valuePackEndColumn.setComments("当isPlaceholder=0时，需要用指定的字符包裹值，这个存储的是值后要追加的字符，默认值为英文输入法下的单引号");
		columns.add(valuePackEndColumn);
		
		CfgColumn isNullabledColumn = new CfgColumn("is_nullabled", DataTypeConstants.INTEGER, 1);
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("是否可为空:默认为0");
		isNullabledColumn.setDefaultValue("0");
		columns.add(isNullabledColumn);
		
		CfgColumn inOutColumn = new CfgColumn("in_out", DataTypeConstants.INTEGER, 1);
		inOutColumn.setName("参数的in/out类型");
		inOutColumn.setComments("参数的in/out类型:in=1、out=2、inOut=3，默认值是1");
		inOutColumn.setDefaultValue("1");
		columns.add(inOutColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		CfgColumn remarkColumn = new CfgColumn("remark", DataTypeConstants.STRING, 80);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("sql脚本参数信息表");
		table.setRemark("sql脚本参数信息表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}
	
	public String toDropTable() {
		return "CFG_SQL_PARAMETER";
	}
	
	public String validNotNullProps() {
		if(!isIgnoreValidNotNullProps){
			if(StrUtils.isEmpty(sqlScriptId)){
				return "sql脚本参数对象，关联的sql脚本id不能为空";
			}
			if(StrUtils.isEmpty(name)){
				return "sql脚本参数名不能为空";
			}
			if(StrUtils.isEmpty(dataType)){
				return "sql脚本参数的数据类型不能为空";
			}
			if(!DataTypeConstants.isLegalDataType(dataType)){
				return "sql脚本参数的数据类型不合法，目前系统支持的数据类型值包括:[char]、[string]、[boolean]、[integer]、[double]、[date]、[clob]、[blob]、[sys_refcursor]";
			}
			if((DataTypeConstants.STRING.equals(dataType) 
					|| DataTypeConstants.CHAR.equals(dataType) 
					|| DataTypeConstants.DOUBLE.equals(dataType)
					|| DataTypeConstants.INTEGER.equals(dataType)) 
						&& (length == null || length < 1)){
				return "sql脚本参数的长度不能为空，且必须大于0！";
			}
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			if(dataType == null){
				dataType = DataTypeConstants.STRING;
			}
			
			if(BuiltinParameters.isBuiltinParams(name)){
				valueFrom = 1;
			}
			
			if(inOut == 0 || inOut == 5 || inOut == 1){// in (inOut == 0是sqlserver的input，inOut == 5是sqlserver的readonly，inOut == 1是oracle的input)
				inOut = 1;
			}else if(inOut == 4 || inOut == 2){// out (inOut == 4是sqlserver的output，inOut == 2是oracle的output)
				inOut = 2;
			}else if(inOut == 3){// in out (inOut==3是oracle的in out)
				inOut = 3;
			}else{
				throw new IllegalArgumentException("系统在解析存储过程参数的时候，无法处理inOut="+inOut+"的类型，请联系后台系统开发人员");
			}
		}
		return null;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	// ----------------------------------------------------------------------
	/**
	 * 参数来源
	 * <p>0.用户输入</p>
	 */
	public static final Integer USER_INPUT = 0;
	/**
	 * 参数来源
	 * <p>1.系统内置</p>
	 */
	public static final Integer SYSTEM_BUILTIN = 1;
	/**
	 * 参数来源
	 * <p>2.自动编码</p>
	 */
	public static final Integer AUTO_CODE = 2;
}
