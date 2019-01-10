package com.api.thread.operdb.websocket.pushmessage;

import org.hibernate.Session;

import com.api.thread.operdb.HibernateOperDBThread;

/**
 * 推送消息的线程父类
 * @author DougLei
 */
public abstract class PMThread extends HibernateOperDBThread{

	/**
	 * 批次编号
	 */
	protected String batchNum;
	
	public PMThread(Session session, String batchNum) {
		super(session);
		this.batchNum = batchNum;
	}
}
