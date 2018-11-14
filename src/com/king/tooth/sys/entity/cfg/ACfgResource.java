package com.king.tooth.sys.entity.cfg;

import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.util.StrUtils;

/**
 * 资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class ACfgResource extends BasicEntity implements ICfgResource{
	
	/**
	 * 汉字描述名称
	 */
	private String name;
	/**
	 * 资源模型的资源名称
	 */
	protected String resourceName;
	/**
	 * 是否被创建
	 * <p>默认值为0</p>
	 * <p>[针对CfgTable资源描述]该字段在建模时，值改为1，后续修改字段信息等，该值均不变，只有在取消建模时，才会改为0</p>
	 */
	protected Integer isCreated;
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	protected Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none表示都不支持</p>
	 * <p>默认值：all</p>
	 */
	protected String requestMethod;
	/**
	 * 备注
	 */
	private String remark;
	
	
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = resourceName;
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIsCreated() {
		return isCreated;
	}
	public void setIsCreated(Integer isCreated) {
		this.isCreated = isCreated;
	}
	public Integer getIsEnabled() {
		if(isEnabled == null){
			isEnabled=1;
		}
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	public CfgResource turnToResource() {
		CfgResource resource = new CfgResource();
		resource.setRefResourceId(id);
		resource.setResourceName(resourceName);
		resource.setIsEnabled(getIsEnabled());
		resource.setRequestMethod(requestMethod);
		return resource;
	}
	
	public boolean isUpdateResourceInfo(ICfgResource oldResource) {
		if(!oldResource.getResourceName().equals(this.getResourceName()) 
				|| !oldResource.getRequestMethod().equals(this.getRequestMethod()) 
				|| oldResource.getIsEnabled() != this.getIsEnabled()){
			return true;
		}
		return false;
	}
}
