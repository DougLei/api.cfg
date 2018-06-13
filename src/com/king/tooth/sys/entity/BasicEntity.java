package com.king.tooth.sys.entity;

import java.io.Serializable;
import java.util.Date;

import com.king.tooth.constants.ResourceNameConstants;

/**
 * 基础实体类资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BasicEntity implements Serializable{
	/**
	 * 所属项目主键
	 */
	protected String projectId;
	/**
	 * 主键
	 */
	protected String id;
	/**
	 * 创建人主键
	 */
	protected String createUserId;
	/**
	 * 最后修改人主键
	 */
	protected String lastUpdatedUserId;
	/**
	 * 创建时间
	 */
	protected Date createTime;
	/**
	 * 最后修改时间
	 */
	protected Date lastUpdateTime;
	
	/**
	 * 处理基础资源对象的属性
	 * @param entityJson
	 */
	public void processBasicEntityProps(EntityJson entityJson){
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		entityJson.put(ResourceNameConstants.LAST_UPDATE_TIME, lastUpdateTime);
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	// --------
	/**
	 * 是否验证了不能为空的属性
	 * <p>针对IEntityPropAnalysis接口的实现类</p>
	 */
	protected boolean isValidNotNullProps;
	/**
	 * 验证不能为空的属性的结果
	 * <p>针对IEntityPropAnalysis接口的实现类</p>
	 */
	protected String validNotNullPropsResult;
}
