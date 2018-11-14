package com.king.tooth.plugins.jdbc.table.impl.sqlserver;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.database.DatabaseConstraintConstants;
import com.king.tooth.plugins.jdbc.table.impl.ATableHandler;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.util.database.DBUtil;

/**
 * sqlserver创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends ATableHandler{

	protected String analysisColumnType(CfgColumn column, StringBuilder columnSql) {
		StringBuilder tmpBuffer = new StringBuilder();
		String columnType = column.getColumnType();
		if(DataTypeConstants.STRING.equals(columnType)){
			tmpBuffer.append("varchar");
		}else if(DataTypeConstants.CHAR.equals(columnType)){
			tmpBuffer.append("char");
		}else if(DataTypeConstants.BOOLEAN.equals(columnType)){
			tmpBuffer.append("char(1)");
		}else if(DataTypeConstants.INTEGER.equals(columnType)){
			tmpBuffer.append("int");
		}else if(DataTypeConstants.DOUBLE.equals(columnType)){
			tmpBuffer.append("decimal");
		}else if(DataTypeConstants.DATE.equals(columnType)){
			tmpBuffer.append("datetime");
		}else if(DataTypeConstants.CLOB.equals(columnType)){
			tmpBuffer.append("text");
		}else if(DataTypeConstants.BLOB.equals(columnType)){
			tmpBuffer.append("image");
		}else{
			throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成sqlserver对应的数据类型");
		}
		if(columnSql != null){
			columnSql.append(tmpBuffer);
		}
		return tmpBuffer.toString();
	}

	protected String analysisColumnLength(CfgColumn column, StringBuilder columnSql) {
		// 验证哪些类型，sqlserver不需要加长度限制
		String columnType = column.getColumnType();
		if(DataTypeConstants.INTEGER.equals(columnType) 
				|| DataTypeConstants.DATE.equals(columnType)
				|| DataTypeConstants.CLOB.equals(columnType)
				|| DataTypeConstants.BLOB.equals(columnType)
				|| DataTypeConstants.BOOLEAN.equals(columnType)){
			return null;
		}
		
		StringBuilder tmpBuffer = new StringBuilder();
		Integer length = column.getLength();
		if(DataTypeConstants.STRING.equals(columnType) || DataTypeConstants.CHAR.equals(columnType)){
			if(length < 1 || length > 8000){
				tmpBuffer.append("(8000)");
			}else{
				tmpBuffer.append("(").append(length).append(")");
			}
		}else if(length > 0){
			tmpBuffer.append("(");
			tmpBuffer.append(length);
			
			Integer precision = column.getPrecision();
			if(precision != null && precision > 0){
				tmpBuffer.append(",").append(precision);
			}
			tmpBuffer.append(")");
		}
		if(columnSql != null){
			columnSql.append(tmpBuffer);
		}
		return tmpBuffer.toString();
	}
	
	protected void analysisTableComments(CfgTable table, boolean isAdd) {
		if(table.getRemark() != null){
			createCommentSql.append("execute ");
			if(isAdd){
				createCommentSql.append("sp_addextendedproperty");
			}else{
				createCommentSql.append("sp_updateextendedproperty");
			}
			createCommentSql.append(" 'MS_Description','")
							.append(table.getRemark())
						    .append("','user','dbo','table','")
						    .append(table.getTableName())
						    .append("',null,null; ");
		}
	}

	protected void analysisColumnComments(String tableName, CfgColumn column, boolean isAdd, StringBuilder columnSql) {
		if(column.getComments() != null){
			columnSql.append("execute ");
			if(isAdd){
				columnSql.append("sp_addextendedproperty");
			}else{
				columnSql.append("sp_updateextendedproperty");
			}
			columnSql.append(" 'MS_Description','")
					 .append(column.getComments())
					 .append("','user','dbo','table','")
					 .append(tableName)
					 .append("','column','")
					 .append(column.getColumnName())
					 .append("';");
		}
	}

	protected void addDefaultValueConstraint(String tableName, CfgColumn column, StringBuilder operColumnSql) {
		operColumnSql.append("alter table ").append(tableName).append(" add constraint ")
				 	 .append(DBUtil.getConstraintName(tableName, column.getColumnName(), DatabaseConstraintConstants.DEFAULT_VALUE));
		if(DataTypeConstants.STRING.equals(column.getColumnType())){
			operColumnSql.append(" default '").append(column.getDefaultValue()).append("'");
		}else{
			operColumnSql.append(" default ").append(column.getDefaultValue());
		}
		operColumnSql.append(" for ").append(column.getColumnName()).append(";");
	}

	protected void deleteDefaultValueConstraint(String tableName, CfgColumn column, StringBuilder operColumnSql) {
		dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.DEFAULT_VALUE);
	}

	public void installDeleteColumnSql(String tableName, CfgColumn column) {
		JSONObject oldColumnInfo = column.getOldColumnInfo();
		if(oldColumnInfo != null){
			// 是否唯一
			if(oldColumnInfo.get("isUnique") != null && oldColumnInfo.getInteger("isUnique") == 1){ 
				dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.UNIQUE);
			}
			// 默认值
			if(oldColumnInfo.get("havaOldDefaultValue") != null && oldColumnInfo.getBoolean("havaOldDefaultValue") && oldColumnInfo.get("defaultValue") != null){ 
				dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.DEFAULT_VALUE);
			}
			oldColumnInfo.clear();
		}
		
		if(column.getIsUnique() != null && column.getIsUnique() == 1){
			dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.UNIQUE);
		}
		if(column.getDefaultValue() != null ){
			dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.DEFAULT_VALUE);
		}
		super.installDeleteColumnSql(tableName, column);
	}

	protected void reColumnName(String tableName, String oldColumnName, String newColumnName) {
		operColumnSql.append("exec sp_rename '").append(tableName).append(".").append(oldColumnName).append("','").append(newColumnName).append("','column';");
	}
	
	public String getReTableNameSql(String newTableName, String oldTableName) {
		return "exec sp_rename '"+oldTableName+"', '"+newTableName+"'";
	}

	// --------------------------------------------------------------------------------------
	public void installCreateTableDataTypeSql(CfgTable table) {
		operTableDataTypeSql.append("create type ").append(table.getTableName()).append(" as table(");
		List<CfgColumn> columns = table.getColumns();
		for (CfgColumn column : columns) {
			operTableDataTypeSql.append(" ").append(column.getColumnName()).append(installColumnInfo(column)).append(",");
		}
		operTableDataTypeSql.setLength(operTableDataTypeSql.length()-1);
		operTableDataTypeSql.append(")");
	}

	public void installDropTableDataTypeSql(CfgTable table) {
		operTableDataTypeSql.append("drop type ").append(table.getTableName());
	}
}
