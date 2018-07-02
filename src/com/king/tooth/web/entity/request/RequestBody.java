package com.king.tooth.web.entity.request;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComCode;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.common.ComCodeService;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.util.StrUtils;
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
	 * 请求的url参数键值对
	 */
	private Map<String, String> requestUrlParams;
	
	/**
	 * 请求体
	 */
	private Object formData;
	
	/**
	 * 请求的路由对象
	 */
	private RouteBody routeBody;
	
	/**
	 * 请求的资源类型
	 * 1：表资源类型
	 * 2：sql脚本资源类型
	 * 3：代码资源类型
	 * <p>@see ISysResource</p>
	 */
	private int requestResourceType;
	
	/**
	 * 请求的代码资源对象
	 */
	private ComCode reqCodeResource;
	
	/**
	 * 请求的sql脚本资源对象
	 */
	private ComSqlScript reqSqlScriptResource;
	
	public RequestBody() {
	}
	public RequestBody(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * 解析routeBody
	 * <p>路由体</p>
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
	 * 解析请求的资源
	 * <p>如果是代码资源，或sql脚本资源，顺便获得其资源对象实例</p>
	 * @throws ResourceException 
	 */
	public void analysisRequestResource() {
		ComSysResourceService comSysResourceService = new ComSysResourceService();
		// 声明父、子资源对象实例
		ComSysResource requestResource = comSysResourceService.findResourceByResourceName(routeBody.getResourceName());
		analysisResourceInstance(requestResource);
		
		ComSysResource requestParentResource = null;
		if(StrUtils.notEmpty(routeBody.getParentResourceName())){
			if(requestResource.getResourceType() == ISysResource.SQLSCRIPT){
				throw new IllegalArgumentException("平台目前不支持处理主子sql资源");
			}
			if(requestResource.getResourceType() == ISysResource.CODE){
				throw new IllegalArgumentException("平台目前不支持处理主子代码资源");
			}
			
			requestParentResource = comSysResourceService.findResourceByResourceName(routeBody.getParentResourceName());
			
			if(requestParentResource.getResourceType() != requestResource.getResourceType()){
				throw new IllegalArgumentException("平台目前不支持同时处理不同类型的资源");
			}
		}
		// 获得资源类型
		requestResourceType = requestResource.getResourceType();
	}
	
	/**
	 * 解析资源对象实例
	 * @param resource
	 */
	private void analysisResourceInstance(ComSysResource resource) {
		if(resource.getResourceType() == ISysResource.SQLSCRIPT){
			reqSqlScriptResource = new ComSqlScriptService().findSqlScriptResourceById(resource.getRefResourceId());
		}
		if(resource.getResourceType() == ISysResource.CODE){
			reqCodeResource = new ComCodeService().findCodeResourceById(resource.getRefResourceId());
		}
	}
	
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
	public ComCode getReqCodeResource() {
		return reqCodeResource;
	}
	public ComSqlScript getReqSqlScriptResource() {
		return reqSqlScriptResource;
	}
}
