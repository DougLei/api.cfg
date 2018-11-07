package com.king.tooth.web.servlet.route;

import java.io.Serializable;

import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

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
	 * 是否是代码
	 */
	private boolean isCode;
	/**
	 * 请求的code uri
	 */
	private String codeUri;
	
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
	public boolean getIsCode() {
		return isCode;
	}
	public void setIsCode(boolean isCode) {
		this.isCode = isCode;
	}
	public String getCodeUri() {
		return codeUri;
	}
	public void setCodeUri(String codeUri) {
		this.codeUri = codeUri;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getRouteRuleIdentity() {
		return routeRuleIdentity;
	}
	public void setRouteRuleIdentity(String routeRuleIdentity) {
		this.routeRuleIdentity = routeRuleIdentity;
	}
	public void setParentResourceName(String parentResourceName) {
		this.parentResourceName = parentResourceName;
		if(StrUtils.notEmpty(parentResourceName)){
			CurrentThreadContext.getReqLogData().getReqLog().setParentResourceName(parentResourceName);
		}
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		if(StrUtils.notEmpty(resourceName)){
			CurrentThreadContext.getReqLogData().getReqLog().setResourceName(resourceName);
		}
	}
}
