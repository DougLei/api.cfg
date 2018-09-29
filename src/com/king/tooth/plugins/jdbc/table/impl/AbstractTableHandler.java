package com.king.tooth.plugins.jdbc.table.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.database.DatabaseConstraintConstants;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.database.DBUtil;

/**
 * 数据表操作的抽象类(创建/删除)
 * @author DougLei
 */
public abstract class AbstractTableHandler {

	/**
	 * 创建表的createTableSql
	 */
	protected final StringBuilder createTableSql = new StringBuilder();
	
	/**
	 * 创建表和列注释的createCommentSql
	 */
	protected final StringBuilder createCommentSql = new StringBuilder();
	
	/**
	 * 操作列的operColumnSql
	 */
	protected final StringBuilder operColumnSql = new StringBuilder();
	
	/**
	 * 组装创建表的sql语句
	 * <p>包括创建表和创建表/列注释的sql</p>
	 * <p>通过getCreateTableSql()和getCreateCommentSql()方法，获得结果sql语句</p>
	 * @param tabledata
	 */
	public void installCreateTableSql(ComTabledata tabledata) {
		String tableName = tabledata.getTableName();
		analysisTable(tabledata);
		analysisTableComments(tabledata, true);// 解析表注释
		createTableSql.append(" ( ");
		List<ComColumndata> columns = tabledata.getColumns();
		for (ComColumndata column : columns) {
			analysisColumn(column, createTableSql);
			analysisColumnType(column, createTableSql);
			analysisColumnLength(column, createTableSql);
			analysisColumnProps(tableName, column, createTableSql, operColumnSql);
			createTableSql.append(",");
			analysisColumnComments(tabledata.getTableName(), column, true, createCommentSql);// 解析列注释
		}
		createTableSql.setLength(createTableSql.length() - 1);
		createTableSql.append(")");
	}
	
	/**
	 * 解析表
	 * @param table
	 */
	private void analysisTable(ComTabledata table){
		createTableSql.append("create table ").append(table.getTableName());
	}
	
	/**
	 * 解析字段
	 * @param column
	 * @param columnSql
	 */
	private void analysisColumn(ComColumndata column, StringBuilder columnSql){
		columnSql.append(column.getColumnName()).append(" ");
	}
	
