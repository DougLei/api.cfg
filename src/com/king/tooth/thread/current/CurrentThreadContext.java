package com.king.tooth.thread.current;

import org.hibernate.Session;

import com.king.tooth.cache.DatabaseInstancesMapping;
import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.entity.sys.reqlog.ReqLogData;
import com.king.tooth.thread.operdb.account.online.status.UpdateAccountOnlineStatusThread;
import com.king.tooth.util.hibernate.HibernateUtil;

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
	public static SysAccountOnlineStatus getCurrentAccountOnlineStatus(){
		setCurrentThreadData();
		return currentThreadContext.get().getCurrentAccountOnlineStatus();
	}
	/**
	 * 设置当前线程的账户在线对象
	 * @param session
	 */
	public static void setCurrentAccountOnlineStatus(SysAccountOnlineStatus currentAccountOnlineStatus){
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
	 * 获取当前线程的租户主键
	 * @return
	 */
	public static final String getCustomerId(){
		setCurrentThreadData();
		return currentThreadContext.get().getCustomerId();
	}
	/**
	 * 给当前线程设置要调用的租户主键
	 * @param projectId
	 */
	public static final void setCustomerId(String customerId){
		setCurrentThreadData();
		currentThreadContext.get().setCustomerId(customerId);
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
	 * 获取当前线程配置的项目id
	 * <p>配置系统专用</p>
	 * @return
	 */
	public static final String getConfProjectId(){
		setCurrentThreadData();
		return currentThreadContext.get().getConfProjectId();
	}
	/**
	 * 给当前线程设置配置的项目id
	 * <p>配置系统专用</p>
	 * @param confProjectId
	 */
	public static final void setConfProjectId(String confProjectId){
		setCurrentThreadData();
		currentThreadContext.get().setConfProjectId(confProjectId);
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程的请求日志数据对象
	 * @return
	 */
	public static final ReqLogData getReqLogData(){
		setCurrentThreadData();
		return currentThreadContext.get().getReqLogData();
	}
	/**
	 * 给当前线程的请求日志中增加一条操作的sql日志
	 * @param sqlScript
	 * @param sqlParams
	 */
	public static final void toReqLogDataAddOperSqlLog(String sqlScript, Object sqlParams){
		SysReqLog reqLog = getReqLogData().getReqLog();
		if(reqLog != null){
			reqLog.addOperSqlLog(sqlScript, sqlParams);
		}
	}
	
	//-------------------------------------------------------------------
	/**
	 * 获取当前线程对应的database实例
	 * @return
	 */
	public static CfgDatabase getDatabaseInstance() {
		return  DatabaseInstancesMapping.getDatabasInstance(CurrentThreadContext.getDatabaseId());
	}

	//-------------------------------------------------------------------
	/**
	 * 更新相关数据
	 * @param isClearCurrentThreadData 是否清空当前线程中的数据
	 */
	public static void updateDatas(boolean isClearCurrentThreadData) {
		// 修改账户在线状态信息
		new UpdateAccountOnlineStatusThread(HibernateUtil.openNewSession(),
				getCurrentAccountOnlineStatus(),
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
				CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
				CurrentThreadContext.getProjectId(),
				CurrentThreadContext.getCustomerId()).start();
		
		// 记录日志
		getReqLogData().recordLogs();
		
		// (是否)清除本次请求的线程数据
		if(isClearCurrentThreadData){
			clearCurrentThreadData();
		}
	}
}
