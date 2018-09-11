package com.king.tooth.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.entity.cfg.ComSqlScript;
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
		
		String coreQuerySql = sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql()+
				builtinQueryMethodProcesser.getSql().append(getFromSql());
		String querySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		
		processSelectSqlResultsets(sqlScriptResource, coreQuerySql);
		
		Query query = createQuery(0, querySql);
		PageResultEntity pageResultEntity = loadPageResultEntity(query);
		List<Map<String, Object>> dataList = executeQuery(query, sqlScriptResource.getOutSqlResultsetsList().get(0));
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataList(dataList, pageResultEntity, true);
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
