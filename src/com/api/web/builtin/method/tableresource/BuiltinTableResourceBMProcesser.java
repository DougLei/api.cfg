package com.api.web.builtin.method.tableresource;

import java.util.List;
import java.util.Map;

import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.util.StrUtils;
import com.api.web.builtin.method.AbstractCommonBuiltinBMProcesser;
import com.api.web.builtin.method.tableresource.parentsub.BuiltinParentsubQueryMethodProcesser;
import com.api.web.builtin.method.tableresource.query.BuiltinQueryMethodProcesser;
import com.api.web.builtin.method.tableresource.querycond.BuiltinQueryCondMethodProcesser;
import com.api.web.builtin.method.tableresource.recursive.BuiltinRecursiveMethodProcesser;
import com.api.web.builtin.method.tableresource.sort.BuiltinSortMethodProcesser;
import com.api.web.builtin.method.tableresource.sublist.BuiltinSublistMethodProcesser;
import com.api.web.entity.request.RequestBody;

/**
 * 表资源的内置函数的处理器对外接口
 * @author DougLei
 */
public class BuiltinTableResourceBMProcesser extends AbstractCommonBuiltinBMProcesser{
	
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
	 * 内置父子资源链接查询函数处理器实例
	 */
	private BuiltinParentsubQueryMethodProcesser parentsubQueryMethodProcesser;
	/**
	 * 内置子列表资源查询函数处理器实例 
	 */
	private BuiltinSublistMethodProcesser sublistMethodProcesser;
	
	/**
	 * 解析请求的url参数集合
	 * 调用不同的子类去处理参数
	 * @param hqlParameterValues
	 */
	private void analysisRequestUrlParams(List<Object> hqlParameterValues) {
		// 内置聚焦函数处理器实例
		setFocusedIdProcesser(requestBuiltinParams);
		setTurnToObjectProcesser(requestBuiltinParams);
		// 内置创建导出文件的函数处理器
		setCreateExportFileProcesser(requestBuiltinParams);
		// 内置分页函数处理器实例
		setPagerProcesser(requestBuiltinParams);
		// 内置查询函数处理器实例
		setQueryProcesser(requestBuiltinParams);
		// 内置排序函数处理器实例
		setSortProcesser(requestBuiltinParams);
		// 内置子资源数据集合查询函数处理器实例
		setSublistMethodProcesser(requestBuiltinParams);
		// 内置递归函数处理器实例
		setRecursiveProcesser(requestBuiltinParams, requestParentResourceParams, hqlParameterValues);
		// 内置父子资源链接查询函数处理器实例
		setParentsubQueryMethodProcesser(requestBuiltinParams, requestParentResourceParams, hqlParameterValues);
		// 最后剩下的数据，就都是条件查询的参数了【这个一定要放到最后被调用！】
		// 内置查询条件函数处理器
		setQuerycondProcesser(requestResourceParams, hqlParameterValues);
	}
	
