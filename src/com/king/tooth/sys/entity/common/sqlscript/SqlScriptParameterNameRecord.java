package com.king.tooth.sys.entity.common.sqlscript;

import java.util.ArrayList;
import java.util.List;

/**
 * sql脚本参数名的记录对象
 * @author DougLei
 */
public class SqlScriptParameterNameRecord {
	/**
	 * 第几个sql语句
	 */
	private int sqlIndex;
	/**
	 * 参数名集合
	 */
	private List<String> parameterNames;
	
	public SqlScriptParameterNameRecord() {
	}
	public SqlScriptParameterNameRecord(int sqlIndex) {
		this.sqlIndex = sqlIndex;
	}
	
	/**
	 * 添加参数名称
	 * @param parameterName
	 */
	public void addParameterName(String parameterName){
		if(parameterNames == null){
			parameterNames = new ArrayList<String>();
		}
		parameterNames.add(parameterName);
	}
	
	public int getSqlIndex() {
		return sqlIndex;
	}
	public List<String> getParameterNames() {
		return parameterNames;
	}
}
