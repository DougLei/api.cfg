package com.douglei.mini.license.client;

/**
 * 验证结果
 * @author DougLei
 */
public abstract class ValidationResult {
	
	/**
	 * 返回message
	 * @return
	 */
	public abstract String getMessage();
	
	/**
	 * 返回code, 后续可以集成国际化
	 * @return
	 */
	public final String getCode() {
		return "license.validate." + getCode_();
	}
	
	/**
	 * 返回code, 后续可以集成国际化
	 * @return
	 */
	protected abstract String getCode_();
	
	/**
	 * 匹配国际化消息中的占位符参数
	 * @return
	 */
	public Object[] getI18nParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
