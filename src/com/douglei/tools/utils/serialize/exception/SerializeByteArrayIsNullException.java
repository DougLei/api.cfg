package com.douglei.tools.utils.serialize.exception;

/**
 * 序列化的byte数组为空异常
 * @author DougLei
 */
public class SerializeByteArrayIsNullException extends RuntimeException{
	private static final long serialVersionUID = -8735943532858303039L;

	public SerializeByteArrayIsNullException() {
		super("反序列化时, 传入的byte数组为空");
	}
}
