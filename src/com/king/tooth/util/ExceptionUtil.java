package com.king.tooth.util;

/**
 * 异常操作工具类
 * @author DougLei
 */
public class ExceptionUtil {
	
	/**
	 * 获取异常信息
	 * @param e
	 * @return
	 */
	public static String getErrMsg(Exception e) {
		e.printStackTrace();
		StringBuilder errMsg = new StringBuilder();
		errMsg.append("异常的信息为：").append(e.getMessage());
		if(e.getCause() != null){
			errMsg.append("。").append(e.getCause().getMessage());
		}
		return errMsg.toString();
	}
}
