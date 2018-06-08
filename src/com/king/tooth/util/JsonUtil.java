package com.king.tooth.util;

import java.util.List;
import java.util.Map;

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
	 * 将对象转换为json对象
	 * @param data
	 * @return
	 */
	public static JSONObject toJsonObject(Object data){
		String jsonStr = JSONObject.toJSONString(data);
		if(jsonStr == null){
			throw new NullPointerException("要转换为json对象的data实体对象不能为空");
		}
		return JSONObject.parseObject(jsonStr);
	}

	/**
	 * 将map转换成指定的java实体
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T turnMapToJavaEntity(Map<String, Object> map, Class<T> clazz) {
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
		String jsonStr = toJsonString(maps, false);
		return JSONObject.parseArray(jsonStr, clazz);
	}
}
