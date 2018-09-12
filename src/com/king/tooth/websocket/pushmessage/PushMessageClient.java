package com.king.tooth.websocket.pushmessage;

import javax.websocket.Session;

/**
 * 推送消息的客户端
 * @author DougLei
 */
public class PushMessageClient {
	
	/**
	 * 与客户端的连接对象
	 */
	private Session session;

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
}
