package com.king.tooth.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.king.tooth.job.BackupUploadFileJob;
import com.king.tooth.job.CreateLogTableJob;
import com.king.tooth.util.QuartzUtil;

/**
 * 初始化系统数据Listener
 * @author DougLei
 */
public class InitSysJobListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sc) {
		// 添加【创建log信息表的任务】
		QuartzUtil.addJob("创建log信息表的任务", "system", CreateLogTableJob.class, "0 0 0 * * ?", false);
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
