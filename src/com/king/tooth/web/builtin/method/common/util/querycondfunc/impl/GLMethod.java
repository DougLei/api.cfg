package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * 大于 xxx and 小于 xxx
 * @author DougLei
 */
public class GLMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		return " (" + propName + " > ? and "+propName+" < ?) ";
	}

	protected String getMethodName() {
		return "gl";
	}
}
