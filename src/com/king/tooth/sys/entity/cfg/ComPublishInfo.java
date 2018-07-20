package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;

/**
 * 系统发布信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPublishInfo extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 发布的数据库主键
	 */
	private String publishDatabaseId;
	/**
	 * 发布的项目主键
	 */
	private String publishProjectId;
	/**
	 * 发布的资源主键
	 */
	private String publishResourceId;
	/**
	 * 发布的资源名
	 * <p>冗余字段</p>
	 */
	private String publishResourceName;
	/**
	 * 发布的资源类型
	 */
	private Integer resourceType;
	/**
	 * 是否成功
	 */
	private Integer isSuccess;
	/**
	 * 错误消息
	 */
	private String errMsg;
	
	//----------------------------------------------------------------
	
	public String getPublishDatabaseId() {
		return publishDatabaseId;
	}
	public void setPublishDatabaseId(String publishDatabaseId) {
		this.publishDatabaseId = publishDatabaseId;
	}
	public String getPublishProjectId() {
		return publishProjectId;
	}
	public void setPublishProjectId(String publishProjectId) {
		this.publishProjectId = publishProjectId;
	}
	public String getPublishResourceId() {
		return publishResourceId;
	}
	public void setPublishResourceId(String publishResourceId) {
		this.publishResourceId = publishResourceId;
	}
	public String getPublishResourceName() {
		return publishResourceName;
	}
	public void setPublishResourceName(String publishResourceName) {
		this.publishResourceName = publishResourceName;
	}
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PUBLISH_INFO", 0);
		table.setName("系统发布信息资源对象表");
		table.setComments("系统发布信息资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.CONFIG_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(14);
		
		ComColumndata publishDatabaseIdColumn = new ComColumndata("publish_database_id", BuiltinCodeDataType.STRING, 32);
		publishDatabaseIdColumn.setName("发布的数据库主键");
		publishDatabaseIdColumn.setComments("发布的数据库主键");
		publishDatabaseIdColumn.setOrderCode(1);
		columns.add(publishDatabaseIdColumn);
		
		ComColumndata publishProjectIdColumn = new ComColumndata("publish_project_id", BuiltinCodeDataType.STRING, 32);
		publishProjectIdColumn.setName("发布的项目主键");
		publishProjectIdColumn.setComments("发布的项目主键");
		publishProjectIdColumn.setOrderCode(2);
		columns.add(publishProjectIdColumn);
		
		ComColumndata publishResourceIdColumn = new ComColumndata("publish_resource_id", BuiltinCodeDataType.STRING, 32);
		publishResourceIdColumn.setName("发布的资源主键");
		publishResourceIdColumn.setComments("发布的资源主键");
		publishResourceIdColumn.setOrderCode(3);
		columns.add(publishResourceIdColumn);
		
		ComColumndata publishResourceNameColumn = new ComColumndata("publish_resource_name", BuiltinCodeDataType.STRING, 60);
		publishResourceNameColumn.setName("发布的资源名");
		publishResourceNameColumn.setComments("发布的资源名:冗余字段");
		publishResourceNameColumn.setOrderCode(4);
		columns.add(publishResourceNameColumn);
		
		ComColumndata resourceTypeColumn = new ComColumndata("resource_type", BuiltinCodeDataType.INTEGER, 1);
		resourceTypeColumn.setName("发布的资源类型");
		resourceTypeColumn.setComments("发布的资源类型");
		resourceTypeColumn.setIsNullabled(0);
		resourceTypeColumn.setOrderCode(5);
		columns.add(resourceTypeColumn);
		
		ComColumndata isSuccessColumn = new ComColumndata("is_success", BuiltinCodeDataType.INTEGER, 1);
		isSuccessColumn.setName("是否成功");
		isSuccessColumn.setComments("是否成功");
		isSuccessColumn.setDefaultValue("0");
		isSuccessColumn.setOrderCode(6);
		columns.add(isSuccessColumn);
		
		ComColumndata errMsgColumn = new ComColumndata("err_msg", BuiltinCodeDataType.STRING, 1000);
		errMsgColumn.setName("错误消息");
		errMsgColumn.setComments("错误消息");
		errMsgColumn.setOrderCode(7);
		columns.add(errMsgColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_PUBLISH_INFO";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComPublishInfo";
	}
}
