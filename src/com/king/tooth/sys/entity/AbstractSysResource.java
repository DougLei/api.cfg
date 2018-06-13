package com.king.tooth.sys.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.util.DateUtil;

/**
 * 系统资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class AbstractSysResource extends BasicEntity implements ISysResource, IPublish{
	/**
	 * 资源是否有效
	 */
	protected Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 */
	protected String reqResourceMethod;
	/**
	 * 是否内置资源
	 * <p>这个字段由开发人员控制，不开放给用户</p>
	 */
	protected Integer isBuiltin;
	/**
	 * 资源是否需要发布
	 */
	protected Integer isNeedDeploy;
	/**
	 * 资源是否需要补发布
	 * <p>例如，当配置平台开发出来一个新的运行平台通用功能时，将这个字段值改为1，去给所有已经发布的项目，补发增加新的功能</p>
	 */
	protected Integer isNeedRedeploy;
	/**
	 * 资源是否被创建
	 * <p>在配置平台中，主要是给平台开发人员使用，也是标识表资源是否被加载到sessionFactory中</p>
	 * <p>在运行平台中，这个字段标识资源是否被加载，主要是指表资源是否被加载到sessionFactory中</p>
	 * <p>针对说明：数据库/项目，在配置平台为0，发布后，值改为1，取消发布后，值改回0</p>
	 */
	protected Integer isCreated;
	
	// -----------------------------------------------------------------
	
	/**
	 * 批量发布时的信息记录
	 * 该字段在批量发布的时候用到，存储发布每个数据的发布结果，例如是否已经发布？，是否无效而不能发布等信息
	 */
	@JSONField(serialize = false)
	private String batchPublishMsg;
	
	/**
	 * 转换为资源对象
	 */
	public ComSysResource turnToResource(){
		ComSysResource resource = new ComSysResource();
		resource.setRefResourceId(id);
		resource.setIsEnabled(isEnabled);
		resource.setReqResourceMethod(reqResourceMethod);
		resource.setIsBuiltin(isBuiltin);
		resource.setIsNeedDeploy(isNeedDeploy);
		if(isBuiltin !=null && isBuiltin == 1){
			resource.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		}else{
			resource.setValidDate(DateUtil.parseDate("2019-12-31 23:59:59"));
		}
		return resource;
	}
	
	public ComPublishInfo turnToPublish() {
		// 这些字段值，是发布到远程数据库表中的数据
		this.isBuiltin = 0;
		this.isNeedDeploy = 0;
		Date publishDate = new Date();
		this.createTime = publishDate;
		String userId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
		this.createUserId = userId;
		this.lastUpdateTime = publishDate;
		this.lastUpdatedUserId = userId;
		return null;
	}
	
	/**
	 * 处理资源对象的属性
	 * @param entityJson
	 */
	public void processSysResourceProps(EntityJson entityJson){
		super.processBasicEntityProps(entityJson);
		entityJson.put("isEnabled", isEnabled);
		entityJson.put("isBuiltin", isBuiltin);
		entityJson.put("isNeedDeploy", isNeedDeploy);
		entityJson.put("isNeedRedeploy", isNeedRedeploy);
		entityJson.put("isCreated", isCreated);
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Integer getIsNeedDeploy() {
		return isNeedDeploy;
	}
	public void setIsNeedDeploy(Integer isNeedDeploy) {
		this.isNeedDeploy = isNeedDeploy;
	}
	public Integer getIsBuiltin() {
		return isBuiltin;
	}
	public void setIsBuiltin(Integer isBuiltin) {
		this.isBuiltin = isBuiltin;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
	public String getReqResourceMethod() {
		return reqResourceMethod;
	}
	public Integer getIsCreated() {
		return isCreated;
	}
	public void setIsCreated(Integer isCreated) {
		this.isCreated = isCreated;
	}
	public String getBatchPublishMsg() {
		return batchPublishMsg;
	}
	public void setBatchPublishMsg(String batchPublishMsg) {
		this.batchPublishMsg = batchPublishMsg;
	}
	public Integer getIsNeedRedeploy() {
		return isNeedRedeploy;
	}
	public void setIsNeedRedeploy(Integer isNeedRedeploy) {
		this.isNeedRedeploy = isNeedRedeploy;
	}
}
