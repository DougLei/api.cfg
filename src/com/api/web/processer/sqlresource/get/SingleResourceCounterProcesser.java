package com.api.web.processer.sqlresource.get;

import org.hibernate.Query;

import com.api.web.entity.resulttype.TextResultEntity;

/**
 * 处理这种请求路径格式的处理器：/Counter/{resourceType}
 * @author DougLei
 */
public final class SingleResourceCounterProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceCounterProcesser";
	}
	
	protected boolean doGetProcess() {
		String querySql = builtinSqlScriptMethodProcesser.getReqSql().getFinalSqlScriptList().get(0).getFinalCteSql() + 
						  getFromSql().toString();
		Query query = createQuery(0, querySql);
		long totalCount = (long) query.uniqueResult();
		TextResultEntity textResult = new TextResultEntity(totalCount);
		installResponseBodySimple(null, textResult);
		return true;
	}

	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder(" select count(1) from (");
		sql.append(builtinSqlScriptMethodProcesser.getReqSql().getFinalSqlScriptList().get(0).getFinalSelectSqlScript())
		   .append(" ) s_ ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
