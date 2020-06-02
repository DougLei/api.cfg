package com.douglei.mini.license.client.property;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.mini.license.client.ValidationResult;

/**
 * 
 * @author DougLei
 */
public class MacProperty extends HardwareProperty {
	private static final Logger logger = LoggerFactory.getLogger(MacProperty.class);
	
	public MacProperty(String value) {
		super("mac", value);
	}
	
	/**
	 * 验证mac
	 * @return
	 */
	public ValidationResult verify() {
		final String localhostMac = getLocalhostMac();
		for(String mac : getMacs()) {
			if(localhostMac.equals(mac)) {
				return null;
			}
		}
		return new ValidationResult() {
			
			@Override
			public String getMessage() {
				return "本机MAC地址["+localhostMac+"]不合法，合法的MAC地址包括" + Arrays.toString(getMacs());
			}
			
			@Override
			protected String getCode_() {
				return "mac.unlegal";
			}
			
			@Override
			public Object[] getI18nParams() {
				return new Object[] {localhostMac, Arrays.toString(getMacs())};
			}
		};
	}
	
	private String[] macs;
	private String[] getMacs() {
		if(macs == null) {
			macs = value.split(",");
			for (int i = 0; i < macs.length; i++) {
				macs[i] = macs[i].trim();
			}
		}
		return macs;
	}
	
	// 获取本机的mac地址
	private String getLocalhostMac() {
		InetAddress inetAddress = getInetAddress();
		if(inetAddress != null) {
			try {
				NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
				byte[] macByte = networkInterface.getHardwareAddress();
				StringBuilder sb = new StringBuilder(20);
				for (int i = 0; i < macByte.length; i++) {
					sb.append(String.format("%02X%s", macByte[i], (i<macByte.length-1)?"-":""));
				}
				return sb.toString();
			} catch (SocketException e) {
				logger.error("无法获取主机的MAC信息: {}", e.getMessage());
			}
		}
			
		return null;
	}
}
