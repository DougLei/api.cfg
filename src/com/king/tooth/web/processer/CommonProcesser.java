package com.king.tooth.web.processer;

import java.util.List;
import java.util.Map;

import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.PageResultEntity;
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
	 * 简单组装ResponseBody对象
	 * @param message 
	 * @param data 
	 */
	protected final void installResponseBodySimple(String message, Object data){
		ResponseBody responseBody = new ResponseBody(message, data);;
		setResponseBody(responseBody);
	}
	
	/**
	 * 查询数据集合时，组装ResponseBody对象
	 * @param message
	 * @param dataList
	 * @param pageResultEntity
	 */
	protected final void installResponseBodyForQueryDataList(String message, List<Map<String, Object>> dataList, PageResultEntity pageResultEntity){
		Object data = dataList;
		if(pageResultEntity != null){
			// 分页查询，要先将结果集存储到pageResultEntity中，再把pageResultEntity存储到responseBody中
			pageResultEntity.setResultDatas(dataList);
			data = pageResultEntity;
		}
		installResponseBodySimple(message, data);
	}
	
	/**
	 * 设置请求体对象
	 * @param requestBody
	 */
	public final void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}
}
