package com.king.tooth.web.builtin.method;

import java.util.Map;
import java.util.Set;

import com.king.tooth.constants.ResourcePropNameConstants;
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
		String focusedId = requestUrlParams.remove("_focusedId");
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
	
	/**
	 * 解析父资源的查询条件
	 * @param parentResourceQueryCond
	 * @param requestUrlParams
	 */
	protected void anlaysisParentResourceQueryCond(Map<String, String> parentResourceQueryCond, Map<String, String> requestUrlParams) {
		/* 
		 * 系统处理逻辑说明：在请求的url中
		 * 
		 * 父资源id的值可以使用   <"_"+"名称"(例如:_root，以下就用_root说明)> 的方式书写，实现占位符对象功能
		 * 在请求的url参数中，可以通过 _root.父资源属性名=xxx，设置查询父资源时的条件
		 * 其中:	_root变量必须以'_'下划线开头
		 *    	_xxx中的xxx，可以自定义，在url参数中，必须用_xxx.父资源属性名=值来设置查询条件
		 */
		if(requestUrlParams.size() > 0 && parentResourceId.startsWith("_")){
			parentResourceId = parentResourceId.toLowerCase() + ".";
			
			// 在requestUrlParams中寻找，是否有父资源的查询条件
			String pid = null;
			Set<String> keys = requestUrlParams.keySet();
			String tmpKey;
			for (String k : keys) {
				if(k.startsWith(parentResourceId)){
					tmpKey = k.replace(parentResourceId, "");
					if(tmpKey.equals(ResourcePropNameConstants.ID)){
						pid = requestUrlParams.get(k);
					}else{
						parentResourceQueryCond.put(k.replace(parentResourceId, ""), requestUrlParams.get(k));
					}
				}
			}
			
			// 如果找到父资源的查询条件，则将其从requestUrlParams中移除
			if(parentResourceQueryCond.size() > 0){
				keys = parentResourceQueryCond.keySet();
				for (String k : keys) {
					requestUrlParams.remove(parentResourceId+k);
				}
			}
			
			if(pid != null){
				requestUrlParams.remove(parentResourceId+ResourcePropNameConstants.ID);
				parentResourceId = pid;
			}
		}
	}
}
