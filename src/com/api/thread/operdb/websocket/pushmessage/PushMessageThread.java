package com.api.thread.operdb.websocket.pushmessage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.api.sys.entity.sys.SysPushMessageInfo;
import com.api.sys.entity.sys.pushmessage.PushMessage;
import com.api.thread.pool.ThreadPool;
import com.api.util.ExceptionUtil;
import com.api.util.Log4jUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.websocket.pushmessage.PushMessageUtil;

/**
 * 消息推送的线程
 * @author DougLei
 */
public final class PushMessageThread extends PMThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "PushMessage_";
	
	/**
	 * 批量推送消息时的session
	 */
	private Session batchSession;
	
	/**
	 * 要推送的信息集合
	 */
	private List<PushMessage> pushMessages;
	
	public PushMessageThread(Session session, Session batchSession, List<PushMessage> pushMessages, String currentAccountId, String currentUserId, String projectId, String customerId, String batchNum) {
		super(session, batchNum);
		this.batchSession = batchSession;
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.pushMessages = pushMessages;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}

	protected boolean isGoOn() {
		List<PushMessage> batchPushMessages = new ArrayList<PushMessage>(pushMessages.size());
		PushMessage pushMessage;
		for(int i=0;i<pushMessages.size();i++){
			pushMessage = pushMessages.get(i);
			if(pushMessage.getSendType() == SysPushMessageInfo.DIRECT_SEND){
				batchPushMessages.add(pushMessage);
				pushMessages.remove(i);
				i--;
			}
		}
		
		if(batchPushMessages.size() > 0){
			ThreadPool.execute(new BatchPushMessageThread(batchSession, pushMessages, currentAccountId, currentUserId, projectId, customerId, batchNum));// 启动批量推送消息的线程
		}else{
			batchSession.close();
		}
		
		if(pushMessages.size() > 0){
			return true;
		}
		return false;
	}
	
	protected void doRun() throws Exception {
		SysPushMessageInfo basicPushMsgInfo = new SysPushMessageInfo(currentAccountId, currentUserId, projectId, customerId, batchNum);
		for (PushMessage pushMessage : pushMessages) {
			pushMessage(basicPushMsgInfo, pushMessage);
		}
		pushMessages.clear();
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("消息推送处理时出现异常信息：{}", ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
	}
	
	/**
	 * 消息推送
	 * @param basicPushMsgInfo 
	 * @param pushMessage
	 * @throws CloneNotSupportedException 
	 */
	private void pushMessage(SysPushMessageInfo basicPushMsgInfo, PushMessage pushMessage) throws CloneNotSupportedException {
		basicPushMsgInfo.setMsgType(pushMessage.getMsgType());
		basicPushMsgInfo.setSendType(pushMessage.getSendType());
		basicPushMsgInfo.setSourceMsg(pushMessage.getMessage());
		
		SysPushMessageInfo pushMsgInfo;
		String[] toUserIdArray;
		int msgBatchOrderCode = 1;
		int msgOrderCode = 1;
		
		while(pushMessage.hasMoreToUserId(session, customerId)){
			toUserIdArray = pushMessage.getActualToUserIdArr();
			if(toUserIdArray != null && toUserIdArray.length > 0){
				for (String toUserId : toUserIdArray) {
					pushMsgInfo = (SysPushMessageInfo) basicPushMsgInfo.clone();
					pushMsgInfo.setId(ResourceHandlerUtil.getIdentity());
					pushMsgInfo.setReceiveUserId(toUserId);
					pushMsgInfo.setMsgBatchOrderCode(msgBatchOrderCode);
					pushMsgInfo.setMsgOrderCode(msgOrderCode++);
					pushMsgInfo.analyzeActualSendMessage();
					pushMsgInfo.recordPushResultCode(PushMessageUtil.pushMessage(pushMsgInfo.getReceiveUserId(), pushMsgInfo.getTargetMsg()));// 推送消息，并记录推送结果
					session.save(SysPushMessageInfoEntityName, pushMsgInfo.toEntityJson());// 保存推送的消息
				}
				msgBatchOrderCode++;
			}
		}
		pushMessage.clear();
	}
	/** 消息推送的实体名 */
	private static final String SysPushMessageInfoEntityName = "SysPushMessageInfo";
}
