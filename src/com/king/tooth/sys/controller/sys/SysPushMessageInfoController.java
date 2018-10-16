package com.king.tooth.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.sys.service.sys.SysPushMessageInfoService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.thread.operdb.websocket.pushmessage.PushMessageThread;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 推送消息信息表Controller
 * @author DougLei
 */
@Controller
public class SysPushMessageInfoController extends AController{
	
	/**
	 * 推送消息
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object pushMessage(HttpServletRequest request, IJson ijson){
		List<PushMessage> pushMessages = getDataInstanceList(ijson, PushMessage.class, false);
		analysisResourceProp(pushMessages);
		if(analysisResult == null){
			String batchNum = ResourceHandlerUtil.getBatchNum();
			new PushMessageThread(HibernateUtil.openNewSession(),
					HibernateUtil.openNewSession(),
					pushMessages,
					CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
					CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
					CurrentThreadContext.getProjectId(),
					CurrentThreadContext.getCustomerId(), batchNum).start();// 启动推送消息的线程
			resultObject = ijson.getJson();
		}
		return getResultObject(pushMessages, null);
	}
	
	/**
	 * 消息阅读
	 * <p>请求方式：GET</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object readMessage(HttpServletRequest request, IJson ijson){
		String id = request.getParameter(ResourcePropNameConstants.ID);
		if(StrUtils.isEmpty(id)){
			return "要阅读的消息id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("SysPushMessageInfoService", SysPushMessageInfoService.class).readMessage(id);
		return getResultObject(null, null);
	}
	
	/**
	 * 修改消息的阅读状态
	 * <p>改为已读或未读</p>
	 * <p>请求方式：PUT</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object updateMessageReadStatus(HttpServletRequest request, IJson ijson){
		List<SysPushMessageInfo> sysPushMessageInfos = getDataInstanceList(ijson, SysPushMessageInfo.class, true);
		if(analysisResult == null){
			for (SysPushMessageInfo spmi : sysPushMessageInfos) {
				resultObject = BuiltinResourceInstance.getInstance("SysPushMessageInfoService", SysPushMessageInfoService.class).updateMessageReadStatus(spmi);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(sysPushMessageInfos, OperDataTypeConstants.EDIT);
	}
}
