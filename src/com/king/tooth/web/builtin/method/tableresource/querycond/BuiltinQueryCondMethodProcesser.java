package com.king.tooth.web.builtin.method.tableresource.querycond;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.king.tooth.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置查询条件函数处理器
 * <p>where ... </p>
 * @author DougLei
 */
public class BuiltinQueryCondMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
	/**
	 * 查询条件参数集合
	 */
	private Map<String, String> queryCondParams;
	
	public BuiltinQueryCondMethodProcesser(Map<String, String> queryCondParams) {
		super.isUsed = true;
		this.hql.append(" where ");
		this.queryCondParams = queryCondParams;
	}
	public BuiltinQueryCondMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinQueryCondMethodProcesser内置方法处理器");
	}

	public StringBuilder getHql() {
		execAnalysisParams();
		return hql;
	}
	
	/**
	 * 返回 and 开头的hql语句
	 * <p>而不是 where 开头的hql语句</p>
	 * @param alias 别名，可传递参数，或不传递参数
	 * @return
	 */
	public String getHqlStartbyAnd(String alias) {
		this.alias = alias;
		String hqlStr = getHql().toString();
		if(hqlStr.length() > 0){
			hqlStr = hqlStr.replaceFirst("where", " and ");
		}
		return hqlStr;
	}
	
	protected void execAnalysisParam() {
		Set<Entry<String, String>> se = this.queryCondParams.entrySet();
		// 解析请求的查询条件参数集合,整理成hql
		BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ResourceInfoConstants.TABLE, resourceName, se, hqlParameterValues, hql, alias);
		Log4jUtil.debug("[BuiltinQueryCondMethodProcesser.execAnalysisParam]解析出来，要执行的条件数据库脚本语句为： {}", hql);
		Log4jUtil.debug("[BuiltinQueryCondMethodProcesser.execAnalysisParam]解析出来，要执行的条件数据库脚本参数集合为：{}", hqlParameterValues);
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.QUERY_COND;
	}
	
	public void clearInvalidMemory() {
		if(this.queryCondParams != null && this.queryCondParams.size() > 0){
			this.queryCondParams.clear();
		}
	}
}
