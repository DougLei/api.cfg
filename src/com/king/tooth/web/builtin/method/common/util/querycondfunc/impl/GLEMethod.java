package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * 大于等于 xxx and 小于等于 xxx
 * @author DougLei
 */
public class GLEMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		return " (" + propName + " >= ? and "+propName+" <= ?) ";
	}

	protected String getMethodName() {
		return "gle";
	}
}
