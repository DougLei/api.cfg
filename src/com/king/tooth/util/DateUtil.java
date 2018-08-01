package com.king.tooth.util;

import java.sql.Timestamp;
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
	 * <p>只包括年月日</p>
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		if(date == null){
			return null;
		}
		return sdfSimple.format(date);
	}
	
	/**
	 * 格式化日期对象为字符串
	 * <p>包括时分秒</p>
	 * @param date
	 * @return
	 */
	public static String formatDatetime(Date date){
		if(date == null){
			return null;
		}
		return sdfDetail.format(date);
	}
	
	/**
	 * 格式化日期字符串为日期对象
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr){
		if(StrUtils.isEmpty(dateStr)){
			throw new NullPointerException("[DateUtil.parseDate]格式化日期字符串["+dateStr+"]不能为空！");
		}
		try {
			if(dateStr.indexOf(":") != -1){
				return sdfDetail.parse(dateStr);
			}else{
				return sdfSimple.parse(dateStr);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("[DateUtil.parseDate]格式化日期字符串["+dateStr+"]为日期对象时出现错误："+ExceptionUtil.getErrMsg("DateUtil", "parseDate", e));
		}
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date parseSqlDate(String dateStr){
		return new java.sql.Date(parseDate(dateStr).getTime());
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date parseSqlDate(Date date){
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * @param dateStr
	 * @return
	 */
	public static Timestamp parseTimestamp(String dateStr){
		return new Timestamp(parseDate(dateStr).getTime());
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * @param dateStr
	 * @return
	 */
	public static Timestamp parseTimestamp(Date date){
		return new Timestamp(date.getTime());
	}
}