	/**
	 * 解析字段的属性配置
	 * <p>1.解析字段的值是否可为null(添加到语句中)</p>
	 * <p>2.解析字段是否主键(单独创建添加约束的语句)</p>
	 * <p>3.解析字段的默认值(单独创建添加约束的语句)</p>
	 * <p>4.解析字段是否唯一(单独创建添加约束的语句)</p>
	 * @param tableName
	 * @param column
	 * @param columnSql
	 * @param operColumnSql 
	 */
	private void analysisColumnProps(String tableName, ComColumndata column, StringBuilder columnSql, StringBuilder operColumnSql) {
		if(columnSql != null){
			if(column.getIsNullabled() != null && 0 == column.getIsNullabled()){
				columnSql.append(" not null ");
			}
		}
		
		if(operColumnSql != null){
			if(column.getIsPrimaryKey() != null && 1 == column.getIsPrimaryKey()){
				operColumnSql.append("alter table ").append(tableName).append(" add constraint ")
							 .append(DBUtil.getConstraintName(tableName, column.getColumnName(), DatabaseConstraintConstants.PRIMARY_KEY))
							 .append(" primary key (").append(column.getColumnName()).append(")")
					         .append(";");
			}
			if(column.getDefaultValue() != null){
				if(BuiltinDataType.DATE.equals(column.getColumnType())){
					throw new IllegalArgumentException("系统目前不支持给日期类型添加默认值");
				}
				addDefaultValueConstraint(tableName, column, operColumnSql);
			}
			if(column.getIsUnique() != null && 1 == column.getIsUnique()){
				operColumnSql.append("alter table ").append(tableName).append(" add constraint ")
							 .append(DBUtil.getConstraintName(tableName, column.getColumnName(), DatabaseConstraintConstants.UNIQUE))
							 .append(" unique(").append(column.getColumnName()).append(")")
				             .append(";");
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * 组装添加列的sql语句
	 * @param tableName
	 * @param column
	 */
	public void installCreateColumnSql(String tableName, ComColumndata column) {
		operColumnSql.append("alter table ").append(tableName).append(" add ");
		analysisColumn(column, operColumnSql);
		analysisColumnType(column, operColumnSql);
		analysisColumnLength(column, operColumnSql);
		analysisColumnProps(tableName, column, operColumnSql, null);// 先解析是否可为空
		operColumnSql.append(";");// 结束
		analysisColumnProps(tableName, column, null, operColumnSql);// 再解析其他约束配置
		analysisColumnComments(tableName, column, true, operColumnSql);// 最后解析列注释        
	}

	/**
	 * 修改列名
	 * @param tableName
	 * @param oldColumnName
	 * @param newColumnName
	 */
	protected abstract void reColumnName(String tableName, String oldColumnName, String newColumnName);
	
	/**
	 * 组装修改列的sql语句
	 * @param tableName
	 * @param column
	 */
	public void installModifyColumnSql(String tableName, ComColumndata column){
		JSONObject oldColumnInfo = column.getOldColumnInfo();
		if(oldColumnInfo != null){
			
			// 列名
			if(oldColumnInfo.get("columnName") != null){ 
				reColumnName(tableName, oldColumnInfo.getString("columnName"), column.getColumnName());
			}
			
			// 字段数据类型，字段长度，数据精度，是否可为空
			if(oldColumnInfo.get("columnType") != null || oldColumnInfo.get("length") != null || oldColumnInfo.get("precision") != null || oldColumnInfo.get("isNullabled") != null){ 
				operColumnSql.append("alter table ").append(tableName).append(" alter column ").append(column.getColumnName()).append(" ");
				analysisColumnType(column, operColumnSql);
				analysisColumnLength(column, operColumnSql);
				
				if(column.getIsNullabled() == 0){
					operColumnSql.append(" not null ");
				}else if(column.getIsNullabled() == 1){
					operColumnSql.append(" null ");
				}
				operColumnSql.append(";");
			}
			
			// 是否唯一
			if(oldColumnInfo.get("isUnique") != null){ 
				Integer isUnique = column.getIsUnique();
				if(isUnique == 0){
					dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.UNIQUE);
				}else if(isUnique == 1){
					operColumnSql.append("alter table ").append(tableName).append(" add constraint ")
								 .append(DBUtil.getConstraintName(tableName, column.getColumnName(), DatabaseConstraintConstants.UNIQUE))
								 .append(" unique(").append(column.getColumnName()).append(")")
					             .append(";");
				}
			}
			
			// 默认值
			if(oldColumnInfo.get("havaOldDefaultValue") != null && oldColumnInfo.getBoolean("havaOldDefaultValue")){ 
				// 原来存在默认值约束，则要删除之前的默认值约束
				if(oldColumnInfo.get("defaultValue") != null){
					deleteDefaultValueConstraint(tableName, column, operColumnSql);
				}
				
				// 如果存在新的默认值约束，则就添加
				if(column.getDefaultValue() != null){
					addDefaultValueConstraint(tableName, column, operColumnSql);
				}
			}
			
			// 注释
			if(oldColumnInfo.get("havaComments") != null && oldColumnInfo.getBoolean("havaComments")){ 
				boolean isAdd = oldColumnInfo.get("comments") == null;
				analysisColumnComments(tableName, column, isAdd, operColumnSql);
			}
			oldColumnInfo.clear();
		}
	}
	
	/**
	 * 组装删除列的sql语句
	 * @param tableName
	 * @param column
	 */
	public void installDeleteColumnSql(String tableName, ComColumndata column) {
		operColumnSql.append("alter table ").append(tableName).append(" drop column " + column.getColumnName()).append(";");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 删除约束
	 * @param tableName
	 * @param columnName
	 * @param operColumnSql
	 * @param constraintType
	 */
	protected void dropConstraint(String tableName, String columnName, StringBuilder operColumnSql, String constraintType){
		operColumnSql.append("alter table ").append(tableName)
					 .append(" drop constraint ")
					 .append(DBUtil.getConstraintName(tableName, columnName, constraintType))
					 .append(";");
	}
	
	/**
	 * 添加默认值约束
	 * @param tableName
	 * @param column
	 * @param operColumnSql
	 */
	protected abstract void addDefaultValueConstraint(String tableName, ComColumndata column, StringBuilder operColumnSql);
	
	/**
	 * 删除默认值约束
	 * @param tableName
	 * @param column
	 * @param operColumnSql
	 */
	protected abstract void deleteDefaultValueConstraint(String tableName, ComColumndata column, StringBuilder operColumnSql);
	
	// ----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 解析字段类型
	 * <pre>
	 * 		字符类型 string
	 * 		整数类型 integer
	 * 		小数类型 double
	 *         oracle的number、sqlserver的decimal
	 *         数据长度(12,2)：表示的含义为：该字段的总长度为12位，其中小数位占2位
	 *                   		           如果小数位超出两位，数据库在存储时会进行四舍五入的操作，存储精度只保存两位
	 * 		日期类型 date
	 * 		大字段字符类型 clob
	 * 		大字段二进制类型 blob
	 * </pre>
	 * @param column
	 * @param columnSql
	 */
	protected abstract void analysisColumnType(ComColumndata column, StringBuilder columnSql);
	
	/**
	 * 解析字段长度
	 * @param column
	 * @param columnSql
	 */
	protected abstract void analysisColumnLength(ComColumndata column, StringBuilder columnSql);
	
	// ----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 解析表的注释
	 * @param table
	 * @param isAdd 是否是添加，不是添加，就是修改
	 */
	protected abstract void analysisTableComments(ComTabledata table, boolean isAdd);
	
	/**
	 * 解析字段注释
	 * @param tableName
	 * @param column
	 * @param isAdd 是否是添加，不是添加，就是修改
	 * @param columnSql
	 */
	protected abstract void analysisColumnComments(String tableName, ComColumndata column, boolean isAdd, StringBuilder columnSql);

	// ----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 获得create表的sql
	 * @return
	 */
	public String getCreateTableSql() {
		try {
			return createTableSql.toString();
		} finally {
			createTableSql.setLength(0);
		}
	}
	/**
	 * 获得create注释的sql
	 * @return
	 */
	public String getCreateCommentSql() {
		try {
			return createCommentSql.toString();
		} finally {
			createCommentSql.setLength(0);
		}
	}

	/**
	 * 获取操作列的sql
	 * @return
	 */
	public String getOperColumnSql() {
		try {
			return operColumnSql.toString();
		} finally {
			operColumnSql.setLength(0);
		}
	}

	/**
	 * 获取修改表名的sql
	 * @param newTableName
	 * @param oldTableName
	 * @return
	 */
	public abstract String getReTableNameSql(String newTableName, String oldTableName);
}
