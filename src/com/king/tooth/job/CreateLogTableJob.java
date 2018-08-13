package com.king.tooth.job;

import java.io.Serializable;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.sys.SysReqLog;

/**
 * 创建log信息表的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CreateLogTableJob implements Job, Serializable{
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Date currentDate = new Date();
		String firstDay = SysReqLog.getDay(currentDate);
		if("01".equals(firstDay)){
			SysReqLog.yyyyMM = SysReqLog.getYearMonth(currentDate);
			
			ComTabledata sysReqLog = BuiltinObjectInstance.sysReqLog.toCreateTable();
			ComTabledata sysOperSqlLog = BuiltinObjectInstance.sysOperSqlLog.toCreateTable();
			
			
			
			
			
		}
	}
}
