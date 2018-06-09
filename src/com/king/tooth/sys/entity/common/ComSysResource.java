package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;

/**
 * 系统资源对象
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
	private Integer resourceType;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 资源有效期
	 */
	private Date validDate;
	
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
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_SYS_RESOURCE", 0);
		table.setIsResource(1);
		table.setVersion(1);
		table.setName("系统资源对象表");
		table.setComments("系统资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(16);
		
		ComColumndata refResourceIdColumn = new ComColumndata("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("引用的资源主键");
		refResourceIdColumn.setComments("引用的资源主键");
		refResourceIdColumn.setOrderCode(1);
		columns.add(refResourceIdColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", DataTypeConstants.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setOrderCode(2);
		columns.add(resourceNameColumn);
		
		ComColumndata resourceTypeColumn = new ComColumndata("resource_type", DataTypeConstants.INTEGER, 1);
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		resourceTypeColumn.setOrderCode(3);
		columns.add(resourceTypeColumn);
		
		ComColumndata validDateColumn = new ComColumndata("valid_date", DataTypeConstants.DATE, 0);
		validDateColumn.setName("资源有效期");
		validDateColumn.setComments("资源有效期");
		validDateColumn.setOrderCode(4);
		columns.add(validDateColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_RESOURCE";
	}
	
	public String getEntityName() {
		return "ComSysResource";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("resourceType", resourceType);
		entityJson.put("isEnabled", isEnabled);
		entityJson.put("validDate", validDate);
		entityJson.put("isBuiltin", isBuiltin);
		entityJson.put("isNeedDeploy", isNeedDeploy);
		entityJson.put("isDeployed", isDeployed);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
	
	public void analysisResourceData() {
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
}
