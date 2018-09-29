package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.other.ResourceMetadataInfo;
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
	private String sqlId;
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
	
	//------------------------------------------------------------------------------
	
	/**
	 * 记录存储过程，输入表类型参数，对应列的元数据信息集合
	 * <p>通过tableId查询可以得到</p>
	 */
	@JSONField(serialize = false)
	private List<ResourceMetadataInfo> inSqlResultSetMetadataInfos;
	
	public CfgSqlResultset(String columnName, int orderCode, int inOut) {
		this.orderCode = orderCode;
		this.inOut = inOut;
		this.columnName = columnName;
		if("id".equalsIgnoreCase(columnName)){
			this.propName = ResourcePropNameConstants.ID;
		}else{
			this.propName = NamingProcessUtil.columnNameTurnPropName(columnName);
		}
	}
	public CfgSqlResultset() {
	}

	public String getSqlId() {
		return sqlId;
	}
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
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
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9+7);
		
		ComColumndata sqlIdColumn = new ComColumndata("sql_id", DataTypeConstants.STRING, 32);
		sqlIdColumn.setName("关联的sql脚本id");
		sqlIdColumn.setComments("关联的sql脚本id");
		columns.add(sqlIdColumn);
		
		ComColumndata sqlParameterIdColumn = new ComColumndata("sql_parameter_id", DataTypeConstants.STRING, 32);
		sqlParameterIdColumn.setName("关联的sql脚本参数id");
		sqlParameterIdColumn.setComments("oracle通过输出参数返回结果集,[oracle使用字段]");
		columns.add(sqlParameterIdColumn);
		
		ComColumndata tableIdColumn = new ComColumndata("table_id", DataTypeConstants.STRING, 32);
		tableIdColumn.setName("关联的表id");
		tableIdColumn.setComments("存储过程参数有表/游标参数类型时，这个记录对应的表类型id，数据验证的时候，用这个id去查询对应的表列信息");
		columns.add(tableIdColumn);
		
		ComColumndata batchOrderColumn = new ComColumndata("batch_order", DataTypeConstants.INTEGER, 1);
		batchOrderColumn.setName("结果集批次顺序");
		batchOrderColumn.setComments("sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]");
		columns.add(batchOrderColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 40);
		nameColumn.setName("结果集名");
		nameColumn.setComments("sqlserver直接返回结果集，这个用来配置每个结果集的名称，前端通过该key来取，如果没有配置，则使用dataSet1、dataSet2...自增[sqlserver使用字段]");
		columns.add(nameColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name", DataTypeConstants.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columns.add(columnNameColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name", DataTypeConstants.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		columns.add(propNameColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		ComColumndata inOutColumn = new ComColumndata("in_out", DataTypeConstants.INTEGER, 1);
		inOutColumn.setName("传入还是传出");
		inOutColumn.setComments("标识是传入的结果集信息，还是传出的结果集信息，in=1、out=2");
		columns.add(inOutColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
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
