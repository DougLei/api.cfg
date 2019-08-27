package com.api.socket;

import java.lang.reflect.Field;

import com.api.util.Log4jUtil;

/**
 * 
 * @author DougLei
 */
public class SocketOrderContext {
	public static final char[] socketOrderContextTest = {0x73, 0x6f, 0x63, 0x6b, 0x65, 0x74, 0x4f, 0x72, 0x64, 0x65, 0x72, 0x43, 0x6f, 0x6e, 0x74, 0x65, 0x78, 0x74, 0x54, 0x65, 0x73, 0x74}; // socketOrderContextTest命令, 16进制字符串常量
	public static final char[] close = {0x63, 0x6c, 0x6f, 0x73, 0x65}; // close命令, 16进制字符串常量

	public static char[] getHexOrder(String orderName){
		try {
			Field field = SocketOrderContext.class.getField(orderName);
			return (char[]) field.get(null);
		} catch (Exception e) {
			Log4jUtil.error("获取socket命令时出现异常");
		}
		return socketOrderContextTest;
	}
	
	public static void main(String[] args) {
		String order = "socketOrderContextTest";// 这里填写命令名称
		StringBuilder orderHex = new StringBuilder("public static final char[] "+order+" = {");
		for(int i=0;i<order.length();i++){
			orderHex.append("0x").append(Integer.toHexString(order.charAt(i)));
			if(i < order.length()-1){
				orderHex.append(", ");
			}
		}
		orderHex.append("}; // "+order+"命令, 16进制字符串常量");
		System.out.println(orderHex.toString());
	}
}
