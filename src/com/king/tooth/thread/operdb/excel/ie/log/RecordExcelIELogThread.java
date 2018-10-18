package com.king.tooth.thread.operdb.excel.ie.log;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.king.tooth.sys.entity.sys.SysExcelImportExportLog;
import com.king.tooth.thread.operdb.HibernateOperDBThread;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 记录excel导入导出日志的线程
 * @author DougLei
 */
public final class RecordExcelIELogThread extends HibernateOperDBThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "RecordExcelIELog_";
	
	/**
	 * 要保存的excelIELog数据
	 */
	private List<SysExcelImportExportLog> excelIELogs;

	public RecordExcelIELogThread(Session session, List<SysExcelImportExportLog> excelIELogs, String currentAccountId, String currentUserId, String projectId, String customerId) {
		super(session);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.excelIELogs = excelIELogs;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}
	
	protected boolean isGoOn() {
		return true;
	}
	
	protected void doRun() {
		if(excelIELogs != null && excelIELogs.size() > 0){
			Date currentDate = new Date();
			for (SysExcelImportExportLog excelIELog : excelIELogs) {
				ResourceHandlerUtil.setBasicPropVals(excelIELog, currentAccountId, projectId, customerId, currentDate);
				session.save(excelIELog.getEntityName(), excelIELog.toEntityJson());
			}
		}
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("保存excelIELog时出现异常信息：{}", ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
		if(excelIELogs != null && excelIELogs.size() > 0){
			excelIELogs.clear();
		}
	}
}
