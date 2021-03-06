package com.api.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.IEntityPropAnalysis;
import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.api.util.NamingProcessUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;

/**
 * sql结果集信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgSqlResultset extends BasicEntity implements IEntity, IEntityPropAnalysis{
	/**
	 * 关联的sql脚本id
	 */
	private String sqlScriptId;
	/**
	 * 关联的sql脚本参数id
	 * <p>oracle通过输出参数返回结果集,[oracle使用字段]</p>
	 */
	private String sqlParameterId;
	/**
	 * 关联的表id
	 * <p>存储过程参数有表/游标参数类型时，这个记录对应的表类型id，数据验证的时候，用这个id去查询对应的表列信息</p>
	 */
	private String tableId;
	/**
	 * 结果集批次顺序
	 * <p>sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]</p>
	 */
	private Integer batchOrder;
	/**
	 * 结果集名
	 * <p>sqlserver直接返回结果集，这个用来配置每个结果集的名称，前端通过该key来取，如果没有配置，则使用dataSet1、dataSet2...自增[sqlserver使用字段]</p>
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
	 * 字段描述名
	 * <p>目前是导出时用到该字段，例如姓名、年龄，如果没有值，默认为propName</p>
	 */
	private String descName;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 传入还是传出
	 * <p>标识是传入的结果集信息，还是传出的结果集信息</p>
	 * <p>in=1、out=2</p>
	 */
	private Integer inOut;
	
	/**
	 * 是否导出
	 * <p>如果是传出的结果集，即in_out的值为2，且是select语句时，该字段值为1，标识都导出</p>
	 */
	private Integer isExport;
	/**
	 * 导出排序
	 * <p>该字段值和order_code的值一致</p>
	 */
	private Integer exportOrderCode;
	/**
	 * 字段数据类型
	 * <p>默认值为string，查询列的数据类型</p>
	 */
	private String dataType;
	
	//------------------------------------------------------------------------------
	
	/**
	 * 记录存储过程，输入表类型参数，对应列的元数据信息集合
	 * <p>通过tableId查询可以得到</p>
	 */
	@JSONField(serialize = false)
	private List<ResourceMetadataInfo> inSqlResultSetMetadataInfos;
	
	public CfgSqlResultset(String sqlScriptType, String columnName, int orderCode, int inOut) {
		this.columnName = columnName;
		this.orderCode = orderCode;
		this.inOut = inOut;
		
		this.exportOrderCode = orderCode;
		if(inOut == OUT && SqlStatementTypeConstants.SELECT.equals(sqlScriptType)){
			this.isExport = 1;
		}
		if("id".equalsIgnoreCase(columnName)){
			this.propName = ResourcePropNameConstants.ID;
		}else{
			this.propName = NamingProcessUtil.columnNameTurnPropName(columnName);
		}
		// 如果是内置的属性名，默认不导出
		if(this.isExport != null && this.isExport == 1 && ResourceHandlerUtil.isBuildInProps(propName)){
			this.isExport=0;
		}
		this.descName = this.propName;
	}
	public CfgSqlResultset() {
	}

	public String getSqlScriptId() {
		return sqlScriptId;
	}
	public void setSqlScriptId(String sqlScriptId) {
		this.sqlScriptId = sqlScriptId;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getSqlParameterId() {
		return sqlParameterId;
	}
	public void setSqlParameterId(String sqlParameterId) {
		this.sqlParameterId = sqlParameterId;
	}
	public Integer getBatchOrder() {
		return batchOrder;
	}
	public void setBatchOrder(Integer batchOrder) {
		this.batchOrder = batchOrder;
	}
	public String getName() {
		return name;
	}
	public String getName(Integer index){
		if(StrUtils.isEmpty(name)){
			return "dataSet"+(index+1);
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public String getDescName() {
		return descName;
	}
	public void setDescName(String descName) {
		this.descName = descName;
	}
	public Integer getInOut() {
		return inOut;
	}
	public void setInOut(Integer inOut) {
		this.inOut = inOut;
	}
	public List<ResourceMetadataInfo> getInSqlResultSetMetadataInfos() {
		return inSqlResultSetMetadataInfos;
	}
	public void setInSqlResultSetMetadataInfos(List<ResourceMetadataInfo> inSqlResultSetMetadataInfos) {
		this.inSqlResultSetMetadataInfos = inSqlResultSetMetadataInfos;
	}
	public Integer getIsExport() {
		return isExport;
	}
	public void setIsExport(Integer isExport) {
		this.isExport = isExport;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getExportOrderCode() {
		return exportOrderCode;
	}
	public void setExportOrderCode(Integer exportOrderCode) {
		this.exportOrderCode = exportOrderCode;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(13+7);
		
		CfgColumn sqlScriptIdColumn = new CfgColumn("sql_script_id", DataTypeConstants.STRING, 32);
		sqlScriptIdColumn.setName("关联的sql脚本id");
		sqlScriptIdColumn.setComments("关联的sql脚本id");
		columns.add(sqlScriptIdColumn);
		
		CfgColumn sqlParameterIdColumn = new CfgColumn("sql_parameter_id", DataTypeConstants.STRING, 32);
		sqlParameterIdColumn.setName("关联的sql脚本参数id");
		sqlParameterIdColumn.setComments("oracle通过输出参数返回结果集,[oracle使用字段]");
		columns.add(sqlParameterIdColumn);
		
		CfgColumn tableIdColumn = new CfgColumn("table_id", DataTypeConstants.STRING, 32);
		tableIdColumn.setName("关联的表id");
		tableIdColumn.setComments("存储过程参数有表/游标参数类型时，这个记录对应的表类型id，数据验证的时候，用这个id去查询对应的表列信息");
		columns.add(tableIdColumn);
		
		CfgColumn batchOrderColumn = new CfgColumn("batch_order", DataTypeConstants.INTEGER, 1);
		batchOrderColumn.setName("结果集批次顺序");
		batchOrderColumn.setComments("sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]");
		columns.add(batchOrderColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 40);
		nameColumn.setName("结果集名");
		nameColumn.setComments("sqlserver直接返回结果集，这个用来配置每个结果集的名称，前端通过该key来取，如果没有配置，则使用dataSet1、dataSet2...自增[sqlserver使用字段]");
		columns.add(nameColumn);
		
		CfgColumn columnNameColumn = new CfgColumn("column_name", DataTypeConstants.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columns.add(columnNameColumn);
		
		CfgColumn propNameColumn = new CfgColumn("prop_name", DataTypeConstants.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		columns.add(propNameColumn);
		
		CfgColumn descNameColumn = new CfgColumn("desc_name", DataTypeConstants.STRING, 60);
		descNameColumn.setName("字段描述名");
		descNameColumn.setComments("目前是导出时用到该字段，例如姓名、年龄，如果没有值，默认为propName");
		columns.add(descNameColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		CfgColumn inOutColumn = new CfgColumn("in_out", DataTypeConstants.INTEGER, 1);
		inOutColumn.setName("传入还是传出");
		inOutColumn.setComments("标识是传入的结果集信息，还是传出的结果集信息，in=1、out=2");
		columns.add(inOutColumn);
		
		CfgColumn isExportColumn = new CfgColumn("is_export", DataTypeConstants.INTEGER, 1);
		isExportColumn.setName("是否导出");
		isExportColumn.setComments("如果是传出的结果集，即in_out的值为2，且是select语句时，该字段值为1，标识都导出");
		columns.add(isExportColumn);
		
		CfgColumn exportOrderCodeColumn = new CfgColumn("export_order_code", DataTypeConstants.INTEGER, 3);
		exportOrderCodeColumn.setName("导出排序");
		exportOrderCodeColumn.setComments("默认和order_code的值一致");
		columns.add(exportOrderCodeColumn);
		
		CfgColumn dataTypeColumn = new CfgColumn("data_type", DataTypeConstants.STRING, 20);
		dataTypeColumn.setName("字段数据类型");
		dataTypeColumn.setComments("默认值为string，查询列的数据类型");
		dataTypeColumn.setDefaultValue(DataTypeConstants.STRING);
		columns.add(dataTypeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("sql结果集信息表");
		table.setRemark("sql结果集信息表");
		
		table.setColumns(getColumnList());
		return table;
	}
	
	public String toDropTable() {
		return "CFG_SQL_RESULTSET";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgSqlResultset";
	}
	
	public String validNotNullProps() {
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	// -------------------------------------------------------------------
	/**
	 * 传入
	 * <p>标识是传入的结果集信息</p>
	 * <p>in=1</p>
	 */
	public static final Integer IN = 1;
	/**
	 * 传出
	 * <p>标识是传出的结果集信息</p>
	 * <p>out=2</p>
	 */
	public static final Integer OUT = 2;
}
