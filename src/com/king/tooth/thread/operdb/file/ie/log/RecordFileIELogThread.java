package com.king.tooth.thread.operdb.file.ie.log;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.king.tooth.sys.entity.sys.SysFileImportExportLog;
import com.king.tooth.thread.operdb.HibernateOperDBThread;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 记录文件导入导出日志的线程
 * @author DougLei
 */
public final class RecordFileIELogThread extends HibernateOperDBThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "RecordFileIELog_";
	
	/**
	 * 要保存的fileIELog数据
	 */
	private List<SysFileImportExportLog> fileIELogs;

	public RecordFileIELogThread(Session session, List<SysFileImportExportLog> fileIELogs, String currentAccountId, String currentUserId, String projectId, String customerId) {
		super(session);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.fileIELogs = fileIELogs;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}
	
	protected boolean isGoOn() {
		return true;
	}
	
	protected void doRun() {
		if(fileIELogs != null && fileIELogs.size() > 0){
			Date currentDate = new Date();
			for (SysFileImportExportLog fileIELog : fileIELogs) {
				ResourceHandlerUtil.setBasicPropVals(fileIELog, currentAccountId, projectId, customerId, currentDate);
				session.save(fileIELog.getEntityName(), fileIELog.toEntityJson());
			}
		}
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("保存fileIELog时出现异常信息：{}", ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
		if(fileIELogs != null && fileIELogs.size() > 0){
			fileIELogs.clear();
		}
	}
}
