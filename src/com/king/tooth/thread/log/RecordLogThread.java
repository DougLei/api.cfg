package com.king.tooth.thread.log;

import org.hibernate.Session;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;

/**
 * 记录日志的线程
 * @author DougLei
 */
public class RecordLogThread extends Thread{
	/**
	 * 处理log的session对象
	 */
	private Session logSession;
	/**
	 * 要保存的logs数据
	 */
	private JSONArray logs;

	public RecordLogThread() {
	}
	public RecordLogThread(Session logSession, JSONArray logs) {
		this.logSession = logSession;
		this.logs = logs;
	}
	
	public void run() {
		Log4jUtil.debug("线程{}启动，保存日志数据", getName());
		
		if(logSession == null){
			Log4jUtil.warn("保存log的session对象为空，请检查程序逻辑");
			return;
		}
		
		try {
			logSession.beginTransaction();
			
			int size = logs.size();
			JSONObject log;
			for(int i=0; i<size; i++){
				log = logs.getJSONObject(i);
				logSession.save(log.remove(BuiltinParameterKeys.ENTITY_NAME).toString(), log);
			}
			
			logSession.flush();
			logSession.getTransaction().commit();
		} catch (Exception e) {
			logSession.getTransaction().rollback();
			Log4jUtil.warn("保存log时出现异常信息：{}", ExceptionUtil.getErrMsg("RecordLogThread", "run", e));
		} finally{
			logSession.close();
			JsonUtil.clearJsonArray(logs);
		}
	}
}
