package com.api.util.hibernate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.entity.HibernateClassMetadata;
import org.hibernate.internal.SessionFactoryImpl;

import com.alibaba.fastjson.JSONObject;
import com.api.constants.OperDataTypeConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.IEntity;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.ExceptionUtil;
import com.api.util.JsonUtil;
import com.api.util.Log4jUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.database.DynamicDBUtil;
import com.api.web.builtin.method.common.pager.PageQueryEntity;

/**
 * hibernate工具类
 * @author DougLei
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class HibernateUtil {
	
	/**
	 * 获得对应的sessionFactory对象
	 * @return
	 */
	private static SessionFactoryImpl getSessionFactory(){
		return DynamicDBUtil.getSessionFactory(CurrentThreadContext.getDatabaseId());
	}
	
	/**
	 * 获得一个新Session对象
	 * <p>这个session必须有调用者自己处理</p>
	 * @return
	 */
	public static Session openNewSession(){
		return getSessionFactory().openSession();
	}
	
	/**
	 * 获得当前线程的session对象
	 * @return
	 */
	public static Session getCurrentThreadSession(){
		Session session = CurrentThreadContext.getCurrentSession();
		if(session == null){
			session = openSessionToCurrentThread();
		}
		return session;
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 在当前线程打开一个session
	 * <p>将session存储到CurrentThreadHbSessionContext中</p>
	 */
	public static Session openSessionToCurrentThread() {
		Session session = getSessionFactory().openSession();
		CurrentThreadContext.setCurrentSession(session);
		Log4jUtil.debug("[HibernateUtil.openSessionToCurrentThread]在当前线程打开一个session，并开启事务，再将session存储到CurrentThreadHbSessionContext中");
		return session;
	}
	
	/**
	 * 在当前线程开启事务
	 */
	public static void beginTransaction(){
		getCurrentThreadSession().beginTransaction();
		Log4jUtil.debug("[HibernateUtil.beginTransaction]开启事务");
	}
	
	/**
	 * 在当前线程提交事务
	 */
	public static void commitTransaction() {
		Session session = getCurrentThreadSession();
		session.flush();
		session.getTransaction().commit();
		Log4jUtil.debug("[HibernateUtil.commitTransaction]提交当前事务");
	}
	/**
	 * 在当前线程回滚事务
	 */
	public static void rollbackTransaction() {
		getCurrentThreadSession().getTransaction().rollback();
		Log4jUtil.debug("[HibernateUtil.rollbackTransaction]回滚当前事务");
	}
	
	/**
	 * 关闭当前线程的session
	 */
	public static void closeCurrentThreadSession() {
		getCurrentThreadSession().close();
		CurrentThreadContext.setCurrentSession(null);
		Log4jUtil.debug("[HibernateUtil.closeSessionFromCurrentThread]关闭当前线程的session，并将session从CurrentThreadContext中移除");
	}
	
	/**
	 * 获取当前操作的数据库类型
	 * sqlserver、oracle等
	 * @return
	 */
	public static String getCurrentDatabaseType(){
		return getSessionFactory().getDatabaseType();
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 【数据流方式】添加新的配置文件
	 * @param hbmContents
	 */
	public static void appendNewConfig(List<String> hbmContents){
		if(hbmContents != null && hbmContents.size() > 0){
			List<InputStream> inputs = new ArrayList<InputStream>(hbmContents.size());
			for(String hbmContent: hbmContents){
				if(StrUtils.isEmpty(hbmContent)){
					break;
				}
				inputs.add(new ByteArrayInputStream(hbmContent.getBytes(StandardCharsets.UTF_8)));
			}
			getSessionFactory().appendNewHbmConfig(inputs);
		}
	}
	
	/**
	 * 【数据流方式】添加新的配置文件
	 * @param hbmContent
	 */
	public static void appendNewConfig(String... hbmContent){
		if(hbmContent != null && hbmContent.length > 0){
			List<InputStream> inputs = new ArrayList<InputStream>(hbmContent.length);
			for(String hc: hbmContent){
				if(StrUtils.isEmpty(hc)){
					break;
				}
				inputs.add(new ByteArrayInputStream(hc.getBytes()));
			}
			getSessionFactory().appendNewHbmConfig(inputs);
		}
	}
	
	/**
	 * 删除配置
	 * @param entityName
	 */
	public static void removeConfig(String entityName){
		if(StrUtils.isEmpty(entityName)){
			return;
		}
		getSessionFactory().removeHbmConfig(entityName);
	}
	
	/**
	 * 是否存在指定的hbm映射信息
	 * @param entityName
	 */
	public static boolean hbmConfigIsExists(String entityName){
		if(StrUtils.isEmpty(entityName)){
			return false;
		}
		return getSessionFactory().hbmConfigIsExists(entityName);
	}
	
	/**
	 * 获取hibernate类元数据
	 * @param resourceNameArr
	 * @return
	 */
	public static List<HibernateClassMetadata> getHibernateClassMetadatas(String... resourceNameArr) {
		return getSessionFactory().getClassMetadatas(resourceNameArr);
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 获取父子资源对应的关系资源名
	 * @param parentResourceName
	 * @param resourceName
	 * @return
	 */
	public static String getDataLinkResourceName(String parentResourceName, String resourceName){
		return "SysDataLinks";
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 保存对象
	 * @param entity
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return 
	 */
	public static JSONObject saveObject(IEntity entity, String shortDesc){
		return saveObject(entity.getEntityName(), entity.toEntityJson(), shortDesc);
	}
	
	/**
	 * 保存对象
	 * @param entityName 实体名
	 * @param data 要保存的对象数据
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return 
	 */
	public static JSONObject saveObject(String entityName, JSONObject data, String shortDesc){
		ResourceHandlerUtil.initBasicPropValsForSave(entityName, data, shortDesc);
		try {
			getCurrentThreadSession().save(entityName, data);
			
			CurrentThreadContext.toReqLogDataAddOperSqlLog("insert " + entityName, data); // 日志记录save的数据
			
			data.put(ResourcePropNameConstants.FOCUSED_OPER, data.getString(ResourcePropNameConstants.ID) + "_" + OperDataTypeConstants.ADD);
			Log4jUtil.debug("保存数据成功[{}]", data);
			return data;
		} catch (Exception e) {
			Log4jUtil.debug("保存数据[{}]失败，异常信息为：", data, ExceptionUtil.getErrMsg(e));
			throw e;
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 删除对象
	 * @param entity
	 * @param shortDesc
	 * @return
	 */
	public static JSONObject deleteObject(IEntity entity, String shortDesc){
		return deleteObject(entity.getEntityName(), entity.toEntityJson());
	}
	
	/**
	 * 删除对象
	 * @param entityName
	 * @param data
	 */
	public static JSONObject deleteObject(String entityName, JSONObject data){
		Object deleteDataId = data.get(ResourcePropNameConstants.ID);
		if(deleteDataId == null){
			throw new NullPointerException("要删除的数据id值不能为空");
		}
		try {
			List<Object> parameters = new ArrayList<Object>(1);
			parameters.add(deleteDataId);
			
			String deleteHql = "delete " + entityName + " where " + ResourcePropNameConstants.ID + "=?";
			executeUpdateByHql(SqlStatementTypeConstants.DELETE, deleteHql, parameters);
			
			CurrentThreadContext.toReqLogDataAddOperSqlLog("delete " + entityName, data);// 日志记录delete的数据
			
			data.put(ResourcePropNameConstants.FOCUSED_OPER, data.getString(ResourcePropNameConstants.ID) + "_" + OperDataTypeConstants.DELETE);
			Log4jUtil.debug("删除数据成功[{}]", data);
			return data;
		} catch (Exception e) {
			Log4jUtil.debug("删除数据[{}]失败，异常信息为：", data, ExceptionUtil.getErrMsg(e));
			throw e;
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 修改对象
	 * 通过拼接update hql语句修改对象
	 * <b>修改已有的实体类数据，例如CfgTable,CfgSql...</b>
	 * <p>用在各个代码功能中，例如controller、service...，和updateObject方法在组装hql语句中有些区别</p>
	 * @param entity
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return 
	 */
	public static JSONObject updateEntityObject(IEntity entity, String shortDesc){
		JSONObject data = entity.toEntityJson();
		String updateId = data.getString(ResourcePropNameConstants.ID);
		if(updateId == null){
			throw new NullPointerException("要修改的数据id值不能为空");
		}
		
		ResourceHandlerUtil.initBasicPropValsForUpdate(entity.getEntityName(), data, shortDesc);
		try {
			List<Object> parameters = new ArrayList<Object>(data.size()-1);
			String updateHql = installEntityUpdateHql(entity.getEntityName(), updateId, data, parameters);
			
			executeUpdateByHql(SqlStatementTypeConstants.UPDATE, updateHql, parameters);
			
			CurrentThreadContext.toReqLogDataAddOperSqlLog("update " + entity.getEntityName(), data);// 日志记录update的数据
			
			data.put(ResourcePropNameConstants.FOCUSED_OPER, data.getString(ResourcePropNameConstants.ID) + "_" + OperDataTypeConstants.EDIT);
			Log4jUtil.debug("修改数据成功[{}]", data);
			return data;
		} catch (Exception e) {
			Log4jUtil.debug("修改数据[{}]失败，异常信息为：", data, ExceptionUtil.getErrMsg(e));
			throw e;
		}
	}
	
	/**
	 * 组装update hql语句
	 * <b>组装已有的实体类数据update hql语句，例如CfgTable,CfgSql...</b>
	 * <p>用在各个代码功能中，例如controller、service...，和installUpdateHql方法在组装hql语句中有些区别</p>
	 * @param entityName
	 * @param id
	 * @param data
	 * @param finalUpdateHql
	 * @param parameters
	 */
	public static String installEntityUpdateHql(String entityName, String id, JSONObject data, List<Object> parameters){
		StringBuilder finalUpdateEntityHql = new StringBuilder();
		finalUpdateEntityHql.append("update ").append(entityName).append(" set ");
		
		Set<String> propNames = data.keySet();
		for (String pn : propNames) {
			if(pn.equalsIgnoreCase(ResourcePropNameConstants.ID) || (data.get(pn) == null)){
				continue;
			}
			if(StrUtils.isEmpty(data.get(pn))){// 这块和installUpdateHql方法不同
				finalUpdateEntityHql.append(pn);
				finalUpdateEntityHql.append("=null").append(",");
			}else{
				finalUpdateEntityHql.append(pn);
				finalUpdateEntityHql.append("=?").append(",");
				parameters.add(data.get(pn));
			}
		}
		
		finalUpdateEntityHql.setLength(finalUpdateEntityHql.length()-1);
		finalUpdateEntityHql.append(" where ").append(ResourcePropNameConstants.ID).append("=?");
		parameters.add(id);
		
		return finalUpdateEntityHql.toString();
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 修改对象
	 * <b>修改没有的实体类数据update hql语句，主要是配置的各种表信息</b>
	 * <p>用在修改表资源的功能中</p>
	 * @param entityName
	 * @param data
	 * @param otherQueryCondHql 其他查询条件hql
	 * @param otherQueryCondHqlParamValues 其他查询条件hql的参数值值
	 */
	public static JSONObject updateObject(String entityName, JSONObject data, String otherQueryCondHql, List<Object> otherQueryCondHqlParamValues){
		String updateId = data.getString(ResourcePropNameConstants.ID);
		if(updateId == null){
			throw new NullPointerException("要修改的数据id值不能为空");
		}
		try {
			List<Object> parameters = new ArrayList<Object>(data.size()-1);
			String updateHql = installUpdateHql(entityName, data, parameters, otherQueryCondHql, otherQueryCondHqlParamValues);
			
			executeUpdateByHql(SqlStatementTypeConstants.UPDATE, updateHql, parameters);
			
			CurrentThreadContext.toReqLogDataAddOperSqlLog("update " + entityName, data);// 日志记录update的数据
			
			data.put(ResourcePropNameConstants.FOCUSED_OPER, data.getString(ResourcePropNameConstants.ID) + "_" + OperDataTypeConstants.EDIT);
			Log4jUtil.debug("修改数据成功[{}]", data);
			return data;
		} catch (Exception e) {
			Log4jUtil.debug("修改数据[{}]失败，异常信息为：", data, ExceptionUtil.getErrMsg(e));
			throw e;
		}
	}
	
	/**
	 * 组装update hql语句
	 * <b>组装没有的实体类数据update hql语句，主要是配置的各种表信息</b>
	 * <p>用在修改表资源的功能中</p>
	 * @param entityName
	 * @param data
	 * @param parameters
	 * @param otherQueryCondHql 其他查询条件hql
	 * @param otherQueryCondHqlParamValues 其他查询条件hql的参数值值
	 * @return
	 */
	private static String installUpdateHql(String entityName, JSONObject data, List<Object> parameters, String otherQueryCondHql, List<Object> otherQueryCondHqlParamValues){
		StringBuilder finalUpdateHql = new StringBuilder();
		ResourceHandlerUtil.initBasicPropValsForUpdate(entityName, data, null);
		
		finalUpdateHql.setLength(0);
		finalUpdateHql.append("update ").append(entityName).append(" set ");
		
		String whereIdHql = null;// 记录id的where条件，这个可有可无。还能根据builtinQueryCondMethodProcesser查询参数做为条件，更新数据
		String idValue = null;// 记录id的值
		
		Set<String> propsNames = data.keySet();
		
		for (String pn : propsNames) {
			if(pn.equalsIgnoreCase(ResourcePropNameConstants.ID)){
				whereIdHql = " where " + ResourcePropNameConstants.ID + "=? ";
				idValue = data.getString(pn);
				continue;
			}
			if(data.get(pn) == null){// 这块和installEntityUpdateHql方法不同
				finalUpdateHql.append(pn);
				finalUpdateHql.append("=null").append(",");
			}else{
				finalUpdateHql.append(pn);
				finalUpdateHql.append("=?").append(",");
				parameters.add(data.get(pn));
			}
		}
		finalUpdateHql.setLength(finalUpdateHql.length()-1);
		
		if(StrUtils.notEmpty(whereIdHql)){
			finalUpdateHql.append(whereIdHql);
			parameters.add(idValue);
		}
		if(StrUtils.notEmpty(otherQueryCondHql)){
			otherQueryCondHql = otherQueryCondHql.replace("where", "and");// 去掉otherQueryCondHql的hql语句的where，换成and，如果有where的话
			finalUpdateHql.append(otherQueryCondHql);
			
			if(otherQueryCondHqlParamValues != null && otherQueryCondHqlParamValues.size() > 0){
				parameters.addAll(otherQueryCondHqlParamValues);
			}
		}
		return finalUpdateHql.toString();
	}
	
	//------------------------------------------------------------------------------------------------------
	/**
	 * 获取关联关系对象信息
	 * @param entityName 实体名
	 * @param leftId
	 * @param rightId
	 */
	public static List<JSONObject> queryDataLinks(String entityName, String leftId, String rightId){
		List<Object> params = new ArrayList<Object>(3);
		String hql = "from " + entityName + " where projectId=?";
		params.add(CurrentThreadContext.getProjectId());
		
		if(leftId != null){
			hql += " and leftId=?";
			params.add(leftId);
		}
		if(rightId != null){
			hql += " and rightId=?";
			params.add(rightId);
		}
		return executeListQueryByHql(null, null, hql, params);
	}
	
	/**
	 * 保存关联关系对象信息
	 * @param entityName 实体名
	 * @param leftId
	 * @param rightId
	 */
	public static void saveDataLinks(String entityName, String leftId, String rightId){
		JSONObject datalink = ResourceHandlerUtil.getDataLinksObject(leftId, rightId, 1, null, null);
		HibernateUtil.saveObject(entityName, datalink, null);
	}
	
	/**
	 * 删除关联关系对象信息
	 * @param entityName 实体名
	 * @param leftId
	 * @param rightId
	 */
	public static void deleteDataLinks(String entityName, String leftId, String rightId){
		List<Object> params = new ArrayList<Object>(3);
		String hql = "delete "+entityName+" where projectId=?";
		params.add(CurrentThreadContext.getProjectId());
		
		if(leftId != null){
			hql += " and leftId=?";
			params.add(leftId);
		}
		if(rightId != null){
			hql += " and rightId=?";
			params.add(rightId);
		}
		executeUpdateByHql(SqlStatementTypeConstants.DELETE, hql, params);
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param hqlDes @see SqlStatementTypeConstants
	 * @param modifyHql
	 * @param parameters
	 * @return 
	 * @throws IllegalArgumentException 
	 */
	public static int executeUpdateByHql(String hqlDes, String modifyHql, List<Object> parameters) throws IllegalArgumentException {
		Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]要{}数据的hql为：{}", hqlDes, modifyHql);
		Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]要{}数据的hql所带的参数值集合为：{}", hqlDes, parameters);
		
		try {
			Query query = getCurrentThreadSession().createQuery(modifyHql);
			setParamters(query, parameters);
			int modifyCount = query.executeUpdate();
			Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]{}了{}条数据", hqlDes, modifyCount);
			return modifyCount;
		} catch (HibernateException e) {
			throw new IllegalArgumentException("[HibernateUtil.executeUpdateByHql]["+hqlDes+"]数据的时候出现了异常信息：" + ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param hqlDes @see SqlStatementTypeConstants
	 * @param modifyHql
	 * @param parameterArr
	 */
	public static void executeUpdateByHqlArr(String hqlDes, String modifyHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		executeUpdateByHql(hqlDes, modifyHql, parameters);
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * hql查询多条数据
	 * @param rows 一页显示多少行，可为null，为null则不分页
	 * @param pageNo 显示第几页，可为null，为null则不分页
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	public static List executeListQueryByHqlArr(String rows, String pageNo, String queryHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		return executeListQueryByHql(rows, pageNo, queryHql, parameters);
	}
	
	/**
	 * hql分页查询多条数据
	 * @param rows 一页显示多少行，可为null，为null则不分页
	 * @param pageNo 显示第几页，可为null，为null则不分页
	 * @param queryHql
	 * @param parameters
	 * @return
	 */
	public static List executeListQueryByHql(String rows, String pageNo, String queryHql, List<Object> parameters){
		Query query = getCurrentThreadSession().createQuery(queryHql);
		setParamters(query, parameters);
		setPageQuery(query, rows, pageNo);
		return query.list();
	}
	
	/**
	 * hql查询一条数据
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	public static Object executeUniqueQueryByHqlArr(String queryHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		return executeUniqueQueryByHql(queryHql, parameters);
	}
	
	/**
	 * hql查询一条数据
	 * @param queryHql
	 * @param parameters
	 * @return
	 */
	public static Object executeUniqueQueryByHql(String queryHql, List<Object> parameters){
		Query query = getCurrentThreadSession().createQuery(queryHql);
		
		setParamters(query, parameters);
		return query.uniqueResult();
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 给query对象中的参数赋值
	 * <p>即给hql/sql中的?占位符赋值</p>
	 * @param query
	 * @param parameters
	 * @return
	 */
	public static void setParamters(Query query, List<Object> parameters){
		if(parameters != null && parameters.size() > 0){
			int i = 0;
			for (Object val : parameters) {
				query.setParameter(i++, val);
			}
			parameters.clear();
		}
	}
	
	/**
	 * 将数组参数，转换为list参数
	 * @param parameterArr
	 * @return
	 */
	private static List<Object> processParameterArr(Object... parameterArr){
		List<Object> parameters = null;
		if(parameterArr != null && parameterArr.length > 0){
			int len = parameterArr.length;
			parameters = new ArrayList<Object>(len);
			for(int i=0;i<len;i++){
				if(parameterArr[i] != null){
					parameters.add(parameterArr[i]);
				}
			}
		}
		return parameters;
	}
	
	/**
	 * 设置分页查询
	 * @param query
	 * @param rows 一页显示多少行
	 * @param pageNo 显示第几页
	 */
	private static void setPageQuery(Query query, String rows, String pageNo) {
		if(rows != null && pageNo != null){
			PageQueryEntity pageQueryEntity = new PageQueryEntity(null, null, rows, pageNo);
			pageQueryEntity.execAnalysisPageQueryParams();
			
			query.setFirstResult(pageQueryEntity.getFirstResult());
			query.setMaxResults(pageQueryEntity.getMaxResults());
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 【扩展】hql查询多条数据
	 * <p>该方法针对hql查询结果是map的使用</p>
	 * <p>因为系统现在将例如cfgTabledata这些都转换为了map用hibernate操作，在查询的时候，需要通过这个方法转换为原来的集合对象</p>
	 * @param clazz
	 * @param rows 一页显示多少行，可为null，为null则不分页
	 * @param pageNo 显示第几页，可为null，为null则不分页
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	public static <T> List<T> extendExecuteListQueryByHqlArr(Class<T> clazz, String rows, String pageNo, String queryHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		List<Map<String, Object>> map = executeListQueryByHql(rows, pageNo, queryHql, parameters);
		return JsonUtil.turnListMapToJavaListEntity(map, clazz);
	}
	
	/**
	 * 【扩展】hql查询一条数据
	 * <p>该方法针对hql查询结果是map的使用</p>
	 * <p>因为系统现在将例如cfgTabledata这些都转换为了map用hibernate操作，在查询的时候，需要通过这个方法转换为原来的实体对象</p>
	 * @param clazz
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	public static <T> T extendExecuteUniqueQueryByHqlArr(Class<T> clazz, String queryHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		Map<String, Object> map = (Map<String, Object>) executeUniqueQueryByHql(queryHql, parameters);
		return JsonUtil.turnMapToJavaEntity(map, clazz);
	}
	
	/**
	 * 【扩展】hql查询一条数据
	 * <p>该方法针对hql查询结果是map的使用</p>
	 * <p>因为系统现在将例如cfgTabledata这些都转换为了map用hibernate操作，在查询的时候，需要通过这个方法转换为原来的实体对象</p>
	 * @param clazz
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	public static <T> T extendExecuteUniqueQueryByHql(Class<T> clazz, String queryHql, List<Object> parameters){
		Map<String, Object> map = (Map<String, Object>) executeUniqueQueryByHql(queryHql, parameters);
		return JsonUtil.turnMapToJavaEntity(map, clazz);
	}
	
	//------------------------------------------------
	/**
	 * sql查询一条数据
	 * @param querySql
	 * @param parameterArr
	 * @return
	 */
	public static Object executeUniqueQueryBySqlArr(String querySql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		return executeUniqueQueryBySql(querySql, parameters);
	}
	/**
	 * sql查询一条数据
	 * @param querySql
	 * @param parameters
	 * @return
	 */
	public static Object executeUniqueQueryBySql(String querySql, List<Object> parameters){
		Query query = getCurrentThreadSession().createSQLQuery(querySql);
		setParamters(query, parameters);
		return query.uniqueResult();
	}
	
	/**
	 * sql查询列表数据
	 * @param querySql
	 * @param parameterArr
	 * @return
	 */
	public static List executeListQueryBySqlArr(String querySql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		return executeListQueryBySql(querySql, parameters);
	}
	/**
	 * sql查询列表数据
	 * @param querySql
	 * @param parameters
	 * @return
	 */
	public static List executeListQueryBySql(String querySql, List<Object> parameters){
		Query query = getCurrentThreadSession().createSQLQuery(querySql);
		setParamters(query, parameters);
		return query.list();
	}
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param sqlDes @see SqlStatementTypeConstants
	 * @param modifySql
	 * @param parameters
	 * @return 
	 * @throws IllegalArgumentException 
	 */
	public static int executeUpdateBySql(String sqlDes, String modifySql, List<Object> parameters) throws IllegalArgumentException {
		Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]要{}数据的sql为：{}", sqlDes, modifySql);
		Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]要{}数据的sql所带的参数值集合为：{}", sqlDes, parameters);
		
		try {
			Query query = getCurrentThreadSession().createSQLQuery(modifySql);
			setParamters(query, parameters);
			int modifyCount = query.executeUpdate();
			Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]{}了{}条数据", sqlDes, modifyCount);
			return modifyCount;
		} catch (HibernateException e) {
			throw new IllegalArgumentException("[HibernateUtil.executeUpdateBySql]["+sqlDes+"]数据的时候出现了异常信息：" + ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param sqlDes @see SqlStatementTypeConstants
	 * @param modifySql
	 * @param parameterArr
	 */
	public static void executeUpdateBySqlArr(String sqlDes, String modifySql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		executeUpdateBySql(sqlDes, modifySql, parameters);
	}
}
