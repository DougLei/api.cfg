package com.king.tooth.constants;

import com.king.tooth.cache.SysConfig;

/**
 * 登录功能的一些常量
 * @author DougLei
 */
public class LoginConstants {
	
	/**
	 * 登录超时时限
	 * @see api.platform.basic.properties
	 */
	public static final int loginTimeoutDatelimit = Integer.valueOf(SysConfig.getSystemConfig("login.timeout.datelimit")) * 60 * 1000;
	
	/**
	 * 连续尝试登录的最大次数
	 * @see api.platform.basic.properties
	 */
	public static final int tryLoginTimes = Integer.valueOf(SysConfig.getSystemConfig("try.login.times"));
	
	/**
	 * 连续尝试登录失败后间隔的时间
	 * @see api.platform.basic.properties
	 */
	public static final int overLoginfailTimesDateDuration = Integer.valueOf(SysConfig.getSystemConfig("over.loginfail.times.date.duration")) * 60 * 1000;
}
