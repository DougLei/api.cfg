package com.api.web.processer.tableresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.api.constants.ResourcePropNameConstants;
import com.api.web.entity.resulttype.PageResultEntity;

/**
 * 抽象的递归查询处理器，实现递归"独特"的查询方法
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public abstract class RecursiveQueryProcesser extends GetProcesser{
	
	/**
	 * 递归查询
	 * @param dataList 之前递归查询到的数据集合
	 * @param recursiveQueryHql 递归查询用hql
	 * @param deepLevel 递归查询的钻取深度
	 */
	protected final void recursiveQuery(List<Map<String, Object>> dataList, String recursiveQueryHql, int deepLevel) {
		deepLevel--; // 自减递归查询的深度
		if(deepLevel == 0){ // 判断是否到了要求递归查询的钻取深度，如果达到了则停止递归查询，return
			return;
		}
		if(dataList != null && dataList.size() > 0){
			Query recursiveQuery = null;
			List<Map<String, Object>> recursiveQueryDataList = null;
			for (Map<String, Object> map : dataList) {
				// 移除上一个查询的parentId的值
				hqlParameterValues.remove(0);
				// 将本次查询用的parentId的值传入
				hqlParameterValues.add(0, map.get(ResourcePropNameConstants.ID).toString());
				
				// 执行查询
				recursiveQuery = createQuery(recursiveQueryHql);
				recursiveQueryDataList = recursiveQuery.list();
				
				// 将查询的子结果集合存储起来
				map.put("children", recursiveQueryDataList);
				
				// 再进行递归查询
				recursiveQuery(recursiveQueryDataList, recursiveQueryHql, deepLevel);
			}
		}
	}
	
	
	/**
	 * 加载第一次递归查询的hql
	 * <p>要判断，是否对第一次递归查询有查询条件的约束</p>
	 * @param firstRecursiveQueryhqlParameterValues 存储第一次递归查询用的条件值集合
	 */
	protected StringBuilder loadFirstRecursiveQueryHql(List<Object> firstRecursiveQueryhqlParameterValues){
		StringBuilder hql = new StringBuilder();// 存储第一次递归查询用的hql
		boolean firstRecursiveQueryHaveCond = builtinRecursiveMethodProcesser.installFirstRecursiveQueryHql(hql, firstRecursiveQueryhqlParameterValues);
		if(!firstRecursiveQueryHaveCond){
			hql.append(getFromHql());
		}
		return hql;
	}
	
	/**
	 * 第一次递归查询分页查询，加载PageResultEntity类的实例，如果调用了分页查询的功能，则同时将查询的条件值set到query参数对象中
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query 
	 * @param queryHql 
	 * @param firstRecursiveQueryhqlParameterValues 
	 * @return 分页结果对象
	 */
	protected final PageResultEntity loadFirstRecursiveQueryPageResultEntity(Query query, String queryHql, List<Object> firstRecursiveQueryhqlParameterValues){
		PageResultEntity pageResultEntity = null;
		if(builtinPagerMethodProcesser.getIsUsed()){
			pageResultEntity =  new PageResultEntity();

			// 获得查询总数量的hql语句
			String countHql = builtinPagerMethodProcesser.getHql() + queryHql;
			Query countQuery = createQuery(countHql, firstRecursiveQueryhqlParameterValues);
			long totalCount = (long) countQuery.uniqueResult();// 查询获得数据总数
			pageResultEntity.setTotalCount(totalCount);
			pageResultEntity.setPageNum(builtinPagerMethodProcesser.getPageQueryEntity().getPageNum());
			pageResultEntity.setPageTotalCount(builtinPagerMethodProcesser.getPageQueryEntity().getPageTotalCount(totalCount));
			
			if(builtinFocusedIdMethodProcesser.getIsUsed()){
				pageResultEntity.setFocusedId(builtinFocusedIdMethodProcesser.getFocusedId());
			}
			
			pageResultEntity.setPageSize(builtinPagerMethodProcesser.getPageQueryEntity().getPageSize());
			pageResultEntity.setFirstDataIndex(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			
			if(totalCount>0 && !builtinCreateExportFileMethodProcesser.getIsUsed()){// 如果有数据，同时没有开启生成导出文件功能，再进行分页查询
				query.setFirstResult(pageResultEntity.getFirstDataIndex());
				query.setMaxResults(pageResultEntity.getPageSize());
			}
		}
		return pageResultEntity;
	}
}
