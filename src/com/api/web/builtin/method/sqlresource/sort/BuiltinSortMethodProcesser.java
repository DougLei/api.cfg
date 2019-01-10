package com.api.web.builtin.method.sqlresource.sort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.api.util.Log4jUtil;
import com.api.util.NamingProcessUtil;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

/**
 * 内置排序函数处理器
 * <p>order by ... </p>
 * @author DougLei
 */
public class BuiltinSortMethodProcesser extends AbstractSqlResourceBuiltinMethodProcesser{
	
	/**
	 * 通用的正则表达匹配器
	 */
	private transient Matcher commonMatcher;
	
	/**
	 * 获取排序标识的空格前字符的正则表达式编译器对象
	 * <p>例如:Name desc中的Name</p>
	 */
	private transient static final Pattern BEFORE_SPACE_PATTERN = Pattern.compile(".*(?= )");
	
	/**
	 * 排序参数
	 * <p>例如：[Name desc,Age asc]</p>
	 */
	private String sort;
	
	public BuiltinSortMethodProcesser(String sort) {
		super.isUsed = true;
		this.sql.append(" order by ");
		this.sort = sort;
	}
	public BuiltinSortMethodProcesser(boolean isUsed) {
		super.isUsed = isUsed;
		Log4jUtil.debug("此次请求，没有使用到BuiltinSortMethodProcesser内置方法处理器");
	}
	public BuiltinSortMethodProcesser() {
	}

	/**
	 * sql语句，解析请求的排序参数集合
	 * <p>要处理排序的属性名，将属性名，转换为列名</p>
	 */
	protected void execAnalysisParam() {
		String[] resultOrderBy = sort.split(",");
		String reqPropName = null;
		int len = resultOrderBy.length;
		for(int i=0; i<len; i++){
			commonMatcher = BEFORE_SPACE_PATTERN.matcher(resultOrderBy[i].trim());
			if(commonMatcher.find()){
				reqPropName = commonMatcher.group();
				resultOrderBy[i] = resultOrderBy[i].replace(reqPropName, NamingProcessUtil.propNameTurnColumnName(reqPropName));
			}else{
				resultOrderBy[i] = NamingProcessUtil.propNameTurnColumnName(resultOrderBy[i].trim());
			}
		}
		
		for (String spv : resultOrderBy) {
			sql.append("s_.").append(spv).append(",");
		}
		sql.setLength(sql.length() - 1);
		Log4jUtil.debug("[BuiltinSortMethodProcesser.analysisSortParamsToSql]解析出来，要执行的排序sql语句为：{}", sql);
	}

	public int getProcesserType() {
		return BuiltinMethodProcesserType.SORT;
	}
	
	public StringBuilder getSql() {
		execAnalysisParams();
		return sql;
	}
	
	public void clearInvalidMemory() {
	}
}
