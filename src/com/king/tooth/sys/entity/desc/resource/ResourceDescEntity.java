package com.king.tooth.sys.entity.desc.resource;

import java.io.Serializable;

import com.king.tooth.sys.entity.desc.resource.table.TableResource;

/**
 * 资源描述实体
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ResourceDescEntity implements Serializable {
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 资源请求的方式
	 */
	private String reqResourceMethods;
	/**
	 * 表资源对象
	 */
	private TableResource table;
	
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getReqResourceMethods() {
		return reqResourceMethods;
	}
	public void setReqResourceMethods(String reqResourceMethods) {
		this.reqResourceMethods = reqResourceMethods;
	}
	public TableResource getTable() {
		return table;
	}
	public void setTable(TableResource table) {
		this.table = table;
	}
}
