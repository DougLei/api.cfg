package com.king.tooth.util.database;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.plugins.datasource.dynamic.druid.DynamicDruidDataSourceHandler;
import com.king.tooth.plugins.orm.hibernate.dynamic.sf.DynamicHibernateSessionFactoryHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.util.SpringContextHelper;
import com.king.tooth.util.StrUtils;

/**
 * <pre>
 * 	动态数据库操作工具类
 * 		例如:	动态添加、删除数据源
 *          动态添加、删除hibernate sessionFactory
 * </pre>
 * @author DougLei
 */
public class DynamicDBUtil {
	/**
	 * 动态datasource操作者
	 */
	private transient static final DynamicDruidDataSourceHandler dynamicDruidDataSourceHandler = 
			SpringContextHelper.getBean(DynamicDruidDataSourceHandler.class);
	/**
	 * 动态hibernate sessionfactory操作者
	 */
	private transient static final DynamicHibernateSessionFactoryHandler dynamicSessionFactoryHandler = 
			SpringContextHelper.getBean(DynamicHibernateSessionFactoryHandler.class);
	
	/**
	 * <pre>
	 * 动态添加数据源
	 *   同时也会动态创建一个对应的hibernate sessionFactory对象
	 * </pre>
	 * @param database
	 */
	public static void addDataSource(ComDatabase database){
		if(StrUtils.isEmpty(database)){
			throw new NullPointerException("添加数据源和对应sessionFactory时，database参数不能为空!");
		}
		DataSource dataSource = dynamicDruidDataSourceHandler.addDataSource(database);
		dynamicSessionFactoryHandler.addSessionFactory(database.getId(), dataSource, database.getDialect());
	}
	
	/**
	 * <pre>
	 * 动态删除数据源
	 *   同时也会动态删除对应的hibernate sessionFactory对象
	 * </pre>
	 * @param databaseId
	 */
	public static void removeDataSource(String databaseId){
		if(StrUtils.isEmpty(databaseId)){
			throw new NullPointerException("删除数据源和对应sessionFactory时，databaseId值不能为空!");
		}
		SessionFactoryImpl sfi = dynamicSessionFactoryHandler.removeSessionFactory(databaseId);
		sfi.close();
		
		dynamicDruidDataSourceHandler.removeDataSource(databaseId);
	}
	
	//----------------------------------------------------------------------------------------------------------------------
	/**
	 * 根据databaseId获取对应的数据源
	 * @param databaseId
	 * @return
	 */
	public static DataSource getDataSource(String databaseId){
		return dynamicDruidDataSourceHandler.getDataSource(databaseId);
	}
	
	/**
	 * 根据databaseId获取对应的sessionFactory
	 * @param databaseId
	 * @return
	 */
	public static SessionFactoryImpl getSessionFactory(String databaseId){
		return dynamicSessionFactoryHandler.getSessionFactory(databaseId);
	}
	
	//----------------------------------------------------------------------------------------------------------------------
	/**
	 * 获取动态的数据源对象集合
	 * @return
	 */
	public static Map<String, DataSource> getAllDynamicDataSources(){
		return dynamicDruidDataSourceHandler.getAllDynamicDataSources();
	}
	
	/**
	 * 获取动态的sessionFactory对象集合
	 * @return
	 */
	public static Map<String, SessionFactory> getAllDynamicSessionFactorys(){
		return dynamicSessionFactoryHandler.getAllDynamicSessionFactorys();
	}
}
