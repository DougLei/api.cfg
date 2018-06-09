package com.king.tooth.sys.entity.desc.resource.table;

/**
 * 列资源
 * @author DougLei
 */
public class ColumnResource {
	
	/**
	 * 列名
	 */
	private Object columnName;
	/**
	 * 属性名
	 */
	private Object propName;
	/**
	 * 注释
	 */
	private Object comments;

	
	public Object getColumnName() {
		return columnName;
	}
	public void setColumnName(Object columnName) {
		this.columnName = columnName;
	}
	public Object getPropName() {
		return propName;
	}
	public void setPropName(Object propName) {
		this.propName = propName;
	}
	public Object getComments() {
		return comments;
	}
	public void setComments(Object comments) {
		this.comments = comments;
	}
}
