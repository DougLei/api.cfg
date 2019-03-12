package com.api.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.api.constants.ResourceInfoConstants;
import com.api.plugins.jdbc.table.DBTableHandler;
import com.api.sys.builtin.data.BuiltinObjectInstance;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.entity.cfg.CfgHibernateHbm;
import com.api.sys.entity.cfg.CfgResource;
import com.api.sys.entity.cfg.CfgTable;
import com.api.sys.entity.sys.SysOperSqlLog;
import com.api.sys.entity.sys.SysOperationLog;
import com.api.sys.entity.sys.SysReqLog;
import com.api.sys.service.cfg.CfgTableService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.DateUtil;
import com.api.util.ExceptionUtil;
import com.api.util.Log4jUtil;
import com.api.util.hibernate.HibernateHbmUtil;
import com.api.util.hibernate.HibernateUtil;

/**
 * 创建log信息表的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CreateLogTableJob implements Job, Serializable{
	private static final int logTableSize = 3;
	
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Log4jUtil.info(CreateLogTableJob.class, "execute", "执行创建log信息表的任务，任务的cron表达式为：0 0 0 * * ?");
		Date currentDate = new Date();
		if(DateUtil.isFirstDayOfMonth(currentDate)){
			Log4jUtil.info("月初，创建新的日志表");
			
			// 准备和数据库连接
			CurrentThreadContext.setDatabaseId(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId());
			
			// 备份之前日志表的年月后缀
			String yyyyMMBak = SysReqLog.yyyyMM;
			// 获取新的日志表的年月后缀
			SysReqLog.yyyyMM = SysReqLog.getYearMonth(currentDate);
			
			// 获取两个日志表对象
			List<CfgTable> logTables = new ArrayList<CfgTable>(logTableSize);
			logTables.add(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toCreateTable());
			logTables.add(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toCreateTable());
			logTables.add(BuiltinResourceInstance.getInstance("SysOperationLog", SysOperationLog.class).toCreateTable());// 前端提出的操作日志表
			
			DBTableHandler dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
			try {
				HibernateUtil.openSessionToCurrentThread();
				HibernateUtil.beginTransaction();
				
				createLogTables(currentDate, logTables, dbTableHandler);
				
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				Log4jUtil.error("系统在自动创建日志信息表时出现异常，请联系系统管理员:{}", ExceptionUtil.getErrMsg(e));
				HibernateUtil.rollbackTransaction();
				
				// 恢复之前日志表的年月后缀
				SysReqLog.yyyyMM = yyyyMMBak;
				
				for (CfgTable logTable : logTables) {
					BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).cancelBuildModel(dbTableHandler, logTable, null, false);
				}
			} finally{
				// 关闭连接
				HibernateUtil.closeCurrentThreadSession();
				clearTables(logTables);
			}
		}else{
			Log4jUtil.info("不是月初，无需创建新的日志表");
		}
	}
	
	/**
	 * 创建日志表，并建模
	 * @param currentDate
	 * @param logTables
	 * @param dbTableHandler 
	 */
	private void createLogTables(Date currentDate, List<CfgTable> logTables, DBTableHandler dbTableHandler){
		// create日志表
		dbTableHandler.batchCreateTable(logTables, true); 
		
		// 获取日志表的hbmContent以及CfgHibernateHbm对象
		List<String> hbmContents = new ArrayList<String>(logTableSize);
		CfgHibernateHbm hbm;
		CfgResource resource;
		int i = 0;
		for (CfgTable logTable : logTables) {
			hbmContents.add(HibernateHbmUtil.createHbmMappingContent(logTable, false));
			
			// 2、插入hbm
			hbm = new CfgHibernateHbm(logTable);
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.setRefTableId(ResourceInfoConstants.BUILTIN_RESOURCE);
			hbm.setContent(hbmContents.get(i++));
			HibernateUtil.saveObject(hbm, null);
			
			// 3、插入资源数据
			resource = logTable.turnToResource();
			resource.setRefResourceId(ResourceInfoConstants.BUILTIN_RESOURCE);
			HibernateUtil.saveObject(resource, null);
		}
		
		// 4、将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
	}
	
	/**
	 * 清除表信息
	 * @param tables
	 */
	private void clearTables(List<CfgTable> tables){
		if(tables != null && tables.size() > 0){
			for (CfgTable table : tables) {
				table.clear();
			}
			tables.clear();
		}
	}
}
