package com.king.tooth.web.processer.tableresource.delete;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.tableresource.RequestProcesser;

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
	 * @param deleteIds
	 * @param isSuccess
	 */
	protected final void installResponseBodyForDeleteData(List<Object> deleteIds, boolean isSuccess){
		JSONObject json = new JSONObject(1);
		json.put(ResourceNameConstants.IDS, deleteIds.toString().replace("[", "").replace("]", ""));
		setResponseBody(new ResponseBody(json, isSuccess));
	}
}
