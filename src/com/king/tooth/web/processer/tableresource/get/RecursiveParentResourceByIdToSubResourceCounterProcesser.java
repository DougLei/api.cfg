package com.king.tooth.web.processer.tableresource.get;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.web.entity.resulttype.TextResultEntity;

/**
 * 处理这种请求路径格式的处理器：/Counter/{parentResourceType}/{parentId}/{resourceType}/
 * <p>处理递归查询总数量：只查询第一级的数据总数量</p>
 * @author DougLei
 */
public final class RecursiveParentResourceByIdToSubResourceCounterProcesser extends RecursiveQueryProcesser {
	
	public String getProcesserName() {
		return "【Get-TableResource】RecursiveParentResourceByIdToSubResourceCounterProcesser";
	}

	protected boolean doGetProcess() {
		List<Object> firstRecursiveQueryhqlParameterValues = new ArrayList<Object>(); // 存储第一次递归查询用的条件值集合
		StringBuilder hql = loadFirstRecursiveQueryHql(firstRecursiveQueryhqlParameterValues);
		
		Query query = null;
		if(firstRecursiveQueryhqlParameterValues.size() > 0){ // 证明第一次查询，有条件筛选
			hql.insert(0, "select count("+ResourceNameConstants.ID+") ");
			query = createQuery(hql.toString(), firstRecursiveQueryhqlParameterValues);
		}else{// 否则走正常查询逻辑
			query = createQuery(hql.toString());
		}
		
		long totalCount = (long) query.uniqueResult();
		TextResultEntity textResult = new TextResultEntity(totalCount);
		installResponseBodyForQueryCounter(textResult, true);
		return true;
	}
	
	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder(" select count(").append(ResourceNameConstants.ID).append(") ");
		
		hql.append(" from ")
		   .append(requestBody.getRouteBody().getResourceName())
		   .append(builtinRecursiveMethodProcesser.getHql())
		   .append(builtinQueryCondMethodProcesser.getHqlStartbyAnd(null));
		return hql;
	}
}
