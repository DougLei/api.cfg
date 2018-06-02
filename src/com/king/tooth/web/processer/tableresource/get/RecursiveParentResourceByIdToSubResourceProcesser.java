package com.king.tooth.web.processer.tableresource.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.web.entity.resulttype.PageResultEntity;


/**
 * 处理这种请求路径格式的处理器：/{parentResourceType}/{parentId}/{resourceType}
 * <p>处理递归查询</p>
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public final class RecursiveParentResourceByIdToSubResourceProcesser extends RecursiveQueryProcesser {
	
	public String getProcesserName() {
		return "【Get-TableResource】RecursiveParentResourceByIdToSubResourceProcesser";
	}

	protected boolean doGetProcess() {
		List<Object> firstRecursiveQueryhqlParameterValues = new ArrayList<Object>(); // 存储第一次递归查询用的条件值集合
		StringBuilder hql = loadFirstRecursiveQueryHql(firstRecursiveQueryhqlParameterValues);
		String qHql = hql.toString();// 将获得的hql语句存储到这个变量中，分页查询loadFirstRecursiveQueryPageResultEntity()的方法，要调用
		
		String queryHql = hql.insert(0, builtinQueryMethodProcesser.getHql())
							 .append(builtinSortMethodProcesser.getHql())
							 .toString();
		
		Query query = null;
		PageResultEntity pageResultEntity = null;
		if(firstRecursiveQueryhqlParameterValues.size() > 0){ // 证明第一次查询，有条件筛选
			query = createQuery(queryHql, firstRecursiveQueryhqlParameterValues);
			pageResultEntity = loadFirstRecursiveQueryPageResultEntity(query, qHql, firstRecursiveQueryhqlParameterValues);
		}else{ // 否则走正常查询逻辑
			query = createQuery(queryHql);
			pageResultEntity = loadPageResultEntity(query);
		}
		
		List<Map<String, Object>> dataList = query.list();// 查询
		// 判断是否要进行递归查询
		if(builtinRecursiveMethodProcesser.getIsRecursive() && builtinRecursiveMethodProcesser.getDeepLevel() > 1){
			queryHql = builtinQueryMethodProcesser.getHql().append(getFromHql())
														   .append(builtinQueryCondMethodProcesser.getHqlStartbyAnd(null))
														   .append(builtinSortMethodProcesser.getHql())
														   .toString();
			recursiveQuery(dataList, queryHql, builtinRecursiveMethodProcesser.getDeepLevel());
		}
		
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataList(dataList, pageResultEntity);
		return true;
	}
	
	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from ")
		   .append(requestBody.getRouteBody().getResourceName())
		   .append(builtinRecursiveMethodProcesser.getHql());
		return hql;
	}
}
