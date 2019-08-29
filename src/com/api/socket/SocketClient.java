package com.api.socket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.util.ExceptionUtil;

// http://localhost:8080/api.cfg/common/socket
// {"host":"192.168.1.252","port":504, "message":"start"}
// post
public class SocketClient {
	private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
	
	private Socket socket;
	private OutputStreamWriter writer;
	
	private String serverHost;
	private int serverPort;
	
	public SocketClient(String serverHost, int serverPort) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}
	
	public char[] sendMessage(String message) {
		char[] order = SocketOrderContext.getHexOrder(message);
		if(order != null) {
			try {
				socket = new Socket(serverHost, serverPort);
				writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1);
				writer.write(order);
				writer.flush();
			} catch (IOException e) {
				logger.error("socket连接发送消息时出现异常: {}", ExceptionUtil.getErrMsg(e));
				order = null;
			} finally {
				close();
			}
		}
		return order;
	}
	
	private void close() {
		if(writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error("在关闭OutputStreamWriter时出现异常: {}", ExceptionUtil.getErrMsg(e));
			}
			writer = null;
		}
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("在关闭socket连接时出现异常: {}", ExceptionUtil.getErrMsg(e));
			}
			socket = null;
		}
	}
	
	public static void main(String[] args) {
		SocketClient client = new SocketClient("192.168.1.252", 504);
		client.sendMessage("close");
	}
}
