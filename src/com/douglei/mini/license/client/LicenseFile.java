package com.douglei.mini.license.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import com.douglei.mini.license.client.property.ExpiredProperty;
import com.douglei.mini.license.client.property.IpProperty;
import com.douglei.mini.license.client.property.MacProperty;
import com.douglei.mini.license.client.property.SignatureProperty;

/**
 * 授权文件实例
 * @author DougLei
 */
public class LicenseFile {
	protected ExpiredProperty expired;
	protected IpProperty ip;
	protected MacProperty mac ;
	protected SignatureProperty signature;
	
	/**
	 * 获取授权文件的内容摘要
	 * @return
	 */
	protected byte[] getContentDigest() {
		StringBuilder content = new StringBuilder(500);
		content.append(expired.getContent());
		if(ip != null)
			content.append(ip.getContent());
		if(mac != null)
			content.append(mac.getContent());
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(content.toString().getBytes());
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	
	/**
	 * 设置授权文件内容
	 * @param content
	 * @throws ParseException 
	 */
	void setContent(String content) throws ParseException {
		int equalSignIndex = getEqualSignIndex(content);
		String value = content.substring(equalSignIndex+1);
		switch(content.substring(0, equalSignIndex)) {
			case "expired":
				expired = new ExpiredProperty(value);
				break;
			case "ip":
				ip = new IpProperty(value);
				break;
			case "mac":
				mac = new MacProperty(value);
				break;
			case "signature":
				signature = new SignatureProperty(value);
				break;
		}
		
	}
	// 获取等号的下标, 用以拆分key和value
	private int getEqualSignIndex(String content) {
		for(int i=0;i<content.length();i++) {
			if(content.charAt(i) == '=') {
				return i;
			}
		}
		return -1;
	}
}
