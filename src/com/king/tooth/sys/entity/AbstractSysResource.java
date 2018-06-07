package com.king.tooth.sys.entity;

import java.util.Date;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.util.DateUtil;

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
	 * 资源有效期
	 */
	protected Date validDate;
	/**
	 * 资源是否需要发布
	 */
	protected int isNeedDeploy;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 */
	protected String reqResourceMethod = ALL;
	/**
	 * 是否内置资源
	 */
	protected int isBuiltin = 0;
	/**
	 * 资源所属于的平台类型
	 * <p>1:配置平台、2:运行平台、3:公用</p>
	 */
	protected int platformType;
	/**
	 * 是否已经创建资源
	 */
	protected int isCreatedResource;
	
	// -----------------------------------------------------------------
	
	/**
	 * 转换为资源对象
	 */
	public ComSysResource turnToResource(){
		ComSysResource resource = new ComSysResource();
		resource.setIsEnabled(isEnabled);
		resource.setValidDate(validDate);
		resource.setIsNeedDeploy(isNeedDeploy);
		resource.setReqResourceMethod(getReqResourceMethod());
		resource.setIsBuiltin(isBuiltin);
		resource.setPlatformType(platformType);
		resource.setIsCreatedResource(isCreatedResource);
		return resource;
	}
	
	
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Date getValidDate() {
		if(validDate == null && isBuiltin == 1){
			validDate = DateUtil.parseDate("2099-12-31 23:59:59");
		}
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
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
	public int getPlatformType() {
		return platformType;
	}
	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}
	public int getIsCreatedResource() {
		return isCreatedResource;
	}
	public void setIsCreatedResource(int isCreatedResource) {
		this.isCreatedResource = isCreatedResource;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
	public String getReqResourceMethod() {
		return reqResourceMethod;
	}
	
	// -----------------------------------------------------------------
	/**
	 * 是否被解析过
	 */
	protected boolean isAnalysed;
}
