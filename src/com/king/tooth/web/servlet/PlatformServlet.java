package com.king.tooth.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.RequestUrlParamKeyConstants;
import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.request.RequestBody;

/**
 * @author DougLei
 */
@SuppressWarnings("serial")
public class PlatformServlet extends BasicHttpServlet{

	/**
	 * 解析出来的请求体，直接赋值给属性requestBody
	 */
	protected void analysisRequestBody(HttpServletRequest request) {
		requestBody = new RequestBody(request);
		requestBody.analysisRouteBody();
		requestBody.analysisRequestResourceType();
		requestBody.setRequestUrlParams(analysisUrlParams(request));
		requestBody.setFormData(analysisFormData(request));
	}
	
	/**
	 * 解析请求体
	 * @param request
	 * @return
	 */
	private Object analysisFormData(HttpServletRequest request) {
		if(request.getContentLength() <= 0){
			return null;
		}
		StringBuilder formData = new StringBuilder();
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(request.getInputStream(), EncodingConstants.UTF_8);
			br = new BufferedReader(reader);
			String tmp = null;
			while((tmp = br.readLine()) != null){
				formData.append(tmp);
			}
		} catch (UnsupportedEncodingException e) {
			Log4jUtil.debug("[PlatformServlet.analysisBody]方法出现异常信息:{}", e.getMessage());
		} catch (IOException e) {
			Log4jUtil.debug("[PlatformServlet.analysisBody]方法出现异常信息:{}", e.getMessage());
		}finally{
			CloseUtil.closeIO(br);
			CloseUtil.closeIO(reader);
		}
		return formData;
	}

	/**
	 * 解析请求url后的键值对参数
	 * <p>所有的key值，都转小写，以及去除前后空格</p>
	 * <p>value只做了转码处理，由iso8859-1转换为utf-8</p>
	 * <p>如果路由中包括resourceId，则将其也存储到map集合中，key值为_resourceid</p>
	 * @param request
	 * @return
	 */
	private Map<String, String> analysisUrlParams(HttpServletRequest request) {
		if(requestBody.getRequestResourceType() == ISysResource.CODE){// 如果请求的是代码资源，则不需要解析请求的参数
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		Enumeration<String> parameterNames = request.getParameterNames();
		if(parameterNames != null && parameterNames.hasMoreElements()){
			String tmpKey = null;
			String tmpValue = null;
			while(parameterNames.hasMoreElements()){
				tmpKey = parameterNames.nextElement();// 获取key
				
				if(filterUnusedKey(tmpKey)){
					continue;
				}
				
				tmpValue = StrUtils.turnStrEncoding(request.getParameter(tmpKey).trim(), EncodingConstants.ISO8859_1, EncodingConstants.UTF_8);// 获取value，并转码
				tmpKey = tmpKey.trim().toLowerCase();
				params.put(tmpKey, tmpValue);
			}
		}
		
		// 处理一些特别的数据
		processSpecialData(params);
		return params;
	}
	
	/**
	 * 在请求的url参数中，要过滤的key值数组
	 * <pre>
	 * 		_:Key为_，是jquery.ajax请求时，设置了cache:false时，自动加入的一个时间参数，防止出现缓存问题，所以这里需要判断，如果存在，则把这个参数去掉
	 * </pre>
	 */
	private static final String[] filter_key_arr = {"_"};
	
	/**
	 * 过滤不用的key
	 * @param key
	 * @return 如果过滤到了，返回true，否则返回false
	 */
	private boolean filterUnusedKey(String key){
		for (String k : filter_key_arr) {
			if(k.equals(key)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 处理一些特别的请求的数据
	 * @param params
	 */
	private void processSpecialData(Map<String, String> params) {
		// 将资源名存储到map集合中
		params.put(RequestUrlParamKeyConstants.RESOURCE_NAME, requestBody.getRouteBody().getResourceName());
		
		// 如果路由中包括resourceId，则将其也存储到map集合中，key值为_resourceid
		// @see HqlQueryCondFuncEntity.processSpecialThings()  在该方法中取出，作为查询条件
		if(StrUtils.notEmpty(requestBody.getRouteBody().getResourceId())){
			params.put(RequestUrlParamKeyConstants.RESOURCE_ID, requestBody.getRouteBody().getResourceId());
		}
		// 如果路由体现出父子资源查询，则将主资源名，主资源id 放到map集合中
		if(StrUtils.notEmpty(requestBody.getRouteBody().getParentResourceName())){
			params.put(RequestUrlParamKeyConstants.PARENT_RESOURCE_NAME, requestBody.getRouteBody().getParentResourceName());
		}
		if(StrUtils.notEmpty(requestBody.getRouteBody().getParentId())){
			params.put(RequestUrlParamKeyConstants.PARENT_RESOURCE_ID, requestBody.getRouteBody().getParentId());
		}
	}
}
