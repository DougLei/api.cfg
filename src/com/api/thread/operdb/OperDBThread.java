package com.api.thread.operdb;

/**
 * 操作数据库的线程
 * @author DougLei
 */
public abstract class OperDBThread extends Thread{
	/**
	 * 当前操作的账户id
	 */
	protected String currentAccountId;
	/**
	 * 当前操作的用户id
	 */
	protected String currentUserId;
	/**
	 * 项目id
	 */
	protected String projectId;
	/**
	 * 客户id
	 */
	protected String customerId;
}
