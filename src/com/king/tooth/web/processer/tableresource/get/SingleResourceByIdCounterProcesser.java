package com.king.tooth.web.processer.tableresource.get;

import org.hibernate.Query;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.web.entity.resulttype.TextResult;

/**
 * 处理这种请求路径格式的处理器：/Counter/{resourceType}/{resourceId}
 * @author DougLei
 */
public final class SingleResourceByIdCounterProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-TableResource】SingleResourceByIdCounterProcesser";
	}
	
	protected boolean doGetProcess() {
		String queryHql = builtinQueryMethodProcesser.getHql().append(getFromHql()).toString();
		
		Query query = createQuery(queryHql);
		
		long totalCount = (long) query.uniqueResult();
		TextResult textResult = new TextResult(totalCount);
		installResponseBodyForQueryCounter(textResult);
		return true;
	}
	
	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder(" select count(").append(ResourceNameConstants.ID).append(") ");
		hql.append(" from ").append(requestBody.getRouteBody().getResourceName())
			.append(builtinQueryCondMethodProcesser.getHql());
		return hql;
	}
}
