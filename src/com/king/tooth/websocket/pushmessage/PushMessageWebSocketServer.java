package com.king.tooth.websocket.pushmessage;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.websocket.pushmessage.entity.Customer;
import com.king.tooth.websocket.pushmessage.entity.PushMessageClient;
import com.king.tooth.websocket.pushmessage.enums.PushMessageReturnCodeEnum;
import com.king.tooth.websocket.pushmessage.mapping.CustomerMapping;
import com.king.tooth.websocket.pushmessage.mapping.PushMessageClientMapping;

/**
 * 推送消息的webSocket服务端
 * @author DougLei
 */
@ServerEndpoint("/websocket/message/push/{customerToken}/{clientIdentity}")  
public class PushMessageWebSocketServer {
    
	/**
     * 建立连接
     * @param customerToken 票，客户的(用户名+密码)后再md5加密的值
     * @param clientIdentity 客户端的唯一标示，一般传入一个绝对唯一的值，例如uuid
     * @param session
     * @throws IOException
     */
    @OnOpen 
    public void onOpen(@PathParam("customerToken")String customerToken, @PathParam("clientIdentity")String clientIdentity, Session session) throws IOException {
    	Customer customer = CustomerMapping.getCustomer(customerToken);
    	if(customer != null){
    		addOnlineCount(); 
    		PushMessageClientMapping.put(clientIdentity, new PushMessageClient(session));
    		Log4jUtil.debug("客户{}中，clientIdentity为{}的连接上消息推送服务器", customer.getRealName(), clientIdentity);
    	}else{
    		session.close();
    		Log4jUtil.warn("不存在customerToken值为{}的客户，请注意可能是恶意请求", customerToken);
    	}
    } 
   
    /**
     * 关闭连接
     * <p>在调用session.close()的时候，会调用到该方法</p>
     * @param clientIdentity
     * @throws IOException
     */
    @OnClose 
    public void onClose(@PathParam("clientIdentity")String clientIdentity) throws IOException { 
    	PushMessageClientMapping.remove(clientIdentity);
        subOnlineCount(); 
    } 
   
    /**
     * 当连接出现异常时
     * @param session
     * @param error
     */
    @OnError 
    public void onError(Session session, Throwable error) { 
        error.printStackTrace(); 
        Log4jUtil.error("消息推送服务器的连接出现异常信息：{}", error.getMessage());
    }
    
    /**
     * 当收到客户端传来的消息时
     * <p>该方法可以实现在线聊天的功能，将客户端传来的消息再推出</p>
     * @param message
     * @throws IOException
     */
    @OnMessage 
    public int onMessage(String message) throws IOException { 
        return 1; 
    } 
   
    /**
     * 推送消息的方法
     * @param clientIdentity
     * @param message
     * @throws IOException
     */
    public static int sendMessage(String clientIdentity, String message) {
    	PushMessageClient client = PushMessageClientMapping.get(clientIdentity);
    	if(client == null){
    		Log4jUtil.debug("被推送的，clientIdentity值为{}的客户端不在线，推送消息失败", clientIdentity);
    		return PushMessageReturnCodeEnum.UN_ONLINE.getCode();
    	}else{
    		client.getSession().getAsyncRemote().sendText(message);
    		return PushMessageReturnCodeEnum.SUCCESS.getCode();
    	}
    } 
    
    /**
     * 增加一个在线的数量
     */
    private static synchronized void addOnlineCount() { 
    	PushMessageClientMapping.onlineCount++; 
    }
   
    /**
     * 减少一个在线的数量
     */
    private static synchronized void subOnlineCount() { 
    	PushMessageClientMapping.onlineCount--; 
	}
}
