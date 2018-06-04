package com.king.tooth.web.processer;

import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.ResponseBody;

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
