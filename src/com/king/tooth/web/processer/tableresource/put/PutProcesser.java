package com.king.tooth.web.processer.tableresource.put;

import java.util.List;

import org.hibernate.internal.HbmConfPropMetadata;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.ProcessStringTypeJsonExtend;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.tableresource.RequestProcesser;

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
	 * 初始化内置的函数属性对象
	 * 方便子类使用
	 */
	private void initBuiltinMethods(){
		builtinQueryCondMethodProcesser = builtinTableResourceBMProcesser.getQuerycondProcesser();
	}
	
	/**
	 * 解析formJsonData数据
	 * @param formJsonData
	 * @return 返回null，证明请求的json数据为空
	 *         否则，返回请求数据的String格式
	 */
	private IJson analysisFormData(Object formJsonData){
		if(StrUtils.isEmpty(formJsonData)){
			installResponseBodyForUpdateData(getProcesserName()+"处理器要保存的formData数据值为null", null, false);
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
			return false;
		}
		
		initBuiltinMethods();
		
		boolean isKeepOn = doPutProcess();
		return isKeepOn;
	}
	
	/**
	 * 处理put请求
	 * @return
	 */
	protected abstract boolean doPutProcess();
	
	/**
	 * 获取包括update在内的，后续的hql语句，由各个子类实现
	 * <p>例1： update from tableName set xxxx = ?, xxx = ?...</p>
	 * @param updatedJsonObj 要被更新的json对象
	 * @param params 记录set以及where id条件的值
	 * @param hibernateDefineResourceProps 当前更新的资源，在系统hibernate定义的属性集合
	 * @return
	 */
	protected abstract StringBuilder getUpdateHql(JSONObject updatedJsonObj, List<Object> params, HbmConfPropMetadata[] hibernateDefineResourceProps);
	protected final StringBuilder updateHql = new StringBuilder();// 因为子类会多次操作这个对象，所以提取出来，作为属性，方便调用
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法
	
	
	
	/**
	 * 修改数据后，组装ResponseBody对象
	 * @param message
	 * @param data
	 * @param isSuccess
	 */
	protected final void installResponseBodyForUpdateData(String message, Object data, boolean isSuccess){
		ResponseBody responseBody = new ResponseBody(message, data, isSuccess);
		setResponseBody(responseBody);
	}
}
