package com.king.tooth.util.websocket.pushmessage;

/**
 * 推送消息的工具类
 * @author DougLei
 */
public class PushMessageUtil {

	/**
	 * 给指定用户推送消息
	 * @param message
	 * @param targetUserId
	 * @return 推送结果的编码
	 */
	public static int pushMessage(String message, String targetUserId) {
		// TODO 远程推送消息
		return 1;
	}
	
	/**
	 * 批量给用户推送消息
	 * @param message
	 * @param targetUserIds
	 * @return
	 */
	public static int batchPushMessage(String message, String[] targetUserIds){
		// TODO 远程推送消息
		return 1;
	}
	
	/**
	 * 批量给用户推送个性消息
	 * <p>即一个用户一个消息，如果对应不上，则抛出异常</p>
	 * @param messages
	 * @param targetUserIds
	 * @return
	 */
	public static int batchPushIndividualityMessage(String[] messages, String[] targetUserIds){
		// TODO 远程推送消息
		return 1;
	}
}
