package com.king.tooth.web.processer.tableresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.king.tooth.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.query.BuiltinQueryMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.recursive.BuiltinRecursiveMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.sort.BuiltinSortMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.sublist.BuiltinSublistMethodProcesser;
import com.king.tooth.web.entity.resulttype.PageResultEntity;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.entity.resulttype.TextResultEntity;
import com.king.tooth.web.processer.tableresource.RequestProcesser;

/**
 * get请求处理器
 * @author DougLei
 */
public abstract class GetProcesser extends RequestProcesser {
	
	/**
	 * select
	 */
	protected BuiltinQueryMethodProcesser builtinQueryMethodProcesser;
	
	/**
	 * order by
	 */
	protected BuiltinSortMethodProcesser builtinSortMethodProcesser;
	
	/**
	 * 分页查询
	 */
	protected BuiltinPagerMethodProcesser builtinPagerMethodProcesser;
	
	/**
	 * 聚焦数据
	 */
	protected BuiltinFocusedIdMethodProcesser builtinFocusedIdMethodProcesser;
	
	/**
	 * 递归
	 */
	protected BuiltinRecursiveMethodProcesser builtinRecursiveMethodProcesser;
	
	/**
	 * 子资源集合
	 */
	protected BuiltinSublistMethodProcesser builtinSublistMethodProcesser;
	
	/**
	 * 处理请求
	 */
	protected final boolean doProcess() {
		initBuiltinMethods();
		boolean isKeepOn = doGetProcess();
		return isKeepOn;
	}
	
	/**
	 * 初始化内置的函数属性对象
	 * 方便子类使用
	 */
	private void initBuiltinMethods(){
		builtinQueryCondMethodProcesser = builtinTableResourceBMProcesser.getQuerycondProcesser();
		builtinQueryMethodProcesser = builtinTableResourceBMProcesser.getQueryProcesser();
		builtinSortMethodProcesser = builtinTableResourceBMProcesser.getSortProcesser();
		builtinPagerMethodProcesser = builtinTableResourceBMProcesser.getPagerProcesser();
		builtinFocusedIdMethodProcesser = builtinTableResourceBMProcesser.getFocusedIdProcesser();
		builtinRecursiveMethodProcesser = builtinTableResourceBMProcesser.getRecursiveProcesser();
		builtinParentsubQueryMethodProcesser = builtinTableResourceBMProcesser.getParentsubQueryMethodProcesser();
		builtinSublistMethodProcesser = builtinTableResourceBMProcesser.getSublistMethodProcesser();
	}
	
	/**
	 * 处理get请求
	 * @return
	 */
	protected abstract boolean doGetProcess();
	
	/**
	 * 获取包括from在内的，后续的hql语句，由各个子类实现
	 * <p>例1： from tableName where xxxx</p>
	 * <p>例2： from tableName where id in (select pid from subTableName where id = xxx) and xxx </p>
	 * @return
	 */
	protected abstract StringBuilder getFromHql();
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法
	
