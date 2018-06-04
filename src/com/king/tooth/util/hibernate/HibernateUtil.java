package com.king.tooth.util.hibernate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.HbmConfPropMetadata;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.Work;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.sqlscript.ProcedureSqlScriptParameter;
import com.king.tooth.sys.entity.common.sqlscript.SqlQueryResultColumn;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.SpringContextHelper;
import com.king.tooth.util.StrUtils;

/**
 * hibernate工具类
 * @author DougLei
 */
public class HibernateUtil {
	
	/**
	 * 获得对应的sessionFactory对象
	 * @return
	 */
	private static SessionFactoryImpl getSessionFactory(){
		return (SessionFactoryImpl) SpringContextHelper.getBean("cfgSessionFactory");
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
		getCurrentThreadSession().getTransaction().commit();
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
		getCurrentThreadSession().flush();
		getCurrentThreadSession().close();
		Log4jUtil.debug("[HibernateUtil.closeSessionFromCurrentThread]关闭当前线程的session，并将session从CurrentThreadHbSessionContext中移除");
	}
	
	/**
	 * 获取当前操作的数据库类型
	 * sqlserver、oracle等
	 * @return
	 */
	public static String getCurrentDatabaseType(){
		return getSessionFactory().getDatabaseType();
	}
	
	/**
	 * 获取查询sql语句，查询结果的列集合
	 * @see SqlStatementParserUtil.getSelectSqlOfResultColumnNames()使用到
	 * @param querySql
	 * @param queryCondParameters 
	 * @return
	 */
	public static List<SqlQueryResultColumn> getQueryResultColumns(final String querySql, final List<Object> queryCondParameters){
		if(StrUtils.isEmpty(querySql)){
			return null;
		}
		final List<SqlQueryResultColumn> resultColumns = new ArrayList<SqlQueryResultColumn>();
		getCurrentThreadSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					pst = connection.prepareStatement(querySql);
					if(queryCondParameters != null && queryCondParameters.size()>0){
						int i=1;
						for (Object paramValue : queryCondParameters) {
							pst.setObject(i++, paramValue);
						}
					}
					rs = pst.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int len = rsmd.getColumnCount();
					SqlQueryResultColumn src = null;
					for(int i=1;i<=len;i++){
						src = new SqlQueryResultColumn(rsmd.getColumnName(i), rsmd.getColumnName(i));
						resultColumns.add(src);
					}
				} finally{
					if(queryCondParameters != null){
						queryCondParameters.clear();
					}
					CloseUtil.closeDBConn(rs, pst);// 从当前线程session中获取的connection，会在最后同session一同关闭，不需要单独关闭。即execute中的connection参数
				}
			}
		});
		Log4jUtil.debug("执行的sql语句为：{}", querySql);
		Log4jUtil.debug("执行sql语句的条件参数集合为：{}", queryCondParameters);
		return resultColumns;
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 【数据流方式】添加新的配置文件
	 * @param hbmContent
	 */
	public static void appendNewConfig(String... hbmContents){
		if(hbmContents != null && hbmContents.length > 0){
			List<InputStream> inputs = new ArrayList<InputStream>(hbmContents.length);
			for(String hbmContent: hbmContents){
				inputs.add(new ByteArrayInputStream(hbmContent.getBytes()));
			}
			getSessionFactory().appendNewHbmConfig(inputs);
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 根据实体类名，即资源名，获取hibernate映射文件中，配置定义的属性信息集合
	 * @param resourceName
	 * @return
	 */
	public static HbmConfPropMetadata[] getHibernateDefineResourceProps(String resourceName){
		HbmConfPropMetadata[] properties = getSessionFactory().getHibernateDefineResourceProps(resourceName);
		return properties;
	}
	
	/**
	 * 获取hibernate映射文件中，配置定义的属性元数据对象
	 * @param defineProps 配置定义的属性集合
	 * @param propName 前端传递过来的属性名
	 * @return
	 */
	public static HbmConfPropMetadata getDefinePropMetadata(HbmConfPropMetadata[] defineProps, String propName){
		if(propName.startsWith("_") // 规范，属性名不能以_开头
				|| defineProps == null || defineProps.length == 0){ 
			return new HbmConfPropMetadata(propName, DataTypeConstants.STRING);
		}
		for (HbmConfPropMetadata dp : defineProps) {
			if(dp.getPropName().equalsIgnoreCase(propName)){
				return dp;
			}
		}
		return new HbmConfPropMetadata(propName, DataTypeConstants.STRING);
	}
	
	/**
	 * 获取hibernate映射文件中，配置定义的属性名
	 * @param defineProps 配置定义的属性集合
	 * @param propName 前端传递过来的属性名
	 * @return
	 */
	public static String getDefinePropName(HbmConfPropMetadata[] defineProps, String propName){
		if(propName.startsWith("_") // 规范，属性名不能以_开头
				|| defineProps == null || defineProps.length == 0){ 
			return propName;
		}
		for (HbmConfPropMetadata dp : defineProps) {
			if(dp.getPropName().equalsIgnoreCase(propName)){
				return dp.getPropName();
			}
		}
		return propName;
	}
	
	/**
	 * 根据资源名，以及属性名，从hibernate映射文件中，获取配置定义的属性名
	 * @param resourceName
	 * @param propName
	 * @return
	 */
	public static String getDefinePropName(String resourceName, String propName){
		HbmConfPropMetadata[] defineProps = getHibernateDefineResourceProps(resourceName);
		return getDefinePropName(defineProps, propName);
	}
	
	/**
	 * 获取父子资源对应的关系资源名
	 * @param parentResourceName
	 * @param resourceName
	 * @return
	 */
	public static String getDataLinkResourceName(String parentResourceName, String resourceName){
		String datalinkResourceName = parentResourceName + resourceName + ResourceNameConstants.DATALINK_RESOURCENAME_SUFFIX;
		if(getSessionFactory().isExistsInHibernateDataLinkResourceNameList(datalinkResourceName)){
			return datalinkResourceName;
		}
		return ResourceNameConstants.COMMON_DATALINK_RESOURCENAME;
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 保存对象
	 * @param entityName 实体名
	 * @param data 要保存的对象数据
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return id
	 */
	public static String saveObject(Object data, String shortDesc){
		String id = ResourceHandlerUtil.initBasicPropValsForSave(data, shortDesc);
		try {
			getCurrentThreadSession().save(data);
			Log4jUtil.debug("保存数据成功[{}]", JSONObject.toJSONString(data));
			return id;
		} catch (Exception e) {
			Log4jUtil.debug("保存数据[{}]失败，异常信息为：", JSONObject.toJSONString(data), ExceptionUtil.getErrMsg(e));
		}
		return null;
	}
	
	/**
	 * 保存对象
	 * @param entityName 实体名
	 * @param data 要保存的对象数据
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return id
	 */
	public static String saveObject(String entityName, Map<String, Object> data, String shortDesc){
		ResourceHandlerUtil.initBasicPropValsForSave(entityName, data, shortDesc);
		try {
			getCurrentThreadSession().save(entityName, data);
			Log4jUtil.debug("保存数据成功[{}]", JSONObject.toJSONString(data));
			return data.get(ResourceNameConstants.ID)+"";
		} catch (Exception e) {
			Log4jUtil.debug("保存数据[{}]失败，异常信息为：", JSONObject.toJSONString(data), ExceptionUtil.getErrMsg(e));
		}
		return null;
	}
	
	/**
	 * 修改对象
	 * @param entityName 实体名
	 * @param data 要保存的对象数据
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 */
	public static void updateObject(Object data, String shortDesc){
		ResourceHandlerUtil.initBasicPropValsForUpdate(data, shortDesc);
		try {
			getCurrentThreadSession().update(data);
			Log4jUtil.debug("修改数据成功[{}]", JSONObject.toJSONString(data));
		} catch (Exception e) {
			Log4jUtil.debug("修改数据[{}]失败，异常信息为：", JSONObject.toJSONString(data), ExceptionUtil.getErrMsg(e));
		}
	}
	
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param hqlDes 传入的hql语句的描述，例如传入"insert"或"update"或"delete"或"添加"或"修改"或"删除"等简短描述，主要用作Log4j日记记录  @see SqlStatementType
	 * @param modifyHql
	 * @param parameters
	 */
	public static void executeUpdateByHql(String hqlDes, String modifyHql, List<Object> parameters) {
		Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]要{}数据的hql为：{}", hqlDes, modifyHql);
		Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]要{}数据的hql所带的参数值集合为：{}", hqlDes, parameters);
		
		try {
			Query query = getCurrentThreadSession().createQuery(modifyHql);
			setParamters(query, parameters);
			int modifyCount = query.executeUpdate();
			Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]{}了{}条数据", hqlDes, modifyCount);
		} catch (HibernateException e) {
			Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]{}数据的时候出现了异常信息：{}", hqlDes, ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param sqlDes 传入的sql语句的描述，例如传入"insert"或"update"或"delete"或"添加"或"修改"或"删除"等简短描述，主要用作Log4j日记记录 @see SqlStatementType
	 * @param modifySql
	 * @param parameters
	 */
	public static void executeUpdateBySql(String sqlDes, String modifySql, List<Object> parameters) {
		Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]要{}数据的hql为：{}", sqlDes, modifySql);
		Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]要{}数据的hql所带的参数值集合为：{}", sqlDes, parameters);
		
		try {
			Query query = getCurrentThreadSession().createSQLQuery(modifySql);
			setParamters(query, parameters);
			int modifyCount = query.executeUpdate();
			Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]{}了{}条数据", sqlDes, modifyCount);
		} catch (HibernateException e) {
			Log4jUtil.debug("[HibernateUtil.executeUpdateBySql]{}数据的时候出现了异常信息：{}", sqlDes, ExceptionUtil.getErrMsg(e));
		}
	}
	
	private static List<Object> processParameterArr(Object... parameterArr){
		List<Object> parameters = null;
		if(parameterArr != null && parameterArr.length > 0){
			int len = parameterArr.length;
			parameters = new ArrayList<Object>(len);
			for(int i=0;i<len;i++){
				parameters.add(parameterArr[i]);
			}
		}
		return parameters;
	}
	
	/**
	 * 查询多条数据
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List executeListQueryByHqlArr(String queryHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		Query query = getCurrentThreadSession().createQuery(queryHql);
		setParamters(query, parameters);
		return query.list();
	}
	
	/**
	 * 查询多条数据
	 * @param queryHql
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List executeListQueryByHql(String queryHql, List<Object> parameters){
		Query query = getCurrentThreadSession().createQuery(queryHql);
		setParamters(query, parameters);
		return query.list();
	}
	
	/**
	 * 查询一条数据
	 * @param queryHql
	 * @param parameterArr
	 * @return
	 */
	public static Object executeUniqueQueryByHqlArr(String queryHql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		Query query = getCurrentThreadSession().createQuery(queryHql);
		setParamters(query, parameters);
		return query.uniqueResult();
	}
	
	/**
	 * 查询一条数据
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
	 * 查询多条数据
	 * @param querySql
	 * @param parameterArr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List executeListQueryBySqlArr(String querySql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		Query query = getCurrentThreadSession().createSQLQuery(querySql);
		setParamters(query, parameters);
		return query.list();
	}
	
	/**
	 * 查询多条数据
	 * @param querySql
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List executeListQueryBySql(String querySql, List<Object> parameters){
		Query query = getCurrentThreadSession().createSQLQuery(querySql);
		setParamters(query, parameters);
		return query.list();
	}
	
	/**
	 * 查询一条数据
	 * @param querySql
	 * @param parameterArr
	 * @return
	 */
	public static Object executeUniqueQueryBySqlArr(String querySql, Object... parameterArr){
		List<Object> parameters = processParameterArr(parameterArr);
		Query query = getCurrentThreadSession().createSQLQuery(querySql);
		setParamters(query, parameters);
		return query.uniqueResult();
	}
	
	/**
	 * 查询一条数据
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
	 * 执行存储过程
	 * @param procedureName
	 * @param procedureSqlScriptParameterList
	 * @return
	 */
	public static Map<String, Object> executeProcedure(final String procedureName, final List<ProcedureSqlScriptParameter> procedureSqlScriptParameterList) {
		final Map<String, Object> data = new HashMap<String, Object>(procedureSqlScriptParameterList.size());
		getCurrentThreadSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				String procedure = callProcedure(procedureName, procedureSqlScriptParameterList);
				CallableStatement cs = null;
				try {
					cs = connection.prepareCall(procedure);
					setParameters(cs, procedureSqlScriptParameterList);
					cs.execute();
					setOutputValues(cs, procedureSqlScriptParameterList);
				} finally {
					CloseUtil.closeDBConn(cs);
				}
			}
			
			/**
			 * 设置值
			 * @param cs
			 * @param procedureSqlScriptParameterList
			 * @throws SQLException 
			 */
			private void setParameters(CallableStatement cs, List<ProcedureSqlScriptParameter> procedureSqlScriptParameterList) throws SQLException {
				if(procedureSqlScriptParameterList != null && procedureSqlScriptParameterList.size() > 0){
					for (ProcedureSqlScriptParameter pssp : procedureSqlScriptParameterList) {
						if(pssp.getInOut() == 1){//in
							cs.setObject(pssp.getIndex(), pssp.getActualValue());
						}else if(pssp.getInOut() == 2){//out
							cs.registerOutParameter(pssp.getIndex(), pssp.getTypes());
						}else if(pssp.getInOut() == 3){//in out
							cs.setObject(pssp.getIndex(), pssp.getActualValue());
							cs.registerOutParameter(pssp.getIndex(), pssp.getTypes());
						}
					}
				}
			}
			
			/**
			 * 设置output类型的值
			 * @param cs
			 * @param procedureSqlScriptParameterList
			 * @throws SQLException 
			 */
			private void setOutputValues(CallableStatement cs, List<ProcedureSqlScriptParameter> procedureSqlScriptParameterList) throws SQLException {
				if(procedureSqlScriptParameterList != null && procedureSqlScriptParameterList.size() > 0){
					for (ProcedureSqlScriptParameter pssp : procedureSqlScriptParameterList) {
						if(pssp.getInOut() == 2 || pssp.getInOut() == 3){
//							pssp.setOutValue(cs.getObject(pssp.getIndex()));
							data.put(NamingTurnUtil.columnNameTurnPropName(pssp.getParameterName()), cs.getObject(pssp.getIndex()));
						}
					}
				}
			}
		});
		Log4jUtil.debug("执行procedure名为：{}", procedureName);
		Log4jUtil.debug("执行procedure的条件参数集合为：{}", JsonUtil.toJsonString(procedureSqlScriptParameterList, false));
		
		if(procedureSqlScriptParameterList != null && procedureSqlScriptParameterList.size() > 0){
			procedureSqlScriptParameterList.clear();
		}
		return data;
	}
	
	/**
	 * 组装调用存储过程的语句
	 * @param procedureName
	 * @param procedureSqlScriptParameterList
	 * @return
	 */
	private static String callProcedure(final String procedureName, final List<ProcedureSqlScriptParameter> procedureSqlScriptParameterList) {
		StringBuilder procedure = new StringBuilder();
		procedure.append("{call ").append(procedureName).append("(");
		if(procedureSqlScriptParameterList != null && procedureSqlScriptParameterList.size() > 0){
			int len = procedureSqlScriptParameterList.size();
			for (int i=0;i<len ;i++) {
				procedure.append("?,");
			}
			procedure.setLength(procedure.length() - 1);
		}
		procedure.append(")}");
		Log4jUtil.debug("调用procedure的字符串为：{}", procedure);
		return procedure.toString();
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 给query对象中的参数赋值
	 * <p>即给hql/sql中的?占位符赋值</p>
	 * @param query
	 * @param parameters
	 * @return
	 */
	private static void setParamters(Query query, List<Object> parameters){
		if(parameters != null && parameters.size() > 0){
			int i = 0;
			for (Object val : parameters) {
				query.setParameter(i++, val);
			}
			parameters.clear();
		}
	}
}
