package com.king.tooth.web.processer.sqlresource.get;

import org.hibernate.Query;

import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.web.entity.resulttype.TextResultEntity;

/**
 * 处理这种请求路径格式的处理器：/Counter/{parentResourceType}/{parentId}/{resourceType}/
 * <p>处理递归查询总数量：只查询第一级的数据总数量</p>
 * @author DougLei
 */
public final class RecursiveParentResourceByIdToSubResourceCounterProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】RecursiveParentResourceByIdToSubResourceCounterProcesser";
	}
	
	protected boolean doGetProcess() {
		ComSqlScript sqlScriptResource = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		if(sqlScriptResource.getSqlQueryResultColumnList() != null){
			validIdColumnIsExists(sqlScriptResource);
		}
		
		String querySql = sqlScriptResource.getFinalSqlScript().getFinalCteSql() + 
						  getFromSql().toString();
		Query query = createQuery(0, querySql);
		long totalCount = (long) query.uniqueResult();
		TextResultEntity textResult = new TextResultEntity(totalCount);
		installResponseBodyForQueryCounter(textResult, true);
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