	/**
	 * 分页查询，加载PageResultEntity类的实例，如果调用了分页查询的功能，则同时将查询的条件值set到query参数对象中
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query 
	 * @return 分页结果对象
	 */
	protected final PageResultEntity loadPageResultEntity(Query query){
		PageResultEntity pageResultEntity = null;
		if(builtinPagerMethodProcesser.getIsUsed()){
			pageResultEntity =  new PageResultEntity();
			pageResultEntity.setFirstDataIndex(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			pageResultEntity.setPageSize(builtinPagerMethodProcesser.getPageQueryEntity().getPageSize());
			
			// 获得查询总数量的hql语句
			String countHql = builtinPagerMethodProcesser.getHql() + getFromHql();
			Query countQuery = createQuery(countHql);
			long totalCount = (long) countQuery.uniqueResult();// 查询获得数据总数
			pageResultEntity.setTotalCount(totalCount);
			pageResultEntity.setPageNum(builtinPagerMethodProcesser.getPageQueryEntity().getPageNum(totalCount));
			pageResultEntity.setPageTotalCount(builtinPagerMethodProcesser.getPageQueryEntity().getPageTotalCount(totalCount));
			
			if(builtinFocusedIdMethodProcesser.getIsUsed()){
				pageResultEntity.setFocusedId(builtinFocusedIdMethodProcesser.getFocusedId());
			}
			
			query.setFirstResult(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			query.setMaxResults(builtinPagerMethodProcesser.getPageQueryEntity().getMaxResult());
		}
		return pageResultEntity;
	}
	
	/**
	 * 执行查询
	 * <p>如果有聚焦id，则要处理</p>
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final List<Map<String, Object>> executeQuery(Query query){
		List<Map<String, Object>> dataList = query.list();
		if(builtinFocusedIdMethodProcesser.getIsUsed()){
			List<Object> addFocusedIds = builtinFocusedIdMethodProcesser.getAddFocusedIds();
			if(addFocusedIds != null && addFocusedIds.size() > 0){
				StringBuilder hql = new StringBuilder("from " + requestBody.getResourceName() + " where " + ResourcePropNameConstants.ID + " in(");
				int size = addFocusedIds.size();
				for (int i=0;i<size;i++) {
					hql.append("?,");
					dataList.remove(dataList.size()-1);// 挤掉最后的数据
				}
				hql.setLength(hql.length()-1);
				hql.append(")");
				hql.append(builtinSortMethodProcesser.getHql());
				
				List<Map<String, Object>> addDataList = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), addFocusedIds);
				dataList.addAll(0, addDataList);
				hql.setLength(0);
			}
		}
		return dataList;
	}

	/**
	 * 二次处理查询的数据结果集合 
	 * @param dataList
	 * @return 
	 */
	protected final List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList) {
		if(builtinQueryMethodProcesser.getIsNeedProcessDataCollection() && dataList != null && dataList.size() > 0){
			dataList = builtinQueryMethodProcesser.doProcessDataCollection(dataList);
		}
		return dataList;
	}
	
	/**
	 * 处理查询子资源数据集合
	 * @param dataList
	 */
	protected final void doProcessSubListQuery(List<Map<String, Object>> dataList){
		if(builtinSublistMethodProcesser.getIsUsed() && dataList != null && dataList.size() > 0){
			String parentId;
			List<Map<String, Object>> nullSubList = null;
			for (Map<String, Object> map : dataList) {
				parentId = (String) map.get(ResourcePropNameConstants.ID);
				if(parentId == null){
					map.put("children", nullSubList);
				}else{
					map.put("children", builtinSublistMethodProcesser.querySubList(parentId));
				}
			}
		}
	}
	
	/**
	 * 查询数据集合时，组装ResponseBody对象
	 * @param dataList
	 * @param pageResultEntity
	 * @param isSuccess
	 */
	protected final void installResponseBodyForQueryDataList(List<Map<String, Object>> dataList, PageResultEntity pageResultEntity, boolean isSuccess) {
		ResponseBody responseBody = null;
		if(pageResultEntity == null){
			// 不是分页查询
			responseBody = new ResponseBody(dataList, isSuccess);
		}else{
			// 分页查询，要先将结果集存储到pageResultEntity中，再把pageResultEntity存储到responseBody中
			pageResultEntity.setResultDatas(dataList);
			responseBody = new ResponseBody(pageResultEntity, isSuccess);
		}
		setResponseBody(responseBody);
	}
	
	/**
	 * 根据ID，查询单个数据对象时，组装ResponseBody对象
	 * @param dataList
	 * @param isSuccess
	 */
	protected final void installResponseBodyForQueryDataObject(List<Map<String, Object>> dataList, boolean isSuccess) {
		Map<String, Object> data = null;
		if(dataList != null && dataList.size() == 1){
			data = dataList.get(0);
		}
		ResponseBody responseBody = new ResponseBody(data, isSuccess);
		setResponseBody(responseBody);
	}
	
	/**
	 * 查询总数量时，组装ResponseBody对象
	 * @param textResult 
	 * @param isSuccess 
	 */
	protected final void installResponseBodyForQueryCounter(TextResultEntity textResult, boolean isSuccess){
		ResponseBody responseBody = new ResponseBody(textResult, isSuccess);;
		setResponseBody(responseBody);
	}
}
