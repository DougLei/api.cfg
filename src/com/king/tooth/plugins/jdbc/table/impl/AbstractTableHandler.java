package com.king.tooth.plugins.jdbc.table.impl;

import java.util.List;

import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.StrUtils;

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
		analysisTable(tabledata);
		analysisTableComments(tabledata);// 解析表注释
		createTableSql.append(" ( ");
		List<ComColumndata> columns = tabledata.getColumns();
		for (ComColumndata column : columns) {
			analysisColumn(column, createTableSql);
			analysisColumnType(column, createTableSql);
			analysisColumnLength(column, createTableSql);
			analysisColumnProp(column, createTableSql);
			createTableSql.append(",");
			analysisColumnComments(tabledata.getTableName(), column, createTableSql);// 解析列注释
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
	 * <p>1.解析字段是否主键</p>
	 * <p>2.解析字段的默认值</p>
	 * <p>3.解析字段的值是否可为null</p>
	 * <p>4.解析字段是否唯一</p>
	 * @param column
	 * @param columnSql
	 */
	private void analysisColumnProp(ComColumndata column, StringBuilder columnSql) {
		if(column.getIsPrimaryKey() != null && 1 == column.getIsPrimaryKey()){
			columnSql.append(" primary key ");
		}
		if(StrUtils.notEmpty(column.getDefaultValue())){
			if(BuiltinDataType.DATE.equals(column.getColumnType())){
				throw new IllegalArgumentException("系统目前不支持给日期类型添加默认值");
			}
			if(BuiltinDataType.STRING.equals(column.getColumnType())){
				columnSql.append(" default '").append(column.getDefaultValue()).append("' ");
			}else{
				columnSql.append(" default ").append(column.getDefaultValue()).append(" ");
			}
		}
		if(column.getIsNullabled() != null && 0 == column.getIsNullabled()){
			columnSql.append(" not null ");
		}
		if(column.getIsUnique() != null && 1 == column.getIsUnique()){
			columnSql.append(" unique ");
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
		analysisColumnProp(column, operColumnSql);             
		analysisColumnComments(tableName, column, operColumnSql);// 解析列注释        
	}

	/**
	 * 组装修改列的sql语句
	 * @param tableName
	 * @param column
	 */
	public void installModifyColumnSql(String tableName, ComColumndata column) {
		
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
	 */
	protected abstract void analysisTableComments(ComTabledata table);
	
	/**
	 * 解析字段注释
	 * @param tableName
	 * @param column
	 * @param columnSql
	 */
	protected abstract void analysisColumnComments(String tableName, ComColumndata column, StringBuilder columnSql);

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
}
