package com.king.tooth.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.entity.common.ComSqlScript;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}/{resourceId}
 * @author DougLei
 */
public final class SingleResourceByIdProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceByIdProcesser";
	}
	
	protected boolean doGetProcess() {
		ComSqlScript sqlScriptResource = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		if(sqlScriptResource.getSqlQueryResultColumnList() != null){
			validIdColumnIsExists(sqlScriptResource);
		}
		
		String coreQuerySql =  sqlScriptResource.getFinalSqlScript().getFinalCteSql()+
				  builtinQueryMethodProcesser.getSql().append(getFromSql());
		String querySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		
		processSelectSqlQueryResultColumns(sqlScriptResource, coreQuerySql);
		
		Query query = createQuery(0, querySql);
		List<Map<String, Object>> dataList = executeList(query, sqlScriptResource.getSqlQueryResultColumnList());
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataObject(dataList, true);
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
