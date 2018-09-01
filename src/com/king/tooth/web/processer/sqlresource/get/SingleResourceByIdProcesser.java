package com.king.tooth.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.entity.cfg.ComSqlScript;

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
		
		String coreQuerySql =  sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql()+
				  builtinQueryMethodProcesser.getSql().append(getFromSql());
		processSelectSqlResultsets(sqlScriptResource, coreQuerySql);
		
		if(sqlScriptResource.getOutSqlResultsetsList() != null && sqlScriptResource.getOutSqlResultsetsList().get(0) != null){
			validIdColumnIsExists(sqlScriptResource);
		}
		
		String querySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		
		Query query = createQuery(0, querySql);
		List<Map<String, Object>> dataList = executeList(query, sqlScriptResource.getOutSqlResultsetsList().get(0));
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataObject(dataList, true);
		return true;
	}

	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ( ")
		   .append(builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScriptList().get(0).getFinalSelectSqlScript())
		   .append(" ) s_ ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
