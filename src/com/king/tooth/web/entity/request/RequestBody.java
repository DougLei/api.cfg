package com.king.tooth.web.entity.request;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.sys.entity.AbstractSysResourceEntity;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.servlet.route.RouteBody;

/**
 * 请求体，获取并封装客户端的请求
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RequestBody implements Serializable{
	
	public RequestBody() {
	}
	
	public RequestBody(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * 解析routeBody
	 */
	public void analysisRouteBody() {
		/*
		 *  将项目名、servlet名都取消掉，减少uri中不必要的数据
		 *  再根据uri，解析出RouteBody
		 */
		String requestUri = request.getRequestURI().replace(
				request.getContextPath() + request.getServletPath() + "/", "");
		this.routeBody = new RouteBody(requestUri);
	}
	
	/**
	 * 解析请求的资源类型
	 * @throws ResourceException 
	 */
	public void analysisRequestResourceType() {
		ComSysResourceService comSysResourceService = new ComSysResourceService();
		// 声明父、子资源对象实例
		ComSysResource requestResource = comSysResourceService.findResourceByResourceName(routeBody.getResourceName());
		if(requestResource == null){
			throw new NullPointerException("平台目前不支持处理名为["+routeBody.getResourceName()+"]的资源");
		}
		
		ComSysResource requestParentResource = null;
		if(StrUtils.notEmpty(routeBody.getParentResourceName())){
			requestParentResource = comSysResourceService.findResourceByResourceName(routeBody.getParentResourceName());
			if(requestParentResource == null){
				throw new NullPointerException("平台目前不支持处理名为["+routeBody.getParentResourceName()+"]的父资源");
			}
			
			if(requestParentResource.getResourceType() != requestResource.getResourceType()){
				throw new IllegalArgumentException("平台目前不支持同时处理不同类型[sql资源和table资源]的资源");
			}
			
			if(requestResource.getResourceType() == AbstractSysResourceEntity.SQLSCRIPT_RESOURCE_TYPE
					&& !requestResource.getResourceName().equals(requestParentResource.getResourceName())){
				throw new IllegalArgumentException("平台目前不支持处理[sql资源]的主子关系查询");
			}
		}
		// 获得资源类型
		requestResourceType = requestResource.getResourceType();
	}
	
	/**
	 * httpServletRequest请求对象
	 */
	private HttpServletRequest request;
	
	/**
	 * 请求的url参数键值对
	 */
	private Map<String, String> requestUrlParams;
	
	/**
	 * 请求体
	 * <p>string类型</p>
	 */
	private Object formData;
	
	/**
	 * 请求的资源体对象
	 */
	private RouteBody routeBody;
	
	/**
	 * 请求的资源类型
	 * 1：表资源类型
	 * 2：sql脚本资源类型
	 * <p>@see AbstractSysResourceEntity.XXX_RESOURCE_TYPE</p>
	 */
	private int requestResourceType;
	
	public Map<String, String> getRequestUrlParams() {
		return requestUrlParams;
	}
	public void setRequestUrlParams(Map<String, String> requestUrlParams) {
		this.requestUrlParams = requestUrlParams;
	}
	public Object getFormData() {
		return formData;
	}
	public void setFormData(Object formData) {
		this.formData = formData;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
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
	public int getRequestResourceType() {
		return requestResourceType;
	}
}
