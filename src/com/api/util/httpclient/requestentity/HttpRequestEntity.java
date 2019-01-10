package com.api.util.httpclient.requestentity;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 再封装httpClient的RequestEntity
 * @author DougLei
 */
public interface HttpRequestEntity extends RequestEntity{
	/**
	 * 获取HttpMethodParams对象
	 * @return
	 */
	HttpMethodParams getHttpMethodParams();
}
