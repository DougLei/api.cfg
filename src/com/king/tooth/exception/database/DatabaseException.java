package com.king.tooth.exception.database;

/**
 * 平台database操作相关异常
 * @author DougLei
 */
@SuppressWarnings("serial")
public class DatabaseException extends Exception{
	public DatabaseException() {
	}
	public DatabaseException(String errorMsg) {
		super(errorMsg);
	}
}
