package com.king.tooth.web.builtin.method.common.util.resulttype;

import java.util.Arrays;

import com.king.tooth.util.ReflectUtil;
import com.king.tooth.util.StrUtils;

/**
 * resultType工具类
 * @author DougLei
 */
public class ResultTypeUtil {
	
	/**
	 * 内置的resultType类型
	 */
	private transient static final String[] builtinResultType= {"KeyValues", "Strings", "Text"};
	
	/**
	 * 获取内置的resultType类型，给resultType属性
	 * @param resultType
	 * @return
	 */
	private static String getBuiltinResultType(String resultType) {
		String tmp = resultType.trim().toLowerCase();
		for (String rt : builtinResultType) {
			if(rt.toLowerCase().equals(tmp)){
				return rt;
			}
		}
		throw new IllegalArgumentException("_resultType参数传入的value错误，请检查传入的value是否是以下任意一个值，注意拼写是否一致：[" + Arrays.toString(builtinResultType) + "]");
	}
	
	/**
	 * 获取resultType实例
	 * @param resultType
	 * @return
	 */
	public static IResultType getResultTypeInstance(String resultType) {
		if(StrUtils.isEmpty(resultType)){
			resultType = "Anonymous";
		}else{
			resultType = getBuiltinResultType(resultType);
		}
		return ReflectUtil.newInstance("com.king.tooth.web.builtin.method.common.util.resulttype.impl." + resultType + "ResultType");
	}
}
