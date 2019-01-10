package com.api.web.builtin.method.common.util.querycondfunc.impl;

import com.api.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * 小于或等于
 * @author DougLei
 */
public class LeMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		return " (" + propName + " <= ?) ";
	}

	protected String getMethodName() {
		return "le";
	}
}
