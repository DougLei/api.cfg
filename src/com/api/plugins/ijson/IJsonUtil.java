package com.api.plugins.ijson;

import java.io.Serializable;

import com.api.util.Log4jUtil;
import com.api.util.StrUtils;

/**
 * ijson的工具类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IJsonUtil implements Serializable{
	
	/**
	 * 获取IJson对象
	 * @param jsonStr
	 * @return
	 */
	public static IJson getIJson(Object json) {
		if(StrUtils.isEmpty(json)){
			return null;
		}
		return getIJson(json.toString());
	}
	
	/**
	 * 获取IJson对象
	 * @param jsonStr
	 * @return
	 */
	public static IJson getIJson(String jsonStr) {
		IJson ijson = null;
		jsonStr = jsonStr.trim();
		Log4jUtil.debug("调用Json.getIJson()方法时，传入的json格式字符串为：{}", jsonStr);
		
		jsonStr = processSepcialWord(jsonStr);
		if(jsonStr.startsWith("{") && jsonStr.endsWith("}")){
			ijson = new JSONObjectExtend(jsonStr);
		}else if(jsonStr.startsWith("[") && jsonStr.endsWith("]")){
			ijson = new JSONArrayExtend(jsonStr);
		}else{
			throw new IllegalArgumentException("传入的json格式错误，请检查："+ jsonStr);
		}
		return ijson;
	}

	/**
	 * 处理json串的特殊字符
	 * @param jsonStr
	 * @return
	 */
	private static String processSepcialWord(String jsonStr) {
		// 1.如果前端请求的json字符串前后带有"，则处理掉
		if(jsonStr.startsWith("\"")){
			jsonStr = jsonStr.substring(1, jsonStr.length()-1);
		}
		return jsonStr;
	}
}
