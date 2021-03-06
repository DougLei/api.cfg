package com.api.web.processer.tableresource.delete;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.api.constants.OperDataTypeConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.web.entity.resulttype.ResponseBody;
import com.api.web.processer.tableresource.RequestProcesser;

/**
 * delete请求处理器
 * @author DougLei
 */
public abstract class DeleteProcesser extends RequestProcesser {
	
	/**
	 * 初始化内置的函数属性对象
	 * 方便子类使用
	 */
	private final void initBuiltinMethods(){
		builtinQueryCondMethodProcesser = builtinTableResourceBMProcesser.getQuerycondProcesser();
		builtinParentsubQueryMethodProcesser = builtinTableResourceBMProcesser.getParentsubQueryMethodProcesser();
	}
	
	/**
	 * 处理请求
	 */
	public final boolean doProcess() {
		initBuiltinMethods();
		boolean isKeepOn = doDeleteProcess();
		return isKeepOn;
	}
	
	/**
	 * 处理delete请求
	 * @return
	 */
	protected abstract boolean doDeleteProcess();
	
	/**
	 * 得到删除语句的删除条件hql
	 * @return
	 */
	protected abstract StringBuilder getDeleteHql();
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法
	
	/**
	 * 删除数据后，组装ResponseBody对象
	 * @param message
	 * @param deleteIds
	 */
	protected final void installResponseBodyForDeleteData(String message, List<Object> deleteIds){
		JSONObject json = new JSONObject(2);
		String ids = deleteIds.toString().replace("[", "").replace("]", "");
		json.put(BuiltinParameterKeys._IDS, ids);
		json.put(ResourcePropNameConstants.FOCUSED_OPER, ids.replace(",", "_"+OperDataTypeConstants.DELETE+",") + "_" +OperDataTypeConstants.DELETE);
		setResponseBody(new ResponseBody(message, json));
	}
}
