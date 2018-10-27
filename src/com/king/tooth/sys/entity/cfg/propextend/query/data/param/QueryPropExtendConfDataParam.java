package com.king.tooth.sys.entity.cfg.propextend.query.data.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;

/**
 * 查询属性扩展配置的对应数据列表用参数
 * @author DougLei
 */
@SuppressWarnings("serial")
public class QueryPropExtendConfDataParam implements Serializable{
	
	/**
	 * 显示的列属性名
	 */
	private String refKeyPropName;
	
	/**
	 * 排序的列属性名
	 */
	private String refOrderByPropName;
	/**
	 * 排序的方式
	 * <p>默认为asc</p>
	 */
	private String orderBy;
	
	/**
	 * 一次查询的数量
	 * <p>进行分页查询，减少内存消耗，默认为100</p>
	 * <p>如果为-1，则不进行分页查询，一次查询全部</p>
	 */
	private int querySize;
	
	/**
	 * 查询条件集合
	 */
	private List<QueryPropExtendConfDataCondition> conditions;
	/** 根据查询条件集合，解析出的条件查询hql */
	private String conditionHql;
	/** 根据查询条件集合，解析出的条件查询值集合 */
	private List<Object> conditionValues;
	
	public String getRefKeyPropName() {
		return refKeyPropName;
	}
	public void setRefKeyPropName(String refKeyPropName) {
		this.refKeyPropName = refKeyPropName;
	}
	public String getRefOrderByPropName() {
		return refOrderByPropName;
	}
	public void setRefOrderByPropName(String refOrderByPropName) {
		this.refOrderByPropName = refOrderByPropName;
	}
	public String getOrderBy() {
		if(StrUtils.isEmpty(orderBy)){
			orderBy = "asc";
		}
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public int getQuerySize() {
		if(querySize != -1 && querySize < 100){
			querySize = 100;
		}
		return querySize;
	}
	public void setQuerySize(int querySize) {
		this.querySize = querySize;
	}
	public List<QueryPropExtendConfDataCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryPropExtendConfDataCondition> conditions) {
		this.conditions = conditions;
	}
	public String getConditionHql() {
		return conditionHql;
	}
	public List<Object> getConditionValues() {
		return conditionValues;
	}
	
	/**
	 * 处理查询条件hql语句，以及查询条件值集合
	 * @param initSize
	 * @return this
	 */
	public QueryPropExtendConfDataParam processConditionHqlAndConditionValues(int initSize) {
		if(initSize < 1){
			throw new IllegalArgumentException("集合的初始长度值，不能小于1");
		}
		if(conditions != null && conditions.size() > 0){
			StringBuilder hql = new StringBuilder();
			int conditionsSize = conditions.size();
			
			conditionValues = new ArrayList<Object>(conditionsSize + initSize);
			
			Map<String, String> queryCondParams = new HashMap<String, String>(conditionsSize);
			for (QueryPropExtendConfDataCondition condition : conditions) {
				queryCondParams.put(condition.getPropName(), condition.getValue());
			}
			
			BuiltinQueryCondFuncUtil.installQueryCondHql(ResourceInfoConstants.TABLE, queryCondParams.entrySet(), conditionValues, hql);
			conditionHql = hql.toString();
			
			hql.setLength(0);
			queryCondParams.clear();
		}else{
			conditionHql = "";
			conditionValues = new ArrayList<Object>(initSize);
		}
		return this;
	}
	
	public void clear(){
		if(conditions != null && conditions.size() > 0){
			conditions.clear();
		}
	}
}
