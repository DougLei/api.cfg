package com.king.tooth.web.processer;

import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 抽象的公用处理器
 * <p>目的在于提取公共的属性、方法</p>
 * <p>主要就是请求体和响应体</p>
 * <p>**任何新的处理器类型，都必须继承这个类**</p>
 * @author DougLei
 */
public abstract class CommonProcesser {

	/**
	 * 请求体
	 */
	protected RequestBody requestBody;
	
	/**
	 * 处理结果对象
	 */
	protected ResponseBody responseBody;
	
	/**
	 * 设置响应体
	 * <p>在处理器中使用到</p>
	 * <p>也可以在前、后处理器中使用到</p>
	 * @param data
	 */
	protected final void setResponseBody(ResponseBody responseBody){
		this.responseBody = responseBody;
	}
	
	/**
	 * 设置请求体对象
	 * @param requestBody
	 */
	public final void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}
}
