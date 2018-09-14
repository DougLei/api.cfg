package com.king.tooth.websocket.pushmessage.entity;

import com.king.tooth.util.StrUtils;
import com.king.tooth.websocket.pushmessage.enums.PushMessageReturnCodeEnum;

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
	
	/**
	 * 验证所有不能为空的属性
	 * @return
	 */
	private PushMessageReturnCodeEnum validNotNullProps() {
		if(StrUtils.isEmpty(toUserId)){
			return PushMessageReturnCodeEnum.TO_USERID_NOTNULL;
		}
		if(StrUtils.isEmpty(message)){
			return PushMessageReturnCodeEnum.MESSAGE_NOTNULL;
		}
		return null;
	}
	
	/**
	 * 解析资源属性
	 * @return
	 */
	public PushMessageReturnCodeEnum analysisResourceProp() {
		return validNotNullProps();
	}
}
