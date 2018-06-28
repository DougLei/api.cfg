package com.king.tooth.plugins.thread;

import org.hibernate.Session;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;

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
	 * 获取当前线程的账户在线对象
	 * @return
	 */
	public static ComSysAccountOnlineStatus getCurrentAccountOnlineStatus(){
		setCurrentThreadData();
		return currentThreadContext.get().getCurrentAccountOnlineStatus();
	}
	/**
	 * 设置当前线程的账户在线对象
	 * @param session
	 */
	public static void setCurrentAccountOnlineStatus(ComSysAccountOnlineStatus currentAccountOnlineStatus){
		setCurrentThreadData();
		currentThreadContext.get().setCurrentAccountOnlineStatus(currentAccountOnlineStatus);
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
		currentThreadContext.get().setDatabaseId(ProjectIdRefDatabaseIdMapping.getDbId(projectId));
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程的请求日志对象
	 * @return
	 */
	public static final ComReqLog getCurrentReqLog(){
		setCurrentThreadData();
		return currentThreadContext.get().getCurrentReqLog();
	}
	/**
	 * 给当前线程设置请求日志对象
	 * @param currentReqLog
	 */
	public static final void setCurrentReqLog(ComReqLog currentReqLog){
		setCurrentThreadData();
		currentThreadContext.get().setCurrentReqLog(currentReqLog);
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程配置的项目id
	 * <p>配置系统专用</p>
	 * @return
	 */
	public static final String getCurrentConfProjectId(){
		setCurrentThreadData();
		return currentThreadContext.get().getConfProjectId();
	}
	/**
	 * 给当前线程设置配置的项目id
	 * <p>配置系统专用</p>
	 * @param confProjectId
	 */
	public static final void setCurrentConfProjectId(String confProjectId){
		setCurrentThreadData();
		currentThreadContext.get().setConfProjectId(confProjectId);
	}
}
