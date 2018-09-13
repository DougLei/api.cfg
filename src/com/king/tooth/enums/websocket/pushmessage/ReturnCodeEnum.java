package com.king.tooth.enums.websocket.pushmessage;

/**
 * 返回编码枚举
 * @author DougLei
 */
public enum ReturnCodeEnum {
	
	SUCCESS(1, "推送成功"),
	UN_ONLINE(2, "被推送的客户端不在线");
	
	/**
	 * 编码
	 */
	private final int code;
	/**
	 * 描述
	 */
	private final String desc;
	
	private ReturnCodeEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}
