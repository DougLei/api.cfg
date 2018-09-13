package com.king.tooth.sys.controller.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.thread.operdb.websocket.pushmessage.PushMessageThread;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 推送消息信息表Controller
 * @author DougLei
 */
@Controller
public class SysPushMessageInfoController extends AbstractController{
	
	/**
	 * 推送消息
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object pushMessage(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<PushMessage> pushMessages = getDataInstanceList(ijson, PushMessage.class, false);
		analysisResourceProp(pushMessages);
		if(analysisResult == null){
			new PushMessageThread(HibernateUtil.openNewSession(),
					pushMessages,
					CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
					CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
					CurrentThreadContext.getProjectId(),
					CurrentThreadContext.getCustomerId()).start();// 启动推送消息的线程
			resultObject = ijson.getJson();
		}
		return getResultObject();
	}
}
