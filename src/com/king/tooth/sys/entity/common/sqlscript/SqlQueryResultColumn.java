package com.king.tooth.sys.entity.common.sqlscript;

import com.king.tooth.util.NamingTurnUtil;

/**
 * sql查询结果的列名对象
 * @see ComSqlScript使用到
 * @author DougLei
 */
public class SqlQueryResultColumn {
	/**
	 * 查询结果的列名
	 */
	private String resultColumnName;
	/**
	 * 查询结果的属性名
	 */
	private String resultPropName;
	/**
	 * 列的类型
	 */
	private String dataType;
	
	public SqlQueryResultColumn(String resultColumnName, String dataType) {
		this.resultColumnName = resultColumnName;
		this.resultPropName = NamingTurnUtil.columnNameTurnPropName(resultColumnName);
		this.dataType = dataType;
	}
	public SqlQueryResultColumn() {
	}
	public String getResultColumnName() {
		return resultColumnName;
	}
	public String getResultPropName() {
		return resultPropName;
	}
	public String getDataType() {
		return dataType;
	}
}
