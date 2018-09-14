package com.king.tooth.enums.websocket.pushmessage;

/**
 * 消息推送的返回编码枚举
 * @author DougLei
 */
public enum PushMessageReturnCodeEnum {
	
	LOCAL_EXCEPTION(-2, "调用推送消息接口，业务系统出现异常，调用失败"),
	EXCEPTION(-1, "调用推送消息接口，接口系统出现异常，调用失败"),
	NOTNULL(0, "调用推送消息接口，传入的数据不能为空"),
	SUCCESS(1, "推送成功"),
	UN_ONLINE(2, "被推送的客户端不在线"),
	TO_USERID_NOTNULL(3, "要接收推送消息的用户id不能为空"),
	MESSAGE_NOTNULL(4, "推送消息的内容不能为空"),
	CUSTOMER_NOTNULL(101, "客户信息不能为空"),
	CUSTOMER_INVALID(102, "客户信息无效");
	
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