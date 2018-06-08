package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.common.ComSysResource;

/**
 * 系统资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class AbstractSysResource extends BasicEntity implements ISysResource{
	/**
	 * 资源是否有效
	 */
	protected int isEnabled = 1;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 */
	protected String reqResourceMethod = ALL;
	/**
	 * 是否内置资源
	 * <p>这个字段由开发人员控制，不开放给用户</p>
	 */
	protected int isBuiltin = 0;
	/**
	 * 资源是否需要发布
	 */
	protected int isNeedDeploy = 1;
	/**
	 * 资源是否发布
	 */
	protected int isDeployed;
	
	// -----------------------------------------------------------------
	
	/**
	 * 转换为资源对象
	 */
	public ComSysResource turnToResource(){
		ComSysResource resource = new ComSysResource();
		resource.setIsEnabled(isEnabled);
		resource.setReqResourceMethod(getReqResourceMethod());
		resource.setIsBuiltin(isBuiltin);
		resource.setIsNeedDeploy(isNeedDeploy);
		resource.setIsDeployed(isDeployed);
		return resource;
	}
	
	
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public int getIsNeedDeploy() {
		return isNeedDeploy;
	}
	public void setIsNeedDeploy(int isNeedDeploy) {
		this.isNeedDeploy = isNeedDeploy;
	}
	public int getIsBuiltin() {
		return isBuiltin;
	}
	public void setIsBuiltin(int isBuiltin) {
		this.isBuiltin = isBuiltin;
	}
	public int getIsDeployed() {
		return isDeployed;
	}
	public void setIsDeployed(int isDeployed) {
		this.isDeployed = isDeployed;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
	public String getReqResourceMethod() {
		return reqResourceMethod;
	}
}
