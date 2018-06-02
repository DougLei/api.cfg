package com.king.tooth.plugins.orm.hibernate.dynamic.sf;

import java.util.Map;

import org.hibernate.SessionFactory;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;

/**
 * 动态hibernate sessionFactory的持有者
 * @author DougLei
 */
public class DynamicHibernateSessionFactoryHolder {
	
	/**
	 * sessionFactory集合，用于动态操作
	 */
	private transient static Map<String, SessionFactory> sessionFactorys;
	public static void setSessionFactorys(Map<String, SessionFactory> sessionFactorys) {
		DynamicHibernateSessionFactoryHolder.sessionFactorys = sessionFactorys;
	}

	/**
	 * 指定databaseId的sessionFactory是否存在
	 * @param databaseId
	 * @return
	 */
	private boolean sessionFactoryIsExists(String databaseId){
		return sessionFactorys.containsKey(databaseId);
	}
	
	/**
	 * 动态添加sessionFactory
	 * <p>如果已经存在，则不再覆盖添加</p>
	 * @param databaseId
	 * @param sf
	 */
	synchronized void addSessionFactory(String databaseId, SessionFactory sf){
		if(sessionFactoryIsExists(databaseId)){
			Log4jUtil.info("系统中已经存在databaseId值为 [{}]的sessionFactory！",databaseId);
			return;
		}
		sessionFactorys.put(databaseId, sf);
	}
	
	/**
	 * 动态删除sessionFactory
	 * @param databaseId
	 */
	synchronized SessionFactory removeSessionFactory(String databaseId){
		if(!sessionFactoryIsExists(databaseId)){
			throw new NullPointerException("不存在databaseId值为 [{"+databaseId+"}]的sessionFactory！删除失败！");
		}
		return sessionFactorys.remove(databaseId);
	}
	
	/**
	 * 获取sessionFactory
	 */
	SessionFactory getSessionFactory(){
		String databaseId = getDatabaseId();
		if(StrUtils.isEmpty(databaseId)){
			throw new NullPointerException("要获取sessionFactory的databaseId值为null");
		}
		
		if(sessionFactoryIsExists(databaseId)){
			return sessionFactorys.get(databaseId);
		}else{
			throw new IllegalArgumentException("不存在databaseId值为 ["+databaseId+"]的sessionFactory！");
		}
	}
	
	/**
	 * 获取当前线程的项目主键
	 * @return
	 */
	protected String getDatabaseId(){
		return CurrentThreadContext.getDatabaseId();
	}

	/**
	 * 根据databaseId，获取sessionFactory
	 * @param databaseId
	 */
	public SessionFactory getSessionFactory(String databaseId) {
		return sessionFactorys.get(databaseId);
	}

	/**
	 * 获取动态的sessionFactory对象集合
	 * @return
	 */
	public Map<String, SessionFactory> getAllDynamicSessionFactorys() {
		return sessionFactorys;
	}
}
