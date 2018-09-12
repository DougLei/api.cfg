package com.king.tooth.websocket.pushmessage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;

/**
 * 推送消息的webSocket服务端
 * @author DougLei
 */
@ServerEndpoint("/message/push/{userId}/{token}")  
public class PushMessageWebSocketServer {
	
	private static int onlineCount = 0; 
    private static Map<String, PushMessageClient> clients = new ConcurrentHashMap<String, PushMessageClient>(); 
       
    @OnOpen 
    public void onOpen(@PathParam("username") String username, Session session) throws IOException { 
    	
    	
    	
        addOnlineCount(); 
        clients.put(username, this);
        System.out.println("已连接");
    } 
   
    @OnClose 
    public void onClose() throws IOException { 
        clients.remove(username); 
        subOnlineCount(); 
    } 
   
    @OnMessage 
    public void onMessage(String message) throws IOException { 
   
        JSONObject jsonTo = JSONObject.parseObject(message);
        String mes = (String) jsonTo.get("message");
         
        if (!jsonTo.get("To").equals("All")){ 
            sendMessageTo(mes, jsonTo.get("To").toString()); 
        }else{ 
            sendMessageAll("给所有人"); 
        } 
    } 
   
    @OnError 
    public void onError(Session session, Throwable error) { 
        error.printStackTrace(); 
    } 
   
    public void sendMessageTo(String message, String To) throws IOException { 
        for (PushMessageWebSocketServer item : clients.values()) { 
            if (item.username.equals(To) ) 
                item.session.getAsyncRemote().sendText(message); 
        } 
    } 
       
    public void sendMessageAll(String message) throws IOException { 
        for (PushMessageWebSocketServer item : clients.values()) { 
            item.session.getAsyncRemote().sendText(message); 
        } 
    } 
   
    public static synchronized int getOnlineCount() { 
        return onlineCount; 
    } 
   
    public static synchronized void addOnlineCount() { 
    	PushMessageWebSocketServer.onlineCount++; 
    } 
   
    public static synchronized void subOnlineCount() { 
    	PushMessageWebSocketServer.onlineCount--; 
    } 
   
    public static synchronized Map<String, PushMessageWebSocketServer> getClients() { 
        return clients; 
    } 
}
