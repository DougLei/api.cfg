package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * 不等于
 * @author DougLei
 */
public class NeMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		if(valueIsNullStr){// 如果条件值写的是null
			if(isInversion){
				return " (" + propName + " is null or "+propName+" = ?)";
			}else{
				return " (" + propName + " is not null or "+propName+" != '')";
			}
		}else{
			return " (" + propName + " != ?) ";
		}
	}

	protected String getMethodName() {
		return "ne";
	}
}
