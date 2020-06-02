package com.douglei.tools.utils.serialize.exception;

import java.io.File;

/**
 * 反序列化异常
 * @author DougLei
 */
public class DeserializeException extends RuntimeException{
	private static final long serialVersionUID = -2448455005766619088L;

	public DeserializeException(Class<?> targetClass, File serializationFile, Throwable t) {
		super("读取文件["+serializationFile.getAbsolutePath()+"], 反序列化对象["+targetClass.getName()+"]时出现异常", t);
	}

	public DeserializeException(Class<?> targetClass, byte[] _byte, Exception e) {
		super("读取byte数组["+_byte+"], 反序列化对象["+targetClass.getName()+"]时出现异常", e);
	}
}
