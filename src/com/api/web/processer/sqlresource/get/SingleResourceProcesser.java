package com.api.web.processer.sqlresource.get;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.api.sys.entity.cfg.CfgSql;
import com.api.web.entity.resulttype.PageResultEntity;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends GetProcesser{

	public String getProcesserName() {
		return "【Get-SqlResource】SingleResourceProcesser";
	}
	
	protected boolean doGetProcess() {
		CfgSql sqlScriptResource = builtinSqlScriptMethodProcesser.getReqSql();
		
		String coreQuerySql = sqlScriptResource.getFinalSqlScriptList().get(0).getFinalCteSql()+
				builtinQueryMethodProcesser.getSql().append(getFromSql());
		String querySql = coreQuerySql + builtinSortMethodProcesser.getSql();
		
		recordCoreSqlBuffer(coreQuerySql);
		reocrdCoreSqlParams(null);
		
		Query query = createQuery(0, querySql);
		PageResultEntity pageResultEntity = loadPageResultEntity(query);
		if(isCreateExportFile(builtinCreateExportFileMethodProcesser, pageResultEntity, query)){
			return true;
		}
		List<Map<String, Object>> dataList = executeQuery(query, sqlScriptResource.getOutSqlResultsetsList().get(0));
		dataList = doProcessDataCollection(dataList);
		installResponseBodyForQueryDataList(null, dataList, pageResultEntity);
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
