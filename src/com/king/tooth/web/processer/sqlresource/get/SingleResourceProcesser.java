package com.king.tooth.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends GetProcesser{

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceProcesser";
	}
	
	protected boolean doGetProcess() {
		ComSqlScript sqlScriptResource = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		
		String coreQuerySql = sqlScriptResource.getFinalSqlScript().getFinalCteSql()+
				builtinQueryMethodProcesser.getSql().append(getFromSql());
		String querySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		
		processSelectSqlQueryResultColumns(sqlScriptResource, coreQuerySql);
		
		Query query = createQuery(1, querySql);
		PageResultEntity pageResultEntity = loadPageResultEntity(query);
		List<Map<String, Object>> dataList = executeList(query, sqlScriptResource.getSqlQueryResultColumnList());
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataList(dataList, pageResultEntity, true);
		return true;
	}

	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ( ")
		   .append(builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScript().getFinalSelectSqlScript())
		   .append(" ) s_ ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
