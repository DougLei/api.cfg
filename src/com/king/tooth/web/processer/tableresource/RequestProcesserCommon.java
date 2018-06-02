package com.king.tooth.web.processer.tableresource;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.tableresource.BuiltinTableResourceBMProcesser;
import com.king.tooth.web.builtin.method.tableresource.parentsub.BuiltinParentsubQueryMethodProcesser;
import com.king.tooth.web.builtin.method.tableresource.querycond.BuiltinQueryCondMethodProcesser;
import com.king.tooth.web.processer.common.CommonProcesser;

/**
 * 通用的请求处理器类
 * <p>主要是一些通用的方法</p>
 * @author DougLei
 */
public class RequestProcesserCommon extends CommonProcesser{
	
	/**
	 * 内置表资源函数处理器
	 */
	protected BuiltinTableResourceBMProcesser builtinTableResourceBMProcesser;
	
	/**
	 * hql语句中的参数值集合
	 */
	protected final List<Object> hqlParameterValues = new ArrayList<Object>(16);
	
	/**
	 * where
	 */
	protected BuiltinQueryCondMethodProcesser builtinQueryCondMethodProcesser;
	
	/**
	 * 父子资源链接查询内置函数实例
	 * <p>需要子类根据情况，自行决定是否需要初始化</p>
	 */
	protected BuiltinParentsubQueryMethodProcesser builtinParentsubQueryMethodProcesser;
	
	/**
	 * 释放不用的内存
	 */
	protected final void releaseInvalidMemory() {
		// 清除hql语句中的参数值集合
		hqlParameterValues.clear();
		// 清除内置函数处理器的无效数据
		builtinTableResourceBMProcesser.releaseInvalidMemory();
	}
	
	/**
	 * 创建查询对象
	 * @param hql
	 * @return
	 */
	protected final Query createQuery(String hql){
		Query query = HibernateUtil.getCurrentThreadSession().createQuery(hql);
		setQueryCondParamters(query);
		
		Log4jUtil.debug("【最后执行的hql语句为：{}】", hql);
		Log4jUtil.debug("【最后执行的hql语句对应的条件值集合为：{}】", hqlParameterValues);
		return query;
	}
	
	/**
	 * 给查询对象query设置各个条件的值
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query
	 */
	private void setQueryCondParamters(Query query){
		if(hqlParameterValues.size() > 0){
			int i = 0;
			for (Object val : hqlParameterValues) {
				query.setParameter(i++, val);
			}
		}
	}
	
	/**
	 * 根据自定义的参数值集合(hqlParameterValues)创建查询对象
	 * @param hql
	 * @param hqlParameterValues
	 * @return
	 */
	protected final Query createQuery(String hql, List<Object> hqlParameterValues){
		Query query = HibernateUtil.getCurrentThreadSession().createQuery(hql);
		setQueryCondParamters(query, hqlParameterValues);
		
		Log4jUtil.debug("【最后执行的hql语句为：{}】", hql);
		Log4jUtil.debug("【最后执行的hql语句对应的条件值集合为：{}】", hqlParameterValues);
		return query;
	}
	
	/**
	 * 根据自定义的参数值集合(hqlParameterValues)，给查询对象query设置各个条件的值
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query
	 * @param hqlParameterValues
	 */
	private void setQueryCondParamters(Query query, List<Object> hqlParameterValues){
		if(hqlParameterValues.size() > 0){
			int i = 0;
			for (Object val : hqlParameterValues) {
				query.setParameter(i++, val);
			}
		}
	}
}
