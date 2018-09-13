package com.king.tooth.sys.entity.sys.reqlog;

import java.io.Serializable;

import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.thread.operdb.log.RecordLogThread;
import com.king.tooth.util.hibernate.HibernateUtil;

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
			throw new NullPointerException("[ReqLogData.recordLogs()]时，reqLog对象为空，请检查系统逻辑");
		}
		// 新线程保存日志数据
		new RecordLogThread(HibernateUtil.openNewSession(),
				reqLog,
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
				CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
				CurrentThreadContext.getProjectId(),
				CurrentThreadContext.getCustomerId()).start();
	}
}
