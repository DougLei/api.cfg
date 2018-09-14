package com.king.tooth.websocket.pushmessage.entity;

import java.io.IOException;
import java.util.Date;

import javax.websocket.Session;

/**
 * 推送消息的客户端对象
 * @author DougLei
 */
public class PushMessageClient {
	
	/**
	 * 与客户端的连接对象
	 */
	private Session session;
	
	/**
	 * 连接上的时间
	 * <p>即何时连接上</p>
	 */
	private Date connectDate;

	public PushMessageClient(Session session) {
		this.session = session;
	}
	public PushMessageClient() {
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Date getConnectDate() {
		return connectDate;
	}
	public void setConnectDate(Date connectDate) {
		this.connectDate = connectDate;
	}
	
	/**
	 * 关闭连接
	 * @throws IOException 
	 */
	public void closeSession() throws IOException {
		if(session != null && session.isOpen()){
			session.close();
		}
	}
}
