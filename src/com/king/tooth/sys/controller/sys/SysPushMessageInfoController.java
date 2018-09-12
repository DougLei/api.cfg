package com.king.tooth.sys.controller.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.pushmessage.PushMessage;
import com.king.tooth.thread.CurrentThreadContext;

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
		List<PushMessage> pushMessages = getDataInstanceList(ijson, PushMessage.class);
		analysisResourceProp(pushMessages);
		if(analysisResult == null){
			SysPushMessageInfo basicPushMsgInfo = new SysPushMessageInfo(
					CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
					CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId());
			
			if(pushMessages.size() == 1){
				resultObject = BuiltinObjectInstance.sysPushMessageInfoService.pushMessage(basicPushMsgInfo, pushMessages.get(0));
			}else{
				for (PushMessage pushMessage : pushMessages) {
					resultObject = BuiltinObjectInstance.sysPushMessageInfoService.pushMessage(basicPushMsgInfo, pushMessage);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
}
