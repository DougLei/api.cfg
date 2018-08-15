package com.king.tooth.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.sys.SysHibernateHbm;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateHbmUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 创建log信息表的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CreateLogTableJob implements Job, Serializable{
	private static final int logTableSize = 2;
	
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Date currentDate = new Date();
		if(DateUtil.isFirstDayOfMonth(currentDate)){
			Log4jUtil.debug("月初，创建新的日志表");
			
			// 准备和数据库连接
			CurrentThreadContext.setDatabaseId(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId());
			
			// 备份之前日志表的年月后缀
			String yyyyMMBak = SysReqLog.yyyyMM;
			// 获取新的日志表的年月后缀
			SysReqLog.yyyyMM = SysReqLog.getYearMonth(currentDate);
			
			// 获取两个日志表对象
			List<ComTabledata> logTables = new ArrayList<ComTabledata>(logTableSize);
			logTables.add(BuiltinObjectInstance.sysReqLog.toCreateTable());
			logTables.add(BuiltinObjectInstance.sysOperSqlLog.toCreateTable());
			
			DBTableHandler dbTableHandler = new DBTableHandler(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance);
			try {
				HibernateUtil.openSessionToCurrentThread();
				HibernateUtil.beginTransaction();
				
				createLogTables(currentDate, logTables, dbTableHandler);
				
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				Log4jUtil.error("系统在自动创建日志信息表时出现异常，请联系系统管理员:{}", ExceptionUtil.getErrMsg("CreateLogTableJob", "execute", e));
				HibernateUtil.rollbackTransaction();
				
				// 恢复之前日志表的年月后缀
				SysReqLog.yyyyMM = yyyyMMBak;
				
				for (ComTabledata logTable : logTables) {
					BuiltinObjectInstance.tableService.cancelBuildModel(dbTableHandler, logTable, false);
				}
			} finally{
				// 关闭连接
				HibernateUtil.closeCurrentThreadSession();
				ResourceHandlerUtil.clearTables(logTables);
			}
		}else{
			Log4jUtil.debug("不是月初，无需创建新的日志表");
		}
	}
	
	/**
	 * 创建日志表，并建模
	 * @param currentDate
	 * @param logTables
	 * @param dbTableHandler 
	 */
	private void createLogTables(Date currentDate, List<ComTabledata> logTables, DBTableHandler dbTableHandler){
		// create日志表
		dbTableHandler.createTable(logTables, true); 
		
		// 获取日志表的hbmContent以及SysHibernateHbm对象
		List<String> hbmContents = new ArrayList<String>(logTableSize);
		SysHibernateHbm hbm;
		int i = 0;
		for (ComTabledata logTable : logTables) {
			hbmContents.add(HibernateHbmUtil.createHbmMappingContent(logTable, false));
			
			// 2、插入hbm
			hbm = new SysHibernateHbm();
			hbm.tableTurnToHbm(logTable);
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.setHbmContent(hbmContents.get(i++));
			HibernateUtil.saveObject(hbm, null);
			
			// 3、插入资源数据
			new SysResourceService().saveSysResource(logTable);
		}
		
		// 4、将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
	}
}
