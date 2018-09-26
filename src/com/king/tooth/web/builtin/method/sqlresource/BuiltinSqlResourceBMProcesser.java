package com.king.tooth.web.builtin.method.sqlresource;

import java.util.List;
import java.util.Map;

import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.AbstractCommonBuiltinBMProcesser;
import com.king.tooth.web.builtin.method.sqlresource.query.BuiltinQueryMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.querycond.BuiltinQueryCondMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.recursive.BuiltinRecursiveMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sort.BuiltinSortMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sqlscript.BuiltinSqlMethodProcesser;

/**
 * sql资源的内置函数的处理器对外接口
 * @author DougLei
 */
public class BuiltinSqlResourceBMProcesser extends AbstractCommonBuiltinBMProcesser{
	
	/**
	 * 内置查询函数处理器实例
	 */
	private BuiltinQueryMethodProcesser queryProcesser;
	/**
	 * 内置递归函数处理器实例
	 */
	private BuiltinRecursiveMethodProcesser recursiveProcesser;
	/**
	 * 内置排序函数处理器实例
	 */
	private BuiltinSortMethodProcesser sortProcesser;
	/**
	 * 内置查询条件函数处理器实例
	 */
	private BuiltinQueryCondMethodProcesser querycondProcesser;
	/**
	 * 内置sql脚本处理器
	 */
	private BuiltinSqlMethodProcesser sqlScriptMethodProcesser;
	
	/**
	 * 解析请求的url参数集合
	 * 调用不同的子类去处理参数
	 * @param reqSqlScriptResource
	 * @param requestBuiltinParams
	 * @param requestResourceParams
	 * @param requestParentResourceParams
	 * @param sqlParameterValues
	 */
	private void analysisRequestUrlParams(ComSqlScript reqSqlScriptResource, Map<String, String> requestBuiltinParams, Map<String, String> requestResourceParams, Map<String, String> requestParentResourceParams, List<List<Object>> sqlParameterValues) {
		// 内置聚焦函数处理器实例
		setFocusedIdProcesser(requestBuiltinParams);
		// 内置分页函数处理器实例
		setPagerProcesser(requestBuiltinParams);
		// 内置查询函数处理器实例
		setQueryProcesser(requestBuiltinParams);
		// 内置排序函数处理器实例
		setSortProcesser(requestBuiltinParams);
		// 内置递归函数处理器实例
		setRecursiveProcesser(requestBuiltinParams, requestParentResourceParams, sqlParameterValues);
		// 内置sql脚本处理器实例
		setSqlScriptMethodProcesser(reqSqlScriptResource, sqlParameterValues);
		// 最后剩下的数据，就都是条件查询的参数了【这个一定要放到最后被调用！】
		// 内置查询条件函数处理器
		setQuerycondProcesser(requestResourceParams, sqlParameterValues);
	}
	
	/**
	 * 内置sql脚本处理器实例
	 * @param reqSqlScriptResource 
	 * @param requestResourceParams
	 * @param sqlParameterValues 
	 */
	public void setSqlScriptMethodProcesser(ComSqlScript reqSqlScriptResource, List<List<Object>> sqlParameterValues) {
		sqlScriptMethodProcesser = new BuiltinSqlMethodProcesser(reqSqlScriptResource);
		sqlScriptMethodProcesser.setResourceName(resourceName);
		sqlScriptMethodProcesser.setParentResourceName(parentResourceName);
		sqlScriptMethodProcesser.setSqlParameterValues(sqlParameterValues);
	}

	/**
	 * 内置查询函数处理器实例
	 * @param requestBuiltinParams
	 */
	private void setQueryProcesser(Map<String, String> requestBuiltinParams) {
		String resultType = requestBuiltinParams.remove("_resultType");
		String select = requestBuiltinParams.remove("_select");
		String split = requestBuiltinParams.remove("_split");
		queryProcesser = new BuiltinQueryMethodProcesser(resultType, select, split);
		queryProcesser.setResourceName(resourceName);
	}
	/**
	 * 内置排序函数处理器实例
	 * @param requestBuiltinParams
	 */
	private void setSortProcesser(Map<String, String> requestBuiltinParams) {
		String sort = requestBuiltinParams.remove("_sort");
		if(StrUtils.notEmpty(sort)){
			sortProcesser = new BuiltinSortMethodProcesser(sort);
			sortProcesser.setResourceName(resourceName);
		}
	}
	
