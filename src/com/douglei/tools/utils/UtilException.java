package com.douglei.tools.utils;

/**
 * 
 * @author DougLei
 */
public class UtilException extends RuntimeException{
	private static final long serialVersionUID = -1537505195948268259L;
	
	public UtilException(String message) {
		super(message);
	}
	public UtilException(String message, Throwable cause) {
		super(message, cause);
	}
}
