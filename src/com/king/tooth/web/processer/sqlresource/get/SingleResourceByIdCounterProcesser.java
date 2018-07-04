package com.king.tooth.web.processer.sqlresource.get;

import org.hibernate.Query;

import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.web.entity.resulttype.TextResult;

/**
 * 处理这种请求路径格式的处理器：/Counter/{resourceType}/{resourceId}
 * @author DougLei
 */
public final class SingleResourceByIdCounterProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceByIdCounterProcesser";
	}
	
	protected boolean doGetProcess() {
		ComSqlScript sqlScriptResource = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		if(sqlScriptResource.getSqlQueryResultColumnList() != null){
			validIdColumnIsExists(sqlScriptResource);
		}
		
		String querySql = sqlScriptResource.getFinalSqlScript().getFinalCteSql() + 
						  getFromSql().toString();
		Query query = createQuery(1, querySql);
		long totalCount = (long) query.uniqueResult();
		TextResult textResult = new TextResult(totalCount);
		installResponseBodyForQueryCounter(textResult);
		return true;
	}
	
	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder(" select count(1) from (");
		sql.append(builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScript().getFinalSelectSqlScript())
		   .append(" ) s_ ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
