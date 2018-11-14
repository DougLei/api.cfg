package com.king.tooth.plugins.jdbc.table.impl.oracle;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.plugins.jdbc.table.impl.ATableHandler;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * oracle创建表操作的实现类
 * @author DougLei
 */
public class TableImpl extends ATableHandler{

	protected String analysisColumnType(CfgColumn column, StringBuilder columnSql) {
		StringBuilder tmpBuffer = new StringBuilder();
		String columnType = column.getColumnType();
		if(DataTypeConstants.STRING.equals(columnType)){
			tmpBuffer.append("varchar2");
		}else if(DataTypeConstants.CHAR.equals(columnType)){
			tmpBuffer.append("char");
		}else if(DataTypeConstants.BOOLEAN.equals(columnType)){
			tmpBuffer.append("char(1)");
		}else if(DataTypeConstants.INTEGER.equals(columnType)){
			tmpBuffer.append("number");
		}else if(DataTypeConstants.DOUBLE.equals(columnType)){
			tmpBuffer.append("number");
		}else if(DataTypeConstants.DATE.equals(columnType)){
			tmpBuffer.append("date");
		}else if(DataTypeConstants.CLOB.equals(columnType)){
			tmpBuffer.append("clob");
		}else if(DataTypeConstants.BLOB.equals(columnType)){
			tmpBuffer.append("blob");
		}else{
			throw new IllegalArgumentException("系统目前不支持将["+columnType+"]转换成oracle对应的数据类型");
		}
		if(columnSql != null){
			columnSql.append(tmpBuffer);
		}
		return tmpBuffer.toString();
	}

	protected String analysisColumnLength(CfgColumn column, StringBuilder columnSql) {
		// 验证哪些类型，oracle不需要加长度限制
		String columnType = column.getColumnType();
		if(DataTypeConstants.DATE.equals(columnType)
				|| DataTypeConstants.CLOB.equals(columnType)
				|| DataTypeConstants.BLOB.equals(columnType)
				|| DataTypeConstants.BOOLEAN.equals(columnType)){
			return null;
		}
		
		StringBuilder tmpBuffer = new StringBuilder();
		Integer length = column.getLength();
		if(DataTypeConstants.STRING.equals(columnType)){
			if(length < 1 || length > 4000){
				tmpBuffer.append("(4000)");
			}else{
				tmpBuffer.append("(").append(length).append(")");
			}
		}else if(DataTypeConstants.CHAR.equals(columnType)){
			if(length < 1 || length > 2000){
				tmpBuffer.append("(2000)");
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
			createCommentSql.append("comment on table ")
							.append(table.getTableName())
							.append(" is '")
							.append(table.getRemark())
							.append("';");
		}
	}

	protected void analysisColumnComments(String tableName, CfgColumn column, boolean isAdd, StringBuilder columnSql) {
		if(column.getComments() != null){
			columnSql.append("comment on column ")
					 .append(tableName).append(".").append(column.getColumnName())
					 .append(" is '")
					 .append(column.getComments())
					 .append("';");
		}
	}
	
	protected void addDefaultValueConstraint(String tableName, CfgColumn column, StringBuilder operColumnSql) {
		operColumnSql.append("alter table ").append(tableName).append(" modify ")
				 	 .append(column.getColumnName());
		if(DataTypeConstants.STRING.equals(column.getColumnType())){
			operColumnSql.append(" default '").append(column.getDefaultValue()).append("'");
		}else{
			operColumnSql.append(" default ").append(column.getDefaultValue());
		}
		operColumnSql.append(";");
	}

	protected void deleteDefaultValueConstraint(String tableName, CfgColumn column, StringBuilder operColumnSql) {
	}
	
	protected void reColumnName(String tableName, String oldColumnName, String newColumnName) {
		operColumnSql.append("alter table ").append(tableName).append(" rename column ").append(oldColumnName).append(" to ").append(newColumnName).append(";");

	}

	public String getReTableNameSql(String newTableName, String oldTableName) {
		return "alter table "+oldTableName+" rename to "+ newTableName;
	}

	public void installCreateTableDataTypeSql(CfgTable table) {
		// TODO 组装创建游标的sql语句
	}

	public void installDropTableDataTypeSql(CfgTable table) {
		// TODO 组装删除游标的sql语句
	}
}
