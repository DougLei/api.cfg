package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
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
	 * 列的数据类型
	 * <p>存储过程传入表参数时，需要知道每个列的数据类型[sqlserver使用字段]</p>
	 */
	private String dataType;
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

	public String getSqlScriptId() {
		return sqlScriptId;
	}
	public void setSqlScriptId(String sqlScriptId) {
		this.sqlScriptId = sqlScriptId;
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(16);
		
		ComColumndata sqlScriptIdColumn = new ComColumndata("sql_script_id", BuiltinDataType.STRING, 32);
		sqlScriptIdColumn.setName("关联的sql脚本id");
		sqlScriptIdColumn.setComments("关联的sql脚本id");
		columns.add(sqlScriptIdColumn);
		
		ComColumndata sqlParameterIdColumn = new ComColumndata("sql_parameter_id", BuiltinDataType.STRING, 32);
		sqlParameterIdColumn.setName("关联的sql脚本参数id");
		sqlParameterIdColumn.setComments("oracle通过输出参数返回结果集,[oracle使用字段]");
		columns.add(sqlParameterIdColumn);
		
		ComColumndata batchOrderColumn = new ComColumndata("batch_order", BuiltinDataType.INTEGER, 1);
		batchOrderColumn.setName("结果集批次顺序");
		batchOrderColumn.setComments("sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]");
		columns.add(batchOrderColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinDataType.STRING, 40);
		nameColumn.setName("结果集名");
		nameColumn.setComments("sqlserver直接返回结果集，这个用来配置每个结果集的名称，前端通过该key来取，如果没有配置，则使用dataSet1、dataSet2...自增[sqlserver使用字段]");
		columns.add(nameColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name", BuiltinDataType.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columns.add(columnNameColumn);
		
		ComColumndata dataTypeColumn = new ComColumndata("data_type", BuiltinDataType.STRING, 20);
		dataTypeColumn.setName("列的数据类型");
		dataTypeColumn.setComments("存储过程传入表参数时，需要知道每个列的数据类型[sqlserver使用字段]");
		columns.add(dataTypeColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name", BuiltinDataType.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		columns.add(propNameColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		ComColumndata inOutColumn = new ComColumndata("in_out", BuiltinDataType.INTEGER, 1);
		inOutColumn.setName("传入还是传出");
		inOutColumn.setComments("标识是传入的结果集信息，还是传出的结果集信息，in=1、out=2");
		columns.add(inOutColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_SQL_RESULTSET", 0);
		table.setName("sql结果集信息表");
		table.setComments("sql结果集信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0); 
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
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
