package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 资源信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysResource extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 引用的资源主键
	 */
	private String refResourceId;
	/**
	 * 资源类型
	 */
	private Integer resourceType;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 是否有效
	 */
	private Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 * <p>默认值：all</p>
	 */
	private String requestMethod;
	
	//-------------------------------------------------------------------------
	
	public String getResourceName() {
		return resourceName;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata refResourceIdColumn = new ComColumndata("ref_resource_id", BuiltinDataType.STRING, 32);
		refResourceIdColumn.setName("引用的资源主键");
		refResourceIdColumn.setComments("引用的资源主键");
		columns.add(refResourceIdColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", BuiltinDataType.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		columns.add(resourceNameColumn);
		
		ComColumndata resourceTypeColumn = new ComColumndata("resource_type", BuiltinDataType.INTEGER, 1);
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		columns.add(resourceTypeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toGetTableName());
		table.setName("资源信息表");
		table.setComments("资源信息表");
		table.setColumns(getColumnList());
		return table;
	}

	public String toGetTableName() {
		return "SYS_RESOURCE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysResource";
	}
	
	/**
	 * 是否是内置资源
	 * @return
	 */
	public boolean isBuiltinResource(){
		return BUILTIN_RESOURCE.equals(refResourceId);
	}
	/**
	 * 内置资源标识
	 * <p>系统内置资源的refResourceId的值为内置资源标识</p>
	 */
	private static final String BUILTIN_RESOURCE = "builtinResource";
}
