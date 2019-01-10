package com.king.tooth.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.ijson.IJson;
import com.king.tooth.plugins.ijson.IJsonUtil;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.servlet.route.RouteBody;

/**
 * 请求数据的预处理过滤器
 * @author DougLei
 */
public class ReqDataPreProcesserFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		Object requestBody = analysisRequestBody(request);
		
		if(requestBody == null){
			installFailResponseBody(req, "[ReqDataPreProcesserFilter]解析请求体(requestBody)解析结果为null，请联系系统开发人员");
		}else if(requestBody instanceof String){
			installFailResponseBody(req, requestBody.toString());
		}else{
			request.setAttribute(BuiltinParameterKeys._REQUEST_BODY_KEY, requestBody);
			chain.doFilter(req, resp);
		}
	}
	
	/**
	 * 根据reqeust，解析请求体
	 * @return
	 */
	private Object analysisRequestBody(HttpServletRequest request) {
		RequestBody requestBody = new RequestBody(request);
		requestBody.setFormData(analysisFormData(request));
		if(requestBody.getResourceInfo().getReqResource().isTableResource() && (requestBody.isPostRequest() || requestBody.isPutRequest()) && (requestBody.getFormData()==null || requestBody.getFormData().size()==0)){
			requestBody.clear();
			return "系统要保存或修改的表资源"+requestBody.getResourceInfo().getReqResource().getResourceName()+"，表单数据不能为空";
		}
		analysisUrlParams(request, requestBody);
		
		String validResult = requestBody.validResourceData();
		if(validResult != null){
			return validResult;
		}
		requestBody.analysisResourcePropCodeRule();
		return requestBody;
	}
	
	/**
	 * 解析请求的body
	 * @param request
	 * @return
	 */
	private IJson analysisFormData(HttpServletRequest request) {
		Object obj = HttpHelperUtil.analysisFormData(request);
		if(StrUtils.notEmpty(obj)){
			IJson ijson = IJsonUtil.getIJson(obj.toString());
			CurrentThreadContext.getReqLogData().getReqLog().setReqData(ijson.toString());// 记录请求体
			return ijson;
		}
		return null;
	}

	/**
	 * 解析请求url后的键值对参数
	 * @param request
	 * @param requestBody 
	 * @return 
	 */
	private void analysisUrlParams(HttpServletRequest request, RequestBody requestBody) {
		Map<String, String> urlParams = new HashMap<String, String>(16);
		Enumeration<String> parameterNames = request.getParameterNames();
		if(parameterNames != null && parameterNames.hasMoreElements()){
			String key = null;
			while(parameterNames.hasMoreElements()){
				key = parameterNames.nextElement();// 获取key
				if(key.equals("_")){
					continue;
				}
				urlParams.put(key, request.getParameter(key).trim());
			}
		}
		
		// 记录请求url参数
		if(urlParams.size() > 0){
			CurrentThreadContext.getReqLogData().getReqLog().setReqData(JsonUtil.toJsonString(urlParams, false));
		}
		processRouteData(requestBody, urlParams);
		
		requestBody.setRequestBuiltinParams(analysisBuiltinParams(urlParams));
		requestBody.setRequestParentResourceParams(analysisParentResourceParams(urlParams));
		requestBody.setRequestResourceParams(analysisResourceParams(urlParams));
	}
	
	/**
	 * 处理一些路由中的数据
	 * 将路由中的数据，也保存到urlMap集合中
	 * @param requestBody 
	 * @param params
	 */
	private void processRouteData(RequestBody requestBody, Map<String, String> urlParams) {
		RouteBody routeBody = requestBody.getRouteBody();
		
		// 将资源名存储到map集合中
		urlParams.put(BuiltinParameterKeys.RESOURCE_NAME, routeBody.getResourceName());
		
		// 如果路由中包括resourceId，则将其也存储到map集合中，key值为_resourceid
		// @see HqlQueryCondFuncEntity.processSpecialThings()  在该方法中取出，作为查询条件
		if(StrUtils.notEmpty(routeBody.getResourceId())){
			urlParams.put(BuiltinParameterKeys.RESOURCE_ID, routeBody.getResourceId());
		}
		
		// 如果路由体现出父子资源查询，则将主资源名，主资源id 放到map集合中
		if(StrUtils.notEmpty(routeBody.getParentResourceName())){
			urlParams.put(BuiltinParameterKeys.PARENT_RESOURCE_NAME, routeBody.getParentResourceName());
		}
		
		if(StrUtils.notEmpty(routeBody.getParentId())){
			urlParams.put(BuiltinParameterKeys.PARENT_RESOURCE_ID, routeBody.getParentId());
		}
	}
	
	/**
	 * 解析出内置的url参数
	 * @param urlParams
	 * @return
	 */
	private Map<String, String> analysisBuiltinParams(Map<String, String> urlParams) {
		Map<String, String> builtinParams = null;
		if(urlParams.size() > 0){
			builtinParams = new HashMap<String, String>(urlParams.size());
			String builtinParamValue = null;
			for (String bufp : BuiltinParameterKeys.BUILTIN_URL_FUNC_PARAMS) {
				builtinParamValue = urlParams.remove(bufp);
				if(StrUtils.notEmpty(builtinParamValue)){
					builtinParams.put(bufp, builtinParamValue);
				}
			}
		}else{
			builtinParams = new HashMap<String, String>(1);
		}
		return builtinParams;
	}
	
	/**
	 * 解析出请求父资源属性的url参数【主子/递归】
	 * <pre>
	 * 系统处理逻辑说明：在请求的url中
	 * 	父资源id的值可以使用   "_"+"名称"(例如:_root，以下就用_root说明) 的方式书写，实现占位符对象功能
	 * 	在请求的url参数中，可以通过 _root.父资源属性名=xxx，设置查询父资源时的条件
	 * 	其中:	_root变量必须以'_'下划线开头
	 * 	_xxx中的xxx，可以自定义，在url参数中，必须用_xxx.父资源属性名=值来设置查询条件
	 * </pre>
	 * @param urlParams
	 * @return
	 */
	private Map<String, String> analysisParentResourceParams(Map<String, String> urlParams) {
		Map<String, String> parentResourceParams = null;
		String parentResourceId = urlParams.remove(BuiltinParameterKeys.PARENT_RESOURCE_ID);
		if(StrUtils.isEmpty(parentResourceId)){
			parentResourceParams = new HashMap<String, String>(1);
		}else{
			if(urlParams.size() > 0 && parentResourceId.startsWith("_")){
				parentResourceParams = new HashMap<String, String>(urlParams.size());
				parentResourceId += ".";
				
				// 在urlParams中寻找，是否有父资源的查询条件
				Set<String> keys = urlParams.keySet();
				for (String k : keys) {
					if(k.startsWith(parentResourceId)){
						parentResourceParams.put(k.replace(parentResourceId, ""), urlParams.get(k));
					}
				}
				
				// 如果找到父资源的查询条件，则将其从urlParams中移除
				if(parentResourceParams.size() > 0){
					keys = parentResourceParams.keySet();
					for (String k : keys) {
						urlParams.remove(parentResourceId+k);
					}
				}else{
					parentResourceParams.put(ResourcePropNameConstants.ID, parentResourceId);
				}
			}else{
				parentResourceParams = new HashMap<String, String>(2);
				parentResourceParams.put(ResourcePropNameConstants.ID, parentResourceId);
			}
			
			if(parentResourceParams.containsKey(ResourcePropNameConstants.ID)){
				parentResourceParams.put(BuiltinParameterKeys.PARENT_RESOURCE_ID, parentResourceParams.remove(ResourcePropNameConstants.ID));
			}else{
				parentResourceParams.put(BuiltinParameterKeys.PARENT_RESOURCE_ID, parentResourceId);
			}
		}
		return parentResourceParams;
	}
	
	/**
	 * 解析出请求资源属性的url参数
	 * @param urlParams
	 * @return
	 */
	private Map<String, String> analysisResourceParams(Map<String, String> urlParams) {
		return urlParams;
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
