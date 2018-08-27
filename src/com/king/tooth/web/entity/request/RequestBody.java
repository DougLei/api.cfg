package com.king.tooth.web.entity.request;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.web.servlet.route.RouteBody;

/**
 * 请求体，获取并封装客户端的请求
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RequestBody implements Serializable{
	
	/**
	 * 是否停止解析
	 */
	private boolean isStopAnalysis;
	
	/**
	 * 解析过程中的错误信息
	 */
	private String analysisErrMsg;
	
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
		
		String codeUri = "/" + requestUri + "/" + request.getMethod().toLowerCase();
		if(CodeResourceMapping.isCodeResource(codeUri)){
			this.routeBody = new RouteBody();
			routeBody.setResourceName(codeUri);
			routeBody.setIsAction(true);
		}else{
			this.routeBody = new RouteBody(requestUri, this);
		}
	}
	
	/**
	 * 解析请求的资源的各种信息
	 */
	private void analysisResource() {
		if(!isStopAnalysis){
			resourceInfo = new ResourceInfo(this);
		}
		if(!isStopAnalysis){
			resourceMetadataInfo = new ResourceMetadataInfo(this);
		}
	}
	
	/**
	 * 解析资源字段的编码规则，并获取结果值
	 */
	public void analysisResourcePropCodeRule() {
		if(!isStopAnalysis){
			// TODO 暂时不要处理字段编码规则
//			resourcePropCodeRule = new ResourcePropCodeRule(this);
		}
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
	 * 清空数据
	 */
	public void clear() {
		if(resourcePropCodeRule != null){
			resourcePropCodeRule.clear();
		}
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
	public ResourcePropCodeRule getResourcePropCodeRule() {
		return resourcePropCodeRule;
	}
	public void setAnalysisErrMsg(String analysisErrMsg) {
		this.isStopAnalysis = true;
		this.analysisErrMsg = analysisErrMsg;
	}
	public String getAnalysisErrMsg() {
		return analysisErrMsg;
	}
	public boolean getIsStopAnalysis() {
		return isStopAnalysis;
	}
}
