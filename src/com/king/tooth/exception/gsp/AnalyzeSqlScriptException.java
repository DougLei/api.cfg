package com.king.tooth.exception.gsp;

/**
 * gsp 解析sql脚本异常
 * @author DougLei
 */
@SuppressWarnings("serial")
public class AnalyzeSqlScriptException extends Exception{
	public AnalyzeSqlScriptException() {
	}
	public AnalyzeSqlScriptException(String errorMsg) {
		super(errorMsg);
	}
}
