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
	 * @param throwEClassName
	 * @param throwEMethodName
	 * @param e
	 * @return
	 */
	public static String getErrMsg(String throwEClassName, String throwEMethodName, Exception e) {
		if(isDevelop){
			e.printStackTrace();
		}
		StringBuilder errMsg = new StringBuilder("抛出异常信息的位置为：[");
		errMsg.append(throwEClassName).append(".").append(throwEMethodName).append("]>>>>>>");
		errMsg.append("异常的信息为：").append(e.getMessage());
		if(e.getCause() != null){
			errMsg.append(">>>>>>").append(e.getCause().getMessage());
		}
		return errMsg.toString();
	}
}
