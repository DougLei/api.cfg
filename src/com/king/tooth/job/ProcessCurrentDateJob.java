package com.king.tooth.job;

import java.io.Serializable;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 处理当前日期的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProcessCurrentDateJob implements Job, Serializable{

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Date currentDate = new Date();
		
		
	}
}
