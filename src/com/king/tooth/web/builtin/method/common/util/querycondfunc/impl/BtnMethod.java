package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * Between, Btn
 * @author DougLei
 */
public class BtnMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		return " (" + propName + notOperator + " between ? and ? ) ";
	}

	protected String getMethodName() {
		return "btn";
	}
}
