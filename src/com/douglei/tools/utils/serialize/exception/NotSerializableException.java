package com.douglei.tools.utils.serialize.exception;

import java.io.Serializable;

/**
 * 没有实现Serializable接口异常
 * @author DougLei
 */
public class NotSerializableException extends RuntimeException{
	private static final long serialVersionUID = -1235061532609002846L;

	public NotSerializableException(Object object) {
		super(object.getClass().getName() + " 没有实现["+Serializable.class.getName()+"]接口");
	}
}
