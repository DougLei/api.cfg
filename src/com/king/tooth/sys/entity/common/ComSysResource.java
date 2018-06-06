package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]系统资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSysResource extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 引用的资源主键
	 */
	private String refResourceId;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 资源类型
	 */
	private int resourceType;
	/**
	 * 请求资源的方式
	 * <p>get/put/post/delete/all(包括get/put/post/delete)/none(什么都不支持)</p>
	 */
	private String reqResourceMethod;
	/**
	 * 是否启用
	 */
	private int isEnabled;
	
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
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getReqResourceMethod() {
		return reqResourceMethod;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
	
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_SYS_RESOURCE");
		table.setName("[通用的]系统资源对象表");
		table.setComments("[通用的]系统资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(10);
		
		CfgColumndata refResourceIdColumn = new CfgColumndata("ref_resource_id");
		refResourceIdColumn.setName("引用的资源主键");
		refResourceIdColumn.setComments("引用的资源主键");
		refResourceIdColumn.setColumnType(DataTypeConstants.STRING);
		refResourceIdColumn.setLength(32);
		refResourceIdColumn.setOrderCode(1);
		columns.add(refResourceIdColumn);
		
		CfgColumndata resourceNameColumn = new CfgColumndata("resource_name");
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setColumnType(DataTypeConstants.STRING);
		resourceNameColumn.setLength(100);
		resourceNameColumn.setOrderCode(2);
		columns.add(resourceNameColumn);
		
		CfgColumndata resourceTypeColumn = new CfgColumndata("resource_type");
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		resourceTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		resourceTypeColumn.setLength(1);
		resourceTypeColumn.setOrderCode(3);
		columns.add(resourceTypeColumn);
		
		CfgColumndata reqResourceMethodColumn = new CfgColumndata("req_resource_method");
		reqResourceMethodColumn.setName("请求资源的方式");
		reqResourceMethodColumn.setComments("请求资源的方式:get/put/post/delete/all(包括get/put/post/delete)/none(什么都不支持)");
		reqResourceMethodColumn.setColumnType(DataTypeConstants.STRING);
		reqResourceMethodColumn.setLength(20);
		reqResourceMethodColumn.setOrderCode(4);
		columns.add(reqResourceMethodColumn);
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(5);
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		table.setReqResourceMethod(ISysResource.GET);
		table.setIsBuiltin(1);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_RESOURCE";
	}
	
	public String getEntityName() {
		return "ComSysResource";
	}
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("resourceType", resourceType+"");
		json.put("isEnabled", isEnabled+"");
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
