package com.king.tooth.web.builtin.method;

import java.util.Map;

import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.king.tooth.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;

/**
 * 公共抽象的内置函数的处理器对外接口
 * <p>目的在与提取BuiltinTableResourceBMProcesser和BuiltinSqlResourceBMProcesser公共的代码</p>
 * @author DougLei
 */
public abstract class AbstractCommonBuiltinBMProcesser {
	
	/**
	 * 请求的资源名
	 */
	protected String resourceName;
	/**
	 * 请求的父亲资源名
	 */
	protected String parentResourceName;
	/**
	 * 请求的父亲资源Id
	 */
	protected String parentResourceId;
	
	/**
	 * 内置聚焦函数处理器实例
	 */
	protected BuiltinFocusedIdMethodProcesser focusedIdProcesser;
	/**
	 * 内置分页函数处理器实例
	 */
	protected BuiltinPagerMethodProcesser pagerProcesser;
	
	/**
	 * 内置聚焦函数处理器实例
	 * @param requestUrlParams
	 */
	protected void setFocusedIdProcesser(Map<String, String> requestUrlParams) {
		String focusedId = requestUrlParams.remove("_focusedid");
		if(StrUtils.notEmpty(focusedId)){
			focusedIdProcesser = new BuiltinFocusedIdMethodProcesser(focusedId);
		}
	}
	/**
	 * 内置分页函数处理器实例
	 * @param requestUrlParams
	 */
	protected void setPagerProcesser(Map<String, String> requestUrlParams) {
		String limit = requestUrlParams.remove("_limit");
		String start = requestUrlParams.remove("_start");
		
		String rows = requestUrlParams.remove("_rows");
		String page = requestUrlParams.remove("_page");// 这四个参数的内容，需要理清楚
		if((StrUtils.notEmpty(limit) && StrUtils.notEmpty(start)) || (StrUtils.notEmpty(rows) && StrUtils.notEmpty(page))){
			pagerProcesser = new BuiltinPagerMethodProcesser(limit, start, rows, page);
		}
	}
	
	
	public BuiltinFocusedIdMethodProcesser getFocusedIdProcesser() {
		if(focusedIdProcesser == null){
			focusedIdProcesser = new BuiltinFocusedIdMethodProcesser();
		}
		return focusedIdProcesser;
	}
	public BuiltinPagerMethodProcesser getPagerProcesser() {
		if(pagerProcesser == null){
			pagerProcesser = new BuiltinPagerMethodProcesser();
		}
		return pagerProcesser;
	}
}
