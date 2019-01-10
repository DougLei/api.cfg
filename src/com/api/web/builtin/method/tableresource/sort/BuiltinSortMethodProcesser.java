package com.api.web.builtin.method.tableresource.sort;

import com.api.util.Log4jUtil;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置排序函数处理器
 * <p>order by ... </p>
 * @author DougLei
 */
public class BuiltinSortMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
	/**
	 * 排序参数
	 * <p>例如：[Name desc,Age asc]</p>
	 */
	private String sort;
	
	public BuiltinSortMethodProcesser(String sort) {
		super.isUsed = true;
		this.hql.append(" order by ");
		this.sort = sort;
	}
	public BuiltinSortMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinSortMethodProcesser内置方法处理器");
	}

	public StringBuilder getHql() {
		execAnalysisParams();
		return hql;
	}

	/**
	 * hql语句，解析请求的排序参数集合
	 * <p>要处理排序的属性名，确保是hibernate hbm映射文件中配置的</p>
	 */
	protected void execAnalysisParam() {
		hql.append(sort);
		Log4jUtil.debug("[BuiltinSortMethodProcesser.analysisSortParamsToHql]解析出来，要执行的排序hql语句为：{}", hql);
	}

	public int getProcesserType() {
		return BuiltinMethodProcesserType.SORT;
	}
	
	public void clearInvalidMemory() {
	}
}
