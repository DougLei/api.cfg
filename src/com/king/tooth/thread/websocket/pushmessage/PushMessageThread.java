package com.king.tooth.thread.websocket.pushmessage;

import java.util.List;

import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 消息推送的线程
 * @author DougLei
 */
public class PushMessageThread extends Thread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "PushMessage_";
	
	/**
	 * 要推送的信息集合
	 */
	private List<PushMessage> pushMessages;
	
	public PushMessageThread(List<PushMessage> pushMessages) {
		this.pushMessages = pushMessages;
	}

	public void start() {
		setName(threadNamePrefix + ResourceHandlerUtil.getIdentity());
		
		System.out.println(getName());
		
		SysPushMessageInfo basicPushMsgInfo = new SysPushMessageInfo(
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
				CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId());
		
		if(pushMessages.size() == 1){
			BuiltinObjectInstance.sysPushMessageInfoService.pushMessage(basicPushMsgInfo, pushMessages.get(0));
		}else{
			for (PushMessage pushMessage : pushMessages) {
				BuiltinObjectInstance.sysPushMessageInfoService.pushMessage(basicPushMsgInfo, pushMessage);
			}
		}
		pushMessages.clear();
	}
}
