package com.king.tooth.sys.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 基础实体类资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class BasicEntity implements Serializable, ITable{
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
	protected String lastUpdateUserId;
	/**
	 * 最后修改时间
	 */
	protected Date lastUpdateDate;
	
	public JSONObject toEntityJson() {
		return JsonUtil.toJsonObject(this);
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
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
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
		if(StrUtils.isEmpty(customerId)){
			customerId = "unknow";
		}
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
