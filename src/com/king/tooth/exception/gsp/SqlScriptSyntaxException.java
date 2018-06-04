package com.king.tooth.exception.gsp;

/**
 * gsp 解析sql脚本时，sql脚本的语法异常
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlScriptSyntaxException extends Exception{
	public SqlScriptSyntaxException() {
	}
	public SqlScriptSyntaxException(String errorMsg) {
		super(errorMsg);
	}
}
