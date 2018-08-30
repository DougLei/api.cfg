package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;

/**
 * sql结果集信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
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
	
	//------------------------------------------------------------------------------
	
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_SQL_RESULTSET", 0);
		table.setName("sql结果集信息表");
		table.setComments("sql结果集信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0); 
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(13);
		
		ComColumndata sqlScriptIdColumn = new ComColumndata("sql_id", BuiltinCodeDataType.STRING, 32);
		sqlScriptIdColumn.setName("关联的sql脚本id");
		sqlScriptIdColumn.setComments("关联的sql脚本id");
		columns.add(sqlScriptIdColumn);
		
		ComColumndata sqlParameterIdColumn = new ComColumndata("sql_parameter_id", BuiltinCodeDataType.STRING, 32);
		sqlParameterIdColumn.setName("关联的sql脚本参数id");
		sqlParameterIdColumn.setComments("oracle通过输出参数返回结果集,[oracle使用字段]");
		columns.add(sqlParameterIdColumn);
		
		ComColumndata batchOrderColumn = new ComColumndata("batch_order", BuiltinCodeDataType.INTEGER, 1);
		batchOrderColumn.setName("结果集批次顺序");
		batchOrderColumn.setComments("sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]");
		columns.add(batchOrderColumn);
		
		ComColumndata columnNameColumn = new ComColumndata("column_name", BuiltinCodeDataType.STRING, 40);
		columnNameColumn.setName("列名");
		columnNameColumn.setComments("列名");
		columns.add(columnNameColumn);
		
		ComColumndata propNameColumn = new ComColumndata("prop_name", BuiltinCodeDataType.STRING, 40);
		propNameColumn.setName("属性名");
		propNameColumn.setComments("属性名");
		columns.add(propNameColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
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
}
