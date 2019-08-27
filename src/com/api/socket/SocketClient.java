package com.api.socket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.util.ExceptionUtil;
import com.api.util.StrUtils;

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
	
	public void sendMessage(String message) {
		if(StrUtils.isEmpty(message)) {
			return;
		}
		try {
			socket = new Socket(serverHost, serverPort);
			writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write(SocketOrderContext.getHexOrder(message));
			writer.flush();
		} catch (IOException e) {
			logger.error("socket连接发送消息时出现异常: {}", ExceptionUtil.getErrMsg(e));
		} finally {
			close();
		}
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
