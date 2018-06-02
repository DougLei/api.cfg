package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * Any, In
 * @author DougLei
 */
public class InMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		int len = values.length;
		
		StringBuilder hql = new StringBuilder();
		hql.append(" ( ")
		   .append(propName)
		   .append(notOperator)
		   .append(" in (");
		
		for (int i=0; i<len; i++) {
			if(StrUtils.isNullStr((values[i]+""))){
				continue;
			}
			hql.append("?").append(",");
		}
		hql.setLength(hql.length()-1);// 去除最后一个","
		hql.append(") ");
		
		if(valueIsNullStr){
			hql.append(isInversion? " and ": " or ")
			   .append(propName)
			   .append(" is ")
			   .append(notOperator)
			   .append("null");
		}
		
		hql.append(") ");
		return hql.toString();
	}

	protected String getMethodName() {
		return "in";
	}
}
