package com.api.web.processer.sqlresource.get;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.alibaba.fastjson.JSONArray;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.cfg.CfgSql;
import com.api.util.database.ProcedureUtil;
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
		
		if(SqlStatementTypeConstants.PROCEDURE.equals(sqlScriptResource.getType())){
			JSONArray array = ProcedureUtil.executeProcedure(sqlScriptResource, null);
			if(array != null & array.size() > 0){
				installResponseBodyForQueryDataList(null, array.getJSONObject(0).getJSONArray("dataSet1"), null);
			}else{
				installResponseBodyForQueryDataList(null, Collections.emptyList(), null);
			}
		}else{
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
			List<Map<String, Object>> dataList = executeQuery(query, sqlScriptResource.getOutSqlResultsetsList().get(0), pageResultEntity);
			dataList = doProcessDataCollection(dataList);
			installResponseBodyForQueryDataList(null, dataList, pageResultEntity);
		}
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
