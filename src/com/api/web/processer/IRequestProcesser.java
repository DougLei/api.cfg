package com.api.web.processer;

import com.api.web.entity.request.RequestBody;
import com.api.web.entity.resulttype.ResponseBody;

public interface IRequestProcesser {

	/**
	 * 设置请求体对象
	 * @param requestBody
	 */
	public void setRequestBody(RequestBody requestBody);

	/**
	 * 处理请求
	 * @return
	 */
	public ResponseBody doRequestProcess();
	
	/**
	 * 获取处理器名称
	 * @return
	 */
	public String getProcesserName();
}
