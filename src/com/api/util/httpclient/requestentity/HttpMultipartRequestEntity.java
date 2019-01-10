package com.api.util.httpclient.requestentity;

import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 再封装httpClient的MultipartRequestEntity
 * @author DougLei
 */
public class HttpMultipartRequestEntity extends MultipartRequestEntity implements HttpRequestEntity{
	/**
	 * 创建MultipartRequestEntity实例，需要HttpMethodParams这个对象作为参数
	 */
	private HttpMethodParams httpMethodParams;
	
	public HttpMultipartRequestEntity(Part[] parts, HttpMethodParams params) {
		super(parts, params);
		httpMethodParams = params;
	}

	public HttpMethodParams getHttpMethodParams() {
		return this.httpMethodParams;
	}
}
