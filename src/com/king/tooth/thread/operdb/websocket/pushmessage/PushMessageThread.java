package com.king.tooth.thread.operdb.websocket.pushmessage;

import java.util.List;

import org.hibernate.Session;

import com.king.tooth.enums.websocket.pushmessage.ReturnCodeEnum;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.thread.operdb.HibernateOperDBThread;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.websocket.PushMessageUtil;

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
	/**
	 * 记录消息推送失败的次数
	 */
	private int pushFailCount = 0;
	/**
	 * 记录推送结果的对象
	 */
	private StringBuilder resultMessageBuilder = new StringBuilder();
	
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
			pushMessage(session, basicPushMsgInfo, pushMessage, resultMessageBuilder);
		}
		pushMessages.clear();
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("消息推送处理时出现异常信息：{}", ExceptionUtil.getErrMsg("PushMessageThread", "run", e));
		resultMessageBuilder.append("消息推送时系统出现异常，请联系后台系统开发人员:").append(ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
		if(pushFailCount > 0){
			resultMessageBuilder.append("因").append(pushFailCount).append("个用户不在线，给其推送消息失败").append(";");
		}
		if(resultMessageBuilder.length() > 0){
			PushMessageUtil.pushMessage(currentUserId, resultMessageBuilder.toString());
			resultMessageBuilder.setLength(0);
		}
	}
	
	/**
	 * 消息推送
	 * @param session
	 * @param basicPushMsgInfo 
	 * @param pushMessage
	 * @param resultMessageBuilder 
	 * 
	 * @throws CloneNotSupportedException 
	 */
	private void pushMessage(Session session, SysPushMessageInfo basicPushMsgInfo, PushMessage pushMessage, StringBuilder resultMessageBuilder) throws CloneNotSupportedException {
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
					pushMsgInfo.recordPushResultCode(PushMessageUtil.pushMessage(pushMsgInfo));// 推送消息，并记录推送结果
					session.save(SysPushMessageInfoEntityName, pushMsgInfo.toEntityJson());// 保存推送的消息
					
					if(pushMsgInfo.getPushResultCode() != ReturnCodeEnum.SUCCESS.getCode()){
						pushFailCount++;
					}
				}
				msgBatchOrderCode++;
			}
		}
		pushMessage.clear();
	}
	/** 消息推送的实体名 */
	private static final String SysPushMessageInfoEntityName = "SysPushMessageInfo";
}
