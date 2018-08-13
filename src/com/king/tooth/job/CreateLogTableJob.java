package com.king.tooth.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.sys.SysReqLog;

/**
 * 创建log信息表的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CreateLogTableJob implements Job, Serializable{
	
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Date currentDate = new Date();
		String firstDay = SysReqLog.getDay(currentDate);
		if("01".equals(firstDay)){
			SysReqLog.yyyyMM = SysReqLog.getYearMonth(currentDate);
			
			List<ComTabledata> logTables = new ArrayList<ComTabledata>(2);
			logTables.add(BuiltinObjectInstance.sysReqLog.toCreateTable());
			logTables.add(BuiltinObjectInstance.sysOperSqlLog.toCreateTable());
			
			DBTableHandler dbTableHandler = new DBTableHandler(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance);
			dbTableHandler.createTable(logTables, true); // 表信息集合，有可能有关系表
			
			HibernateHbmHandler hbmHandler = new HibernateHbmHandler();
			List<String> hbmContents = new ArrayList<String>(logTables.size());
			
		}
	}
}
