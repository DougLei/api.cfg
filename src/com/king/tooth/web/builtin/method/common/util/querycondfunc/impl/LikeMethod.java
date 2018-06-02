package com.king.tooth.web.builtin.method.common.util.querycondfunc.impl;

import com.king.tooth.web.builtin.method.common.util.querycondfunc.AbstractBuiltinQueryCondFunc;

/**
 * Contains, Ctn
 * @author DougLei
 */
public class LikeMethod extends AbstractBuiltinQueryCondFunc {

	public String toDBScriptStatement(String propName, Object[] values) {
		StringBuilder hql = new StringBuilder();
		hql.append(" ( ");
		
		int len = values.length;
		for (int i = 0; i < len; i++) {
			hql.append(propName)
			   .append(notOperator)
			   .append(" like ? ");
			
			if(i < (len-1)){
				hql.append(isInversion?" and ":" or ");
			}
		}
		hql.append(" ) ");
		return hql.toString();
	}

	protected String getMethodName() {
		return "like";
	}
}
