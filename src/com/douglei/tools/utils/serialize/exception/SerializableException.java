package com.douglei.tools.utils.serialize.exception;

/**
 * 序列化异常
 * @author DougLei
 */
public class SerializableException extends RuntimeException{
	private static final long serialVersionUID = -6934045616173401695L;

	public SerializableException(Object object, Throwable t) {
		super("序列化对象["+object.getClass().getName()+"]时出现异常", t);
	}
}
