package com.king.tooth.thread.operdb.websocket.pushmessage;

import java.util.List;

import org.hibernate.Session;

import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.websocket.pushmessage.PushMessageUtil;

/**
 * 批量消息推送的线程
 * @author DougLei
 */
public final class BatchPushMessageThread extends PMThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "BatchPushMessage_";
	
	/**
	 * 要推送的信息集合
	 */
	private List<PushMessage> pushMessages;
	
	public BatchPushMessageThread(Session session, List<PushMessage> pushMessages, String currentAccountId, String currentUserId, String projectId, String customerId, String batchNum) {
		super(session, batchNum);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.pushMessages = pushMessages;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}

	protected boolean isGoOn() {
		return true;
	}
	
	protected void doRun() throws Exception {
		SysPushMessageInfo basicPushMsgInfo = new SysPushMessageInfo(currentAccountId, currentUserId, projectId, customerId, batchNum);
		for (PushMessage pushMessage : pushMessages) {
			batchPushMessage(basicPushMsgInfo, pushMessage);
		}
		pushMessages.clear();
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("批量消息推送处理时出现异常信息：{}", ExceptionUtil.getErrMsg("BatchPushMessageThread", "run", e));
	}

	protected void doFinally() {
	}
	
	/**
	 * 批量消息推送
	 * @param basicPushMsgInfo 
	 * @param pushMessage
	 * @throws CloneNotSupportedException 
	 */
	private void batchPushMessage(SysPushMessageInfo basicPushMsgInfo, PushMessage pushMessage) throws CloneNotSupportedException {
		basicPushMsgInfo.setMsgType(pushMessage.getMsgType());
		basicPushMsgInfo.setSendType(pushMessage.getSendType());
		basicPushMsgInfo.setSourceMsg(pushMessage.getMessage());
		basicPushMsgInfo.analyzeActualSendMessage();
		String targetMessage = basicPushMsgInfo.getTargetMsg();
		
		SysPushMessageInfo pushMsgInfo = null;
		String[] toUserIdArray = null;
		Integer[] resultCodeArr = null;
		int msgBatchOrderCode = 1;
		int msgOrderCode = 1;
		int length;
		
		while(pushMessage.hasMoreToUserId(session, customerId)){
			toUserIdArray = pushMessage.getActualToUserIdArr();
			if(toUserIdArray != null && toUserIdArray.length > 0){
				resultCodeArr = PushMessageUtil.batchPushMessage(toUserIdArray, targetMessage);
				
				length = toUserIdArray.length;
				for (int i=0;i<length;i++) {
					pushMsgInfo = (SysPushMessageInfo) basicPushMsgInfo.clone();
					pushMsgInfo.setId(ResourceHandlerUtil.getIdentity());
					pushMsgInfo.setReceiveUserId(toUserIdArray[i]);
					pushMsgInfo.setMsgBatchOrderCode(msgBatchOrderCode);
					pushMsgInfo.setMsgOrderCode(msgOrderCode++);
					pushMsgInfo.recordPushResultCode(resultCodeArr[i]);// 记录推送结果
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
