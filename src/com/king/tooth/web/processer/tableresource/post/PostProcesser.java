package com.king.tooth.web.processer.tableresource.post;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.tableresource.RequestProcesser;

/**
 * post请求处理器
 * @author DougLei
 */
public abstract class PostProcesser extends RequestProcesser {
	
	/**
	 * 请求ijson对象
	 */
	protected IJson json;
	
	/**
	 * 初始化内置的函数属性对象
	 * 方便子类使用
	 */
	private void initBuiltinMethods(){
		builtinParentsubQueryMethodProcesser = builtinTableResourceBMProcesser.getParentsubQueryMethodProcesser();
	}
	
	/**
	 * 处理请求
	 */
	public final boolean doProcess() {
		json = requestBody.getFormData();
		initBuiltinMethods();
		
		boolean isKeepOn = doPostProcess();
		return isKeepOn;
	}
	
	/**
	 * 处理post请求
	 * @return
	 */
	protected abstract boolean doPostProcess();
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法

	/**
	 * 保存数据
	 * @param resourceName
	 * @param data
	 */
	protected void saveData(String resourceName, JSONObject data){
		HibernateUtil.saveObject(resourceName, data, null);
	}
	
	/**
	 * 添加数据后，组装ResponseBody对象
	 * @param message
	 * @param data
	 * @param isSuccess
	 */
	protected final void installResponseBodyForSaveData(String message, Object data, boolean isSuccess){
		ResponseBody responseBody = new ResponseBody(message, data, isSuccess);
		setResponseBody(responseBody);
	}
}