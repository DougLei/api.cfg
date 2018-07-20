package com.king.tooth.web.processer.tableresource.get;

import org.hibernate.Query;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.web.entity.resulttype.TextResultEntity;

/**
 * 处理这种请求路径格式的处理器：/Counter/{resourceType}
 * @author DougLei
 */
public final class SingleResourceCounterProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-TableResource】SingleResourceCounterProcesser";
	}
	
	protected boolean doGetProcess() {
		String queryHql = getFromHql().toString();
		Query query = createQuery(queryHql);
		long totalCount = (long) query.uniqueResult();
		TextResultEntity textResult = new TextResultEntity(totalCount);
		installResponseBodyForQueryCounter(textResult, true);
		return true;
	}
	
	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder(" select count(").append(ResourceNameConstants.ID).append(") ");
		hql.append(" from ").append(requestBody.getRouteBody().getResourceName())
			.append(builtinQueryCondMethodProcesser.getHql());
		return hql;
	}
}
