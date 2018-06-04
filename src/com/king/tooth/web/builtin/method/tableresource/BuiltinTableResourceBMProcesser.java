package com.king.tooth.web.builtin.method.tableresource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.king.tooth.constants.RequestUrlParamKeyConstants;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.AbstractCommonBuiltinBMProcesser;
import com.king.tooth.web.builtin.method.tableresource.parentsub.BuiltinParentsubQueryMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.query.BuiltinQueryMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.querycond.BuiltinQueryCondMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.recursive.BuiltinRecursiveMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.sort.BuiltinSortMethodProcesser;

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
	 * 解析请求的url参数集合
	 * 调用不同的子类去处理参数
	 * @param requestUrlParams
	 * @param hqlParameterValues 查询条件参数值集合
	 */
	private void analysisRequestUrlParams(Map<String, String> requestUrlParams, List<Object> hqlParameterValues) {
		// 内置聚焦函数处理器实例
		setFocusedIdProcesser(requestUrlParams);
		// 内置分页函数处理器实例
		setPagerProcesser(requestUrlParams);
		// 内置查询函数处理器实例
		setQueryProcesser(requestUrlParams);
		// 内置排序函数处理器实例
		setSortProcesser(requestUrlParams);
		// 内置递归函数处理器实例
		setRecursiveProcesser(requestUrlParams, hqlParameterValues);
		// 内置父子资源链接查询函数处理器实例
		setParentsubQueryMethodProcesser(requestUrlParams, hqlParameterValues);
		// 最后剩下的数据，就都是条件查询的参数了【这个一定要放到最后被调用！】
		// 内置查询条件函数处理器
		setQuerycondProcesser(requestUrlParams, hqlParameterValues);
	}
	
	/**
	 * 解析父资源的查询条件
	 * <p>说明：因为递归查询和父子表查询都要用到这个方法，在最后调用这个方法的时候，再把isRemoveUrlParam的值设为true，从集合中删除解析出来的数据，否则会造成第一次解析就移除的数据，后面再调用就无法解析到的bug</p>
	 * @param parentResourceQueryCond
	 * @param requestUrlParams
	 */
	private void anlaysisParentResourceQueryCond(Map<String, String> parentResourceQueryCond, Map<String, String> requestUrlParams) {
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
			Set<String> keys = requestUrlParams.keySet();
			for (String k : keys) {
				if(k.startsWith(parentResourceId)){
					parentResourceQueryCond.put(k.replace(parentResourceId, ""), requestUrlParams.get(k));
				}
			}
			
			// 如果找到父资源的查询条件，则将其从requestUrlParams中移除
			if(parentResourceQueryCond.size() > 0){
				keys = parentResourceQueryCond.keySet();
				for (String k : keys) {
					requestUrlParams.remove(parentResourceId+k);
				}
			}
		}
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
	 * 内置递归函数处理器实例
	 * @param requestUrlParams
	 * @param hqlParameterValues
	 */
	private void setRecursiveProcesser(Map<String, String> requestUrlParams, List<Object> hqlParameterValues) {
		String recursive = requestUrlParams.remove("_recursive");
		String deepLevel = requestUrlParams.remove("_deep");
		
		if(StrUtils.notEmpty(parentResourceId)){
			if(StrUtils.isEmpty(recursive)){
				recursive = "false";// 默认不进行递归查询
			}
			if(StrUtils.isEmpty(deepLevel)){
				deepLevel = "2";// 默认递归查询钻取的深度为2
			}
			
			Map<String, String> parentResourceQueryCond = null;
			if(StrUtils.compareIsSame(resourceName, parentResourceName)){// 主资源名和资源名相同，是递归查询
				parentResourceQueryCond = new HashMap<String, String>();
				// 解析父资源查询条件参数
				anlaysisParentResourceQueryCond(parentResourceQueryCond, requestUrlParams);
			}
			
			recursiveProcesser = new BuiltinRecursiveMethodProcesser(recursive, deepLevel, parentResourceQueryCond);
			recursiveProcesser.setResourceName(resourceName);
			recursiveProcesser.setParentResourceId(parentResourceId);
			recursiveProcesser.setHqlParameterValues(hqlParameterValues);
		}
	}
	/**
	 * 内置父子资源链接查询函数处理器实例 
	 * @param requestUrlParams
	 * @param hqlParameterValues hql参数值集合
	 */
	private void setParentsubQueryMethodProcesser(Map<String, String> requestUrlParams, List<Object> hqlParameterValues) {
		String isSimpleParentSubQueryModel = requestUrlParams.remove("_simplemodel");
		String refParentSubPropName = requestUrlParams.remove("_refparsubpropame");
		if(StrUtils.notEmpty(parentResourceId) && StrUtils.notEmpty(parentResourceName)){
			
			Map<String, String> parentResourceQueryCond = null;
			if(StrUtils.compareUnSame(resourceName, parentResourceName)){// 主资源名和资源名不相同，是主子资源联合查询
				parentResourceQueryCond = new HashMap<String, String>();
				// 解析父资源查询条件参数
				anlaysisParentResourceQueryCond(parentResourceQueryCond, requestUrlParams);
			}
			
			parentsubQueryMethodProcesser = new BuiltinParentsubQueryMethodProcesser(parentResourceQueryCond, isSimpleParentSubQueryModel, refParentSubPropName);
			parentsubQueryMethodProcesser.setResourceName(resourceName);
			parentsubQueryMethodProcesser.setParentResourceId(parentResourceId);
			parentsubQueryMethodProcesser.setParentResourceName(parentResourceName);
			parentsubQueryMethodProcesser.setHqlParameterValues(hqlParameterValues);
		}
	}
	/**
	 * 最后剩下的数据，就都是条件查询的参数了
	 * 内置查询条件函数处理器
	 * @param requestUrlParams
	 * @param hqlParameterValues hql参数值集合
	 */
	private void setQuerycondProcesser(Map<String, String> requestUrlParams, List<Object> hqlParameterValues) {
		if(requestUrlParams.size() > 0){
			querycondProcesser = new BuiltinQueryCondMethodProcesser(requestUrlParams);
			querycondProcesser.setResourceName(resourceName);
			querycondProcesser.setHqlParameterValues(hqlParameterValues);
		}
	}
	
	/**
	 * 构造函数
	 * @param requestUrlParams
	 * @param queryCondParameterValues hql参数值集合
	 * @param requestResourceType 请求的资源类型 @see ISysResource.XXX_RESOURCE_TYPE
	 */
	public BuiltinTableResourceBMProcesser(Map<String, String> requestUrlParams, List<Object> hqlParameterValues){
		// 这三个key值来自      @see PlatformServlet.processSpecialData()
		this.resourceName = requestUrlParams.remove(RequestUrlParamKeyConstants.RESOURCE_NAME);
		this.parentResourceName = requestUrlParams.remove(RequestUrlParamKeyConstants.PARENT_RESOURCE_NAME);
		this.parentResourceId = requestUrlParams.remove(RequestUrlParamKeyConstants.PARENT_RESOURCE_ID);
		
		analysisRequestUrlParams(requestUrlParams, hqlParameterValues);// 解析请求的url参数集合，获取不同的子类去解析对应的参数
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
	}
}
