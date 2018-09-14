package com.king.tooth.websocket.pushmessage.entity;

import com.king.tooth.util.CryptographyUtil;

/**
 * 消息推送的客户信息
 * @author DougLei
 */
public class Customer {

	/**
	 * id
	 */
	private String id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 真实姓名
	 */
	private String realName;
	
	public Customer(String id, String userName, String password, String realName) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.realName = realName;
	}
	public Customer() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	/**
	 * 获取唯一标示
	 * @return
	 */
	public String getCustomerToken() {
		return CryptographyUtil.encodeMd5(userName, password);
	}
}
