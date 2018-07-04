package com.king.tooth.sys.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourceNameConstants;

/**
 * 基础实体类资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BasicEntity implements Serializable{
	/**
	 * 主键
	 */
	@JSONField(name = "Id")
	protected String id;
	/**
	 * 所属租户主键
	 */
	protected String customerId;
	/**
	 * 所属项目主键
	 */
	protected String projectId;
	/**
	 * 创建人主键
	 */
	protected String createUserId;
	/**
	 * 创建时间
	 */
	protected Date createDate;
	/**
	 * 最后修改人主键
	 */
	protected String lastUpdatedUserId;
	/**
	 * 最后修改时间
	 */
	protected Date lastUpdateDate;
	
	/**
	 * 处理基础资源对象的属性
	 * @param entityJson
	 */
	protected void processBasicEntityProps(EntityJson entityJson){
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("createDate", createDate);
		entityJson.put("lastUpdateDate", lastUpdateDate);
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
