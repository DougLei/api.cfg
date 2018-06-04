package com.king.tooth.web.processer.sqlresource.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.common.sqlscript.SqlQueryResultColumn;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.king.tooth.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.query.BuiltinQueryMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.query.SelectNaming;
import com.king.tooth.web.builtin.method.sqlresource.querycond.BuiltinQueryCondMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sort.BuiltinSortMethodProcesser;
import com.king.tooth.web.entity.resulttype.PageResultEntity;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.entity.resulttype.TextResult;
import com.king.tooth.web.processer.sqlresource.RequestProcesser;

/**
 * get请求处理器
 * @author DougLei
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class GetProcesser extends RequestProcesser{

	/**
	 * select
	 */
	protected BuiltinQueryMethodProcesser builtinQueryMethodProcesser;
	
	/**
	 * where
	 */
	protected BuiltinQueryCondMethodProcesser builtinQueryCondMethodProcesser;
	
	/**
	 * order by
	 */
	protected BuiltinSortMethodProcesser builtinSortMethodProcesser;
	
	/**
	 * 分页查询
	 */
	protected BuiltinPagerMethodProcesser builtinPagerMethodProcesser;
	
	/**
	 * 聚焦数据
	 */
	protected BuiltinFocusedIdMethodProcesser builtinFocusedIdMethodProcesser;
	
	/**
	 * 处理请求
	 */
	protected final boolean doProcess() {
		initBuiltinMethods();
		boolean isKeepOn = doGetProcess();
		return isKeepOn;
	}
	
	/**
	 * 初始化内置的函数属性对象
	 * 方便子类使用
	 */
	private void initBuiltinMethods(){
		builtinQueryCondMethodProcesser = builtinSqlResourceBMProcesser.getQuerycondProcesser();
		builtinQueryMethodProcesser = builtinSqlResourceBMProcesser.getQueryProcesser();
		builtinSortMethodProcesser = builtinSqlResourceBMProcesser.getSortProcesser();
		builtinPagerMethodProcesser = builtinSqlResourceBMProcesser.getPagerProcesser();
		builtinFocusedIdMethodProcesser = builtinSqlResourceBMProcesser.getFocusedIdProcesser();
	}
	
	/**
	 * sql查询结果集转为list<map>
	 * <p>将列名，转换为属性名，作为key</p>
	 * <p>将值作为value显示</p>
	 * @param queryResultList
	 * @param sqlQueryResultColumns
	 * @return
	 */
	private List<Map<String, Object>> sqlQueryResultToMap(List queryResultList, List<SqlQueryResultColumn> sqlQueryResultColumns){
		if(sqlQueryResultColumns == null || sqlQueryResultColumns.size() == 0){
			throw new NullPointerException("将sql查询结果转为map时，要转换的结果列名对象集合不能为空[sqlQueryResultColumns]");
		}
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(queryResultList.size());
		if(queryResultList != null && queryResultList.size() > 0){
			Map<String, Object> data = null;
			SelectNaming[] selectNamingArr = builtinQueryMethodProcesser.getSelectNamingArr();// 如果编写的sql语句有查询多个列，同时在调用api时，通过_select指定查询某些字段，会用到这个
			
			/*
			 * 说明，这里要先判断是否是object数组
			 * 因为object数组，也是属于object类型的 
			 * 如果直接判断是否是object类型，那么无论list里面存的是什么类型，都会返回true
			 */
			if(queryResultList.get(0) instanceof Object[]){ // 结果集是object数组类型[当编写的查询sql语句查询返回多个字段时]
				List<Object[]> objArrList = (List<Object[]>)queryResultList;
				for(Object[] object : objArrList){
					data = new HashMap<String, Object>(object.length);
					int i = 0;
					if(selectNamingArr != null && selectNamingArr.length > 0){ // 证明指定了_select查询的字段，则结果肯定是这几个字段
						for(SelectNaming sn : selectNamingArr){
							if(StrUtils.isEmpty(sn.getSelectAliasName())){
								data.put(NamingTurnUtil.columnNameTurnPropName(sn.getSelectName()), object[i++]);
							}else{
								data.put(sn.getSelectAliasName(), object[i++]);
							}
						}
					} else { // 否则，就是查询全部字段
						for(SqlQueryResultColumn sqrc : sqlQueryResultColumns){
							data.put(sqrc.getResultPropName(), object[i++]);
						}
					}
					dataList.add(data);
				}
			}
			else{ // 结果集是object类型[当编写的查询sql语句只查询返回一个字段时]
				List<Object> objList = (List<Object>)queryResultList;
				for(Object object : objList){
					data = new HashMap<String, Object>(1);
					if(selectNamingArr != null && selectNamingArr.length > 0){ // 证明指定了_select查询的字段，则结果肯定是这一个字段，这一步主要是防止，如果用户查询一个字段起了个别名
						if(StrUtils.isEmpty(selectNamingArr[0].getSelectAliasName())){
							data.put(NamingTurnUtil.columnNameTurnPropName(selectNamingArr[0].getSelectName()), object);
						}else{
							data.put(selectNamingArr[0].getSelectAliasName(), object);
						}
					}else{
						data.put(sqlQueryResultColumns.get(0).getResultPropName(), object);
					}
					dataList.add(data);
				}
			}
			queryResultList.clear();
		}else{
			Log4jUtil.debug("将sql查询结果集转为list<Map>时，sql查询的结果集为空，转换结果为空");
		}
		sqlQueryResultColumns.clear();
		return dataList;
	}
	
	/**
	 * 处理get请求
	 * @return
	 */
	protected abstract boolean doGetProcess();
	
	/**
	 * 获取包括from在内的，后续的sql语句，由各个子类实现
	 * @return
	 */
	protected abstract StringBuilder getFromSql();
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法
	
	/**
	 * 执行查询
	 * @param query
	 * @param sqlQueryResultColumns
	 * @return
	 */
	protected final List<Map<String, Object>> executeList(Query query, List<SqlQueryResultColumn> sqlQueryResultColumns) {
		List list = query.list();
		List<Map<String, Object>> dataList = sqlQueryResultToMap(list, sqlQueryResultColumns);
		return dataList;
	}
	
	/**
	 * 二次处理查询的数据结果集合 
	 * @param dataList
	 * @return 
	 */
	protected final List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList) {
		if(builtinQueryMethodProcesser.getIsNeedProcessDataCollection() && dataList != null && dataList.size() > 0){
			dataList = builtinQueryMethodProcesser.doProcessDataCollection(dataList);
		}
		return dataList;
	}
	
	/**
	 * 查询数据集合时，组装ResponseBody对象
	 * @param dataList
	 * @param pageResultEntity
	 */
	protected final void installResponseBodyForQueryDataList(List<Map<String, Object>> dataList, PageResultEntity pageResultEntity){
		ResponseBody responseBody = null;
		if(pageResultEntity == null){
			// 不是分页查询
			responseBody = new ResponseBody(dataList);
		}else{
			// 分页查询，要先将结果集存储到pageResultEntity中，再把pageResultEntity存储到responseBody中
			pageResultEntity.setResultDatas(dataList);
			responseBody = new ResponseBody(pageResultEntity);
		}
		setResponseBody(responseBody);
	}
	
	/**
	 * 分页查询，加载PageResultEntity类的实例，如果调用了分页查询的功能，则同时将查询的条件值set到query参数对象中
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query 
	 * @return 分页结果对象
	 */
	protected final PageResultEntity loadPageResultEntity(Query query){
		PageResultEntity pageResultEntity = null;
		if(builtinPagerMethodProcesser.getIsUsed()){
			pageResultEntity =  new PageResultEntity();
			pageResultEntity.setFirstDataIndex(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			pageResultEntity.setPageSize(builtinPagerMethodProcesser.getPageQueryEntity().getPageSize());
			
			// 获得查询总数量的hql语句
			String countSql = builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScript().getFinalCteSql() + builtinPagerMethodProcesser.getSql() + getFromSql();
			Query countQuery = createQuery(1, countSql);
			long totalCount = (long) countQuery.uniqueResult();// 查询获得数据总数
			pageResultEntity.setTotalCount(totalCount);
			pageResultEntity.setPageNum(builtinPagerMethodProcesser.getPageQueryEntity().getPageNum(totalCount));
			pageResultEntity.setPageTotalCount(builtinPagerMethodProcesser.getPageQueryEntity().getPageTotalCount(totalCount));
			
			if(builtinFocusedIdMethodProcesser.getIsUsed()){
				pageResultEntity.setFocusedId(builtinFocusedIdMethodProcesser.getFocusedId());
			}
			
			query.setFirstResult(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			query.setMaxResults(builtinPagerMethodProcesser.getPageQueryEntity().getMaxResult());
		}
		return pageResultEntity;
	}
	
	/**
	 * 根据ID，查询单个数据对象时，组装ResponseBody对象
	 * @param dataList
	 */
	protected final void installResponseBodyForQueryDataObject(List<Map<String, Object>> dataList) {
		Map<String, Object> data = null;
		if(dataList != null && dataList.size() == 1){
			data = dataList.get(0);
		}
		ResponseBody responseBody = new ResponseBody(data);
		setResponseBody(responseBody);
	}
	
	/**
	 * 查询总数量时，组装ResponseBody对象
	 * @param textResult 
	 */
	protected final void installResponseBodyForQueryCounter(TextResult textResult){
		ResponseBody responseBody = new ResponseBody(textResult);;
		setResponseBody(responseBody);
	}
	
	/**
	 * 验证id字段是否存在
	 */
	protected void validIdColumnIsExists() {
		List<SqlQueryResultColumn> sqlQueryResultColumns = builtinSqlScriptMethodProcesser.getSqlScriptResource().getSqlQueryResultColumnList();
		boolean unIncludeIdColumn = true;// 是否不包含id字段，如果不包括，则该处理器无法使用
		for (SqlQueryResultColumn sqrs : sqlQueryResultColumns) {
			if(sqrs.getResultPropName().equalsIgnoreCase(ResourceNameConstants.ID)){
				unIncludeIdColumn = false;
				break;
			}
		}
		if(unIncludeIdColumn){
			throw new IllegalArgumentException("请求的select sql资源的查询结果字段，不包括id列，请求失败");
		}
	}
}
