package com.king.tooth.web.builtin.method.sqlresource.querycond;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.king.tooth.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

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
		this.alias = ResourceNameConstants.ALIAS_RESOURCE;
	}
	public BuiltinQueryCondMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinQueryCondMethodProcesser内置方法处理器");
	}
	
	protected void execAnalysisParam() {
		Set<Entry<String, String>> se = this.queryCondParams.entrySet();
		// 解析请求的查询条件参数集合,整理成sql
		List<Object> queryCondParameters = new ArrayList<Object>();
		BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ISysResource.SQLSCRIPT_RESOURCE_TYPE, resourceName, se, queryCondParameters, sql, alias);
		
		sqlParameterValues.add(queryCondParameters);
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
