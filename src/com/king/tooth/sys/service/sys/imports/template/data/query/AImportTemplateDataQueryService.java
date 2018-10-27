package com.king.tooth.sys.service.sys.imports.template.data.query;

import java.util.List;

import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 导入模版文件中的数据查询
 * @author DougLei
 */
public abstract class AImportTemplateDataQueryService {

	/**
	 * 查询语句的hql
	 */
	protected String queryHql;
	
	/**
	 * 组装查询的hql语句
	 * @param conditionValues
	 * @param valueColumnPropName
	 * @param conditionHql
	 * @param orderByColumnPropName
	 * @param orderBy
	 */
	protected abstract void installQueryHql(List<Object> conditionValues, String valueColumnPropName, String conditionHql, String orderByColumnPropName, String orderBy);
	
	/**
	 * 查询数据列表
	 * @param rows
	 * @param pageNo
	 * @param valueColumnPropName
	 * @param conditionHql
	 * @param conditionValues
	 * @param orderByColumnPropName
	 * @param orderBy
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryDataList(String rows, String pageNo, String valueColumnPropName, String conditionHql, List<Object> conditionValues, String orderByColumnPropName, String orderBy){
		installQueryHql(conditionValues, valueColumnPropName, conditionHql, orderByColumnPropName, orderBy);
		return HibernateUtil.executeListQueryByHql(rows, pageNo, queryHql, conditionValues);
	}
}
