package com.king.tooth.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * 抽象的递归查询处理器，实现递归"独特"的查询方法
 * @author DougLei
 */
public abstract class RecursiveQueryProcesser extends GetProcesser{
	
	/**
	 * 递归查询
	 * @param dataList 之前递归查询到的数据集合
	 * @param recursiveQuerySql 递归查询用sql
	 * @param deepLevel 递归查询的钻取深度
	 * @param sqlResultsets 
	 */
	protected final void recursiveQuery(List<Map<String, Object>> dataList, String recursiveQuerySql, int deepLevel, List<CfgSqlResultset> sqlResultsets) {
		deepLevel--; // 自减递归查询的深度
		if(deepLevel == 0){ // 判断是否到了要求递归查询的钻取深度，如果达到了则停止递归查询，return
			return;
		}
		if(dataList != null && dataList.size() > 0){
			Query recursiveQuery = null;
			List<Map<String, Object>> recursiveQueryDataList = null;
			for (Map<String, Object> map : dataList) {
				// 移除上一个查询的parent_id的值
				sqlParameterValues.get(0).remove(sqlParameterValues.get(0).size()-1);
				// 将本次查询用的parent_id的值传入
				sqlParameterValues.get(0).add(map.get(ResourcePropNameConstants.ID).toString());
				
				// 执行查询
				recursiveQuery = createQuery(0, recursiveQuerySql);
				recursiveQueryDataList = executeQuery(recursiveQuery, sqlResultsets);
				
				// 将查询的子结果集合存储起来
				map.put("children", recursiveQueryDataList);
				
				// 再进行递归查询
				recursiveQuery(recursiveQueryDataList, recursiveQuerySql, deepLevel, sqlResultsets);
			}
		}
	}
	
	
	/**
	 * 执行第一次递归查询，获取query对象
	 * @param sql
	 * @param paramValues
	 * @param
	 */
	protected final Query createRecursiveQuery(String sql, List<Object> paramValues){
		Query query = HibernateUtil.getCurrentThreadSession().createSQLQuery(sql);
		
		if(paramValues != null && paramValues.size() > 0){
			int i = 0;
			for (Object val : paramValues) {
				query.setParameter(i++, val);
			}
		}
		Log4jUtil.debug("【最后执行的sql语句为：{}】", sql);
		Log4jUtil.debug("【最后执行的sql语句对应的条件值集合为：{}】", paramValues);
		return query;
	}
	
	/**
	 * 第一次递归分页查询，加载PageResultEntity类的实例，如果调用了分页查询的功能，则同时将查询的条件值set到query参数对象中
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query 
	 * @param countSql 
	 * @param firstRecursiveQueryParams 
	 * @return 分页结果对象
	 */
	protected final PageResultEntity loadRecursiveQueryPageResultEntity(Query query, String countSql, List<Object> firstRecursiveQueryParams){
		PageResultEntity pageResultEntity = null;
		if(builtinPagerMethodProcesser.getIsUsed()){
			pageResultEntity = new PageResultEntity();
			pageResultEntity.setFirstDataIndex(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			pageResultEntity.setPageSize(builtinPagerMethodProcesser.getPageQueryEntity().getPageSize());
			
			// 获得查询总数量的hql语句
			Query countQuery = createRecursiveQuery(countSql, firstRecursiveQueryParams);
			long totalCount = Long.valueOf(countQuery.uniqueResult()+"");// 查询获得数据总数
			pageResultEntity.setTotalCount(totalCount);
			pageResultEntity.setPageNum(builtinPagerMethodProcesser.getPageQueryEntity().getPageNum(totalCount));
			pageResultEntity.setPageTotalCount(builtinPagerMethodProcesser.getPageQueryEntity().getPageTotalCount(totalCount));
			
			if(builtinFocusedIdMethodProcesser.getIsUsed()){
				pageResultEntity.setFocusedId(builtinFocusedIdMethodProcesser.getFocusedId());
			}
			
			// 在主查询的query对象中，设置分页数据
			query.setFirstResult(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			query.setMaxResults(builtinPagerMethodProcesser.getPageQueryEntity().getMaxResult());
		}
		return pageResultEntity;
	}
}
