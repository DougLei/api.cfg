package com.king.tooth.sys.entity.cfg;

import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.sys.SysResource;

/**
 * 资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class ASysResource extends BasicEntity implements ISysResource{
	/**
	 * 资源模型的资源名称
	 */
	protected String resourceName;
	/**
	 * 是否被创建
	 * <p>默认值为0</p>
	 * <p>该字段在建模时，值改为1，后续修改字段信息等，该值均不变，只有在取消建模时，才会改为0</p>
	 */
	protected Integer isCreated;
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	protected Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 * <p>默认值：all</p>
	 */
	protected String requestMethod;
	
	public Integer getIsCreated() {
		return isCreated;
	}
	public void setIsCreated(Integer isCreated) {
		this.isCreated = isCreated;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public SysResource turnToResource() {
		SysResource resource = new SysResource();
		resource.setRefResourceId(id);
		resource.setResourceName(resourceName);
		resource.setIsEnabled(isEnabled);
		resource.setRequestMethod(requestMethod);
		return resource;
	}
	
	public boolean isUpdateResourceInfo(ISysResource oldResource) {
		ASysResource oldASysResource = (ASysResource) oldResource;
		if(!oldASysResource.getResourceName().equals(this.getResourceName()) || !oldASysResource.getRequestMethod().equals(this.getRequestMethod()) || oldASysResource.getIsEnabled() != this.getIsEnabled()){
			return true;
		}
		return false;
	}
}
