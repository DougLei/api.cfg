package com.king.tooth.plugins.thread;

import org.hibernate.Session;

import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;

/**
 * 当前线程的数据对象
 * @author DougLei
 */
class CurrentThreadData {
	/**
	 * 当前线程的hibernate session的上下文
     * <p>方便管理，比如事务</p>
	 */
	private Session currentSession;
	/**
	 * 当前线程的帐号对象上下文
	 */
	private ComSysAccountOnlineStatus currentAccountOnlineStatus;
	
	
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
