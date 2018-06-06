package com.king.tooth.plugins.thread;

import org.hibernate.Session;

import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;

/**
 * 当前线程的数据对象
 * @author DougLei
 */
class CurrentThreadData {
	/**
	 * 当前线程的项目上下文
	 * <p>记录每个线程请求的项目主键</p>
	 */
	private String projectId;
	/**
	 * 当前线程的数据库上下文
     * <p>通过不同的数据库id，获取不同的dataSource和sessionFactory对象</p>
	 */
	private String databaseId;
	/**
	 * 当前线程的hibernate session的上下文
     * <p>方便管理，比如事务</p>
	 */
	private Session currentSession;
	/**
	 * 当前线程的帐号对象上下文
	 */
	private ComSysAccountOnlineStatus currentAccountOnlineStatus;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	public Session getCurrentSession() {
		return currentSession;
	}
	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}
	public ComSysAccountOnlineStatus getCurrentAccountOnlineStatus() {
		return currentAccountOnlineStatus;
	}
	public void setCurrentAccountOnlineStatus(
			ComSysAccountOnlineStatus currentAccountOnlineStatus) {
		this.currentAccountOnlineStatus = currentAccountOnlineStatus;
	}
}
