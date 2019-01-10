package com.api.util.websocket.pushmessage;


/**
 * 消息推送的数据实体
 * @author DougLei
 */
public class PushMessageDataEntity {
	
	/**
	 * 接收的用户id
	 * <p>多个用,隔开</p>
	 */
	private String toUserId;
	/**
	 * 推送的消息内容
	 */
	private String message;

	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public PushMessageDataEntity(String toUserId, String message) {
		this.toUserId = toUserId;
		this.message = message;
	}
	public PushMessageDataEntity() {
	}
}
