package com.api.web.processer.tableresource.put;

import com.api.plugins.ijson.IJson;
import com.api.web.entity.resulttype.ResponseBody;
import com.api.web.processer.tableresource.RequestProcesser;

/**
 * put请求处理器
 * @author DougLei
 */
public abstract class PutProcesser extends RequestProcesser {
	
	/**
	 * 请求ijson对象
	 */
	protected IJson json;
	
	/**
	 * 处理请求
	 */
	public final boolean doProcess() {
		json = requestBody.getFormData();
		
		boolean isKeepOn = doPutProcess();
		return isKeepOn;
	}
	
	/**
	 * 处理put请求
	 * @return
	 */
	protected abstract boolean doPutProcess();
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法
	
	/**
	 * 修改数据后，组装ResponseBody对象
	 * @param message
	 * @param data
	 * @param isSuccess
	 */
	protected final void installResponseBodyForUpdateData(String message, Object data){
		ResponseBody responseBody = new ResponseBody(message, data);
		setResponseBody(responseBody);
	}
}
