package com.king.tooth.util;

import com.king.tooth.cache.SysConfig;

/**
 * 异常操作工具类
 * @author DougLei
 */
public class ExceptionUtil {
	
	private static final boolean isDevelop;
	static{
		isDevelop = Boolean.valueOf(SysConfig.getSystemConfig("is.develop"));
	}
	
	/**
	 * 获取异常信息
	 * @param e
	 * @return
	 */
	public static String getErrMsg(Exception e) {
		if(isDevelop){
			e.printStackTrace();
		}
		StringBuilder errMsg = new StringBuilder();
		errMsg.append("异常的信息为：").append(e.getMessage());
		if(e.getCause() != null){
			errMsg.append("。").append(e.getCause().getMessage());
		}
		return errMsg.toString();
	}
}
