package com.king.tooth.web.builtin.method.sqlresource.recursive;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.king.tooth.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

/**
 * 内置递归函数处理器
 * @author DougLei
 */
public class BuiltinRecursiveMethodProcesser extends AbstractSqlResourceBuiltinMethodProcesser{
	
	/**
	 * 是否递归
	 * true/false
	 */
	private boolean recursive;
	
	/**
	 * 递归查询的深度
	 * <p>默认为1级</p>
	 */
	private int deepLevel;
	
	/**
	 * 父资源的查询条件参数集合
	 */
	private Map<String, String> parentResourceQueryCond;
	
	/**
	 * 第一次递归查询根数据的id
	 */
	private String firstRecursiveQueryParentResourceId;
	
	public BuiltinRecursiveMethodProcesser(String recursive, String deepLevel, Map<String, String> parentResourceQueryCond) {
		super.isUsed = true;
		this.recursive = Boolean.valueOf(recursive.trim());
		this.deepLevel = Integer.valueOf(deepLevel.trim());
		this.parentResourceQueryCond = parentResourceQueryCond;
	}
	public BuiltinRecursiveMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinRecursiveMethodProcesser内置方法处理器");
	}
	
	public StringBuilder getSql() {
		execAnalysisParams();
		return sql;
	}
	
	protected void execAnalysisParam() {
		sql.append(" where ");
		if(StrUtils.isNullStr(parentResourceId)){
			sql.append(" (parent_id  = ? or parent_id is null)");
			firstRecursiveQueryParentResourceId = "";
		}else{
			sql.append(" parent_id = ? ");
			firstRecursiveQueryParentResourceId = parentResourceId;
		}
		
		Log4jUtil.debug("[BuiltinRecursiveMethodProcesser.execAnalysisParam]解析出来，要执行的递归sql语句为：{}", sql.toString());
		Log4jUtil.debug("[BuiltinRecursiveMethodProcesser.execAnalysisParam]解析出来，要执行的条件sql参数值为：ParentId={}", parentResourceId);
	}
	
	/**
	 * 获取第一次递归查询的sql语句，和参数值集合
	 * <p>这个方法由该类独自实现</p>
	 * <p>sql和sqlParameterValues两个参数中的数据，通过引用传递到方法调用方</p>
	 * @param firstRecursiveQuerySql
	 * @param firstRecursiveQueryParams
	 * @return 第一次递归查询是否有条件
	 */
	public void getFirstRecursiveQuerySql(StringBuilder firstRecursiveQuerySql, List<Object> firstRecursiveQueryParams){
		firstRecursiveQuerySql.append(getSql());
		
		if(sqlParameterValues.size() > 0){
			firstRecursiveQueryParams.addAll(sqlParameterValues.get(0));
		}
		firstRecursiveQueryParams.add(firstRecursiveQueryParentResourceId);
		
		if(parentResourceQueryCond.size() > 0){ // 如果有查询主表的条件集合，即对递归查询的第一层数据进行筛选的查询条件
			firstRecursiveQuerySql.append(" and ");
			Set<Entry<String, String>> queryCondParamsSet = parentResourceQueryCond.entrySet();
			BuiltinQueryCondFuncUtil.installQueryCondHql(ISysResource.SQLSCRIPT, resourceName, queryCondParamsSet , firstRecursiveQueryParams, firstRecursiveQuerySql);
		}
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.RECURSIVE;
	}
	
	/**
	 * 是否递归
	 * true/false
	 */
	public boolean getIsRecursive() {
		return recursive;
	}
	
	/**
	 * 获取钻取的深度
	 * <p>默认为2级</p>
	 * <p>-1为钻到底</p>
	 */
	public int getDeepLevel() {
		return deepLevel;
	}
	
	public void clearInvalidMemory() {
		if(this.parentResourceQueryCond != null && this.parentResourceQueryCond.size() > 0){
			this.parentResourceQueryCond.clear();
		}
	}
}