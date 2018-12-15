package com.king.tooth.web.processer.sqlresource.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.entity.cfg.CfgSql;
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
		CfgSql sqlScriptResource = builtinSqlScriptMethodProcesser.getReqSql();
		
		// 获取首次递归查询根数据的sql语句和参数集合
		StringBuilder firstRecursiveQuerySql = new StringBuilder(sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql());
		List<Object> firstRecursiveQueryParams = new ArrayList<Object>();
		getFirstRecursiveQueryInfo(firstRecursiveQuerySql, firstRecursiveQueryParams);
		
		// 获取二次以后的查询sql语句，并获取查询语句最终的结果字段集合
		String queryMethodSql = builtinQueryMethodProcesser.getSql().toString();
		String coreQuerySql = builtinQueryMethodProcesser.getSql()
														 .insert(0, sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql())
														 .append(getFromSql()).toString();
		
		Query query = createRecursiveQuery(queryMethodSql + firstRecursiveQuerySql + builtinSortMethodProcesser.getSql(), firstRecursiveQueryParams);
		PageResultEntity pageResultEntity = loadRecursiveQueryPageResultEntity(query, "select count(1) " + firstRecursiveQuerySql, firstRecursiveQueryParams);
		List<Map<String, Object>> dataList = executeQuery(query, sqlScriptResource.getOutSqlResultsetsList().get(0));

		if(builtinRecursiveMethodProcesser.getIsRecursive() && (builtinRecursiveMethodProcesser.getDeepLevel() > 1 || builtinRecursiveMethodProcesser.getDeepLevel() == -1)){
			if(builtinQueryCondMethodProcesser.getSql().length() > 0){
				coreQuerySql += " and "+builtinRecursiveMethodProcesser.getRecursiveRefPropName()+" = ?";
			}else{
				coreQuerySql += " where "+builtinRecursiveMethodProcesser.getRecursiveRefPropName()+" = ?";
			}
			String recursiveQuerySql = coreQuerySql + builtinSortMethodProcesser.getSql();
			if(sqlParameterValues.size() > 0){
				sqlParameterValues.get(0).add("tmp");
			}else{
				List<Object> spv = new ArrayList<Object>();
				spv.add("tmp");
				sqlParameterValues.add(spv);
			}
			recursiveQuery(dataList, recursiveQuerySql, builtinRecursiveMethodProcesser.getDeepLevel(), sqlScriptResource.getOutSqlResultsetsList().get(0));
		}
		firstRecursiveQueryParams.clear();
		
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataList(null, dataList, pageResultEntity);
		return true;
	}

	/**
	 * 获取首次递归查询根数据的sql语句和参数集合
	 * @param firstRecursiveQuerySql
	 * @param firstRecursiveQueryParams
	 */
	private void getFirstRecursiveQueryInfo(StringBuilder firstRecursiveQuerySql, List<Object> firstRecursiveQueryParams) {
		firstRecursiveQuerySql.append(" from ( ")
        				      .append(builtinSqlScriptMethodProcesser.getReqSql().getFinalSqlScriptList().get(0).getFinalSelectSqlScript())
        				      .append(" ) s_ ");
		builtinRecursiveMethodProcesser.getFirstRecursiveQuerySql(firstRecursiveQuerySql, firstRecursiveQueryParams);
		
		recordCoreSqlBuffer(firstRecursiveQuerySql);
		reocrdCoreSqlParams(firstRecursiveQueryParams);
	}

	protected StringBuilder getFromSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ( ")
		   .append(builtinSqlScriptMethodProcesser.getReqSql().getFinalSqlScriptList().get(0).getFinalSelectSqlScript())
		   .append(" ) s_ ")
		   .append(builtinQueryCondMethodProcesser.getSql());
		return sql;
	}
}
