package com.king.tooth.websocket.pushmessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.king.tooth.websocket.pushmessage.entity.PushMessageClient;

/**
 * 推送消息的客户端对象的映射
 * @author DougLei
 */
public class PushMessageClientMapping {
	/**
	 * 当前连接的客户端数量
	 */
	public static int onlineCount = 0;
	
	private final static Map<String, PushMessageClient> onlineClientMapping = new ConcurrentHashMap<String, PushMessageClient>(); 
	
	/**
	 * 
	 * @param clientIdentity
	 * @param client
	 */
	public static void put(String clientIdentity, PushMessageClient client){
		onlineClientMapping.put(clientIdentity, client);
	}
	
	/**
	 * 
	 * @param clientIdentity
	 * @return
	 */
	public static PushMessageClient remove(String clientIdentity){
		return onlineClientMapping.remove(clientIdentity);
	}
	
	/**
	 * 
	 * @param clientIdentity
	 * @return
	 */
	public static PushMessageClient get(String clientIdentity){
		return onlineClientMapping.get(clientIdentity);
	}
}
