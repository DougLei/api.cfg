package com.king.tooth.web.entity.request;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysResource;
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
	 * 请求的sql脚本资源对象
	 */
	private ComSqlScript reqSqlScriptResource;
	
	/**
	 * 请求的资源类型
	 * 1：表资源类型
	 * 2：sql脚本资源类型
	 * 3：代码资源类型
	 * <p>@see ISysResource</p>
	 */
	private Integer requestResourceType;
	/**
	 * 请求的代码资源key
	 */
	private String reqCodeResourceKey;
	
	/**
	 * 请求的父资源类型
	 * 1：表资源类型
	 * 2：sql脚本资源类型
	 * 3：代码资源类型
	 * <p>@see ISysResource</p>
	 */
	private Integer requestParentResourceType;
	/**
	 * 请求的父代码资源key
	 */
	private String reqParentCodeResourceKey;
	
	
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
	ComSysResourceService sysResourceService = new ComSysResourceService();
	public void analysisRequestResource() {
		if(routeBody.isAction()){
			if(StrUtils.notEmpty(routeBody.getParentResourceName())){
				throw new IllegalArgumentException("平台目前不支持处理主子action资源");
			}
			requestResourceType = ISysResource.CODE;
			reqCodeResourceKey = CodeResourceMapping.getCodeResourceKey(routeBody.getResourceName(), getRequestMethod(), routeBody.getActionName());
			return;
		}
		
		// 处理子资源类型，如果是sql脚本资源，则顺便查询出来sql脚本资源对象
		reqCodeResourceKey = CodeResourceMapping.getCodeResourceKey(routeBody.getResourceName(), getRequestMethod(), null);
		if(CodeResourceMapping.isCodeResource(reqCodeResourceKey)){
			requestResourceType = ISysResource.CODE;
		}else{
			ComSysResource requestResource = sysResourceService.findResourceByResourceName(routeBody.getResourceName());
			requestResourceType = requestResource.getResourceType();
			
			// 如果是sql脚本资源，则要去查询sql脚本实例
			if(ISysResource.SQLSCRIPT.equals(requestResourceType)){
				ComSqlScriptService sqlScriptService = new ComSqlScriptService();
				reqSqlScriptResource = sqlScriptService.findSqlScriptResourceById(requestResource.getRefResourceId());
				if(StrUtils.isEmpty(reqSqlScriptResource.getSqlScriptParameters())){
					reqSqlScriptResource.setSqlScriptParameterList(sqlScriptService.findSqlScriptParameters(requestResource.getRefResourceId()));
				}
			}
		}
		
		// 处理父资源类型
		if(StrUtils.notEmpty(routeBody.getParentResourceName())){
			if(ISysResource.SQLSCRIPT.equals(requestResourceType)){
				throw new IllegalArgumentException("平台目前不支持处理主子sql资源");
			}
			
			reqParentCodeResourceKey = CodeResourceMapping.getCodeResourceKey(routeBody.getParentResourceName(), getRequestMethod(), null);
			if(CodeResourceMapping.isCodeResource(reqParentCodeResourceKey)){
				requestParentResourceType = ISysResource.CODE;
			}else{
				ComSysResource requestParentResource = sysResourceService.findResourceByResourceName(routeBody.getParentResourceName());
				requestParentResourceType = requestParentResource.getResourceType();
			}
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
	public Integer getRequestResourceType() {
		return requestResourceType;
	}
	public Integer getRequestParentResourceType() {
		return requestParentResourceType;
	}
	public ComSqlScript getReqSqlScriptResource() {
		return reqSqlScriptResource;
	}
	public String getReqCodeResourceKey() {
		return reqCodeResourceKey;
	}
	public String getReqParentCodeResourceKey() {
		return reqParentCodeResourceKey;
	}
}
