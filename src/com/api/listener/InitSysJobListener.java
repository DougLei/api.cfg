package com.api.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.api.cache.SysContext;
import com.api.job.BackupUploadFileJob;
import com.api.util.QuartzUtil;
import com.douglei.mini.license.client.AutoLicenseValidator;

/**
 * 初始化系统数据Listener
 * @author DougLei
 */
public class InitSysJobListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sc) {
		SysContext.licenseValidator = new AutoLicenseValidator("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZISPugceSxGnW7qZ65xQndQJKh6pE41Oldhwd7kBf3D965efhjDLi3teeK20EWJpfdnX8oPn+rF/3WoP0tMXRyB2sQxLqy4YScx7zJ6DVAgyd5+toaMK6UxwOrDX9VrYU8QYVWDA3GR4bYak0tPA1KMHKil3rrRUJkhSb6w6jmQIDAQAB");
		SysContext.licenseValidator.start();
		
		// 添加【创建log信息表的任务】
//		QuartzUtil.addJob("创建log信息表的任务", "system", CreateLogTableJob.class, "0 0 0 * * ?", false);
		// 添加【备份用户在系统中上传的所有文件的任务】
		QuartzUtil.addJob("备份用户在系统中上传的所有文件的任务", "system", BackupUploadFileJob.class, "0 0 0 * * ?", false);
	}
	
	public void contextDestroyed(ServletContextEvent sc) {
	}
	
	/*
	 *	【0 0 0 1 * ?】	每月的1号，0点0分0秒执行
	 *	【0 0 0 * * ?】	每月的每天，0点0分0秒执行
	 */
}
