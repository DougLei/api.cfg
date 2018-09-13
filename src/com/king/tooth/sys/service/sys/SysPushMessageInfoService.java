package com.king.tooth.sys.service.sys;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.websocket.PushMessageUtil;

/**
 * 推送消息信息表Service
 * @author DougLei
 */
@Service
public class SysPushMessageInfoService extends AbstractService{

	/**
	 * 消息推送
	 * @param basicPushMsgInfo 
	 * @param pushMessage
	 * @return
	 */
	public Object pushMessage(SysPushMessageInfo basicPushMsgInfo, PushMessage pushMessage) {
		basicPushMsgInfo.setMsgType(pushMessage.getPushMessageType());
		basicPushMsgInfo.setSourceMsg(pushMessage.getMessage());
		
		SysPushMessageInfo pushMsgInfo;
		String[] toUserIdArray;
		int msgBatchOrderCode = 1;
		int msgOrderCode = 1;
		try {
			while(pushMessage.hasMoreToUserId()){
				toUserIdArray = pushMessage.getActualToUserIdArr();
				if(toUserIdArray != null && toUserIdArray.length > 0){
					for (String toUserId : toUserIdArray) {
						pushMsgInfo = (SysPushMessageInfo) basicPushMsgInfo.clone();
						pushMsgInfo.setReceiveUserId(toUserId);
						pushMsgInfo.setMsgBatchOrderCode(msgBatchOrderCode);
						pushMsgInfo.setMsgOrderCode(msgOrderCode++);
						pushMsgInfo.analyzeActualSendMessage();
						pushMsgInfo.recordPushResultCode(PushMessageUtil.pushMessage(pushMsgInfo));// 推送消息，并记录推送结果
						HibernateUtil.saveObject(pushMsgInfo, null);// 保存推送的消息
					}
					msgBatchOrderCode++;
				}
			}
			pushMessage.clear();
			return pushMessage;
		} catch (CloneNotSupportedException e) {
			return ExceptionUtil.getErrMsg("SysPushMessageInfoService", "pushMessage", e);
		}
	}
}
