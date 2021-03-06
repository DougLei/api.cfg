package com.api.constants;

import com.api.cache.SysContext;

/**
 * 登录功能的一些常量
 * @author DougLei
 */
public class LoginConstants {
	
	/**
	 * 登录超时时限
	 * @see api.platform.basic.properties
	 */
	public static final int loginTimeoutDatelimit = Integer.valueOf(SysContext.getSystemConfig("login.timeout.datelimit")) * 60 * 1000;
	
	/**
	 * 连续尝试登录的最大次数
	 * @see api.platform.basic.properties
	 */
	public static final int tryLoginTimes = Integer.valueOf(SysContext.getSystemConfig("try.login.times"));
	
	/**
	 * 连续尝试登录失败后间隔的时间
	 * @see api.platform.basic.properties
	 */
	public static final int overLoginfailTimesDateDuration = Integer.valueOf(SysContext.getSystemConfig("over.loginfail.times.date.duration")) * 60 * 1000;
}
