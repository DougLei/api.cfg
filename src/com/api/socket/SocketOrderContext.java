package com.api.socket;

import java.lang.reflect.Field;

import com.api.util.Log4jUtil;

/**
 * 
 * @author DougLei
 */
public class SocketOrderContext {
	public static final char[] order = {0x01, 0x06, 0x04, 0x05, 0x13, 0x87, 0xD5, 0xA9}; // 正传命令, 16进制char[]常量
	public static final char[] reverseOrder = {0x01, 0x06, 0x04, 0x05, 0x13, 0x86, 0x14, 0x69}; // 反转命令, 16进制char[]常量
	public static final char[] close = {0x01, 0x06, 0x04, 0x05, 0x13, 0x88, 0x95, 0xAD}; // 关闭命令, 16进制char[]常量
	
	public static char[] getHexOrder(String orderName){
		try {
			Field field = SocketOrderContext.class.getField(orderName);
			return (char[]) field.get(null);
		} catch (Exception e) {
			Log4jUtil.error("获取socket命令时出现异常");
		}
		return null;
	}
	
	public static void main(String[] args) {
//		String order = "01 02";// 这里填写命令名称
//		StringBuilder orderHex = new StringBuilder("public static final char[] "+order+" = {");
//		for(int i=0;i<order.length();i++){
//			orderHex.append("0x").append(Integer.toHexString(order.charAt(i)));
//			if(i < order.length()-1){
//				orderHex.append(", ");
//			}
//		}
//		orderHex.append("}; // "+order+"命令, 16进制char[]常量");
//		System.out.println(orderHex.toString());
	}
}
