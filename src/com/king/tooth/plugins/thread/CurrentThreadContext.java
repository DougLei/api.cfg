package com.king.tooth.plugins.thread;

import org.hibernate.Session;

import com.king.tooth.sys.entity.common.ComSysAccount;

/**
 * 当前线程的上下文
 * <p>记录每个线程请求的数据</p>
 * @author DougLei
 */
public class CurrentThreadContext {
	/**
	 * 当前线程的项目上下文
	 */
	private transient static final ThreadLocal<CurrentThreadData> currentThreadContext = new ThreadLocal<CurrentThreadData>();
	
	/**
	 * 初始化当前线程的数据对象
	 */
	private static final void setCurrentThreadData(){
		if(currentThreadContext.get() == null){
			CurrentThreadData currentThreadData = new CurrentThreadData();
			currentThreadContext.set(currentThreadData);
		}
	}
	
	/**
	 * 清除当前线程的数据对象
	 */
	public static final void clearCurrentThreadData(){
		currentThreadContext.remove();
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程的账户对象
	 * @return
	 */
	public static ComSysAccount getCurrentAccount(){
		setCurrentThreadData();
		return currentThreadContext.get().getCurrentAccount();
	}
	/**
	 * 设置当前线程的账户对象
	 * @param session
	 */
	public static void setCurrentAccount(ComSysAccount currentAccount){
		setCurrentThreadData();
		currentThreadContext.get().setCurrentAccount(currentAccount);
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程的session对象
	 * @return
	 */
	public static Session getCurrentSession(){
		setCurrentThreadData();
		return currentThreadContext.get().getCurrentSession();
	}
	/**
	 * 设置当前线程的session对象
	 * @param session
	 */
	public static void setCurrentSession(Session session){
		setCurrentThreadData();
		currentThreadContext.get().setCurrentSession(session);
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程的数据库主键
	 * @return
	 */
	public static final String getDatabaseId(){
		setCurrentThreadData();
		return currentThreadContext.get().getDatabaseId();
	}
	/**
	 * 给当前线程设置要调用的数据库主键
	 * @param databaseId
	 */
	public static final void setDatabaseId(String databaseId){
		setCurrentThreadData();
		currentThreadContext.get().setDatabaseId(databaseId);
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程的项目主键
	 * @return
	 */
	public static final String getProjectId(){
		setCurrentThreadData();
		return currentThreadContext.get().getProjectId();
	}
	/**
	 * 给当前线程设置要调用的项目主键
	 * @param projectId
	 */
	public static final void setProjectId(String projectId){
		setCurrentThreadData();
		currentThreadContext.get().setProjectId(projectId);
	}
}
