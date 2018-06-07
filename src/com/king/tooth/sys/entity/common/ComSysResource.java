package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
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
public class ComSysResource extends AbstractSysResource implements ITable, IEntity{
	
	/**
	 * 引用的资源主键
	 */
	private String refResourceId;
	/**
	 * 资源类型
	 */
	private int resourceType;
	/**
	 * 资源名
	 */
	private String resourceName;
	
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
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_SYS_RESOURCE", 0);
		table.setIsResource(1);
		table.setName("[通用的]系统资源对象表");
		table.setComments("[通用的]系统资源对象表");
		table.setReqResourceMethod(ISysResource.GET);
		table.setIsBuiltin(1);
		table.setPlatformType(ISysResource.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(16);
		
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
		resourceNameColumn.setLength(60);
		resourceNameColumn.setOrderCode(2);
		columns.add(resourceNameColumn);
		
		CfgColumndata resourceTypeColumn = new CfgColumndata("resource_type");
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		resourceTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		resourceTypeColumn.setLength(1);
		resourceTypeColumn.setOrderCode(3);
		columns.add(resourceTypeColumn);
		
		table.setColumns(columns);
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
		json.put("validDate", validDate);
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("platformType", platformType+"");
		json.put("isCreatedResource", isCreatedResource+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
	
	public void analysisResourceData() {
	}
	
	public ComSysResource turnToResource() {
		if(this == null){
			throw new NullPointerException("当前资源对象为空");
		}
		return this;
	}
}
