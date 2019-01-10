package com.api.web.processer.tableresource.get;

import org.hibernate.Query;

import com.api.constants.ResourcePropNameConstants;
import com.api.web.entity.resulttype.TextResultEntity;

/**
 * 处理这种请求路径格式的处理器：/Counter/{parentResourceType}/{parentId}/{resourceType}/
 * <p>处理父子资源查询总数量</p>
 * @author DougLei
 */
public final class ParentResourceByIdToSubResourceCounterProcesser extends GetProcesser {
	
	public String getProcesserName() {
		return "【Get-TableResource】ParentResourceByIdToSubResourceCounterProcesser";
	}

	protected boolean doGetProcess() {
		String queryHql = getFromHql().toString();

		Query query = createQuery(queryHql);
		long totalCount = (long) query.uniqueResult();
		TextResultEntity textResult = new TextResultEntity(totalCount);
		installResponseBodySimple(null, textResult);
		return true;
	}
	
	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder(" select count(").append(ResourcePropNameConstants.ID).append(") ");
		hql.append(" from ")
		   .append(requestBody.getRouteBody().getResourceName())
		   .append(" s_")
		   .append(builtinParentsubQueryMethodProcesser.getHql())
		   .append(builtinQueryCondMethodProcesser.getHqlStartbyAnd("s_"));
		return hql;
	}
}
