package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;

/**
 * sql结果集信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgSqlResultset extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
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
	public Integer getExportOrderCode() {
		return exportOrderCode;
	}
	public void setExportOrderCode(Integer exportOrderCode) {
		this.exportOrderCode = exportOrderCode;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(11+7);
		
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
		
		CfgColumn exportOrderCodeColumn = new CfgColumn("export_order_code", DataTypeConstants.INTEGER, 4);
		exportOrderCodeColumn.setName("导出排序");
		exportOrderCodeColumn.setComments("默认和order_code的值一致");
		columns.add(exportOrderCodeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("sql结果集信息表");
		table.setComments("sql结果集信息表");
		
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
