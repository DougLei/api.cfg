package com.api.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.constants.OperDataTypeConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.sys.SysPushMessageInfo;
import com.api.sys.entity.sys.pushmessage.PushMessage;
import com.api.sys.service.sys.SysPushMessageInfoService;
import com.api.util.StrUtils;
import com.api.util.websocket.pushmessage.PushMessageUtil;

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
		analysisResourceProp(pushMessages, false);
		if(analysisResult == null){
//			String batchNum = ResourceHandlerUtil.getBatchNum();
			
			PushMessage pushMessage = pushMessages.get(0);
			PushMessageUtil.pushMessage(pushMessage.getToUserId(), pushMessage.getMessage());
			
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
