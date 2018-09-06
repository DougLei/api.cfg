package com.king.tooth.web.processer.sqlresource.get;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.jdbc.Work;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.king.tooth.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.query.BuiltinQueryMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.query.SelectNaming;
import com.king.tooth.web.builtin.method.sqlresource.querycond.BuiltinQueryCondMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.recursive.BuiltinRecursiveMethodProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sort.BuiltinSortMethodProcesser;
import com.king.tooth.web.entity.resulttype.PageResultEntity;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.entity.resulttype.TextResultEntity;
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
	 * 递归
	 */
	protected BuiltinRecursiveMethodProcesser builtinRecursiveMethodProcesser;
	
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
		builtinRecursiveMethodProcesser = builtinSqlResourceBMProcesser.getRecursiveProcesser();
	}
	
	/**
	 * sql查询结果集转为list<map>
	 * <p>将列名，转换为属性名，作为key</p>
	 * <p>将值作为value显示</p>
	 * @param queryResultList
	 * @param sqlResultsets
	 * @return
	 */
	private List<Map<String, Object>> sqlQueryResultToMap(List queryResultList, List<CfgSqlResultset> sqlResultsets){
		if(sqlResultsets == null || sqlResultsets.size() == 0){
			throw new NullPointerException("将sql查询结果转为map时，要参照的结果集列信息集合不能为空[sqlResultsets]");
		}
		
		List<Map<String, Object>> dataList = null;
		if(queryResultList != null && queryResultList.size() > 0){
			dataList = new ArrayList<Map<String, Object>>(queryResultList.size());
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
								data.put(NamingProcessUtil.columnNameTurnPropName(sn.getSelectName()), object[i++]);
							}else{
								data.put(sn.getSelectAliasName(), object[i++]);
							}
						}
					} else { // 否则，就是查询全部字段
						for(CfgSqlResultset csr : sqlResultsets){
							data.put(csr.getPropName(), object[i++]);
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
							data.put(NamingProcessUtil.columnNameTurnPropName(selectNamingArr[0].getSelectName()), object);
						}else{
							data.put(selectNamingArr[0].getSelectAliasName(), object);
						}
					}else{
						data.put(sqlResultsets.get(0).getPropName(), object);
					}
					dataList.add(data);
				}
			}
			queryResultList.clear();
		}else{
			dataList = new ArrayList<Map<String, Object>>(0);
			Log4jUtil.debug("将sql查询结果集转为list<Map>时，sql查询的结果集为空，转换结果为空");
		}
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
	 * @param sqlResultset
	 * @return
	 */
	protected final List<Map<String, Object>> executeList(Query query, List<CfgSqlResultset> sqlResultset) {
		List list = query.list();
		List<Map<String, Object>> dataList = sqlQueryResultToMap(list, sqlResultset);
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
			String countSql = builtinSqlScriptMethodProcesser.getSqlScriptResource().getFinalSqlScriptList().get(0).getFinalCteSql() + builtinPagerMethodProcesser.getSql() + getFromSql();
			Query countQuery = createQuery(0, countSql);
			long totalCount = Long.valueOf(countQuery.uniqueResult()+"");// 查询获得数据总数
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
	 * 查询数据集合时，组装ResponseBody对象
	 * @param dataList
	 * @param pageResultEntity
	 * @param isSuccess
	 */
	protected final void installResponseBodyForQueryDataList(List<Map<String, Object>> dataList, PageResultEntity pageResultEntity, boolean isSuccess){
		ResponseBody responseBody = null;
		if(pageResultEntity == null){
			// 不是分页查询
			responseBody = new ResponseBody(dataList, isSuccess);
		}else{
			// 分页查询，要先将结果集存储到pageResultEntity中，再把pageResultEntity存储到responseBody中
			pageResultEntity.setResultDatas(dataList);
			responseBody = new ResponseBody(pageResultEntity, isSuccess);
		}
		setResponseBody(responseBody);
	}
	
	/**
	 * 根据ID，查询单个数据对象时，组装ResponseBody对象
	 * @param dataList
	 * @param isSuccess
	 */
	protected final void installResponseBodyForQueryDataObject(List<Map<String, Object>> dataList, boolean isSuccess) {
		Map<String, Object> data = null;
		if(dataList != null && dataList.size() == 1){
			data = dataList.get(0);
		}
		ResponseBody responseBody = new ResponseBody(data, isSuccess);
		setResponseBody(responseBody);
	}
	
	/**
	 * 查询总数量时，组装ResponseBody对象
	 * @param textResult 
	 * @param isSuccess 
	 */
	protected final void installResponseBodyForQueryCounter(TextResultEntity textResult, boolean isSuccess) {
		ResponseBody responseBody = new ResponseBody(textResult, isSuccess);
		setResponseBody(responseBody);
	}
	
	/**
	 * 验证id字段是否存在
	 * @param sqlScriptResource 
	 */
	protected void validIdColumnIsExists(ComSqlScript sqlScriptResource) {
		List<CfgSqlResultset> sqlResultsets = sqlScriptResource.getOutSqlResultsetsList().get(0);
		boolean unIncludeIdColumn = true;// 是否不包含id字段，如果不包括，则该处理器无法使用
		for (CfgSqlResultset csr : sqlResultsets) {
			if(csr.getPropName().equalsIgnoreCase(ResourcePropNameConstants.ID)){
				unIncludeIdColumn = false;
				break;
			}
		}
		if(unIncludeIdColumn){
			throw new IllegalArgumentException("请求的select sql资源的查询结果字段，不包括id列，请求失败");
		}
	}
	
	/**
	 * [首次调用]处理select sql语句查询的结果集信息
	 * @param sqlScriptResource
	 */
	protected void processSelectSqlResultsets(ComSqlScript sqlScriptResource, String querySql) {
		if(sqlScriptResource.getOutSqlResultsetsList() == null || sqlScriptResource.getOutSqlResultsetsList().get(0) == null){
			List<Object> queryCondParameters = null;
			if(sqlParameterValues.size() > 0){
				queryCondParameters = new ArrayList<Object>(sqlParameterValues.get(0).size());
				queryCondParameters.addAll(sqlParameterValues.get(0));
			}
			if(builtinQueryCondMethodProcesser.getSql().length() > 0){
				querySql += " and 1=2";
			}else{
				querySql += " where 1=2";
			}
			sqlScriptResource.setOutSqlResultsetsList(processResultSetList(querySql, queryCondParameters, sqlScriptResource.getId()));
		}
	}
	
	/**
	 * 获取select sql语句查询的结果集信息
	 * <p>如果没有结果集，则要获取结果集信息并保存到数据库</p>
	 * @param rs
	 * @param sqlResultsetIndex
	 * @param sqlScriptId 
	 * @throws SQLException 
	 */
	protected List<List<CfgSqlResultset>> processResultSetList(final String querySql, final List<Object> queryCondParameters, final String sqlScriptId){
		if(StrUtils.isEmpty(querySql)){
			return null;
		}
		List<List<CfgSqlResultset>> sqlResultsetsList = new ArrayList<List<CfgSqlResultset>>(1);
		final List<CfgSqlResultset> sqlResultSets = new ArrayList<CfgSqlResultset>();
		sqlResultsetsList.add(sqlResultSets);
		
		HibernateUtil.getCurrentThreadSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					pst = connection.prepareStatement(querySql);
					if(queryCondParameters != null && queryCondParameters.size()>0){
						int i=1;
						for (Object paramValue : queryCondParameters) {
							if(paramValue instanceof Date){
								pst.setDate(i++, new java.sql.Date(((Date)paramValue).getTime()));
							}else{
								pst.setObject(i++, paramValue);
							}
						}
					}
					rs = pst.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int len = rsmd.getColumnCount();
					CfgSqlResultset csr = null;
					for(int i=1;i<=len;i++){
						csr = new CfgSqlResultset(rsmd.getColumnName(i), i, 2);
						csr.setSqlScriptId(sqlScriptId);
						HibernateUtil.saveObject(csr, null);// 将每条结果集信息保存到数据库
						
						sqlResultSets.add(csr);
					}
				}catch (Exception e){
					e.printStackTrace();
				} finally{
					CloseUtil.closeDBConn(rs, pst);// 从当前线程session中获取的connection，会在最后同session一同关闭，不需要单独关闭。即execute中的connection参数
				}
			}
		});
		Log4jUtil.debug("获取select语句查询结果集信息时，执行的sql语句为：{}", querySql);
		Log4jUtil.debug("获取select语句查询结果集信息时，执行sql语句的条件参数集合为：{}", queryCondParameters);
		if(queryCondParameters != null){
			queryCondParameters.clear();
		}
		return sqlResultsetsList;
	}
}
