package com.douglei.tools.instances.scanner;

import java.net.URLConnection;

/**
 * 不支持的urlConnection异常
 * @author DougLei
 */
public class UnsupportUrlConnectionException extends RuntimeException{
	private static final long serialVersionUID = -4742653216323578182L;

	public UnsupportUrlConnectionException(URLConnection urlConnection) {
		super("目前不支持class=["+urlConnection.getClass().getName()+"]的URLConnection");
	}
}
