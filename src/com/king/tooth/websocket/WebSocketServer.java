package com.king.tooth.websocket;

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
 * webSocket的服务端
 * @author DougLei
 */
@ServerEndpoint("/WebSocketServer/{username}")  
public class WebSocketServer {
	
	private static int onlineCount = 0; 
    private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<String, WebSocketServer>(); 
    private Session session; 
    private String username; 
       
    @OnOpen 
    public void onOpen(@PathParam("username") String username, Session session) throws IOException { 
   
        this.username = username; 
        this.session = session; 
           
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
        for (WebSocketServer item : clients.values()) { 
            if (item.username.equals(To) ) 
                item.session.getAsyncRemote().sendText(message); 
        } 
    } 
       
    public void sendMessageAll(String message) throws IOException { 
        for (WebSocketServer item : clients.values()) { 
            item.session.getAsyncRemote().sendText(message); 
        } 
    } 
   
    public static synchronized int getOnlineCount() { 
        return onlineCount; 
    } 
   
    public static synchronized void addOnlineCount() { 
    	WebSocketServer.onlineCount++; 
    } 
   
    public static synchronized void subOnlineCount() { 
    	WebSocketServer.onlineCount--; 
    } 
   
    public static synchronized Map<String, WebSocketServer> getClients() { 
        return clients; 
    } 
}
