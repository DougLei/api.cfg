package com.king.tooth.web.entity.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.code.resource.CodeResourceProcesser;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.request.valid.data.ResourceDataVerifier;
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
	 * 请求的url内置参数键值对
	 */
	private Map<String, String> requestBuiltinParams;
	/**
	 * 请求的resource参数键值对
	 */
	private Map<String, String> requestResourceParams;
	/**
	 * 请求的parentResource参数键值对
	 */
	private Map<String, String> requestParentResourceParams;
	
	/**
	 * 请求的查询资源的元数据信息集合
	 * <p>在查询的时候，进行数据验证和值类型转换</p>
	 */
	protected List<ResourceMetadataInfo> queryResourceMetadataInfos;
	/**
	 * 请求的查询父资源的元数据信息集合
	 * <p>在查询的时候，进行数据验证和值类型转换</p>
	 */
	protected List<ResourceMetadataInfo> queryParentResourceMetadataInfos;
	
	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 请求的资源信息
	 */
	private ResourceInfo resourceInfo;
	
	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 资源的数据校验类
	 */
	private ResourceDataVerifier resourceDataVerifier;
	
	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 请求资源的属性(字段、列)值编码规范
	 */
	private ResourcePropCodeRule resourcePropCodeRule;
	
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
		
		String requestMethod = request.getMethod().toLowerCase();
		String codeUri = "/" + requestUri.toLowerCase() + "/" + requestMethod;
		if(CodeResourceProcesser.isCodeResource(codeUri) 
				|| CodeResourceProcesser.isCodeResource(codeUri = ("/" + requestUri.toLowerCase() + "_" + requestMethod))){
			this.routeBody = new RouteBody();
			routeBody.setCodeUri(codeUri);
			routeBody.setIsCode(true);
		}else{
			this.routeBody = new RouteBody(requestUri);
		}
	}
	
	/**
	 * 解析请求的资源的各种信息
	 */
	private void analysisResource() {
		resourceInfo = new ResourceInfo(this);
	}

	// -------------------------------------------------------------------------------------------------------------------------
	/**
	 * 校验请求资源的数据
	 * @return
	 */
	public String validResourceData() {
		try {
			resourceDataVerifier = new ResourceDataVerifier();
			return resourceDataVerifier.doValidResourceData(this);
		} finally {
			if(resourceDataVerifier != null){
				resourceDataVerifier.clearValidData();
				resourceDataVerifier = null;
			}
		}
	}
	
	/**
	 * 解析资源字段的编码规则，并获取结果值
	 */
	public void analysisResourcePropCodeRule() {
		resourcePropCodeRule = new ResourcePropCodeRule(this);
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
	 * 是否主子资源查询
	 * <p>包括递归</p>
	 * @return
	 */
	public boolean isParentSubResourceQuery(){
		return StrUtils.notEmpty(getParentResourceName());
	}
	
	/**
	 * 是否是递归查询
	 * <p>即主子资源名一样</p>
	 * @return
	 */
	public boolean isRecursiveQuery(){
		return getResourceName().equals(getParentResourceName());
	}
	
	public boolean isGetRequest(){
		return "get".equals(getRequestMethod());
	}
	public boolean isPostRequest(){
		return "post".equals(getRequestMethod());
	}
	public boolean isPutRequest(){
		return "put".equals(getRequestMethod());
	}
	public boolean isDeleteRequest(){
		return "delete".equals(getRequestMethod());
	}
	
	/**
	 * 整合所有url参数的键值对集合
	 * @return
	 */
	public Map<String, String> installAllUrlParams() {
		Map<String, String> urlParams = new HashMap<String, String>(requestBuiltinParams.size() + requestResourceParams.size() + requestParentResourceParams.size());
		urlParams.putAll(requestBuiltinParams);
		urlParams.putAll(requestResourceParams);
		urlParams.putAll(requestParentResourceParams);
		return urlParams;
	}
	
	/**
	 * 清空数据
	 */
	public void clear() {
		if(resourcePropCodeRule != null){
			resourcePropCodeRule.clear();
		}
		if(requestBuiltinParams != null && requestBuiltinParams.size() > 0){
			requestBuiltinParams.clear();
		}
		if(requestResourceParams != null && requestResourceParams.size() > 0){
			requestResourceParams.clear();
		}
		if(requestParentResourceParams != null && requestParentResourceParams.size() > 0){
			requestParentResourceParams.clear();
		}
		if(formData != null && formData.size() > 0){
			formData.clear();
		}
		if(queryResourceMetadataInfos != null && queryResourceMetadataInfos.size() > 0){
			queryResourceMetadataInfos.clear();
		}
		if(queryParentResourceMetadataInfos != null && queryParentResourceMetadataInfos.size() > 0){
			queryParentResourceMetadataInfos.clear();
		}
		if(resourceInfo != null){
			resourceInfo.clear();
		}
		if(resourceDataVerifier != null){
			resourceDataVerifier.clearValidData();
		}
	}
	
	public Map<String, String> getRequestBuiltinParams() {
		return requestBuiltinParams;
	}
	public void setRequestBuiltinParams(Map<String, String> requestBuiltinParams) {
		this.requestBuiltinParams = requestBuiltinParams;
	}
	public Map<String, String> getRequestResourceParams() {
		return requestResourceParams;
	}
	public void setRequestResourceParams(Map<String, String> requestResourceParams) {
		this.requestResourceParams = requestResourceParams;
	}
	public Map<String, String> getRequestParentResourceParams() {
		return requestParentResourceParams;
	}
	public void setRequestParentResourceParams(Map<String, String> requestParentResourceParams) {
		this.requestParentResourceParams = requestParentResourceParams;
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
	public ResourcePropCodeRule getResourcePropCodeRule() {
		return resourcePropCodeRule;
	}
	public List<ResourceMetadataInfo> getQueryResourceMetadataInfos() {
		return queryResourceMetadataInfos;
	}
	public void setQueryResourceMetadataInfos(List<ResourceMetadataInfo> queryResourceMetadataInfos) {
		this.queryResourceMetadataInfos = queryResourceMetadataInfos;
	}
	public List<ResourceMetadataInfo> getQueryParentResourceMetadataInfos() {
		return queryParentResourceMetadataInfos;
	}
	public void setQueryParentResourceMetadataInfos(List<ResourceMetadataInfo> queryParentResourceMetadataInfos) {
		this.queryParentResourceMetadataInfos = queryParentResourceMetadataInfos;
	}
}
