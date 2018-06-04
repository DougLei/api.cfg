package com.king.tooth.exception.redis;

/**
 * 平台redis相关异常
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RedisException extends Exception{
	public RedisException() {
	}
	public RedisException(String errorMsg) {
		super(errorMsg);
	}
}
