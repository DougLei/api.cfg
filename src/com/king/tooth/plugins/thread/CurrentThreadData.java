package com.king.tooth.plugins.thread;

import org.hibernate.Session;

import com.king.tooth.sys.entity.common.ComSysAccount;

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
	private ComSysAccount currentAccount;
	
	
	public Session getCurrentSession() {
		return currentSession;
	}
	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}
	public ComSysAccount getCurrentAccount() {
		return currentAccount;
	}
	public void setCurrentAccount(ComSysAccount currentAccount) {
		this.currentAccount = currentAccount;
	}
}
