package com.king.tooth.web.builtin.method.tableresource.sort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.internal.HbmConfPropMetadata;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置排序函数处理器
 * <p>order by ... </p>
 * @author DougLei
 */
public class BuiltinSortMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
	/**
	 * 通用的正则表达匹配器
	 */
	private transient Matcher commonMatcher;
	
	/**
	 * 获取空格前字符的正则表达式编译器对象
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
		String[] resultOrderBy = sort.split(",");
		HbmConfPropMetadata[] hibernateDefineResourceProps = HibernateUtil.getHibernateDefineResourceProps(resourceName);
		String reqPropName = null;
		int len = resultOrderBy.length;
		for(int i=0; i<len; i++){
			commonMatcher = BEFORE_SPACE_PATTERN.matcher(resultOrderBy[i].trim());
			if(commonMatcher.find()){
				reqPropName = commonMatcher.group();
				resultOrderBy[i] = resultOrderBy[i].replace(reqPropName, HibernateUtil.getDefinePropName(hibernateDefineResourceProps, reqPropName));
			}else{
				resultOrderBy[i] = HibernateUtil.getDefinePropName(hibernateDefineResourceProps, resultOrderBy[i].trim());
			}
		}
		
		for (String spv : resultOrderBy) {
			hql.append(spv).append(",");
		}
		hql.setLength(hql.length() - 1);
		Log4jUtil.debug("[BuiltinSortMethodProcesser.analysisSortParamsToHql]解析出来，要执行的排序hql语句为：{}", hql);
	}

	public int getProcesserType() {
		return BuiltinMethodProcesserType.SORT;
	}
	
	public void clearInvalidMemory() {
	}
}
