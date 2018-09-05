package com.king.tooth.plugins.jdbc.table.impl.sqlserver;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.jdbc.table.impl.AbstractTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.StrUtils;

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
	
	protected void analysisTableComments(ComTabledata table) {
		if(StrUtils.notEmpty(table.getComments())){
			createCommentSql.append("execute sp_addextendedproperty 'MS_Description','")
							.append(table.getComments())
						    .append("','user','dbo','table','")
						    .append(table.getTableName())
						    .append("',null,null; ");
		}
	}

	protected void analysisColumnComments(String tableName, ComColumndata column, StringBuilder columnSql) {
		if(StrUtils.notEmpty(column.getComments())){
			columnSql.append("execute sp_addextendedproperty 'MS_Description','")
					 .append(column.getComments())
					 .append("','user','dbo','table','")
					 .append(tableName)
					 .append("','column','")
					 .append(column.getColumnName())
					 .append("';");
		}
	}

	public void installModifyColumnSql(String tableName, ComColumndata column) {
		JSONObject oldColumnInfo = column.getOldColumnInfo();
		if(oldColumnInfo != null){
			if(oldColumnInfo.get("columnName") != null){ // 列名
				operColumnSql.append("alter table ").append(tableName).append(" rename ").append(oldColumnInfo.get("columnName")).append(" to ").append(column.getColumnName());
			}
			if(oldColumnInfo.get("columnType") != null){ // 字段数据类型
				operColumnSql.append("alter table ").append(tableName).append(" ");
				
			}
			if(oldColumnInfo.get("defaultValue") != null){ // 默认值
				operColumnSql.append("alter table ").append(tableName).append(" ");
				
			}
			if(oldColumnInfo.get("length") != null){ // 字段长度
				operColumnSql.append("alter table ").append(tableName).append(" ");
				
			}
			if(oldColumnInfo.get("precision") != null){ // 数据精度
				operColumnSql.append("alter table ").append(tableName).append(" ");
				
			}
			if(oldColumnInfo.get("isUnique") != null){ // 是否唯一
				operColumnSql.append("alter table ").append(tableName).append(" ");
				
			}
			if(oldColumnInfo.get("isNullabled") != null){ // 是否可为空
				operColumnSql.append("alter table ").append(tableName).append(" ");
				
			}
		}
	}
}
