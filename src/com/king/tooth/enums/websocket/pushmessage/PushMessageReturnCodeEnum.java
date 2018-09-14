package com.king.tooth.enums.websocket.pushmessage;

/**
 * 消息推送的返回编码枚举
 * @author DougLei
 */
public enum PushMessageReturnCodeEnum {
	
	EXCEPTION(-1, "调用推送消息接口，系统出现异常"),
	NOTNULL(0, "调用推送消息接口，传入的数据不能为空"),
	SUCCESS(1, "推送成功"),
	UN_ONLINE(2, "被推送的客户端不在线"),
	TO_USERID_NOTNULL(3, "要接收推送消息的用户id不能为空"),
	MESSAGE_NOTNULL(4, "推送消息的内容不能为空");
	
	/**
	 * 编码
	 */
	private final Integer code;
	/**
	 * 描述
	 */
	private final String desc;
	
	private PushMessageReturnCodeEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}