package com.douglei.tools.instances.scanner;

import java.net.URL;

/**
 * 不支持的protocol异常
 * @author DougLei
 */
public class UnsupportProtocolException extends RuntimeException{
	private static final long serialVersionUID = -5135973500007066772L;

	public UnsupportProtocolException(URL url) {
		super("目前不支持扫描protocol=["+url.getProtocol()+"]的URL");
	}
}
