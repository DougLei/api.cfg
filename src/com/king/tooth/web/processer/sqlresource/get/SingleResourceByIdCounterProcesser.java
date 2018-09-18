package com.king.tooth.web.processer.sqlresource.get;

import org.hibernate.Query;

import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.web.entity.resulttype.TextResultEntity;

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
		
		String querySql = sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql() + 
						  getFromSql().toString();
		
		recordCoreSqlBuffer(querySql);
		reocrdCoreSqlParams(null);
		
		Query query = createQuery(0, querySql);
		long totalCount = (long) query.uniqueResult();
		TextResultEntity textResult = new TextResultEntity(totalCount);
		installResponseBodyForQueryCounter(textResult, true);
		return true;
	}
	
	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder(" select count(1) from (");
		sql.append(builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScriptList().get(0).getFinalSelectSqlScript())
		   .append(" ) s_ ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
