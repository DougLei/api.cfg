package com.king.tooth.web.servlet.route;

import java.io.Serializable;

import com.king.tooth.plugins.thread.CurrentThreadContext;

/**
 * 请求的资源体对象
 * <p>从请求uri的路径中，按照规则获取</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RouteBody implements Serializable{
	
	private static final RouteBodyAnalysis analysis = new RouteBodyAnalysis();
	
	public RouteBody() {
	}
	public RouteBody(String requestUri) {
		analysis.doAnalysis(requestUri, this);
		
		// 记录日志，请求的资源名
		if(resourceName != null){
			CurrentThreadContext.getReqLogData().getReqLog().setResourceName(resourceName);
		}
		if(parentResourceName != null){
			CurrentThreadContext.getReqLogData().getReqLog().setParentResourceName(parentResourceName);
		}
	}
	
	/**
	 * 路由规则的全局唯一标识
	 * <p>如：1_null、2_Counter等</p>
	 */
	private String routeRuleIdentity;
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
	 * 是否请求的是一个action(动作)
	 */
	private boolean isAction;
	/**
	 * 调用的action(动作)名称
	 */
	private String actionName;
	
	public String getParentResourceName() {
		return parentResourceName;
	}
	public String getParentId() {
		return parentId;
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
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public void setParentResourceName(String parentResourceName) {
		this.parentResourceName = parentResourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public boolean isAction() {
		return isAction;
	}
	public void setIsAction(boolean isAction) {
		this.isAction = isAction;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
