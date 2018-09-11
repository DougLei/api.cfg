package com.king.tooth.plugins.jdbc.table.impl.sqlserver;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.database.DatabaseConstraintConstants;
import com.king.tooth.plugins.jdbc.table.impl.AbstractTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.database.DBUtil;

/**
 * sqlserver创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends AbstractTableHandler{

	protected void analysisColumnType(ComColumndata column, StringBuilder columnSql) {
		String columnType = column.getColumnType();
		if(BuiltinDataType.STRING.equals(columnType)){
			columnSql.append("varchar");
		}else if(BuiltinDataType.BOOLEAN.equals(columnType)){
			columnSql.append("char(1)");
		}else if(BuiltinDataType.INTEGER.equals(columnType)){
			columnSql.append("int");
		}else if(BuiltinDataType.DOUBLE.equals(columnType)){
			columnSql.append("decimal");
		}else if(BuiltinDataType.DATE.equals(columnType)){
			columnSql.append("datetime");
		}else if(BuiltinDataType.CLOB.equals(columnType)){
			columnSql.append("text");
		}else if(BuiltinDataType.BLOB.equals(columnType)){
			columnSql.append("image");
		}else{
			throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成sqlserver对应的数据类型");
		}
	}

	protected void analysisColumnLength(ComColumndata column, StringBuilder columnSql) {
		// 验证哪些类型，sqlserver不需要加长度限制
		String columnType = column.getColumnType();
		if(BuiltinDataType.INTEGER.equals(columnType) 
				|| BuiltinDataType.DATE.equals(columnType)
				|| BuiltinDataType.CLOB.equals(columnType)
				|| BuiltinDataType.BLOB.equals(columnType)
				|| BuiltinDataType.BOOLEAN.equals(columnType)){
			return;
		}
		
		Integer length = column.getLength();
		if(BuiltinDataType.STRING.equals(columnType)){
			if(length < 0 || length > 8000){
				columnSql.append("(8000)");
//				columnSql.append("(max)"); // sqlserver的varchar最大长度配置为max，值可以为2G。目前系统不提供这种支持，如果要存储大数据，就用大字段text存储
			}else{
				columnSql.append("(").append(length).append(")");
			}
		}else if(length > 0){
			columnSql.append("(");
			columnSql.append(length);
			
			Integer precision = column.getPrecision();
			if(precision != null && precision > 0){
				columnSql.append(",").append(precision);
			}
			columnSql.append(")");
		}
	}
	
	protected void analysisTableComments(ComTabledata table, boolean isAdd) {
		if(table.getComments() != null){
			createCommentSql.append("execute ");
			if(isAdd){
				createCommentSql.append("sp_addextendedproperty");
			}else{
				createCommentSql.append("sp_updateextendedproperty");
			}
			createCommentSql.append(" 'MS_Description','")
							.append(table.getComments())
						    .append("','user','dbo','table','")
						    .append(table.getTableName())
						    .append("',null,null; ");
		}
	}

	protected void analysisColumnComments(String tableName, ComColumndata column, boolean isAdd, StringBuilder columnSql) {
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

	protected void addDefaultValueConstraint(String tableName, ComColumndata column, StringBuilder operColumnSql) {
		operColumnSql.append("alter table ").append(tableName).append(" add constraint ")
				 	 .append(DBUtil.getConstraintName(tableName, column.getColumnName(), DatabaseConstraintConstants.DEFAULT_VALUE));
		if(BuiltinDataType.STRING.equals(column.getColumnType())){
			operColumnSql.append(" default '").append(column.getDefaultValue()).append("'");
		}else{
			operColumnSql.append(" default ").append(column.getDefaultValue());
		}
		operColumnSql.append(" for ").append(column.getColumnName()).append(";");
	}

	protected void deleteDefaultValueConstraint(String tableName, ComColumndata column, StringBuilder operColumnSql) {
		dropConstraint(tableName, column.getColumnName(), operColumnSql, DatabaseConstraintConstants.DEFAULT_VALUE);
	}

	public void installDeleteColumnSql(String tableName, ComColumndata column) {
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
}
