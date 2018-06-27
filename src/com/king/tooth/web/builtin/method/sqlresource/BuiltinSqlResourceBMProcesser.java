package com.king.tooth.web.builtin.method.sqlresource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.king.tooth.constants.RequestUrlParamKeyConstants;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameter;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.AbstractCommonBuiltinBMProcesser;
import com.king.tooth.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.king.tooth.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.query.BuiltinQueryMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.querycond.BuiltinQueryCondMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sort.BuiltinSortMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sqlscript.BuiltinSqlScriptMethodProcesser;

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
	private BuiltinSqlScriptMethodProcesser sqlScriptMethodProcesser;
	
	/**
	 * 请求体
	 */
	private Object requestFormData;
	
	/**
	 * 解析请求的url参数集合
	 * 调用不同的子类去处理参数
	 * @param reqSqlScriptResource
	 * @param requestUrlParams
	 * @param sqlParameterValues 查询条件参数值集合
	 */
	private void analysisRequestUrlParams(ComSqlScript reqSqlScriptResource, Map<String, String> requestUrlParams, List<List<Object>> sqlParameterValues) {
		// 内置聚焦函数处理器实例
		setFocusedIdProcesser(requestUrlParams);
		// 内置分页函数处理器实例
		setPagerProcesser(requestUrlParams);
		// 内置查询函数处理器实例
		setQueryProcesser(requestUrlParams);
		// 内置排序函数处理器实例
		setSortProcesser(requestUrlParams);
		// 内置sql脚本处理器实例
		setSqlScriptMethodProcesser(reqSqlScriptResource, requestUrlParams, sqlParameterValues);
		// 最后剩下的数据，就都是条件查询的参数了【这个一定要放到最后被调用！】
		// 内置查询条件函数处理器
		setQuerycondProcesser(requestUrlParams, sqlParameterValues);
	}
	
	/**
	 * 解析sql脚本参数
	 * @param sqlScript
	 * @param requestUrlParams
	 * @return
	 */
	private Map<String, String> analysisSqlScriptParam(ComSqlScript sqlScript, Map<String, String> requestUrlParams) {
		Map<String, String> sqlScriptParams = null;
		List<SqlScriptParameter> sqlScriptParameters = sqlScript.getSqlScriptParameterList();
		if(sqlScriptParameters != null && sqlScriptParameters.size() > 0 && requestUrlParams.size() > 0){
			sqlScriptParams = new HashMap<String, String>(16);// 默认初始长度为16
			
			Set<String> keys = requestUrlParams.keySet();
			for (SqlScriptParameter ssp : sqlScriptParameters) {
				for (String key : keys) {
					if(key.equalsIgnoreCase(ssp.getParameterName())){
						sqlScriptParams.put(key, requestUrlParams.get(key));
						break;
					}
				}
			}
			
			if(sqlScriptParams.size() > 0){
				keys = sqlScriptParams.keySet();
				for (String key : keys) {
					requestUrlParams.remove(key);
				}
			}
		}
		return sqlScriptParams;
	}
	
	/**
	 * 内置sql脚本处理器实例
	 * @param reqSqlScriptResource 
	 * @param requestUrlParams
	 * @param sqlParameterValues 
	 */
	public void setSqlScriptMethodProcesser(ComSqlScript reqSqlScriptResource, Map<String, String> requestUrlParams, List<List<Object>> sqlParameterValues) {
		Map<String, String> sqlScriptParams = analysisSqlScriptParam(reqSqlScriptResource, requestUrlParams);
		sqlScriptMethodProcesser = new BuiltinSqlScriptMethodProcesser(reqSqlScriptResource, sqlScriptParams, requestFormData);
		sqlScriptMethodProcesser.setResourceName(resourceName);
		sqlScriptMethodProcesser.setParentResourceName(parentResourceName);
		sqlScriptMethodProcesser.setSqlParameterValues(sqlParameterValues);
	}

	/**
	 * 内置查询函数处理器实例
	 * @param requestUrlParams
	 */
	private void setQueryProcesser(Map<String, String> requestUrlParams) {
		String resultType = requestUrlParams.remove("_resulttype");
		String select = requestUrlParams.remove("_select");
		String split = requestUrlParams.remove("_split");
		queryProcesser = new BuiltinQueryMethodProcesser(resultType, select, split);
		queryProcesser.setResourceName(resourceName);
	}
	/**
	 * 内置排序函数处理器实例
	 * @param requestUrlParams
	 */
	private void setSortProcesser(Map<String, String> requestUrlParams) {
		String sort = requestUrlParams.remove("_sort");
		if(StrUtils.notEmpty(sort)){
			sortProcesser = new BuiltinSortMethodProcesser(sort);
			sortProcesser.setResourceName(resourceName);
		}
	}
	
	/**
	 * 最后剩下的数据，就都是条件查询的参数了
	 * 内置查询条件函数处理器
	 * @param requestUrlParams
	 * @param sqlParameterValues sql参数值集合
	 */
	private void setQuerycondProcesser(Map<String, String> requestUrlParams, List<List<Object>> sqlParameterValues) {
		if(requestUrlParams.size() > 0){
			querycondProcesser = new BuiltinQueryCondMethodProcesser(requestUrlParams);
			querycondProcesser.setResourceName(resourceName);
			querycondProcesser.setSqlParameterValues(sqlParameterValues);
		}
	}
	
	/**
	 * 构造函数
	 * @param reqSqlScriptResource 
	 * @param requestUrlParams
	 * @param sqlParameterValues sql参数值集合
	 * @param requestFormData
	 */
	public BuiltinSqlResourceBMProcesser(ComSqlScript reqSqlScriptResource, Map<String, String> requestUrlParams, Object requestFormData, List<List<Object>> sqlParameterValues){
		// 这三个key值来自      @see PlatformServlet.processSpecialData()
		this.resourceName = requestUrlParams.remove(RequestUrlParamKeyConstants.RESOURCE_NAME);
		this.parentResourceName = requestUrlParams.remove(RequestUrlParamKeyConstants.PARENT_RESOURCE_NAME);
		this.parentResourceId = requestUrlParams.remove(RequestUrlParamKeyConstants.PARENT_RESOURCE_ID);
		this.requestFormData = requestFormData;
		
		analysisRequestUrlParams(reqSqlScriptResource, requestUrlParams, sqlParameterValues);// 解析请求的url参数集合，获取不同的子类去解析对应的参数
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
	public BuiltinQueryMethodProcesser getQueryProcesser() {
		if(queryProcesser == null){
			queryProcesser = new BuiltinQueryMethodProcesser();
		}
		return queryProcesser;
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
	public BuiltinSqlScriptMethodProcesser getSqlScriptMethodProcesser() {
		if(sqlScriptMethodProcesser == null){
			sqlScriptMethodProcesser = new BuiltinSqlScriptMethodProcesser();
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
	}
}
