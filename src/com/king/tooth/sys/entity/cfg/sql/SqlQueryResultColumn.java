package com.king.tooth.sys.entity.cfg.sql;

import com.king.tooth.constants.ResourcePropNameConstants;
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
	
	public SqlQueryResultColumn(String resultColumnName) {
		this.resultColumnName = resultColumnName;
		if("id".equalsIgnoreCase(resultColumnName)){
			this.resultPropName = ResourcePropNameConstants.ID;
		}else{
			this.resultPropName = NamingTurnUtil.columnNameTurnPropName(resultColumnName);
		}
	}
	public SqlQueryResultColumn() {
	}
	public String getResultColumnName() {
		return resultColumnName;
	}
	public String getResultPropName() {
		return resultPropName;
	}
	public void setResultColumnName(String resultColumnName) {
		this.resultColumnName = resultColumnName;
	}
	public void setResultPropName(String resultPropName) {
		this.resultPropName = resultPropName;
	}
}
