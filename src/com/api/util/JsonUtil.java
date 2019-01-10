package com.api.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json操作工具类
 * @author DougLei
 */
public class JsonUtil {
	
	/**
	 * 获得对象的json字符串
	 * @param data
	 * @param isFormat 是否格式化json串
	 * @return
	 */
	public static String toJsonString(Object data, boolean isFormat){
		if(StrUtils.isEmpty(data)){
			return null;
		}
		
		String jsonString = null;
		if(isFormat){
			jsonString = JSONObject.toJSONString(data 
					,SerializerFeature.WriteMapNullValue // 如果key值为null，则要输出null，不能不输出该key
					,SerializerFeature.WriteDateUseDateFormat // 按照指定的时间格式输出  JSONObject.DEFFAULT_DATE_FORMAT 
					,SerializerFeature.PrettyFormat // 格式化
					);
		}else{
			jsonString = JSONObject.toJSONString(data 
					,SerializerFeature.WriteMapNullValue // 如果key值为null，则要输出null，不能不输出该key
					,SerializerFeature.WriteDateUseDateFormat // 按照指定的时间格式输出  JSONObject.DEFFAULT_DATE_FORMAT 
					);
		}
		return jsonString;
	}
	
	/**
	 * 将json字符串转换为对象
	 * @param json
	 * @param targetClassType
	 * @return 
	 */
	public static <T> T parseObject(String json, Class<T> targetClassType){
		if(StrUtils.isEmpty(json)){
			return null;
		}
		return JSONObject.parseObject(json, targetClassType);
	}

	/**
	 * 将json字符串转换为集合对象
	 * @param json
	 * @param targetClassType
	 * @return 
	 */
	public static <T> List<T> parseArray(String json, Class<T> targetClassType){
		if(StrUtils.isEmpty(json)){
			return null;
		}
		return JSONArray.parseArray(json, targetClassType);
	}
	
	/**
	 * 将json字符串转换为JSONObject
	 * @param json
	 * @return 
	 */
	public static JSONObject parseJsonObject(String json){
		if(StrUtils.isEmpty(json)){
			return null;
		}
		return JSONObject.parseObject(json);
	}

	/**
	 * 将json字符串转换为JSONArray
	 * @param json
	 * @return 
	 */
	public static JSONArray parseJsonArray(String json){
		if(StrUtils.isEmpty(json)){
			return null;
		}
		return JSONArray.parseArray(json);
	}
	
	/**
	 * 将对象转换为json对象
	 * @param data
	 * @return
	 */
	public static JSONObject toJsonObject(Object data){
		if(data == null){
			throw new NullPointerException("要转换为json对象的data实体对象不能为空");
		}
		return (JSONObject) JSONObject.toJSON(data);
	}
	
	/**
	 * 将对象转换为java对象
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <T> T toJavaObject(JSON json, Class<T> clz){
		if(json == null){
			throw new NullPointerException("要转换为java对象的json实体对象不能为空");
		}
		return JSONObject.toJavaObject(json, clz);
	}

	/**
	 * 将map转换成指定的java实体
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T turnMapToJavaEntity(Map<String, Object> map, Class<T> clazz) {
		if(map == null || map.size() == 0){
			return null;
		}
		String jsonStr = toJsonString(map, false);
		return JSONObject.parseObject(jsonStr, clazz);
	}
	
	/**
	 * 将 [list<map>] 转换成指定的java [list<实体>]
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> turnListMapToJavaListEntity(List<Map<String, Object>> maps, Class<T> clazz) {
		if(maps == null || maps.size() == 0){
			return null;
		}
		String jsonStr = toJsonString(maps, false);
		return JSONObject.parseArray(jsonStr, clazz);
	}
	
	/**
	 * 清空jsonArray集合对象
	 * @param jsonArray
	 */
	public static void clearJsonArray(JSONArray jsonArray){
		if(jsonArray == null || jsonArray.size() == 0){
			return;
		}
		for(int i = 0; i < jsonArray.size(); i++){
			jsonArray.getJSONObject(i).clear();
		}
		jsonArray.clear();
	}
}
