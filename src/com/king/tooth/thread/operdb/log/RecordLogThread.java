package com.king.tooth.thread.operdb.log;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.king.tooth.sys.entity.sys.SysOperSqlLog;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.thread.operdb.HibernateOperDBThread;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 记录日志的线程
 * @author DougLei
 */
public final class RecordLogThread extends HibernateOperDBThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "RecordLog_";
	
	/**
	 * 要保存的logs数据
	 */
	private SysReqLog reqLog;
	/**
	 * 操作的sql日志集合
	 */
	private List<SysOperSqlLog> operSqlLogs;

	public RecordLogThread(Session session, SysReqLog reqLog, String currentAccountId, String currentUserId, String projectId, String customerId) {
		super(session);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.reqLog = reqLog;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}
	
	protected boolean isGoOn() {
		return true;
	}
	
	protected void doRun() {
		Date currentDate = new Date();
		reqLog.setRespDate(currentDate);
		
		ResourceHandlerUtil.setBasicPropVals(reqLog, currentAccountId, projectId, customerId, currentDate);
		session.save(reqLog.getEntityName(), reqLog.toEntityJson());
		
		operSqlLogs = reqLog.getOperSqlLogs();
		if(operSqlLogs != null && operSqlLogs.size() > 0){
			int orderCode = 1;
			for (SysOperSqlLog operSqlLog : operSqlLogs) {
				operSqlLog.setOrderCode(orderCode++);
				ResourceHandlerUtil.setBasicPropVals(operSqlLog, currentAccountId, projectId, customerId, currentDate);
				session.save(operSqlLog.getEntityName(), operSqlLog.toEntityJson());
			}
		}
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("保存log时出现异常信息：{}", ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
		if(operSqlLogs != null && operSqlLogs.size() > 0){
			operSqlLogs.clear();
		}
	}
}