	/**
	 * 内置查询函数处理器实例
	 * @param requestBuiltinParams
	 */
	private void setQueryProcesser(Map<String, String> requestBuiltinParams) {
		String resultType = requestBuiltinParams.remove("_resultType");
		String select = requestBuiltinParams.remove("_select");
		String split = requestBuiltinParams.remove("_split");
		if(isCreateExport){
			select = exportSelectPropNames;
		}
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
	 * 内置子列表资源查询函数处理器实例 
	 * @param requestBuiltinParams
	 * @param hqlParameterValues hql参数值集合
	 */
	public void setSublistMethodProcesser(Map<String, String> requestBuiltinParams) {
		String subResourceName = requestBuiltinParams.remove("_subResourceName");
		String subListRefPropName = requestBuiltinParams.remove("_subListRefPropName");
		String subSort = requestBuiltinParams.remove("_subSort");
		if(StrUtils.notEmpty(subResourceName)){
			sublistMethodProcesser = new BuiltinSublistMethodProcesser(subResourceName, subListRefPropName, subSort);
		}
	}
	/**
	 * 内置递归函数处理器实例
	 * @param requestBuiltinParams
	 * @param requestParentResourceParams 
	 * @param hqlParameterValues
	 */
	private void setRecursiveProcesser(Map<String, String> requestBuiltinParams, Map<String, String> requestParentResourceParams, List<Object> hqlParameterValues) {
		String recursive = requestBuiltinParams.remove("_recursive");
		String deepLevel = requestBuiltinParams.remove("_deep");
		String recursiveRefPropName = requestBuiltinParams.remove("_recursiveRefPropName");
		
		if(StrUtils.notEmpty(parentResourceId)){
			if(StrUtils.isEmpty(recursive)){
				recursive = "false";// 默认不进行递归查询
			}
			if(StrUtils.isEmpty(deepLevel)){
				deepLevel = "2";// 默认递归查询钻取的深度为2
			}
			
			recursiveProcesser = new BuiltinRecursiveMethodProcesser(recursive, deepLevel, recursiveRefPropName, requestParentResourceParams);
			recursiveProcesser.setResourceName(resourceName);
			recursiveProcesser.setParentResourceId(parentResourceId);
			recursiveProcesser.setHqlParameterValues(hqlParameterValues);
			recursiveProcesser.setQueryResourceMetadataInfos(queryResourceMetadataInfos);
		}
	}
	/**
	 * 内置父子资源链接查询函数处理器实例 
	 * @param requestBuiltinParams
	 * @param requestParentResourceParams 
	 * @param hqlParameterValues hql参数值集合
	 */
	private void setParentsubQueryMethodProcesser(Map<String, String> requestBuiltinParams, Map<String, String> requestParentResourceParams, List<Object> hqlParameterValues) {
		String isSimpleParentSubQueryModel = requestBuiltinParams.remove("_simpleModel");
		String psRefPropName = requestBuiltinParams.remove("_psRefPropName");
		if(StrUtils.notEmpty(parentResourceId) && StrUtils.notEmpty(parentResourceName)){
			parentsubQueryMethodProcesser = new BuiltinParentsubQueryMethodProcesser(requestParentResourceParams, isSimpleParentSubQueryModel, psRefPropName);
			parentsubQueryMethodProcesser.setResourceName(resourceName);
			parentsubQueryMethodProcesser.setParentResourceId(parentResourceId);
			parentsubQueryMethodProcesser.setParentResourceName(parentResourceName);
			parentsubQueryMethodProcesser.setHqlParameterValues(hqlParameterValues);
			parentsubQueryMethodProcesser.setQueryParentResourceMetadataInfos(queryParentResourceMetadataInfos);
		}
	}
	/**
	 * 最后剩下的数据，就都是条件查询的参数了
	 * 内置查询条件函数处理器
	 * @param requestResourceParams
	 * @param hqlParameterValues hql参数值集合
	 */
	private void setQuerycondProcesser(Map<String, String> requestResourceParams, List<Object> hqlParameterValues) {
		if(requestResourceParams.size() > 0){
			querycondProcesser = new BuiltinQueryCondMethodProcesser(requestResourceParams);
			querycondProcesser.setResourceName(resourceName);
			querycondProcesser.setHqlParameterValues(hqlParameterValues);
			querycondProcesser.setQueryResourceMetadataInfos(queryResourceMetadataInfos);
		}
	}
	
	public BuiltinTableResourceBMProcesser(RequestBody requestBody, List<Object> hqlParameterValues){
		requestBuiltinParams = requestBody.getRequestBuiltinParams();
		requestResourceParams = requestBody.getRequestResourceParams();
		requestParentResourceParams = requestBody.getRequestParentResourceParams();
		queryResourceMetadataInfos = requestBody.getQueryResourceMetadataInfos();
		queryParentResourceMetadataInfos = requestBody.getQueryParentResourceMetadataInfos();
		
		this.resourceName = requestResourceParams.remove(BuiltinParameterKeys.RESOURCE_NAME);
		this.parentResourceName = requestResourceParams.remove(BuiltinParameterKeys.PARENT_RESOURCE_NAME);
		this.parentResourceId = requestParentResourceParams.remove(BuiltinParameterKeys.PARENT_RESOURCE_ID);
		analysisRequestUrlParams(hqlParameterValues);// 解析请求的url参数集合，获取不同的子类去解析对应的参数
	}
	
	public BuiltinQueryMethodProcesser getQueryProcesser() {
		if(queryProcesser == null){
			queryProcesser = new BuiltinQueryMethodProcesser();
		}
		return queryProcesser;
	}
	public BuiltinSublistMethodProcesser getSublistMethodProcesser() {
		if(sublistMethodProcesser == null){
			sublistMethodProcesser = new BuiltinSublistMethodProcesser();
		}
		return sublistMethodProcesser;
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
	public BuiltinParentsubQueryMethodProcesser getParentsubQueryMethodProcesser() {
		if(parentsubQueryMethodProcesser == null){
			parentsubQueryMethodProcesser = new BuiltinParentsubQueryMethodProcesser();
		}
		return parentsubQueryMethodProcesser;
	}

	/**
	 * 释放不用的内存
	 */
	public void releaseInvalidMemory(){
		if(queryProcesser != null){
			queryProcesser.clearInvalidMemory();
		}
		if(recursiveProcesser != null){
			recursiveProcesser.clearInvalidMemory();
		}
		if(sortProcesser != null){
			sortProcesser.clearInvalidMemory();
		}
		if(querycondProcesser != null){
			querycondProcesser.clearInvalidMemory();
		}
		if(parentsubQueryMethodProcesser != null){
			parentsubQueryMethodProcesser.clearInvalidMemory();
		}
		if(sublistMethodProcesser != null){
			sublistMethodProcesser.clearInvalidMemory();
		}
		if(focusedIdProcesser != null){
			focusedIdProcesser.clearInvalidMemory();
		}
	}
}
