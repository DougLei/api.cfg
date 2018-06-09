package com.king.tooth.sys.entity.desc.resource.table;

import java.util.List;

/**
 * 表资源
 * @author DougLei
 */
public class TableResource {
	
	/**
	 * 表名
	 */
	private Object tableName;
	/**
	 * 资源名
	 */
	private Object resourceName;
	/**
	 * 列资源集合
	 */
	private List<ColumnResource> columns;
	
	
	public Object getTableName() {
		return tableName;
	}
	public void setTableName(Object tableName) {
		this.tableName = tableName;
	}
	public Object getResourceName() {
		return resourceName;
	}
	public void setResourceName(Object resourceName) {
		this.resourceName = resourceName;
	}
	public List<ColumnResource> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnResource> columns) {
		this.columns = columns;
	}
}
