package com.king.tooth.sys.entity;

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
	 * 资源是否被创建
	 * <p>在配置平台中，如果是内置，且是属于[配置平台/通用的]的资源，这个字段才有效，主要是给平台开发人员使用，也是标识表资源是否被加载到sessionFactory中</p>
	 * <p>在运行平台中，这个字段标识资源是否被加载，主要是指表资源是否被加载到sessionFactory中</p>
	 */
	protected Integer isCreated;
	
	// -----------------------------------------------------------------
	
	/**
	 * 转换为资源对象
	 */
	public ComSysResource turnToResource(){
		ComSysResource resource = new ComSysResource();
		resource.setRefResourceId(id);
		resource.setIsEnabled(isEnabled);
		resource.setReqResourceMethod(getReqResourceMethod());
		resource.setIsBuiltin(isBuiltin);
		resource.setIsNeedDeploy(isNeedDeploy);
//		if(isBuiltin !=null && isBuiltin == 1){
//			resource.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
//		}
		resource.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		return resource;
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
}
