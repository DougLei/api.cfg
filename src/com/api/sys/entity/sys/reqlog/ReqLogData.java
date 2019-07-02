package com.api.sys.entity.sys.reqlog;

import java.io.Serializable;

import com.api.sys.entity.sys.SysReqLog;
import com.api.thread.current.CurrentThreadContext;
import com.api.thread.operdb.log.RecordLogThread;
import com.api.thread.pool.ThreadPool;
import com.api.util.Log4jUtil;
import com.api.util.hibernate.HibernateUtil;

/**
 * 请求日志数据
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ReqLogData implements Serializable{
	/**
	 * 请求日志对象
	 */
	private SysReqLog reqLog;
	
	public SysReqLog getReqLog() {
		return reqLog;
	}
	public void setReqLog(SysReqLog reqLog) {
		this.reqLog = reqLog;
	}

	/**
	 * 记录日志
	 * <p>及保存日志</p>
	 */
	public void recordLogs(){
		if(reqLog == null){
			Log4jUtil.error("[ReqLogData.recordLogs()]时，reqLog对象为空，请检查系统逻辑");
			return;
		}
//		if(CurrentThreadContext.getCurrentAccountOnlineStatus() == null){
//			Log4jUtil.error("[ReqLogData.recordLogs()]时，CurrentThreadContext.getCurrentAccountOnlineStatus()对象为空，请检查系统逻辑");
//			return;
//		}
		// 新线程保存日志数据
		ThreadPool.execute(new RecordLogThread(HibernateUtil.openNewSession(),
				reqLog,
				CurrentThreadContext.getCurrentAccountOnlineStatus() == null ? "未登录":CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
				CurrentThreadContext.getCurrentAccountOnlineStatus() == null ? "未登录":CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
				CurrentThreadContext.getProjectId(),
				CurrentThreadContext.getCustomerId()));
	}
}
