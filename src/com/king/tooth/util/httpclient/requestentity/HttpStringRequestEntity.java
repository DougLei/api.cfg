package com.king.tooth.util.httpclient.requestentity;

import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 再封装httpClient的StringRequestEntity
 * @author DougLei
 */
public class HttpStringRequestEntity extends StringRequestEntity implements HttpRequestEntity{
	public HttpStringRequestEntity(String str, String contentType, String encoding) throws UnsupportedEncodingException {
		super(str, contentType, encoding);
	}

	public HttpMethodParams getHttpMethodParams() {
		// 不需要实现
		return null;
	}
}
