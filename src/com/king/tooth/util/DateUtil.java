package com.king.tooth.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	 * 带时区的日期格式化类
	 * <p>UTC通用标准时，以Z来标识</p>
	 */
	private transient static final SimpleDateFormat sdfTimeZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
	
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
			if(dateStr.endsWith("Z")){
				return sdfTimeZone.parse(dateStr.replace("Z", " UTC"));
			}else if(dateStr.indexOf(":") != -1){
				return sdfDetail.parse(dateStr);
			}else{
				return sdfSimple.parse(dateStr);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("[DateUtil.parseDate]格式化日期字符串["+dateStr+"]为日期对象时出现错误："+ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * <p>没有时分秒</p>
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date parseSqlDate(String dateStr){
		return new java.sql.Date(parseDate(dateStr).getTime());
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * <p>没有时分秒</p>
	 * @param dateStr
	 * @return
	 */
	public static java.sql.Date parseSqlDate(Date date){
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * <p>有时分秒</p>
	 * @param dateStr
	 * @return
	 */
	public static Timestamp parseTimestamp(String dateStr){
		return new Timestamp(parseDate(dateStr).getTime());
	}
	
	/**
	 * 格式化日期字符串为sql类型的日期对象
	 * <p>有时分秒</p>
	 * @param dateStr
	 * @return
	 */
	public static Timestamp parseTimestamp(Date date){
		return new Timestamp(date.getTime());
	}
	
	// -----------------------------------------------------------------
	/**
	 * 当前日期是否是当月第一天
	 * @param currentDate
	 * @return
	 */
	public static boolean isFirstDayOfMonth(Date currentDate) {
		if("1".equals(ddSdf.format(currentDate))){
			return true;
		}
		return false;
	}
	/**	dd */
	private static final SimpleDateFormat ddSdf = new SimpleDateFormat("d");
	
	
	/**
	 * 获取当前日期是当月的第几周
	 * <p>周日历</p>
	 * @param date
	 * @return
	 */
	public static int weekCanlendarOfMonth(Date date){
		return weekCanlendar(date, Calendar.WEEK_OF_MONTH);
	}
	
	/**
	 * 获取当前日期是今年的第几周
	 * <p>周日历</p>
	 * @param date
	 * @return
	 */
	public static int weekCanlendarOfYear(Date date){
		return weekCanlendar(date, Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 获取周日历
	 * <p>第几周</p>
	 * @param date
	 * @param calendarWeekType
	 * @return
	 */
	private static int weekCanlendar(Date date, int calendarWeekType){
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.get(calendarWeekType);
	}
	
	// -----------------------------------------------------------------
	/**
	 * 自定义格式化日期对象为字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, SimpleDateFormat sdf){
		if(date == null){
			return null;
		}
		return sdf.format(date);
	}
	
	/**
	 * 自定义格式化日期对象为字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern){
		if(date == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	// -----------------------------------------------------------------
	/**
	 * 获取当前是第几季度
	 * @param date
	 * @param isNumber 是否是数字，不是数字就是汉字
	 * 				   1.2.3.4         一.二.三.四
	 * @return
	 */
	public static String getSeason(Date date, int isNumber) {
		int month = Integer.valueOf(mmSdf.format(date));
		if(isNumber == 1){
			if(month >0 && month <4){
				return "1";
			}else if(month >3 && month <7){
				return "2";
			}else if(month >6 && month <10){
				return "3";
			}else{
				return "4";
			}
		}else{
			if(month >0 && month <4){
				return "一";
			}else if(month >3 && month <7){
				return "二";
			}else if(month >6 && month <10){
				return "三";
			}else{
				return "四";
			}
		}
	}
	/**	MM */
	private static final SimpleDateFormat mmSdf = new SimpleDateFormat("MM");
}
