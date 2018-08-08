package com.king.tooth.web.builtin.method.tableresource.sublist;

import java.util.List;
import java.util.Map;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置子列表资源查询函数处理器
 * @author DougLei
 */
public class BuiltinSublistMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
	/**
	 * 子资源名
	 */
	private String subResourceName;
	
	/**
	 * 子资源中，关联主子源对应的属性名
	 * 如果请求的url中没有指定，则默认为parentId
	 */
	private String refPropName;
	
	/**
	 * 引用的主资源id
	 */
	private String parentId;
	
	/**
	 * 查询子资源的排序
	 */
	private String subSort;
	
	public BuiltinSublistMethodProcesser(String subResourceName, String refPropName, String subSort) {
		super.isUsed = true;
		this.subResourceName = subResourceName;
		this.subSort = subSort;
		
		if(StrUtils.isEmpty(refPropName)){
			refPropName = "parentId";
		}
		this.refPropName = refPropName;
	}
	
	public BuiltinSublistMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinSublistMethodProcesser内置方法处理器");
	}
	
	public StringBuilder getHql() {
		execAnalysisParams();
		return hql;
	}

	protected void execAnalysisParam() {
		hql.append("from ").append(subResourceName).append(" where ").append(refPropName).append(" =?");
		if(StrUtils.notEmpty(subSort)){
			hql.append(" order by ").append(subSort);
		}
		Log4jUtil.debug("[BuiltinSublistMethodProcesser.execAnalysisParam]解析出来，要执行的子资源集合查询的hql语句为：{}", hql.toString());
		Log4jUtil.debug("[BuiltinSublistMethodProcesser.execAnalysisParam]解析出来，要执行的子资源集合查询的hql参数值为："+refPropName+"={}", parentId);
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.SUB_LIST;
	}
	
	public String getRefPropName() {
		return refPropName;
	}

	/**
	 * 执行查询子资源集合
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySubList(String parentId){
		List<Map<String, Object>> subList = null;
		if(isUsed){
			this.parentId = parentId;
			subList = HibernateUtil.executeListQueryByHqlArr(null, null, getHql().toString(), parentId);
			return subList;
		}
		return subList;
	}

	public void clearInvalidMemory() {
	}
}
