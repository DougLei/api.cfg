package com.king.tooth.web.builtin.method.tableresource;

import java.util.List;

import com.king.tooth.web.builtin.method.AbstractBuiltinMethodProcesser;


/**
 * 内置函数处理器类的抽象类，抽取具体实现类的共同属性和方法
 * @author DougLei
 */
public abstract class AbstractTableResourceBuiltinMethodProcesser extends AbstractBuiltinMethodProcesser{

	protected abstract StringBuilder getHql();
	
	public StringBuilder getDBScriptStatement(){
		return getHql();
	}
	
	/**
	 * 拼接后的hql语句
	 * <p>针对表资源处理使用到的</p>
	 */
	protected StringBuilder hql = new StringBuilder();
	
	/**
	 * 查询条件的参数值集合
	 * <p>存储查询语句中?对应的值</p>
	 */
	protected List<Object> hqlParameterValues;

	public void setHqlParameterValues(List<Object> hqlParameterValues) {
		this.hqlParameterValues = hqlParameterValues;
	}
}
