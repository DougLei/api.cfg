package com.king.tooth.web.builtin.method.sqlresource;

import java.util.List;

import com.king.tooth.web.builtin.method.AbstractBuiltinMethodProcesser;


/**
 * 内置函数处理器类的抽象类，抽取具体实现类的共同属性和方法
 * @author DougLei
 */
public abstract class AbstractSqlResourceBuiltinMethodProcesser extends AbstractBuiltinMethodProcesser{

	protected abstract StringBuilder getSql();
	
	public StringBuilder getDBScriptStatement(){
		return getSql();
	}
	
	/**
	 * 拼接后的sql语句
	 * <p>针对sql资源处理使用到的</p>
	 */
	protected StringBuilder sql = new StringBuilder();
	
	/**
	 * sql参数值集合
	 */
	protected List<List<Object>> sqlParameterValues;

	public void setSqlParameterValues(List<List<Object>> sqlParameterValues) {
		this.sqlParameterValues = sqlParameterValues;
	}
}
