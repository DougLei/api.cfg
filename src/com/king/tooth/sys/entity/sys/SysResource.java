package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishBasicData;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 资源信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysResource extends AbstractSysResource implements ITable{
	
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_RESOURCE", 0);
		table.setName("资源信息表");
		table.setComments("资源信息表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata refResourceIdColumn = new ComColumndata("ref_resource_id", BuiltinCodeDataType.STRING, 32);
		refResourceIdColumn.setName("引用的资源主键");
		refResourceIdColumn.setComments("引用的资源主键");
		refResourceIdColumn.setOrderCode(1);
		columns.add(refResourceIdColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", BuiltinCodeDataType.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setOrderCode(2);
		columns.add(resourceNameColumn);
		
		ComColumndata resourceTypeColumn = new ComColumndata("resource_type", BuiltinCodeDataType.INTEGER, 1);
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		resourceTypeColumn.setOrderCode(3);
		columns.add(resourceTypeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_RESOURCE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysResource";
	}
	
	public void analysisResourceData() {
	}
	
	public SysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	public ComPublishInfo turnToPublish() {
		throw new IllegalArgumentException("该资源目前不支持turnToPublish功能");
	}
	
	/**
	 * 转换为要发布的基础数据资源对象
	 * @return
	 */
	public ComPublishBasicData turnToPublishBasicData(Integer belongPlatformType){
		ComPublishBasicData publishBasicData = new ComPublishBasicData();
		publishBasicData.setBasicDataResourceName(getEntityName());
		publishBasicData.setBasicDataJsonStr(JSONObject.toJSONString(this));
		publishBasicData.setBelongPlatformType(belongPlatformType);
		return publishBasicData;
	}
}
