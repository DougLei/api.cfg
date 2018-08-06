package com.king.tooth.web.processer.sqlresource.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * 处理这种请求路径格式的处理器：/{parentResourceType}/{parentId}/{resourceType}
 * <p>处理递归查询</p>
 * @author DougLei
 */
public final class RecursiveParentResourceByIdToSubResourceProcesser extends RecursiveQueryProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】RecursiveParentResourceByIdToSubResourceProcesser";
	}
	
	protected boolean doGetProcess() {
		ComSqlScript sqlScriptResource = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		
		String coreQuerySql = sqlScriptResource.getFinalSqlScript().getFinalCteSql()+
				builtinQueryMethodProcesser.getSql().append(getFromSql());
		processSelectSqlQueryResultColumns(sqlScriptResource, coreQuerySql);
		
		
		
		StringBuilder firstRecursiveQuerySql = new StringBuilder();
		firstRecursiveQuerySql.append(sqlScriptResource.getFinalSqlScript().getFinalCteSql())
		                      .append(builtinQueryMethodProcesser.getSql())
		                      .append(" from ( ")
		                      .append(builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScript().getFinalSelectSqlScript())
		                      .append(" ) s_ ");
		List<Object> firstRecursiveQueryParams = new ArrayList<Object>();
		builtinRecursiveMethodProcesser.getFirstRecursiveQuerySql(firstRecursiveQuerySql, firstRecursiveQueryParams);
		
		
		
		
		
		
		
		
		if(builtinRecursiveMethodProcesser.getIsRecursive() && (builtinRecursiveMethodProcesser.getDeepLevel() > 1 || builtinRecursiveMethodProcesser.getDeepLevel() == -1)){
			
		}
		
		
		
		
		
		
		String recursiveQuerySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		Query query = createQuery(0, recursiveQuerySql);
		
		
		
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
