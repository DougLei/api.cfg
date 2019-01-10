package com.api.web.builtin.method.common.util.querycondfunc.impl;

import com.api.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * 小于
 * @author DougLei
 */
public class LtMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		return " (" + propName + " < ?) ";
	}

	protected String getMethodName() {
		return "lt";
	}
}
