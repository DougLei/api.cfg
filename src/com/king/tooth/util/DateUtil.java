package com.king.tooth.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

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
	 * 流水号日期格式化对象实例
	 */
	private static final SimpleDateFormat serialNumberDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
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
	private transient static final SimpleDateFormat daySdf = new SimpleDateFormat("dd");
	/**
	 * 当前日期是否是当月第一天
	 * @param currentDate
	 * @return
	 */
	public static boolean isFirstDayOfMonth(Date currentDate) {
		if("01".equals(daySdf.format(currentDate))){
			return true;
		}
		return false;
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
	 * 值是否是时间格式
	 * @param value
	 * @return
	 */
	public static boolean valueIsDateFormat(Object value) {
		if(value != null){
			String tmpVal = value.toString();
			if(dateFormat.matcher(tmpVal).matches()){
				return true;
			}
			if(timeZoneFormat.matcher(tmpVal).matches()){
				return true;
			}
		}
		return false;
	}
	/** 判断日期格式的正则表达式 */
	private static final Pattern dateFormat = Pattern.compile("[0-9]{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1])( ([0-9]|0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[0-5][0-9]):(0[0-9]|[0-5][0-9]))?");
	private static final Pattern timeZoneFormat = Pattern.compile("[0-9]{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1])T([0-9]|0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[0-5][0-9]):(0[0-9]|[0-5][0-9]).[0-9][0-9][0-9]( )?Z");
	
	// -----------------------------------------------------------------
	/**
	 * 获取流水号日期字符串
	 * @return
	 */
	public static String getSerialNumberDateStr(){
		return serialNumberDateFormat.format(new Date());
	}
}
