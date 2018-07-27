package com.king.tooth.web.entity.request;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.web.servlet.route.RouteBody;

/**
 * 请求体，获取并封装客户端的请求
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RequestBody implements Serializable{
	/**
	 * httpServletRequest请求对象
	 */
	private HttpServletRequest request;

	/**
	 * 请求的路由对象
	 */
	private RouteBody routeBody;
	
	/**
	 * 请求体
	 */
	private IJson formData;
	
	/**
	 * 请求的url参数键值对
	 */
	private Map<String, String> requestUrlParams;
	
	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 请求的资源信息
	 */
	private ResourceInfo resourceInfo;
	
	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 请求的资源元数据
	 */
	private ResourceMetadataInfo resourceMetadataInfo;
	
	// -------------------------------------------------------------------------------------------------------------------------
	public RequestBody() {
	}
	public RequestBody(HttpServletRequest request) {
		this.request = request;
		analysisRouteBody();
		analysisResource();
	}
	
	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 解析routeBody
	 * <p>路由体</p>
	 */
	private void analysisRouteBody() {
		/*
		 *  将项目名、servlet名都取消掉，减少uri中不必要的数据
		 *  再根据uri，解析出RouteBody
		 */
		String requestUri = request.getRequestURI().replace(
				request.getContextPath() + request.getServletPath() + "/", "");
		this.routeBody = new RouteBody(requestUri);
	}
	
	/**
	 * 解析请求的资源信息
	 */
	private void analysisResource() {
		resourceInfo = new ResourceInfo(this);
		resourceMetadataInfo = new ResourceMetadataInfo(this);
	}
	
	/**
	 * 获取资源名
	 * @return
	 */
	public String getResourceName(){
		return routeBody.getResourceName();
	}
	
	/**
	 * 获取父资源名
	 * @return
	 */
	public String getParentResourceName(){
		return routeBody.getParentResourceName();
	}
	
	/**
	 * 获取子资源名
	 * <p>_subResourceName指定的资源</p>
	 * @return
	 */
	public String getSubResourceName(){
		return requestUrlParams.get("_subResourceName");
	}
	
	/**
	 * 清空数据
	 */
	public void clear() {
		
	}
	
	public Map<String, String> getRequestUrlParams() {
		return requestUrlParams;
	}
	public void setRequestUrlParams(Map<String, String> requestUrlParams) {
		this.requestUrlParams = requestUrlParams;
	}
	public String getFormDataStr() {
		if(formData == null){
			return null;
		}
		return formData.toString();
	}
	public IJson getFormData() {
		return formData;
	}
	public void setFormData(IJson formData) {
		this.formData = formData;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public String getRequestMethod() {
		return request.getMethod().toLowerCase();
	}
	public String getRequestUri() {
		return request.getRequestURI();
	}
	public RouteBody getRouteBody() {
		return routeBody;
	}
	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}
	public ResourceMetadataInfo getResourceMetadataInfo() {
		return resourceMetadataInfo;
	}
}
