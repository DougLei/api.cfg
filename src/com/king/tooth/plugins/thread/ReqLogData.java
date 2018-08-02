package com.king.tooth.plugins.thread;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.entity.sys.SysOperSqlLog;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 请求日志数据
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ReqLogData implements Serializable{
	/**
	 * 处理日志的session对象
	 * <pre>
	 * 	这个和当前线程中的session区分开，主要是因为无论请求是否成功处理，都要保存日志数据
	 * 	如果请求处理失败，事务回滚，也会造成无法插入日志数据，所以这里只能区分
	 * </pre>
	 */
	private Session logSession;
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
	 * 保存日志
	 */
	public void recordLogs(){
		if(reqLog == null){
			throw new NullPointerException("[ReqLogData.recordLogs()]时，reqLog对象为空，请检查系统逻辑");
		}
		List<SysOperSqlLog> operSqlLogs = null;
		try {
			logSession = HibernateUtil.getCurrentThreadLogSession();
			logSession.beginTransaction();
			
			String entityName = reqLog.getEntityName();
			JSONObject reqLogJson = reqLog.toEntityJson();
			ResourceHandlerUtil.initBasicPropValsForSave(entityName, reqLogJson, null);
			logSession.save(entityName, reqLogJson);
			
			operSqlLogs = reqLog.getOperSqlLogs();
			if(operSqlLogs != null && operSqlLogs.size() > 0){
				entityName = operSqlLogs.get(0).getEntityName();
				for (SysOperSqlLog operSqlLog : operSqlLogs) {
					reqLogJson = operSqlLog.toEntityJson();
					ResourceHandlerUtil.initBasicPropValsForSave(entityName, reqLogJson, null);
					logSession.save(entityName, reqLogJson);
				}
			}
			logSession.flush();
			logSession.getTransaction().commit();
		} catch (Exception e) {
			logSession.getTransaction().rollback();
		}finally{
			logSession.close();
			if(operSqlLogs != null){
				operSqlLogs.clear();
			}
		}
	}
}
