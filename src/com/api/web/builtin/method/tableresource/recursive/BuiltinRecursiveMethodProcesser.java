package com.api.web.builtin.method.tableresource.recursive;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.api.constants.ResourceInfoConstants;
import com.api.util.Log4jUtil;
import com.api.util.StrUtils;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.api.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置递归函数处理器
 * @author DougLei
 */
public class BuiltinRecursiveMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
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
	 * 子资源中，关联主子源对应的属性名
	 * 如果请求的url中没有指定，则默认为parentId
	 */
	private String recursiveRefPropName;
	
	/**
	 * 父资源的查询条件参数集合
	 */
	private Map<String, String> parentResourceQueryCond;
	
	public BuiltinRecursiveMethodProcesser(String recursive, String deepLevel, String recursiveRefPropName, Map<String, String> parentResourceQueryCond) {
		super.isUsed = true;
		this.recursive = Boolean.valueOf(recursive.trim());
		this.deepLevel = Integer.valueOf(deepLevel.trim());
		this.parentResourceQueryCond = parentResourceQueryCond;
		
		if(StrUtils.isEmpty(recursiveRefPropName)){
			recursiveRefPropName = "parentId";
		}
		this.recursiveRefPropName = recursiveRefPropName;
	}
	public BuiltinRecursiveMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinRecursiveMethodProcesser内置方法处理器");
	}
	
	public StringBuilder getHql() {
		execAnalysisParams();
		return hql;
	}
	
	public String getRecursiveRefPropName() {
		return recursiveRefPropName;
	}
	
	protected void execAnalysisParam() {
		if(StrUtils.isNullStr(parentResourceId)){
			hql.append(" where ("+recursiveRefPropName+"  = ? or "+recursiveRefPropName+" is null)");
			hqlParameterValues.add("");
		}else{
			hql.append(" where "+recursiveRefPropName+" = ? ");
			hqlParameterValues.add(parentResourceId);
		}
		
		Log4jUtil.debug("[BuiltinRecursiveMethodProcesser.execAnalysisParam]解析出来，要执行的递归hql语句为：{}", hql.toString());
		Log4jUtil.debug("[BuiltinRecursiveMethodProcesser.execAnalysisParam]解析出来，要执行的条件hql参数值为：ParentId={}", parentResourceId);
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
	
	/**
	 * 拼接第一次递归查询的hql语句，和参数值集合
	 * <p>这个方法由该类独自实现</p>
	 * <p>hql和hqlParameterValues两个参数中的数据，通过引用传递到方法调用方</p>
	 * @param hql
	 * @param hqlParameterValues
	 * @return 第一次递归查询是否有条件
	 */
	public boolean installFirstRecursiveQueryHql(StringBuilder hql, List<Object> hqlParameterValues){
		if(parentResourceQueryCond.size() > 0){ // 如果有查询主表的条件集合，即对递归查询的第一层数据进行筛选的查询条件
			hql.append(" from ").append(resourceName).append(" where ");
			
			Set<Entry<String, String>> queryCondParamsSet = parentResourceQueryCond.entrySet();
			BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ResourceInfoConstants.TABLE, queryCondParamsSet, queryResourceMetadataInfos, hqlParameterValues, hql);
			return true;
		}
		return false;
	}
	
	public void clearInvalidMemory() {
		if(this.parentResourceQueryCond != null && this.parentResourceQueryCond.size() > 0){
			this.parentResourceQueryCond.clear();
		}
	}
}
