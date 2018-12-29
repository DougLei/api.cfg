package com.king.tooth.plugins.jdbc.table.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.database.DatabaseConstraintConstants;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DBUtil;

/**
 * 数据表操作的抽象类(创建/删除)
 * @author DougLei
 */
public abstract class ATableHandler {

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
	 * 操作表数据类型的sql
	 * <p>create或drop</p>
	 * <p>sqlserver是表类型、oracle是游标</p>
	 */
	protected final StringBuilder operTableDataTypeSql = new StringBuilder();
	
	/**
	 * 组装创建表的sql语句
	 * <p>包括创建表和创建表/列注释的sql</p>
	 * <p>通过getCreateTableSql()和getCreateCommentSql()方法，获得结果sql语句</p>
	 * @param tabledata
	 */
	public void installCreateTableSql(CfgTable tabledata) {
		String tableName = tabledata.getTableName();
		analysisTable(tabledata);
		analysisTableComments(tabledata, true);// 解析表注释
		createTableSql.append(" ( ");
		List<CfgColumn> columns = tabledata.getColumns();
		for (CfgColumn column : columns) {
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
	private void analysisTable(CfgTable table){
		createTableSql.append("create table ").append(table.getTableName());
	}
	
	/**
	 * 解析字段
	 * @param column
	 * @param columnSql
	 */
	private void analysisColumn(CfgColumn column, StringBuilder columnSql){
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
	private void analysisColumnProps(String tableName, CfgColumn column, StringBuilder columnSql, StringBuilder operColumnSql) {
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
			
			addDefaultValueConstraint(tableName, column, operColumnSql);
			
			if(column.getIsUnique() != null && column.isTableUnique()){
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
	public void installCreateColumnSql(String tableName, CfgColumn column) {
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
	public void installModifyColumnSql(String tableName, CfgColumn column){
		JSONObject oldColumnInfo = column.getOldColumnInfo();
		if(oldColumnInfo != null){
			
			String oldColumnName = null; // 如果修改了列名，关联的各种约束名也要调整 [删除之前的约束，添加新的约束]
			if(oldColumnInfo.get("columnName") != null){
				oldColumnName = oldColumnInfo.getString("columnName");
				reColumnName(tableName, oldColumnName, column.getColumnName());
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
			
			// 是否修改了 [是否唯一] 约束
			if(oldColumnInfo.get("isUnique") == null){ 
				if(oldColumnName != null && column.getIsUnique() == 1){
					dropConstraint(tableName, oldColumnName, operColumnSql, DatabaseConstraintConstants.UNIQUE);
					addUniqueConstraint(tableName, column.getColumnName(), operColumnSql);
				}
			}else{
				Integer isUnique = column.getIsUnique();
				if(isUnique == 0){
					if(oldColumnName == null){
						dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.UNIQUE);
					}else{
						dropConstraint(tableName, oldColumnName, operColumnSql, DatabaseConstraintConstants.UNIQUE);
					}
				}else if(isUnique == 1){
					addUniqueConstraint(tableName, column.getColumnName(), operColumnSql);
				}
			}
			
			// 是否修改了 [默认值] 约束
			if(oldColumnInfo.get("updateDefaultValue") != null){ 
				// 原来存在默认值约束，则要删除之前的默认值约束
				if(oldColumnInfo.get("oldDefaultValue") != null){
					if(oldColumnName == null){
						deleteDefaultValueConstraint(tableName, column, operColumnSql);
					}else{
						deleteDefaultValueConstraint(tableName, new CfgColumn(oldColumnName), operColumnSql);
					}
				}
				
				// 如果存在新的默认值约束，则就添加
				if(column.getDefaultValue() != null){
					addDefaultValueConstraint(tableName, column, operColumnSql);
				}
			}else{
				if(oldColumnName != null && column.getDefaultValue() != null){
					deleteDefaultValueConstraint(tableName, new CfgColumn(oldColumnName), operColumnSql);
					addDefaultValueConstraint(tableName, column, operColumnSql);
				}
			}
			
			// 注释
			if(oldColumnInfo.get("updateComments") != null){ 
				boolean isAdd = StrUtils.isEmpty(oldColumnInfo.get("oldComments"));
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
	public void installDeleteColumnSql(String tableName, CfgColumn column) {
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
	 * 添加唯一约束
	 * @param tableName
	 * @param columnName
	 * @param operColumnSql
	 */
	protected void addUniqueConstraint(String tableName, String columnName, StringBuilder operColumnSql){
		operColumnSql.append("alter table ").append(tableName).append(" add constraint ")
					 .append(DBUtil.getConstraintName(tableName, columnName, DatabaseConstraintConstants.UNIQUE))
					 .append(" unique(").append(columnName).append(")")
				     .append(";");
	}
	
	/**
	 * 添加默认值约束
	 * @param tableName
	 * @param column
	 * @param operColumnSql
	 */
	protected abstract void addDefaultValueConstraint(String tableName, CfgColumn column, StringBuilder operColumnSql);
	
	/**
	 * 删除默认值约束
	 * @param tableName
	 * @param column
	 * @param operColumnSql
	 */
	protected abstract void deleteDefaultValueConstraint(String tableName, CfgColumn column, StringBuilder operColumnSql);
	
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
	 * @return 返回此次调用方法解析出的字段类型结果，不论columnSql参数是否为null
	 */
	protected abstract String analysisColumnType(CfgColumn column, StringBuilder columnSql);
	
	/**
	 * 解析字段长度
	 * @param column
	 * @param columnSql 
	 * @return 返回此次调用方法解析出的字段长度结果，不论columnSql参数是否为null
	 */
	protected abstract String analysisColumnLength(CfgColumn column, StringBuilder columnSql);
	
	// ----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 解析表的注释
	 * @param table
	 * @param isAdd 是否是添加，不是添加，就是修改
	 */
	protected abstract void analysisTableComments(CfgTable table, boolean isAdd);
	
	/**
	 * 解析字段注释
	 * @param tableName
	 * @param column
	 * @param isAdd 是否是添加，不是添加，就是修改
	 * @param columnSql
	 */
	protected abstract void analysisColumnComments(String tableName, CfgColumn column, boolean isAdd, StringBuilder columnSql);

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
	 * 获取操作表数据类型的sql
	 * @return
	 */
	public String getOperTableDataTypeSql() {
		try {
			return operTableDataTypeSql.toString();
		} finally {
			operTableDataTypeSql.setLength(0);
		}
	}

	/**
	 * 获取修改表名的sql
	 * @param newTableName
	 * @param oldTableName
	 * @return
	 */
	public abstract String getReTableNameSql(String newTableName, String oldTableName);

	// --------------------------------------------------------------------------------------
	/**
	 * 组装列的信息，包括数据类型、长度、精度、唯一约束，是否可为空等
	 * @param column
	 * @return
	 */
	protected String installColumnInfo(CfgColumn column) {
		StringBuilder tmpBuffer = new StringBuilder();
		tmpBuffer.append(" ").append(analysisColumnType(column, null)).append(analysisColumnLength(column, null));
		if(StrUtils.notEmpty(column.getDefaultValue())){
			tmpBuffer.append(" default (");
			if(DataTypeConstants.STRING.equals(column.getColumnType())){
				tmpBuffer.append("'").append(column.getDefaultValue()).append("'");
			}else{
				tmpBuffer.append(column.getDefaultValue());
			}
			tmpBuffer.append(")");
		}
		if(column.getIsNullabled() == 0){
			tmpBuffer.append(" not null");
		}
		if(column.getIsUnique() == 1){
			tmpBuffer.append(" unique");
		}else{
			if(column.getIsPrimaryKey() == 1){
				tmpBuffer.append(" primary key");
			}
		}
		return tmpBuffer.toString();
	}
	
	/**
	 * 组装创建表数据类型的sql语句
	 * @param table
	 */
	public abstract void installCreateTableDataTypeSql(CfgTable table);
	
	/**
	 * 组装删除表数据类型的sql语句
	 * @param table
	 */
	public abstract void installDropTableDataTypeSql(CfgTable table);
}
