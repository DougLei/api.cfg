package com.api.thread.operdb.websocket.pushmessage;

import org.hibernate.Session;

import com.api.sys.entity.sys.pushmessage.PushMessage;
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
	
	private PushMessage pushMessage;
	
	public PushMessageThread(Session session, PushMessage pushMessage, String currentAccountId, String currentUserId, String projectId, String customerId, String batchNum) {
		super(session, batchNum);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.pushMessage = pushMessage;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}

	protected boolean isGoOn() {
		return true;
	}
	
	protected void doRun() throws Exception {
		PushMessageUtil.pushMessage(pushMessage.getToUserId(), pushMessage.getMessage());
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("消息推送处理时出现异常信息：{}", ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
	}
}
