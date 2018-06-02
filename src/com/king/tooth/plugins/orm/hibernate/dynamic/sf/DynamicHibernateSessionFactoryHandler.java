package com.king.tooth.plugins.orm.hibernate.dynamic.sf;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.util.StrUtils;

/**
 * 动态hibernate sessionFactory的操作者
 * @author DougLei
 */
public class DynamicHibernateSessionFactoryHandler {
	
	/**
	 * 动态sessionFactory的持有者
	 */
	private DynamicHibernateSessionFactoryHolder sessionFactoryHolder;
	
	public void setSessionFactoryHolder(DynamicHibernateSessionFactoryHolder sessionFactoryHolder) {
		this.sessionFactoryHolder = sessionFactoryHolder;
	}
	
	/**
	 * 动态添加sessionFactory
	 * @param databaseId
	 * @param dataSource
	 * @param hibernateDialect 如果不传值，默认使用平台的方言 @see jdbc.properties
	 */
	public void addSessionFactory(String databaseId, DataSource dataSource, String hibernateDialect){
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setDataSource(dataSource);
		
		Properties hibernateProperties = new Properties();
		// @see jdbc.properties
		if(StrUtils.notEmpty(hibernateDialect)){
			hibernateProperties.setProperty("hibernate.dialect", hibernateDialect);
		}else{
			hibernateProperties.setProperty("hibernate.dialect", SysConfig.getSystemConfig("hibernate.dialect"));
		}
		lsfb.setHibernateProperties(hibernateProperties);
		
		sessionFactoryHolder.addSessionFactory(databaseId, lsfb.getObject());
	}
	
	/**
	 * 动态删除sessionFactory
	 * @param databaseId
	 * @return
	 */
	public SessionFactoryImpl removeSessionFactory(String databaseId){
		if(SysConfig.getSystemConfig("cfg.database.id").equals(databaseId)
				|| SysConfig.getSystemConfig("test.database.id").equals(databaseId)){
			throw new IllegalArgumentException("不能删除系统内置的sessionFactory");
		}
		return (SessionFactoryImpl) sessionFactoryHolder.removeSessionFactory(databaseId);
	}
	
	/**
	 * 获取sessionFactory
	 */
	public SessionFactoryImpl getSessionFactory(){
		return (SessionFactoryImpl) sessionFactoryHolder.getSessionFactory();
	}
	
	/**
	 * 根据databaseId，获取sessionFactory
	 * @param databaseId
	 */
	public SessionFactoryImpl getSessionFactory(String databaseId){
		return (SessionFactoryImpl) sessionFactoryHolder.getSessionFactory(databaseId);
	}

	/**
	 * 获取动态的sessionFactory对象集合
	 * @return
	 */
	public Map<String, SessionFactory> getAllDynamicSessionFactorys() {
		return sessionFactoryHolder.getAllDynamicSessionFactorys();
	}
}
