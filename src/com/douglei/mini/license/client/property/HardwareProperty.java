package com.douglei.mini.license.client.property;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 硬件相关的属性
 * @author DougLei
 */
public abstract class HardwareProperty extends Property{
	private static final Logger logger = LoggerFactory.getLogger(HardwareProperty.class);
	
	protected HardwareProperty(String name, String value) {
		super(name, value);
	}
	
	// 获取到本机ip实例
	protected InetAddress getInetAddress() {
		Enumeration<InetAddress> inetAddresses = null;
		InetAddress inetAddress = null;
		
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while(en.hasMoreElements()) {
				inetAddresses = en.nextElement().getInetAddresses();
				while(inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if(inetAddress instanceof Inet4Address) {
						if(!inetAddress.getHostAddress().equals("127.0.0.1")) {
							return inetAddress; // 返回第一个非127.0.0.1的ip值的ip实例
						}
					}
				}
			}
		} catch (SocketException e) {
			logger.error("无法获取主机的IP实例: {}", e.getMessage());
		}
		return null;
	}
}
