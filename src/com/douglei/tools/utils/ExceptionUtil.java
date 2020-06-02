package com.douglei.tools.utils;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 异常工具类
 * @author StoneKing
 */
public class ExceptionUtil {
	
	/**
	 * 获取异常的详细信息
	 * <p>错在哪个类，哪一行</p>
	 * @param t
	 * @return
	 */
	public static String getExceptionDetailMessage(Throwable t){
		PrintWriter pw = null;
		try {
			StringWriter sw = new StringWriter();
			pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e) {
			throw new UtilException("在获取异常详细信息时出现异常", e);
		} finally {
			pw.close();
			pw = null;
		}
	}
}
