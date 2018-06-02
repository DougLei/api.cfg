package com.king.tooth.plugins.jdbc.table.impl.oracle;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.plugins.jdbc.table.AbstractTableHandler;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.StrUtils;

/**
 * oracle创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends AbstractTableHandler{

	protected void analysisColumnType(CfgColumndata column) {
		String columnType = column.getColumnType();
		switch(columnType){
			case DataTypeConstants.STRING:
				createTableSql.append("varchar2");
				break;
			case DataTypeConstants.BOOLEAN:
				createTableSql.append("char");
				break;
			case DataTypeConstants.INTEGER:
				createTableSql.append("number");
				break;
			case DataTypeConstants.DOUBLE:
				createTableSql.append("number");
				break;
			case DataTypeConstants.DATE:
				createTableSql.append("date");
				break;
			case DataTypeConstants.CLOB:
				createTableSql.append("clob");
				break;
			case DataTypeConstants.BLOB:
				createTableSql.append("blob");
				break;
			default:
				throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成oracle对应的数据类型");
		}
	}

	protected void analysisColumnLength(CfgColumndata column) {
		// 验证哪些类型，oracle不需要加长度限制
		String columnType = column.getColumnType();
		if(DataTypeConstants.DATE.equals(columnType)
				|| DataTypeConstants.CLOB.equals(columnType)
				|| DataTypeConstants.BLOB.equals(columnType)){
			return;
		}else if(DataTypeConstants.BOOLEAN.equals(columnType)){
			createTableSql.append("(1)");
			return;
		}
		
		int length = column.getLength();
		if(DataTypeConstants.STRING.equals(columnType)){
			if(length < 0 || length > 4000){
				createTableSql.append("(4000)");
			}else{
				createTableSql.append("(").append(length).append(")");
			}
		}else if(length > 0){
			createTableSql.append("(");
			createTableSql.append(length);
			
			int precision = column.getPrecision();
			if(precision > 0){
				createTableSql.append(",").append(precision);
			}
			createTableSql.append(")");
		}
	}
	
	protected void analysisTableComments(CfgTabledata table) {
		if(StrUtils.notEmpty(table.getComments())){
			createCommentSql.append("comment on table ")
							.append(table.getTableName())
							.append(" is '")
							.append(table.getComments())
							.append("';");
		}
	}

	protected void analysisColumnComments(String tableName, CfgColumndata column) {
		if(StrUtils.notEmpty(column.getComments())){
			createCommentSql.append("comment on column ")
							.append(tableName).append(".").append(column.getColumnName())
							.append(" is '")
							.append(column.getComments())
							.append("';");
		}
	}
}
