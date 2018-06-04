package com.king.tooth.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.constants.ResourceNameConstants;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}/{resourceId}
 * @author DougLei
 */
public final class SingleResourceByIdProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceByIdProcesser";
	}
	
	protected boolean doGetProcess() {
		validIdColumnIsExists();
		String querySql = builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScript().getFinalCteSql()+
						  builtinQueryMethodProcesser.getSql().append(getFromSql())
															  .append(builtinSortMethodProcesser.getSql())
															  .toString();
		Query query = createQuery(1, querySql);
		List<Map<String, Object>> dataList = executeList(query, builtinSqlScriptMethodProcesser.getSqlScriptResource().getSqlQueryResultColumnList());
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataObject(dataList);
		return true;
	}

	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ( ")
		   .append(builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScript().getFinalSelectSqlScript())
		   .append(" ) ").append(ResourceNameConstants.ALIAS_RESOURCE).append(" ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
