package com.douglei.mini.license.client;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自动的授权文件验证器线程
 * @author DougLei
 */
class AutoLicenseValidatorThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(AutoLicenseValidatorThread.class);
	private AutoLicenseValidator validator;
	private long lastValidateTime; // 记录上一次验证的时间
	
	public AutoLicenseValidatorThread(String name, AutoLicenseValidator validator) {
		super(name);
		this.validator = validator;
	}

	@Override
	public void run() {
		long l, sleep;
		while(true) {
			sleep = sleep();
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			l = System.currentTimeMillis() - lastValidateTime - sleep;
			if(l < -10000) {
				logger.info("当前时间[{}], 减去最后一次验证时间[{}]和sleep时间[{}], 结果为: {}", System.currentTimeMillis(), lastValidateTime, sleep, l);
				validator.updateResult(new ValidationResult() {
					@Override
					public String getMessage() {
						return "系统时间错误";
					}
					@Override
					public String getCode_() {
						return "system.time.error";
					}
				});
				break;
			}
			if(validator.autoVerify() != null)
				break;
			
			logger.info("{}", validator);
		}
		logger.info("{}", validator);
	}
	
	// 获取需要sleep的毫秒数, 获得当前日期到零点之间的毫秒数
	private long sleep() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 2);
	    calendar.set(Calendar.MILLISECOND, 0);
		this.lastValidateTime = System.currentTimeMillis();
		return calendar.getTimeInMillis() - lastValidateTime;
	}
}
