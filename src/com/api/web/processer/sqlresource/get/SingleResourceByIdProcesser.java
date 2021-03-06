package com.api.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.api.sys.entity.cfg.CfgSql;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}/{resourceId}
 * @author DougLei
 */
public final class SingleResourceByIdProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceByIdProcesser";
	}
	
	protected boolean doGetProcess() {
		CfgSql sqlScriptResource = builtinSqlScriptMethodProcesser.getReqSql();
		
		String coreQuerySql =  sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql()+
				  builtinQueryMethodProcesser.getSql().append(getFromSql());
		
		recordCoreSqlBuffer(coreQuerySql);
		reocrdCoreSqlParams(null);
		
		String querySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		
		Query query = createQuery(0, querySql);
		List<Map<String, Object>> dataList = executeQuery(query, sqlScriptResource.getOutSqlResultsetsList().get(0), null);
		dataList = doProcessDataCollection(dataList);
		
		Map<String, Object> data = null;
		if(dataList != null && dataList.size() > 0){
			if(dataList.size() > 1 && !turnToObjectProcesser.isTurnToObject()){
				throw new NullPointerException("getById查询数据，查询到了多条数据");
			}
			data = dataList.get(0);
		}
		
		installResponseBodySimple(null, data);
		return true;
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
