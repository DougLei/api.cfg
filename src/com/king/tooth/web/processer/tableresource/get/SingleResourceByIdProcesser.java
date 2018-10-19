package com.king.tooth.web.processer.tableresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}/{resourceId}
 * @author DougLei
 */
public final class SingleResourceByIdProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-TableResource】SingleResourceByIdProcesser";
	}
	
	@SuppressWarnings("unchecked")
	protected boolean doGetProcess() {
		String queryHql = builtinQueryMethodProcesser.getHql().append(getFromHql())
															  .append(builtinSortMethodProcesser.getHql())
															  .toString();
		Query query = createQuery(queryHql);
		
		List<Map<String, Object>> dataList = query.list();// 查询
		dataList = doProcessDataCollection(dataList);
		doProcessSubListQuery(dataList);
		
		Map<String, Object> data = null;
		if(dataList != null && dataList.size() == 1){
			data = dataList.get(0);
		}
		installResponseBodySimple(null, data);
		return true;
	}

	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from ").append(requestBody.getRouteBody().getResourceName())
			.append(builtinQueryCondMethodProcesser.getHql());
		return hql;
	}
}
