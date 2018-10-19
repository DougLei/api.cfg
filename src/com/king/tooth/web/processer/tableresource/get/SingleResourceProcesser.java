package com.king.tooth.web.processer.tableresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-TableResource】SingleResourceProcesser";
	}

	protected boolean doGetProcess() {
		String queryHql = builtinQueryMethodProcesser.getHql().append(getFromHql())
															  .append(builtinSortMethodProcesser.getHql())
															  .toString();
		Query query = createQuery(queryHql);
		PageResultEntity pageResultEntity = loadPageResultEntity(query);
		if(isCreateExportFile(builtinCreateExportFileMethodProcesser, pageResultEntity, query)){
			return true;
		}
		
		List<Map<String, Object>> dataList = executeQuery(query);// 查询
		dataList = doProcessDataCollection(dataList);
		doProcessSubListQuery(dataList);
		installResponseBodyForQueryDataList(null, dataList, pageResultEntity);
		return true;
	}

	protected StringBuilder getFromHql() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from ").append(requestBody.getRouteBody().getResourceName())
			.append(builtinQueryCondMethodProcesser.getHql());
		return hql;
	}
}
