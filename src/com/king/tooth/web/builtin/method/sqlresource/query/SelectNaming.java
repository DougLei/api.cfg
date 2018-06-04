package com.king.tooth.web.builtin.method.sqlresource.query;

import java.io.Serializable;

/**
 * 查询名称对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SelectNaming implements Serializable{
	/**
	 * 查询的名称
	 */
	private String selectName;
	/**
	 * 查询的别名
	 */
	private String selectAliasName;
	
	public String getSelectName() {
		return selectName;
	}
	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}
	public String getSelectAliasName() {
		return selectAliasName;
	}
	public void setSelectAliasName(String selectAliasName) {
		this.selectAliasName = selectAliasName;
	}
}
