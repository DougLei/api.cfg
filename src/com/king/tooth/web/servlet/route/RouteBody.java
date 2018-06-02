package com.king.tooth.web.servlet.route;

import java.io.Serializable;

import com.king.tooth.util.StrUtils;

/**
 * 请求的资源体对象
 * <p>从请求uri的路径中，按照规则获取</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RouteBody implements Serializable{
	
	private static final RouteBodyAnalysis analysis = new RouteBodyAnalysis();
	
	public RouteBody(String requestUri) {
		analysis.doAnalysis(requestUri, this);
	}
	
	/**
	 * 路由规则的全局唯一标识
	 * <p>如：1_null、2_Counter等</p>
	 */
	private String routeRuleIdentity;
	/**
	 * 父资源类型
	 * <p>例如：SinoForce.Data.AppUser</p>
	 */
	private String parentResourceType;
	/**
	 * 父资源名
	 * <p>例如：AppUser</p>
	 */
	private String parentResourceName;
	/**
	 * 父资源主键ID
	 */
	private String parentId;
	/**
	 * 资源类型
	 * <p>例如：SinoForce.Data.AppUser</p>
	 */
	private String resourceType;
	/**
	 * 资源名
	 * <p>例如：AppUser</p>
	 */
	private String resourceName;
	/**
	 * 资源主键ID
	 */
	private String resourceId;
	/**
	 * 属性名
	 * <p>例如：UserType</p>
	 */
	private String propName;
	
	
	/**
	 * 从资源类型，获得资源名称
	 * <p>例如：SinoForce.Data.AppUser中获取AppUser</p>
	 * @param resourceType
	 * @return
	 */
	private String getResourceName(String resourceType){
		String resourceName = RouteMapping.getRouteResource(resourceType);
		if(StrUtils.isEmpty(resourceName)){
			resourceName = resourceType.substring(resourceType.lastIndexOf(".")+1);
			RouteMapping.setRouteResource(resourceType, resourceName);
		}
		return resourceName;
	}
	
	public String getParentResourceType() {
		return parentResourceType;
	}
	public String getParentResourceName() {
		return parentResourceName;
	}
	public String getParentId() {
		return parentId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public String getResourceName() {
		return resourceName;
	}
	public String getResourceId() {
		return resourceId;
	}
	public String getPropName() {
		return propName;
	}
	
	public void setParentResourceType(String parentResourceType) {
		this.parentResourceType = parentResourceType;
		this.parentResourceName = getResourceName(this.parentResourceType);
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
		this.resourceName = getResourceName(this.resourceType);
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getRouteRuleIdentity() {
		return routeRuleIdentity;
	}
	public void setRouteRuleIdentity(String routeRuleIdentity) {
		this.routeRuleIdentity = routeRuleIdentity;
	}
}
