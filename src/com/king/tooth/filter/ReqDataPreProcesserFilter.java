package com.king.tooth.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.ProcessStringTypeJsonExtend;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.thread.CurrentThreadContext;
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
		RequestBody requestBody = analysisRequestBody(request);
		if(requestBody == null){
			installFailResponseBody(req, "[ReqDataPreProcesserFilter]解析请求体(requestBody)解析结果为null，请联系系统开发人员");
		}else{
			request.setAttribute(BuiltinParameterKeys._REQUEST_BODY_KEY, requestBody);
			chain.doFilter(req, resp);
		}
	}
	
	/**
	 * 根据reqeust，解析请求体
	 * @return
	 */
	private RequestBody analysisRequestBody(HttpServletRequest request) {
		RequestBody requestBody = new RequestBody(request);
		requestBody.setFormData(analysisFormData(request));
		requestBody.setRequestUrlParams(analysisUrlParams(request, requestBody));
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
			IJson ijson = ProcessStringTypeJsonExtend.getIJson(obj.toString());
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
	private Map<String, String> analysisUrlParams(HttpServletRequest request, RequestBody requestBody) {
		Map<String, String> urlParams = new HashMap<String, String>(16);
		Enumeration<String> parameterNames = request.getParameterNames();
		if(parameterNames != null && parameterNames.hasMoreElements()){
			String key = null;
			while(parameterNames.hasMoreElements()){
				key = parameterNames.nextElement();// 获取key
				if(key.equals("_")){
					continue;
				}
				urlParams.put(key, StrUtils.turnStrEncoding(request.getParameter(key).trim(), EncodingConstants.ISO8859_1, EncodingConstants.UTF_8));
			}
		}
		
		// 记录请求体
		if(urlParams.size() > 0){
			CurrentThreadContext.getReqLogData().getReqLog().setReqData(JsonUtil.toJsonString(urlParams, false));
		}
		
		processRouteData(requestBody, urlParams);
		return urlParams;
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
		
		// 加入一个基础数据条件
		urlParams.put("customerId", CurrentThreadContext.getCustomerId());// 客户主键
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
