package com.douglei.tools.utils.serialize.exception;

import java.io.File;

/**
 * 序列化文件不存在异常
 * @author DougLei
 */
public class SerializeFileNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -2075104715048623399L;

	public SerializeFileNotFoundException(File file) {
		super("反序列化时, 路径["+file.getAbsolutePath()+"]下, 不存在序列化文件");
	}
}
