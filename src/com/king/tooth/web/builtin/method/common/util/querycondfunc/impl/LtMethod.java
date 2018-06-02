package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

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
