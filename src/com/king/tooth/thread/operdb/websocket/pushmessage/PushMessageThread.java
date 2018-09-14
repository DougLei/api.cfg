package com.king.tooth.thread.operdb.websocket.pushmessage;

import java.util.List;

import org.hibernate.Session;

import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.thread.operdb.HibernateOperDBThread;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.websocket.pushmessage.PushMessageUtil;

/**
 * 消息推送的线程
 * @author DougLei
 */
public final class PushMessageThread extends HibernateOperDBThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "PushMessage_";
	
	/**
	 * 要推送的信息集合
	 */
	private List<PushMessage> pushMessages;
	
	public PushMessageThread(Session session, List<PushMessage> pushMessages, String currentAccountId, String currentUserId, String projectId, String customerId) {
		super(session);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.pushMessages = pushMessages;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}

	protected void doRun() throws Exception {
		SysPushMessageInfo basicPushMsgInfo = new SysPushMessageInfo(currentAccountId, currentUserId, projectId, customerId);
		for (PushMessage pushMessage : pushMessages) {
			pushMessage(session, basicPushMsgInfo, pushMessage);
		}
		pushMessages.clear();
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("消息推送处理时出现异常信息：{}", ExceptionUtil.getErrMsg("PushMessageThread", "run", e));
	}

	protected void doFinally() {
	}
	
	/**
	 * 消息推送
	 * @param session
	 * @param basicPushMsgInfo 
	 * @param pushMessage
	 * @throws CloneNotSupportedException 
	 */
	private void pushMessage(Session session, SysPushMessageInfo basicPushMsgInfo, PushMessage pushMessage) throws CloneNotSupportedException {
		basicPushMsgInfo.setMsgType(pushMessage.getPushMessageType());
		basicPushMsgInfo.setSourceMsg(pushMessage.getMessage());
		
		SysPushMessageInfo pushMsgInfo;
		String[] toUserIdArray;
		int msgBatchOrderCode = 1;
		int msgOrderCode = 1;
		
		while(pushMessage.hasMoreToUserId()){
			toUserIdArray = pushMessage.getActualToUserIdArr();
			if(toUserIdArray != null && toUserIdArray.length > 0){
				for (String toUserId : toUserIdArray) {
					pushMsgInfo = (SysPushMessageInfo) basicPushMsgInfo.clone();
					pushMsgInfo.setId(ResourceHandlerUtil.getIdentity());
					pushMsgInfo.setReceiveUserId(toUserId);
					pushMsgInfo.setMsgBatchOrderCode(msgBatchOrderCode);
					pushMsgInfo.setMsgOrderCode(msgOrderCode++);
					pushMsgInfo.analyzeActualSendMessage();
					pushMsgInfo.recordPushResultCode(PushMessageUtil.pushMessage(pushMsgInfo.getTargetMsg(), pushMsgInfo.getReceiveUserId()));// 推送消息，并记录推送结果
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
