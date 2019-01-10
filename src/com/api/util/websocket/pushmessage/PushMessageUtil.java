package com.api.util.websocket.pushmessage;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.util.CryptographyUtil;
import com.api.util.ExceptionUtil;
import com.api.util.JsonUtil;
import com.api.util.Log4jUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.httpclient.HttpClientUtil;

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
		WEB_PUSH_MESSAGE_ROOT_API = ResourceHandlerUtil.initConfValue("web.pushmessage.root.api", "http://localhost:8091/api.push.message/common/message");
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
	private static JSONObject callPushMessageApi(String apiUrl, Object data){
		return JsonUtil.parseJsonObject(HttpClientUtil.doPostBasic(apiUrl, null, null, HEADERS, 
				HttpClientUtil.getHttpStringRequestEntity(JsonUtil.toJsonString(data, false), "text/json")));
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
		
		JSONObject json = null;
		try {
			json = callPushMessageApi(pushMessageApi, new PushMessageDataEntity(targetUserId, message));
			return json.getInteger("data");
		} catch (NumberFormatException e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg(e));
			return -2;
		} finally{
			if(json != null){
				json.clear();
			}
		}
	}
	private static final String pushMessageApi = WEB_PUSH_MESSAGE_ROOT_API + "/single/push";
	
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
		JSONObject json = null;
		JSONArray jsonArray = null;
		try {
			for (String targetUserId : targetUserIds) {
				toUserId.append(targetUserId).append(",");
			}
			toUserId.setLength(toUserId.length()-1);
			
			json = callPushMessageApi(batchPushMessageApi, new PushMessageDataEntity(toUserId.toString(), message));
			
			jsonArray = json.getJSONArray("data");
			Integer[] result = new Integer[jsonArray.size()];
			jsonArray.toArray(result);
			
			return result;
		} catch (Exception e) {
			Log4jUtil.error(ExceptionUtil.getErrMsg(e));
			return new Integer[]{-2};
		} finally{
			if(toUserId.length()>0){
				toUserId.setLength(0);
			}
			if(json != null){
				json.clear();
			}
			if(jsonArray != null){
				jsonArray.clear();
			}
		}
	}
	private static final String batchPushMessageApi = WEB_PUSH_MESSAGE_ROOT_API + "/batch/push";
	
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
	private static final String batchPushIndividualityMessageApi = WEB_PUSH_MESSAGE_ROOT_API + "/batch_individuality/push";
	
	// ---------------------------------------------------------------------------------
	/**
	 * 关闭某个用户的websocket连接
	 * @param userId
	 * @return
	 */
	public static String closeSession(String userId){
		Map<String, Object> urlParams = new HashMap<String, Object>(1);
		urlParams.put("clientIdentity", userId);
		JSONObject json = JsonUtil.parseJsonObject(HttpClientUtil.doGetBasic(closeSessionApi, urlParams , null));
		return getPushResultContent(json.getInteger("data"));
		
	}
	private static final String closeSessionApi = WEB_PUSH_MESSAGE_ROOT_API + "/session/close";
	
	// ---------------------------------------------------------------------------------
	/**
	 * 根据消息推送的结果编码，获取相应的具体结果内容
	 * @param pushResourceCode
	 * @return
	 */
	public static String getPushResultContent(Integer pushResourceCode){
		switch (pushResourceCode) {
			case -2:
				return "调用推送消息接口时，本系统出现异常，调用失败";
			case -1:
				return "调用推送消息接口，接口系统出现异常，调用失败";
			case 0:
				return "调用推送消息接口，传入的数据不能为空";
			case 1:
				return "成功";
			case 2:
				return "被推送的客户端不在线";
			case 3:
				return "要接收推送消息的用户id不能为空";
			case 4:
				return "推送消息的内容不能为空";
			case 101:
				return "客户信息不能为空";
			case 102:
				return "客户信息无效";
			default:
				return "目前系统无法解析消息推送的结果编码值:"+pushResourceCode;
		}
	}
}
