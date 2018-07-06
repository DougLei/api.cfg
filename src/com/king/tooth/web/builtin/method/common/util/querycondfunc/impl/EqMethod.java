package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * 等于
 * @author DougLei
 */
public class EqMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		if(valueIsNullStr){// 如果条件值写的是null
			if(isInversion){
				return " (" + propName + " is not null or "+propName+" != ?)";
			}else{
				return " (" + propName + " is null or "+propName+" = ?)";
			}
		}else{
			return " (" + propName + " = ?) ";
		}
	}

	protected String getMethodName() {
		return "eq";
	}
}
