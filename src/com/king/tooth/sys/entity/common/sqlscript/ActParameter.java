package com.king.tooth.sys.entity.common.sqlscript;

/**
 * 在替换sql语句的时候，存储参数名和实际值的对象
 * @author DougLei
 */
public class ActParameter {
	private String parameterName;
	private Object actualValue;
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public Object getActualValue() {
		return actualValue;
	}
	public void setActualValue(Object actualValue) {
		this.actualValue = actualValue;
	}
}
