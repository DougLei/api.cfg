package com.king.tooth.web.processer.tableresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * 处理这种请求路径格式的处理器：/{parentResourceType}/{parentId}/{resourceType}
 * <p>处理父子资源查询</p>
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public final class ParentResourceByIdToSubResourceProcesser extends GetProcesser {
	
	public String getProcesserName() {
		return "【Get-TableResource】ParentResourceByIdToSubResourceProcesser";
	}

	protected boolean doGetProcess() {
		String queryHql = builtinQueryMethodProcesser.getHql().append(getFromHql())
															  .append(builtinSortMethodProcesser.getHql())
															  .toString();
		
		Query query = createQuery(queryHql);
		
		PageResultEntity pageResultEntity = loadPageResultEntity(query);
		
		List<Map<String, Object>> dataList = query.list();// 查询
		dataList = doProcessDataCollection(dataList);
		doProcessSubListQuery(dataList);
		installResponseBodyForQueryDataList(dataList, pageResultEntity, true);
		return true;
	}
	
	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from ")
		   .append(requestBody.getRouteBody().getResourceName())
		   .append(" s_ ")
		   .append(builtinParentsubQueryMethodProcesser.getHql())
		   .append(builtinQueryCondMethodProcesser.getHqlStartbyAnd("s_"));
		return hql;
	}
}
