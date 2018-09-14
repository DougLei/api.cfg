package com.king.tooth.util.websocket.pushmessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.enums.websocket.pushmessage.PushMessageReturnCodeEnum;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.httpclient.HttpClientUtil;

/**
 * 推送消息的工具类
 * @author DougLei
 */
public class PushMessageUtil {

	/**
	 * 调用消息推送系统的接口的header
	 */
	private static final Map<String, String> HEADERS = new HashMap<String, String>(1);
	/**
	 * 消息推送系统的根接口
	 */
	private static final String WEB_PUSH_MESSAGE_ROOT_API;
	static{
		WEB_PUSH_MESSAGE_ROOT_API = ResourceHandlerUtil.initConfValue("web.pushmessage.root.api", "http://localhost:8081/api.cfg/message");
		HEADERS.put("customerToken", CryptographyUtil.encodeMd5(
				ResourceHandlerUtil.initConfValue("web.pushmessage.api.customer.username", "SmartOne"), 
				ResourceHandlerUtil.initConfValue("web.pushmessage.api.customer.password", "1QaZ2wSx,.")));
	}
	
	/**
	 * 调用消息推送的接口
	 * @param apiUrl
	 * @param data
	 * @return
	 */
	private static String callPushMessageApi(String apiUrl, Object data){
		return HttpClientUtil.doPostBasic(apiUrl, null, null, HEADERS, 
				HttpClientUtil.getHttpStringRequestEntity(JsonUtil.toJsonString(data, false), "text/json"));
	}
	
	/**
	 * 给指定用户推送消息
	 * @param targetUserId
	 * @param message
	 * @return 推送结果的编码
	 */
	public static Integer pushMessage(String targetUserId, String message) {
		if(StrUtils.isEmpty(targetUserId)){
			throw new NullPointerException("接收推送消息的用户id不能为空");
		}
		if(StrUtils.isEmpty(message)){
			throw new NullPointerException("推送的消息不能为空");
		}
		
		try {
			String result = callPushMessageApi(pushMessageApi, new PushMessageDataEntity(targetUserId, message));
			return Integer.valueOf(result);
		} catch (NumberFormatException e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg("PushMessageUtil", "pushMessage", e));
			return PushMessageReturnCodeEnum.LOCAL_EXCEPTION.getCode();
		}
	}
	private static final String pushMessageApi = WEB_PUSH_MESSAGE_ROOT_API + "/single_push";
	
	/**
	 * 批量给用户推送消息
	 * @param targetUserIds
	 * @param message
	 * @return
	 */
	public static Integer[] batchPushMessage(String[] targetUserIds, String message){
		if(targetUserIds == null || targetUserIds.length == 0){
			throw new NullPointerException("接收推送消息的用户id不能为空");
		}
		if(StrUtils.isEmpty(message)){
			throw new NullPointerException("推送的消息不能为空");
		}
		
		StringBuilder toUserId = new StringBuilder();
		List<Integer> tmpResultList = null;
		try {
			for (String targetUserId : targetUserIds) {
				toUserId.append(targetUserId).append(",");
			}
			toUserId.setLength(toUserId.length()-1);
			
			String callResult = callPushMessageApi(batchPushMessageApi, new PushMessageDataEntity(toUserId.toString(), message));
			
			tmpResultList = JsonUtil.parseArray(callResult, Integer.class);
			Integer[] result = new Integer[tmpResultList.size()];
			tmpResultList.toArray(result);
			
			return result;
		} catch (Exception e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg("PushMessageUtil", "batchPushMessage", e));
			return new Integer[]{PushMessageReturnCodeEnum.LOCAL_EXCEPTION.getCode()};
		} finally{
			if(toUserId.length()>0){
				toUserId.setLength(0);
			}
			if(tmpResultList != null && tmpResultList.size() > 0){
				tmpResultList.clear();
			}
		}
	}
	private static final String batchPushMessageApi = WEB_PUSH_MESSAGE_ROOT_API + "/batch_push";
	
	/**
	 * 批量给用户推送个性消息
	 * <p>即一个用户一个消息，如果对应不上，则抛出异常</p>
	 * @param messages
	 * @param targetUserIds
	 * @return
	 */
	public static Integer[] batchPushIndividualityMessage(String[] targetUserIds, String[] messages){
		if(targetUserIds == null || targetUserIds.length == 0){
			throw new NullPointerException("接收推送消息的用户id不能为空");
		}
		if(messages == null || messages.length == 0){
			throw new NullPointerException("推送的消息不能为空");
		}
		if(targetUserIds.length != messages.length){
			throw new IllegalArgumentException("推送个性消息时，接收的用户数量和信息数量不等");
		}
		System.out.println(batchPushIndividualityMessageApi);
		return null;
	}
	private static final String batchPushIndividualityMessageApi = WEB_PUSH_MESSAGE_ROOT_API + "/batch_individuality_push";
}
