package com.king.tooth.util.hibernate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.entity.HibernateClassMetadata;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.Work;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.orm.hibernate.dynamic.sf.DynamicHibernateSessionFactoryHandler;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.SpringContextHelper;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.pager.PageQueryEntity;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

/**
 * hibernate工具类
 * @author DougLei
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class HibernateUtil {
	
	/**
	 * 动态hibernate sessionfactory操作者
	 */
	private transient static final DynamicHibernateSessionFactoryHandler dynamicSessionFactoryHandler = 
			SpringContextHelper.getBean(DynamicHibernateSessionFactoryHandler.class);
	
	/**
	 * 获得对应的sessionFactory对象
	 * @return
	 */
	private static SessionFactoryImpl getSessionFactory(){
		return dynamicSessionFactoryHandler.getSessionFactory();
	}
	
	/**
	 * 获得当前线程的logSession对象
	 * @return
	 */
	public static Session getCurrentThreadLogSession(){
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
		getCurrentThreadSession().flush();
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
				inputs.add(new ByteArrayInputStream(hbmContent.getBytes()));
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
		String datalinkResourceName = parentResourceName + resourceName + "Links";
		if(getSessionFactory().isExistsInHibernateDataLinkResourceNameList(datalinkResourceName)){
			return datalinkResourceName;
		}
		return "SysDataLinks";
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 保存对象
	 * @param entity
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return JSONObject
	 */
	public static JSONObject saveObject(IEntity entity, String shortDesc){
		return saveObject(entity.getEntityName(), entity.toEntityJson(), shortDesc);
	}
	
	/**
	 * 保存对象
	 * @param entityName 实体名
	 * @param data 要保存的对象数据
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return JSONObject
	 */
	public static JSONObject saveObject(String entityName, JSONObject data, String shortDesc){
		ResourceHandlerUtil.initBasicPropValsForSave(entityName, data, shortDesc);
		try {
			getCurrentThreadSession().save(entityName, data);
			
			// 日志记录发出的hql/sql语句
			CurrentThreadContext.toReqLogDataAddOperSqlLog("insert " + entityName, data);
			
			Log4jUtil.debug("保存数据成功[{}]", data);
			return data;
		} catch (Exception e) {
			Log4jUtil.debug("保存数据[{}]失败，异常信息为：", data, ExceptionUtil.getErrMsg("HibernateUtil", "saveObject", e));
			throw e;
		}
	}
	
	/**
	 * 修改对象
	 * 通过拼接update hql语句修改对象
	 * <p>目前这个方法，和通用表资源的update不是统一的</p>
	 * @param entity
	 * @param shortDesc 简短描述操作：当没有当前account时，例如注册；如果有account，则该参数传入null即可；这个由具体调用的地方决定如何传值
	 * @return JSONObject
	 */
	public static JSONObject updateObject(IEntity entity, String shortDesc){
		JSONObject data = entity.toEntityJson();
		String updateId = data.getString(ResourcePropNameConstants.ID);
		if(StrUtils.isEmpty(updateId)){
			throw new NullPointerException("要修改的数据id值不能为空");
		}
		
		ResourceHandlerUtil.initBasicPropValsForUpdate(entity.getEntityName(), data, shortDesc);
		try {
			List<Object> parameters = new ArrayList<Object>(data.size()-1);
			StringBuilder updateHql = new StringBuilder("update ");
			updateHql.append(entity.getEntityName()).append(" set ");
			
			Set<String> propNames = data.keySet();
			for (String pn : propNames) {
				if(pn.equalsIgnoreCase(ResourcePropNameConstants.ID) || (data.get(pn) == null)){
					continue;
				}
				
				if(StrUtils.isEmpty(data.get(pn))){
					updateHql.append(pn);
					updateHql.append(" = null").append(",");
				}else{
					updateHql.append(pn);
					updateHql.append(" = ?").append(",");
					parameters.add(data.get(pn));
				}
			}
			
			updateHql.setLength(updateHql.length()-1);
			updateHql.append(" where ").append(ResourcePropNameConstants.ID).append(" =?");
			parameters.add(updateId);
			
			executeUpdateByHql(BuiltinDatabaseData.UPDATE, updateHql.toString(), parameters);
			Log4jUtil.debug("修改数据成功[{}]", data);
			return data;
		} catch (Exception e) {
			Log4jUtil.debug("修改数据[{}]失败，异常信息为：", data, ExceptionUtil.getErrMsg("HibernateUtil", "updateObjectByHql", e));
			throw e;
		}
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
		JSONObject datalink = ResourceHandlerUtil.getDataLinksObject(leftId, rightId, "1", null, null);
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
		executeUpdateByHql(BuiltinDatabaseData.DELETE, hql, params);
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param hqlDes @see BuiltinDatabaseData
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
			
			// 日志记录发出的hql/sql语句
			CurrentThreadContext.toReqLogDataAddOperSqlLog(modifyHql, parameters);
			
			setParamters(query, parameters);
			int modifyCount = query.executeUpdate();
			Log4jUtil.debug("[HibernateUtil.executeUpdateByHql]{}了{}条数据", hqlDes, modifyCount);
			return modifyCount;
		} catch (HibernateException e) {
			throw new IllegalArgumentException("[HibernateUtil.executeUpdateByHql]["+hqlDes+"]数据的时候出现了异常信息：" + ExceptionUtil.getErrMsg("HibernateUtil", "executeUpdateByHql", e));
		}
	}
	
	/**
	 * 修改数据
	 * <p>删除语句、修改语句、新增语句</p>
	 * @param hqlDes @see BuiltinDatabaseData
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
		
		// 日志记录发出的hql/sql语句
		CurrentThreadContext.toReqLogDataAddOperSqlLog(queryHql, parameters);
		
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
		
		// 日志记录发出的hql/sql语句
		CurrentThreadContext.toReqLogDataAddOperSqlLog(queryHql, parameters);
		
		setParamters(query, parameters);
		return query.uniqueResult();
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/**
	 * 创建数据库对象
	 * <p>存储过程、视图等</p>
	 * @param sqls
	 */
	public static void createObjects(List<ComSqlScript> sqls) {
		final boolean isSqlServler = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqls.get(0).getDbType());
		final boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqls.get(0).getDbType());
		if(!isSqlServler && !isOracle){
			throw new IllegalArgumentException("系统目前不支持["+sqls.get(0).getDbType()+"]类型的数据库操作");
		}
		processDBObjects(sqls, true, isSqlServler, isOracle);
	}
	
	/**
	 * 删除数据库对象
	 * <p>存储过程、视图等</p>
	 * @param sqls
	 */
	public static void dropObject(ComSqlScript sql){
		List<ComSqlScript> sqls = new ArrayList<ComSqlScript>(1);
		sqls.add(sql);
		dropObjects(sqls);
		sqls.clear();
	}
	
	/**
	 * 删除数据库对象
	 * <p>存储过程、视图等</p>
	 * @param sqls
	 */
	public static void dropObjects(List<ComSqlScript> sqls){
		final boolean isSqlServler = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqls.get(0).getDbType());
		final boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqls.get(0).getDbType());
		if(!isSqlServler && !isOracle){
			throw new IllegalArgumentException("系统目前不支持["+sqls.get(0).getDbType()+"]类型的数据库操作");
		}
		processDBObjects(sqls, false, isSqlServler, isOracle);
	}
	
	/**
	 * 处理数据库对象，如果存在则删除，再根据参数(isCreate)决定是否重新创建
	 * <p>存储过程、视图等</p>
	 */
	private static void processDBObjects(final List<ComSqlScript> sqls, final boolean isCreate, final boolean isSqlServler, final boolean isOracle) {
		getCurrentThreadSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				Statement st = null;
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					ComSqlScript tmpSql = sqls.get(0);
					if(isSqlServler){
						pst = conn.prepareStatement(BuiltinDatabaseData.sqlserver_queryObjectIsExistsSql);
						if(BuiltinDatabaseData.PROCEDURE.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "P");
						}else if(BuiltinDatabaseData.VIEW.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "V");
						}else{
							throw new IllegalArgumentException("系统目前不支持在sqlserver数据库中创建["+tmpSql.getSqlScriptType()+"]类型的sql对象");
						}
					}else if(isOracle){
						pst = conn.prepareStatement(BuiltinDatabaseData.oracle_queryObjectIsExistsSql);
						if(BuiltinDatabaseData.PROCEDURE.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "PROCEDURE");
						}else if(BuiltinDatabaseData.VIEW.equals(tmpSql.getSqlScriptType())){
							pst.setString(2, "VIEW");
						}else{
							throw new IllegalArgumentException("系统目前不支持在oracle数据库中创建["+tmpSql.getSqlScriptType()+"]类型的sql对象");
						}
					}
					
					st = conn.createStatement();
					for (ComSqlScript sql : sqls) {
						pst.setString(1, sql.getObjectName());
						
						// 如果已经存在对象，则删除
						rs = pst.executeQuery();
						if(rs.next() && (rs.getInt(1) > 0)){
							st.executeUpdate("drop " + sql.getSqlScriptType() + " " + sql.getObjectName());
						}
						
						if(isCreate){
							st.executeUpdate(sql.getSqlScriptContent());// 创建对象
						}
					}
				} finally{
					CloseUtil.closeDBConn(rs, st, pst);
				}
			}
		});
		
	}
	
	/**
	 * 执行存储过程
	 * @param sqlScript
	 * @return
	 */
	public static JSONObject executeProcedure(final ComSqlScript sqlScript) {
		final List<List<ComSqlScriptParameter>> sqlParamsList = sqlScript.getSqlParamsList();
		final boolean sqlScriptHavaParams = (sqlParamsList != null && sqlParamsList.size() > 0);
		if(sqlScriptHavaParams && sqlParamsList.size() >1){
			throw new IllegalArgumentException("系统目前不支持批量处理存储过程，如有需要，请联系系统管理员");
		}
		
		final String sqlScriptId = sqlScript.getId();
		final boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqlScript.getDbType());
		final boolean isSqlServer = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqlScript.getDbType());
		final String procedureName = sqlScript.getObjectName();
		
		final JSONObject json = new JSONObject(10);
		final List<ComSqlScriptParameter> sqlParams = sqlScriptHavaParams?sqlParamsList.get(0):null;
		final List<List<CfgSqlResultset>> sqlResultSetsList = sqlScript.getSqlResultsetsList()==null?new ArrayList<List<CfgSqlResultset>>(5):sqlScript.getSqlResultsetsList();
		
		getCurrentThreadSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				String procedure = callProcedure(procedureName, sqlParams);
				CallableStatement cs = null;
				ResultSet rs = null;
				try {
					cs = connection.prepareCall(procedure);
					setParameters(cs, sqlParams);
					cs.execute();
					putOutputValues(cs, rs, sqlParams);
				} finally {
					CloseUtil.closeDBConn(rs, cs);
					if(sqlParams != null && sqlParams.size() > 0){
						sqlParams.clear();
					}
				}
			}
			
			/**
			 * 设置参数
			 * @param cs
			 * @param sqlParams
			 * @throws SQLException 
			 */
			private void setParameters(CallableStatement cs, List<ComSqlScriptParameter> sqlParams) throws SQLException {
				if(sqlParams != null && sqlParams.size() > 0){
					for (ComSqlScriptParameter parameter : sqlParams) {
						if(parameter.getInOut() == 1){//in
							setParameter(cs, parameter, parameter.getActualInValue());
						}else if(parameter.getInOut() == 2){//out
							cs.registerOutParameter(parameter.getOrderCode(), parameter.getDatabaseDataTypeCode(isOracle, isSqlServer));
						}else if(parameter.getInOut() == 3){//in out
							setParameter(cs, parameter, parameter.getActualInValue());
							cs.registerOutParameter(parameter.getOrderCode(), parameter.getDatabaseDataTypeCode(isOracle, isSqlServer));
						}
					}
				}
			}
			
			/**
			 * 设置参数
			 * @param cs
			 * @param parameter
			 * @param actualInValue
			 * @throws SQLException
			 */
			private void setParameter(CallableStatement cs, ComSqlScriptParameter parameter, Object actualInValue) throws SQLException {
				if(isSqlServer){
					setSqlServerParameter(cs, parameter, actualInValue);
				}else if(isOracle){
					setOracleParameter(cs, parameter, actualInValue);
				}
			}

			/**
			 * 设置oracle的参数
			 * @param cs
			 * @param parameter
			 * @param actualInValue
			 * @throws SQLException
			 */
			private void setOracleParameter(CallableStatement cs, ComSqlScriptParameter parameter, Object actualInValue) throws SQLException {
				if(BuiltinDataType.TABLE.equals(parameter.getParameterDataType())){
					// TODO 处理oracle游标类型的参数
				}else{
					cs.setObject(parameter.getOrderCode(), actualInValue);
				}
			}
			
			/**
			 * 设置sqlserver的参数
			 * @param cs
			 * @param parameter
			 * @param actualInValue
			 * @throws SQLException 
			 */
			private void setSqlServerParameter(CallableStatement cs, ComSqlScriptParameter parameter, Object actualInValue) throws SQLException {
				if(BuiltinDataType.TABLE.equals(parameter.getParameterDataType())){
					// TODO 处理sqlserver表类型的参数
					SQLServerDataTable table = new SQLServerDataTable();
					
					
					
				}else{
					cs.setObject(parameter.getOrderCode(), actualInValue);
				}
			}

			/**
			 * 存储output类型的值
			 * @param cs
			 * @param rs 
			 * @param sqlParams
			 * @throws SQLException 
			 */
			private void putOutputValues(CallableStatement cs, ResultSet rs, List<ComSqlScriptParameter> sqlParams) throws SQLException {
				if(isSqlServer){
					putSqlServerDataSet(cs, rs);
					if(sqlParams != null && sqlParams.size() > 0){
						for (ComSqlScriptParameter sp : sqlParams) {
							if(sp.getInOut() == 2 || sp.getInOut() == 3){
								json.put(sp.getParameterName(), cs.getObject(sp.getOrderCode()));
							}
						}
					}
				}else if(isOracle){
					if(!sqlScriptHavaParams){
						return;
					}
					if(sqlParams != null && sqlParams.size() > 0){
						int sqlResultsetIndex = 0;
						for (ComSqlScriptParameter sp : sqlParams) {
							if(sp.getInOut() == 2 || sp.getInOut() == 3){
								if(BuiltinDataType.TABLE.equals(sp.getParameterDataType())){
									json.put(sp.getParameterName(), getOracleCursorDataSet(cs, rs, sp.getOrderCode(), sqlResultsetIndex, sp.getId()));
									sqlResultsetIndex++;
								}else{
									json.put(sp.getParameterName(), cs.getObject(sp.getOrderCode()));
								}
							}
						}
					}
				}
			}
			
			/**
			 * 获取oracle的游标数据集
			 * @param rs 
			 * @param cs 
			 * @param orderCode
			 * @param sqlResultsetIndex
			 * @param sqlParameterId
			 * @return
			 */
			private List<Map<String, Object>> getOracleCursorDataSet(CallableStatement cs, ResultSet rs, Integer orderCode, int sqlResultsetIndex, String sqlParameterId) throws SQLException {
				try {
					rs = (ResultSet) cs.getObject(orderCode);
					processResultSetList(rs, sqlResultsetIndex, sqlParameterId);
					return sqlQueryResultToMap(rs, sqlResultSetsList.get(sqlResultsetIndex));
				} finally{
					CloseUtil.closeDBConn(rs);
				}
			}

			/**
			 * 存储sqlserver的数据集
			 * @param cs
			 * @param rs 
			 * @throws SQLException 
			 */
			private void putSqlServerDataSet(CallableStatement cs, ResultSet rs) throws SQLException {
				int sqlResultsetIndex = 0;
				rs = cs.getResultSet();
				while(rs != null){
					processResultSetList(rs, sqlResultsetIndex, null);
					json.put(sqlResultSetsList.get(sqlResultsetIndex).get(0).getName(sqlResultsetIndex), sqlQueryResultToMap(rs, sqlResultSetsList.get(sqlResultsetIndex)));
					sqlResultsetIndex++;
					
					cs.getMoreResults();
					rs = cs.getResultSet();
				}
			}
			
			/**
			 * 处理结果集
			 * <p>如果没有结果集，则要获取结果集信息并保存到数据库</p>
			 * @param rs
			 * @param sqlResultsetIndex
			 * @param sqlParameterId
			 * @throws SQLException 
			 */
			private void processResultSetList(ResultSet rs, int sqlResultsetIndex, String sqlParameterId) throws SQLException {
				if(sqlResultSetsList.size() == sqlResultsetIndex){
					ResultSetMetaData rsmd = rs.getMetaData();
					int len = rsmd.getColumnCount();
					
					List<CfgSqlResultset> sqlResultSets = new ArrayList<CfgSqlResultset>(len);
					CfgSqlResultset csr = null;
					for(int i=1;i<=len;i++){
						csr = new CfgSqlResultset(rsmd.getColumnName(i), i);
						csr.setSqlScriptId(sqlScriptId);
						
						if(isSqlServer){
							csr.setBatchOrder(sqlResultsetIndex);
							csr.setName("dataSet"+(sqlResultsetIndex+1));
						}else if(isOracle){
							csr.setSqlParameterId(sqlParameterId);
						}
						HibernateUtil.saveObject(csr, null);// 保存结果集信息
						sqlResultSets.add(csr);
					}
					sqlResultSetsList.add(sqlResultSets);
				}
			}

			/**
			 * sql查询结果集转为list<map>
			 * <p>将列名，转换为属性名，作为key</p>
			 * <p>将值作为value显示</p>
			 * @param rs
			 * @param sqlResultSets
			 * @return
			 * @throws SQLException 
			 */
			private List<Map<String, Object>> sqlQueryResultToMap(ResultSet rs, List<CfgSqlResultset> sqlResultSets) throws SQLException{
				List<Object[]> queryResultSetList = getQueryResultSetList(rs, sqlResultSets);
				List<Map<String, Object>> dataList = null;
				if(queryResultSetList != null && queryResultSetList.size() > 0){
					dataList = new ArrayList<Map<String, Object>>(queryResultSetList.size());
					Map<String, Object> data = null;

					for(Object[] object : queryResultSetList){
						data = new HashMap<String, Object>(object.length);
						int i = 0;
						for(CfgSqlResultset csr : sqlResultSets){
							data.put(csr.getPropName(), object[i++]);
						}
						dataList.add(data);
					}
					queryResultSetList.clear();
				}else{
					Log4jUtil.debug("将sql查询结果集转为list<Map>时，sql查询的结果集为空，转换结果为空");
				}
				return dataList;
			}
			
			/**
			 * 从ResultSet中，获取结果集集合
			 * @param rs
			 * @param sqlResultSets
			 * @return
			 * @throws SQLException 
			 */
			private List<Object[]> getQueryResultSetList(ResultSet rs, List<CfgSqlResultset> sqlResultSets) throws SQLException{
				if(sqlResultSets == null || sqlResultSets.size() == 0){
					throw new NullPointerException("将sql查询结果转为map时，要参照的结果集列信息集合不能为空[sqlResultsets]");
				}
				List<Object[]> queryResultSetList = null;
				
				int flag = 0;
				int size = sqlResultSets.size();
				Object[] objects;
				while(rs.next()){
					if(flag == 0){
						queryResultSetList = new ArrayList<Object[]>();
						flag++;
					}
					objects = new Object[size];
					for(int i=0;i<size;i++){
						objects[i] = rs.getObject(i+1);
					}
					queryResultSetList.add(objects);
				}
				return queryResultSetList;
			}
		});
		Log4jUtil.debug("执行procedure名为：{}", procedureName);
		Log4jUtil.debug("执行procedure的条件参数集合为：{}", JsonUtil.toJsonString(sqlParams, false));
		
		if(sqlScriptHavaParams){
			for (List<ComSqlScriptParameter> sp : sqlParamsList) {
				if(sp != null){
					sp.clear();
				}
			}
			sqlParamsList.clear();
		}
		return json;
	}
	
	/**
	 * 组装调用存储过程的语句
	 * @param procedureName
	 * @param sqlScriptParameterList
	 * @return
	 */
	private static String callProcedure(final String procedureName, final List<ComSqlScriptParameter> sqlParams) {
		StringBuilder procedure = new StringBuilder();
		procedure.append("{call ").append(procedureName).append("(");
		if(sqlParams != null && sqlParams.size() > 0){
			int len = sqlParams.size();
			for (int i=0;i<len ;i++) {
				procedure.append("?,");
			}
			procedure.setLength(procedure.length() - 1);
		}
		procedure.append(")}");
		Log4jUtil.debug("调用的procedure为：{}", procedure);
		
		// 日志记录发出的hql/sql语句
		CurrentThreadContext.toReqLogDataAddOperSqlLog(procedure.toString(), sqlParams);
		
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
			query.setMaxResults(pageQueryEntity.getMaxResult());
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
		
		// 日志记录发出的hql/sql语句
		CurrentThreadContext.toReqLogDataAddOperSqlLog(querySql, parameters);
		
		setParamters(query, parameters);
		return query.uniqueResult();
	}
}
