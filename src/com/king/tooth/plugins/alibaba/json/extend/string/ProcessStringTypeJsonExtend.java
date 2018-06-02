package com.king.tooth.plugins.alibaba.json.extend.string;

import java.io.Serializable;

import com.king.tooth.util.Log4jUtil;

/**
 * 处理String类型的Json扩展类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProcessStringTypeJsonExtend implements Serializable{
	
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
		if(jsonStr.startsWith("{")){
			ijson = new JSONObjectExtend(jsonStr);
		}else if(jsonStr.startsWith("[")){
			ijson = new JSONArrayExtend(jsonStr);
		}else{
			Log4jUtil.debug("调用Json.getIJson()方法时，传入的json格式字符串异常，请检查：{}", jsonStr);
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
		
		// 2.取消json串中的\转义符
		jsonStr = jsonStr.replace("\\", "");
		return jsonStr;
	}
}
