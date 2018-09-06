package com.king.tooth.plugins.jdbc.table.impl.oracle;

import com.king.tooth.plugins.jdbc.table.impl.AbstractTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * oracle创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends AbstractTableHandler{

	protected void analysisColumnType(ComColumndata column, StringBuilder columnSql) {
		String columnType = column.getColumnType();
		if(BuiltinDataType.STRING.equals(columnType)){
			columnSql.append("varchar2");
		}else if(BuiltinDataType.BOOLEAN.equals(columnType)){
			columnSql.append("char(1)");
		}else if(BuiltinDataType.INTEGER.equals(columnType)){
			columnSql.append("number");
		}else if(BuiltinDataType.DOUBLE.equals(columnType)){
			columnSql.append("number");
		}else if(BuiltinDataType.DATE.equals(columnType)){
			columnSql.append("date");
		}else if(BuiltinDataType.CLOB.equals(columnType)){
			columnSql.append("clob");
		}else if(BuiltinDataType.BLOB.equals(columnType)){
			columnSql.append("blob");
		}else{
			throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成oracle对应的数据类型");
		}
	}

	protected void analysisColumnLength(ComColumndata column, StringBuilder columnSql) {
		// 验证哪些类型，oracle不需要加长度限制
		String columnType = column.getColumnType();
		if(BuiltinDataType.DATE.equals(columnType)
				|| BuiltinDataType.CLOB.equals(columnType)
				|| BuiltinDataType.BLOB.equals(columnType)
				|| BuiltinDataType.BOOLEAN.equals(columnType)){
			return;
		}
		
		Integer length = column.getLength();
		if(BuiltinDataType.STRING.equals(columnType)){
			if(length < 0 || length > 4000){
				columnSql.append("(4000)");
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
			createCommentSql.append("comment on table ")
							.append(table.getTableName())
							.append(" is '")
							.append(table.getComments())
							.append("';");
		}
	}

	protected void analysisColumnComments(String tableName, ComColumndata column, boolean isAdd, StringBuilder columnSql) {
		if(column.getComments() != null){
			columnSql.append("comment on column ")
					 .append(tableName).append(".").append(column.getColumnName())
					 .append(" is '")
					 .append(column.getComments())
					 .append("';");
		}
	}
	
	protected void addDefaultValueConstraint(String tableName, ComColumndata column, StringBuilder operColumnSql) {
		operColumnSql.append("alter table ").append(tableName).append(" modify ")
				 	 .append(column.getColumnName());
		if(BuiltinDataType.STRING.equals(column.getColumnType())){
			operColumnSql.append(" default '").append(column.getDefaultValue()).append("'");
		}else{
			operColumnSql.append(" default ").append(column.getDefaultValue());
		}
		operColumnSql.append(";");
	}

	protected void deleteDefaultValueConstraint(String tableName, ComColumndata column, StringBuilder operColumnSql) {
	}
}
