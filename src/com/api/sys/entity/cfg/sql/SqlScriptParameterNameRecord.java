package com.api.sys.entity.cfg.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * sql脚本参数名的记录对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlScriptParameterNameRecord implements Serializable{
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
	
	/**
	 * 添加参数名集合
	 * @param parameterNames
	 */
	public void addParameterNames(List<String> parameterNames){
		if(this.parameterNames == null){
			this.parameterNames = new ArrayList<String>(parameterNames.size());
		}
		this.parameterNames.addAll(parameterNames);
	}
	
	public int getSqlIndex() {
		return sqlIndex;
	}
	public List<String> getParameterNames() {
		return parameterNames;
	}
}
