package com.api.web.builtin.method.sqlresource.querycond;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.api.constants.ResourceInfoConstants;
import com.api.util.Log4jUtil;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.api.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

/**
 * 内置查询条件函数处理器
 * <p>where ... </p>
 * @author DougLei
 */
public class BuiltinQueryCondMethodProcesser extends AbstractSqlResourceBuiltinMethodProcesser{
	
	/**
	 * 查询条件参数集合
	 */
	private Map<String, String> queryCondParams;
	
	public BuiltinQueryCondMethodProcesser(Map<String, String> queryCondParams) {
		super.isUsed = true;
		this.sql.append(" where ");
		this.queryCondParams = queryCondParams;
		this.alias = "s_";
	}
	public BuiltinQueryCondMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinQueryCondMethodProcesser内置方法处理器");
	}
	
	protected void execAnalysisParam() {
		// 解析请求的查询条件参数集合,整理成sql
		List<Object> queryCondParameters = new ArrayList<Object>();
		BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ResourceInfoConstants.SQL, queryCondParams.entrySet(), queryResourceMetadataInfos, queryCondParameters, sql, alias);
		
		if(sqlParameterValues.size() > 0){
			sqlParameterValues.get(0).addAll(queryCondParameters);
			queryCondParameters.clear();
		}else{
			sqlParameterValues.add(queryCondParameters);
		}
		
		Log4jUtil.debug("[BuiltinQueryCondMethodProcesser.execAnalysisParam]解析出来，要执行的条件数据库脚本语句为： {} ", sql);
		Log4jUtil.debug("[BuiltinQueryCondMethodProcesser.execAnalysisParam]解析出来，要执行的条件数据库脚本参数集合为：{}", queryCondParameters);
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.QUERY_COND;
	}
	
	public StringBuilder getSql() {
		execAnalysisParams();
		return sql;
	}
	
	public void clearInvalidMemory() {
		if(this.queryCondParams != null && this.queryCondParams.size() > 0){
			this.queryCondParams.clear();
		}
	}
}
