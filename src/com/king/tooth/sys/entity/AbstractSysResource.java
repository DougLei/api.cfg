package com.king.tooth.sys.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 系统资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class AbstractSysResource extends BasicEntity implements ISysResource, IEntity{
	/**
	 * 资源是否有效
	 * <p>默认值：1</p>
	 */
	protected Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 * <p>默认值：all</p>
	 */
	protected String reqResourceMethod;
	/**
	 * 是否内置资源
	 * <p>这个字段由后端开发人员控制，不开放给用户和前端开发</p>
	 * <p>默认值：0</p>
	 */
	protected Integer isBuiltin;
	/**
	 * 资源是否需要发布
	 * <p>默认值：1</p>
	 */
	protected Integer isNeedDeploy;
	/**
	 * 资源所属的平台类型
	 * <p>1：配置平台、2：运行平台、3：通用</p>
	 * <p>后期开发的功能，如果是每个项目都需要的(基础功能)，则用这个字段控制是否要发布</p>
	 * <p>和isBuiltin有类似的作用，开放给前端开发使用，但还是不开放给用户</p>
	 * <p>isBuiltin控制的是系统内置的资源，belongPlatformType控制的是系统外置的资源</p>
	 * <p>@see ISysResource</p>
	 * <p>默认值：2</p>
	 */
	protected Integer belongPlatformType;
	/**
	 * 资源是否被创建
	 * <p>在配置平台中，主要是给平台开发人员使用，也是标识表资源是否被加载到sessionFactory中</p>
	 * <p>在运行平台中，这个字段标识资源是否被加载，主要是指表资源是否被加载到sessionFactory中</p>
	 * <p>针对说明：数据库/项目，在配置平台为0，发布后，值改为1，取消发布后，值改回0</p>
	 * <p>即在系统启动的时候用来判断该资源是否需要加载</p>
	 * <p>默认值：0</p>
	 */
	protected Integer isCreated;
	/**
	 * 引用的数据主键
	 * <p>发布数据到运行平台时，将发布的数据id(在配置平台中的)保存到这个字段中，然后在运行平台重新创建一个新的id去保存数据</p>
	 */
	protected String refDataId;
	
	// -----------------------------------------------------------------
	
	/**
	 * 批量发布时的信息记录
	 * 该字段在批量发布的时候用到，存储发布每个数据的发布结果，例如是否已经发布？，是否无效而不能发布等(错误/异常)信息
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
		resource.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		return resource;
	}
	
	public ComPublishInfo turnToPublish() {
		// 这些字段值，是发布到远程数据库表中的数据
		this.isBuiltin = 0;
		this.isNeedDeploy = 0;
		Date publishDate = new Date();
		this.createDate = publishDate;
		String userId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
		this.createUserId = userId;
		this.lastUpdateDate = publishDate;
		this.lastUpdatedUserId = userId;
		return null;
	}
	
	public JSONObject toPublishEntityJson(String projectId) {
		JSONObject json = toEntityJson();
		json.put("refDataId", json.getString(ResourceNameConstants.ID));
		json.put(ResourceNameConstants.ID, ResourceHandlerUtil.getIdentity());
		json.put(ResourceNameConstants.PROJECT_ID, projectId);
		return json;
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
		entityJson.put("belongPlatformType", belongPlatformType);
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
	public Integer getBelongPlatformType() {
		return belongPlatformType;
	}
	public void setBelongPlatformType(Integer belongPlatformType) {
		this.belongPlatformType = belongPlatformType;
	}
	public String getRefDataId() {
		return refDataId;
	}
	public void setRefDataId(String refDataId) {
		this.refDataId = refDataId;
	}
}
