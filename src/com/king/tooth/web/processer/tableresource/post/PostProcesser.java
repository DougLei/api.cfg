package com.king.tooth.web.processer.tableresource.post;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.ProcessStringTypeJsonExtend;
import com.king.tooth.util.StrUtils;
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
	 * 解析formJsonData数据
	 * @param formJsonData
	 * @return 返回null，证明请求的json数据为空
	 *         否则，返回请求数据的String格式
	 */
	private IJson analysisFormData(Object formJsonData){
		if(StrUtils.isEmpty(formJsonData)){
			return null;
		}
		IJson json = ProcessStringTypeJsonExtend.getIJson(formJsonData.toString());
		return json;
	}
	
	/**
	 * 处理请求
	 */
	public final boolean doProcess() {
		json = analysisFormData(requestBody.getFormData());
		if(json == null){
			installResponseBodyForSaveData(getProcesserName()+"处理器要保存的formData数据值为null", null, false);
			return false;
		}
		
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