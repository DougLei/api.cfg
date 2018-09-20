package com.king.tooth.util;


/**
 * 异常操作工具类
 * @author DougLei
 */
public class ExceptionUtil {
	
	private static final boolean isDevelop;
	static{
		isDevelop = Boolean.valueOf(ResourceHandlerUtil.initConfValue("is.develop", "false"));
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
		StringBuilder errMsg = new StringBuilder("系统错误信息为： >>>>>>");
		errMsg.append(e.getMessage());
		if(e.getCause() != null){
			errMsg.append("。").append(e.getCause().getMessage());
		}
		return errMsg.toString();
	}
}