	/**
	 * 内置递归函数处理器实例
	 * @param requestBuiltinParams
	 * @param requestParentResourceParams 
	 * @param sqlParameterValues
	 */
	private void setRecursiveProcesser(Map<String, String> requestBuiltinParams, Map<String, String> requestParentResourceParams, List<List<Object>> sqlParameterValues) {
		String recursive = requestBuiltinParams.remove("_recursive");
		String deepLevel = requestBuiltinParams.remove("_deep");
		
		if(StrUtils.notEmpty(parentResourceId)){
			if(StrUtils.isEmpty(recursive)){
				recursive = "false";// 默认不进行递归查询
			}
			if(StrUtils.isEmpty(deepLevel)){
				deepLevel = "2";// 默认递归查询钻取的深度为2
			}
			
			recursiveProcesser = new BuiltinRecursiveMethodProcesser(recursive, deepLevel, requestParentResourceParams);
			recursiveProcesser.setResourceName(resourceName);
			recursiveProcesser.setParentResourceId(parentResourceId);
			recursiveProcesser.setSqlParameterValues(sqlParameterValues);
		}
	}
	
	/**
	 * 最后剩下的数据，就都是条件查询的参数了
	 * 内置查询条件函数处理器
	 * @param requestResourceParams
	 * @param sqlParameterValues sql参数值集合
	 */
	private void setQuerycondProcesser(Map<String, String> requestResourceParams, List<List<Object>> sqlParameterValues) {
		if(requestResourceParams.size() > 0){
			querycondProcesser = new BuiltinQueryCondMethodProcesser(requestResourceParams);
			querycondProcesser.setResourceName(resourceName);
			querycondProcesser.setSqlParameterValues(sqlParameterValues);
		}
	}
	
	public BuiltinSqlResourceBMProcesser(ComSqlScript reqSqlScriptResource, Map<String, String> requestBuiltinParams, Map<String, String> requestResourceParams, Map<String, String> requestParentResourceParams, List<List<Object>> sqlParameterValues){
		this.resourceName = requestResourceParams.remove(BuiltinParameterKeys.RESOURCE_NAME);
		this.parentResourceName = requestResourceParams.remove(BuiltinParameterKeys.PARENT_RESOURCE_NAME);
		this.parentResourceId = requestParentResourceParams.remove(BuiltinParameterKeys.PARENT_RESOURCE_ID);
		
		analysisRequestUrlParams(reqSqlScriptResource, requestBuiltinParams, requestResourceParams, requestParentResourceParams, sqlParameterValues);// 解析请求的url参数集合，获取不同的子类去解析对应的参数
	}
	
	public BuiltinQueryMethodProcesser getQueryProcesser() {
		if(queryProcesser == null){
			queryProcesser = new BuiltinQueryMethodProcesser();
		}
		return queryProcesser;
	}
	public BuiltinRecursiveMethodProcesser getRecursiveProcesser() {
		if(recursiveProcesser == null){
			recursiveProcesser = new BuiltinRecursiveMethodProcesser();
		}
		return recursiveProcesser;
	}
	public BuiltinSortMethodProcesser getSortProcesser() {
		if(sortProcesser == null){
			sortProcesser = new BuiltinSortMethodProcesser();
		}
		return sortProcesser;
	}
	public BuiltinQueryCondMethodProcesser getQuerycondProcesser() {
		if(querycondProcesser == null){
			querycondProcesser = new BuiltinQueryCondMethodProcesser();
		}
		return querycondProcesser;
	}
	public BuiltinSqlMethodProcesser getSqlScriptMethodProcesser() {
		if(sqlScriptMethodProcesser == null){
			sqlScriptMethodProcesser = new BuiltinSqlMethodProcesser();
		}
		return sqlScriptMethodProcesser;
	}
	
	/**
	 * 释放不用的内存
	 */
	public void releaseInvalidMemory(){
		if(queryProcesser != null){
			queryProcesser.clearInvalidMemory();
		}
		if(sortProcesser != null){
			sortProcesser.clearInvalidMemory();
		}
		if(querycondProcesser != null){
			querycondProcesser.clearInvalidMemory();
		}
		if(sqlScriptMethodProcesser != null){
			sqlScriptMethodProcesser.clearInvalidMemory();
		}
		if(focusedIdProcesser != null){
			focusedIdProcesser.clearInvalidMemory();
		}
	}
}
