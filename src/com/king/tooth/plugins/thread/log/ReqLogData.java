package com.king.tooth.plugins.thread.log;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
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
		List<SysOperSqlLog> operSqlLogs = reqLog.getOperSqlLogs();
		JSONArray logs = new JSONArray(1 + (operSqlLogs==null?0:operSqlLogs.size()));
		
		// 获得请求日志json对象
		JSONObject reqLogJson = reqLog.toEntityJson();
		reqLogJson.put(ResourceNameConstants.ENTITY_NAME, reqLog.getEntityName());
		ResourceHandlerUtil.initBasicPropValsForSave(reqLog.getEntityName(), reqLogJson, null);
		logs.add(reqLogJson);// 记录请求日志json
		
		// 循环获得每个操作的sql日志json对象
		JSONObject operSqlLogJson;
		if(operSqlLogs != null && operSqlLogs.size() > 0){
			int orderCode = 1;
			for (SysOperSqlLog operSqlLog : operSqlLogs) {
				operSqlLog.setOrderCode(orderCode++);
				operSqlLogJson = operSqlLog.toEntityJson();
				operSqlLogJson.put(ResourceNameConstants.ENTITY_NAME, operSqlLog.getEntityName());
				ResourceHandlerUtil.initBasicPropValsForSave(operSqlLog.getEntityName(), operSqlLogJson, null);
				logs.add(operSqlLogJson);// 记录操作sql日志json
			}
			operSqlLogs.clear();
		}
		
		// 新线程保存日志数据
		new RecordLogThread(HibernateUtil.getCurrentThreadLogSession(), logs).start();
	}
}
