package com.king.tooth.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author DougLei
 */
public class DateUtil {
	
	/**
	 * -格式，简单日期格式化类
	 * <p>日期格式为：yyyy-MM-dd</p>
	 */
	private transient static final SimpleDateFormat sdfSimple = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * -格式，详细日期格式化类
	 * <p>日期格式为：yyyy-MM-dd HH:mm:ss</p>
	 */
	private transient static final SimpleDateFormat sdfDetail = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 格式化日期对象为字符串
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return sdfDetail.format(date);
	}
	
	/**
	 * 格式化日期字符串为日期对象
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr){
		try {
			if(dateStr.indexOf(":") != -1){
				return sdfDetail.parse(dateStr);
			}else{
				return sdfSimple.parse(dateStr);
			}
		} catch (ParseException e) {
			Log4jUtil.debug("[DateUtil.parseDate]格式化日期字符串[{}]为日期对象时出现错误：{}", dateStr, ExceptionUtil.getErrMsg(e));
		}
		return null;
	}
}
