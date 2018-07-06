package com.king.tooth.plugins.jdbc.table.impl.oracle;

import com.king.tooth.plugins.jdbc.table.AbstractTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.StrUtils;

/**
 * oracle创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends AbstractTableHandler{

	protected void analysisColumnType(ComColumndata column) {
		String columnType = column.getColumnType();
		if(BuiltinCodeDataType.STRING.equals(columnType)){
			createTableSql.append("varchar2");
		}else if(BuiltinCodeDataType.BOOLEAN.equals(columnType)){
			createTableSql.append("char");
		}else if(BuiltinCodeDataType.INTEGER.equals(columnType)){
			createTableSql.append("number");
		}else if(BuiltinCodeDataType.DOUBLE.equals(columnType)){
			createTableSql.append("number");
		}else if(BuiltinCodeDataType.DATE.equals(columnType)){
			createTableSql.append("date");
		}else if(BuiltinCodeDataType.CLOB.equals(columnType)){
			createTableSql.append("clob");
		}else if(BuiltinCodeDataType.BLOB.equals(columnType)){
			createTableSql.append("blob");
		}else{
			throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成oracle对应的数据类型");
		}
	}

	protected void analysisColumnLength(ComColumndata column) {
		// 验证哪些类型，oracle不需要加长度限制
		String columnType = column.getColumnType();
		if(BuiltinCodeDataType.DATE.equals(columnType)
				|| BuiltinCodeDataType.CLOB.equals(columnType)
				|| BuiltinCodeDataType.BLOB.equals(columnType)){
			return;
		}else if(BuiltinCodeDataType.BOOLEAN.equals(columnType)){
			createTableSql.append("(1)");
			return;
		}
		
		Integer length = column.getLength();
		if(BuiltinCodeDataType.STRING.equals(columnType)){
			if(length < 0 || length > 4000){
				createTableSql.append("(4000)");
			}else{
				createTableSql.append("(").append(length).append(")");
			}
		}else if(length > 0){
			createTableSql.append("(");
			createTableSql.append(length);
			
			Integer precision = column.getPrecision();
			if(precision != null && precision > 0){
				createTableSql.append(",").append(precision);
			}
			createTableSql.append(")");
		}
	}
	
	protected void analysisTableComments(ComTabledata table) {
		if(StrUtils.notEmpty(table.getComments())){
			createCommentSql.append("comment on table ")
							.append(table.getTableName())
							.append(" is '")
							.append(table.getComments())
							.append("';");
		}
	}

	protected void analysisColumnComments(String tableName, ComColumndata column) {
		if(StrUtils.notEmpty(column.getComments())){
			createCommentSql.append("comment on column ")
							.append(tableName).append(".").append(column.getColumnName())
							.append(" is '")
							.append(column.getComments())
							.append("';");
		}
	}
}
