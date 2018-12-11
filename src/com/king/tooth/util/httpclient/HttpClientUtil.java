package com.king.tooth.util.httpclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.king.tooth.cache.SysContext;
import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.httpclient.requestentity.HttpMultipartRequestEntity;
import com.king.tooth.util.httpclient.requestentity.HttpRequestEntity;
import com.king.tooth.util.httpclient.requestentity.HttpStringRequestEntity;


/**
 * 接口调用工具类
 * @author DougLei
 */
public class HttpClientUtil {
	
	/**
	 * 处理请求的url
	 * 若有参数，拼装参数到url后
	 * @param reqUrl
	 * @param params
	 * @return
	 */
	private static String dealRequestUrl(String reqUrl, Map<String, Object> params) {
		if(params != null && params.size() >0){
			StringBuilder url = new StringBuilder(reqUrl);
			if(!reqUrl.contains("?")){
				url.append("?");
			}else{
				url.append("&");
			}
			
			Set<Entry<String, Object>> se = params.entrySet();
			for (Entry<String, Object> entry : se) {
				url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			params.clear();
			url.setLength(url.length()-1);
			
			reqUrl = url.toString().replace(" ", "%20");
		}
		return reqUrl;
	}
	
	/**
	 * 处理header
	 * @param method
	 * @param headers
	 */
	private static void processHeader(HttpMethodBase method, Map<String, String> headers) {
		if(headers != null && headers.size() >0){
			Set<Entry<String, String>> se = headers.entrySet();
			for (Entry<String, String> entry : se) {
				method.setRequestHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 处理post请求的requestBody参数
	 * 若有数据，存储到postMethod的requestBody中
	 * @param postMethod 
	 * @param formParams
	 * @return
	 */
	private static void dealRequestBody(PostMethod postMethod, Map<String, String> formParams) {
		NameValuePair[] nameValues = null;
		if(formParams != null && formParams.size() > 0){
			List<NameValuePair> nameValueList = new ArrayList<NameValuePair>();
			Set<Entry<String, String>> se = formParams.entrySet();
			NameValuePair tmp = null;
			for (Entry<String, String> entry : se) {
				tmp = new NameValuePair(entry.getKey(), entry.getValue());
				nameValueList.add(tmp);
			}
			nameValues = nameValueList.toArray(new NameValuePair[0]);
		}
		
		if(nameValues != null && nameValues.length > 0){
			postMethod.setRequestBody(nameValues);
		}
	}
	
	/**
	 * 处理请求体
	 * 例如：上传文件、或提交单一json串(只有value，没有key)等
	 * @param postMethod
	 * @param httpRequestEntity
	 */
	private static void dealRequestEntity(PostMethod postMethod, HttpRequestEntity httpRequestEntity) {
		if(httpRequestEntity != null){
			postMethod.setRequestEntity(httpRequestEntity);
			if(httpRequestEntity.getHttpMethodParams() != null){
				postMethod.setParams(httpRequestEntity.getHttpMethodParams());// 目前这个判断是专门处理MultipartRequestEntity的
			}
		}
	}

	/**
	 * 设置相关的配置信息
	 * @param httpClient
	 * @param getMethod
	 */
	private static void setConfig(HttpClient httpClient, HttpMethodBase httpMethodBase) {
		// 通过网络与服务器建立连接的时间限制【连接超时】
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(Integer.valueOf(SysContext.getSystemConfig("httpclient.conn.timeout")));
		// 读取数据的时间限制【请求超时】
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(Integer.valueOf(SysContext.getSystemConfig("httpclient.req.timeout")));
		// 设置默认编码格式
		httpMethodBase.getParams().setContentCharset(EncodingConstants.UTF_8);
		// 设置请求头中，禁止缓存
		httpMethodBase.setRequestHeader("Cache-Control", "no-cache");
	}
	
	/**
	 * 基础的get请求
	 * @param reqUrl
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String doGetBasic(String reqUrl, Map<String, Object> urlParams, Map<String, String> headers){
		reqUrl = dealRequestUrl(reqUrl, urlParams);// 处理请求的url 若有参数，拼装参数到url后
		HttpClient httpClient = null;
		GetMethod getMethod = null;
		try {
			httpClient = new HttpClient();
			getMethod = new GetMethod(reqUrl);
			processHeader(getMethod, headers);
			setConfig(httpClient, getMethod);// 设置相关的配置信息
			int status = httpClient.executeMethod(getMethod);// 执行
			Log4jUtil.debug("[HttpClientUtil.doGet]方法调用接口\"{}\"的结果为\"{}\"", reqUrl, status);
			return getMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			Log4jUtil.debug("[HttpClientUtil.doGet]方法出现异常：{}", e.getMessage());
		} catch (IOException e) {
			Log4jUtil.debug("[HttpClientUtil.doGet]方法出现异常：{}", e.getMessage());
		}finally{
			if(getMethod != null){
				getMethod.releaseConnection();
			}
		}
		return null;
	}

	/**
	 * 基础的post请求
	 * @param reqUrl
	 * @param urlParams 跟在url后的参数，可为空
	 * @param formParams 提交的参数，可为空
	 * @param headers 头请求
	 * @param httpRequestEntity 【解释：post请求时无参数名称的数据对象，例如只提交一个json串，只有值，没有key。这里根据实际情况使用RequestEntity的各个实现类。】
	 * @return
	 */
	public static String doPostBasic(String reqUrl, Map<String, Object> urlParams, Map<String, String> formParams, Map<String, String> headers, HttpRequestEntity httpRequestEntity){
		reqUrl = dealRequestUrl(reqUrl, urlParams);// 处理请求的url 若有参数，拼装参数到url后
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		String errMsg = null;
		try {
			httpClient = new HttpClient();
			postMethod = new PostMethod(reqUrl);
			processHeader(postMethod, headers);
			dealRequestBody(postMethod, formParams);// 处理请求的form表单数据，并添加到postMethod中
			dealRequestEntity(postMethod, httpRequestEntity);// 处理上传文件、或提交单一json串(只有value，没有key)等
			setConfig(httpClient, postMethod);// 设置相关的配置信息，要在dealRequestEntity()方法后调用
			
			int status = httpClient.executeMethod(postMethod);// 执行
			Log4jUtil.debug("[HttpClientUtil.doPost]方法调用接口\"{}\"的结果为\"{}\"", reqUrl, status);
			return postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			errMsg = ExceptionUtil.getErrMsg(e);
			Log4jUtil.debug("[HttpClientUtil.doPostBasic]方法出现异常：{}", errMsg);
		} catch (IOException e) {
			errMsg = ExceptionUtil.getErrMsg(e);
			Log4jUtil.debug("[HttpClientUtil.doPostBasic]方法出现异常：{}", errMsg);
		}finally{
			if(postMethod != null){
				postMethod.releaseConnection();
			}
		}
		return errMsg;
	}
	
	/**
	 * 获得HttpStringRequestEntity对象
	 * 提交字符串，例如Json串
	 * POST提交用
	 * @param str 字符串
	 * @param contentType 字符串的类型：text/json、text/xml、text/html、text/plain
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static HttpRequestEntity getHttpStringRequestEntity(String str, String contentType){
		HttpStringRequestEntity httpStringRequestEntity  = null;
		try {
			httpStringRequestEntity = new HttpStringRequestEntity(str, contentType, EncodingConstants.UTF_8);
		} catch (UnsupportedEncodingException e) {
			Log4jUtil.debug("[HttpClientUtil.getHttpStringRequestEntity]方法出现异常：{}", e.getMessage());
		}
		return httpStringRequestEntity;
	}
	
	/**
	 * 获得HttpMultipartRequestEntity对象
	 * 上传文件
	 * POST提交用
	 * @param file 文件对象
	 * @param postMethod 调用该方法，需要创建一个postMethod对象，并传入doPost方法中
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static HttpRequestEntity getHttpMultipartRequestEntity(File file){
		HttpMultipartRequestEntity httpMultipartRequestEntity = null;
		try {
			FilePart filePart = new FilePart(file.getName(), file);
			Part[] parts = {filePart};
			HttpMethodParams httpMethodParams = new HttpMethodParams();
			httpMultipartRequestEntity = new HttpMultipartRequestEntity(parts, httpMethodParams);
		} catch (FileNotFoundException e) {
			Log4jUtil.debug("[HttpClientUtil.getHttpMultipartRequestEntity]方法出现异常：{}", e.getMessage());
		}
		return httpMultipartRequestEntity;
	}
}
