package com.king.tooth.plugins.jdbc.table.impl.sqlserver;

import com.king.tooth.plugins.jdbc.table.AbstractTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.StrUtils;

/**
 * sqlserver创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends AbstractTableHandler{

	protected void analysisColumnType(ComColumndata column) {
		String columnType = column.getColumnType();
		if(BuiltinCodeDataType.STRING.equals(columnType)){
			createTableSql.append("varchar");
		}else if(BuiltinCodeDataType.BOOLEAN.equals(columnType)){
			createTableSql.append("char(1)");
		}else if(BuiltinCodeDataType.INTEGER.equals(columnType)){
			createTableSql.append("int");
		}else if(BuiltinCodeDataType.DOUBLE.equals(columnType)){
			createTableSql.append("decimal");
		}else if(BuiltinCodeDataType.DATE.equals(columnType)){
			createTableSql.append("datetime");
		}else if(BuiltinCodeDataType.CLOB.equals(columnType)){
			createTableSql.append("text");
		}else if(BuiltinCodeDataType.BLOB.equals(columnType)){
			createTableSql.append("image");
		}else{
			throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成sqlserver对应的数据类型");
		}
	}

	protected void analysisColumnLength(ComColumndata column) {
		// 验证哪些类型，sqlserver不需要加长度限制
		String columnType = column.getColumnType();
		if(BuiltinCodeDataType.INTEGER.equals(columnType) 
				|| BuiltinCodeDataType.DATE.equals(columnType)
				|| BuiltinCodeDataType.CLOB.equals(columnType)
				|| BuiltinCodeDataType.BLOB.equals(columnType)
				|| BuiltinCodeDataType.BOOLEAN.equals(columnType)){
			return;
		}
		
		Integer length = column.getLength();
		if(BuiltinCodeDataType.STRING.equals(columnType)){
			if(length < 0 || length > 8000){
				createTableSql.append("(8000)");
//				createTableSql.append("(max)"); // sqlserver的varchar最大长度配置为max，值可以为2G。目前系统不提供这种支持，如果要存储大数据，就用大字段text存储
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
			createCommentSql.append("execute sp_addextendedproperty 'MS_Description','")
							.append(table.getComments())
						    .append("','user','dbo','table','")
						    .append(table.getTableName())
						    .append("',null,null; ");
		}
	}

	protected void analysisColumnComments(String tableName, ComColumndata column) {
		if(StrUtils.notEmpty(column.getComments())){
			createCommentSql.append("execute sp_addextendedproperty 'MS_Description','")
							.append(column.getComments())
							.append("','user','dbo','table','")
							.append(tableName)
							.append("','column','")
							.append(column.getColumnName())
							.append("';");
		}
	}
}
