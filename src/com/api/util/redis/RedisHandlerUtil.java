package com.api.util.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.plugins.redis.DefaultRedisHandler;
import com.api.util.Log4jUtil;

/**
 * redis操作类
 * 		支持开发人员直接操作redis，使用的是redisTemplate操作
 * 		与spring集成
 * 		方法前加*号的表示比较常用的方法
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RedisHandlerUtil implements Serializable{
	
	// 通过xml配置注入
	private transient static DefaultRedisHandler defaultRedisOps;
	public void setDefaultRedisOps(DefaultRedisHandler dro) {
		defaultRedisOps = dro;
	}
	
	/**
	 * *将值存储到redis中【value为序列化方式存储】
	 * @param key
	 * @param value
	 * @throws Exception 
	 */
	public static void setInRedisBySerialize(String key, Object value){
		defaultRedisOps.setInRedisBySerialize(key, value);
	}
	
	/**
	 * *根据key，从redis中取值【value为序列化方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public static Object getFromRedisBySerialize(String key){
		return defaultRedisOps.getFromRedisBySerialize(key);
	}
	
	/**
	 * *根据key，从redis中取值【value为序列化方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFromRedisBySerialize(String key, Class<T> classType){
		return (T) defaultRedisOps.getFromRedisBySerialize(key);
	}
	//*****************************************************************************

	
	/**
	 * *将值存储到redis中【value为json方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	public static void setInRedisByJson(String key, Object value){
		defaultRedisOps.setInRedisByJson(key, value);
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonObject方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public static JSONObject getFromRedisByJsonObj(String key){
		return defaultRedisOps.getFromRedisByJsonObj(key);
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonObject方式存储】
	 * @param key
	 * @param classType
	 * @return
	 * @throws RedisException 
	 */
	public static <T> T getFromRedisByJsonObj(String key, Class<T> classType){
		JSONObject jsonObject = defaultRedisOps.getFromRedisByJsonObj(key);
		if(jsonObject != null){
			return jsonObject.toJavaObject(classType);
		}
		
		Log4jUtil.debug("[RedisHandlerUtil.getFromRedisByJsonObj]方法，查询key为:{}，对应的jsonObject为null", key);
		return null;
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonArray方式存储】 
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public static JSONArray getFromRedisByJsonArr(String key){
		return defaultRedisOps.getFromRedisByJsonArr(key);
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonArray方式存储】 
	 * @param key
	 * @param classType 集合存储的类型
	 * @return 
	 * @return
	 * @throws RedisException 
	 */
	public static <T> List<T> getFromRedisByJsonArrList(String key, Class<T> classType){
		JSONArray jsonArray = defaultRedisOps.getFromRedisByJsonArr(key);
		if(jsonArray != null && jsonArray.size() > 0){
			int jsonArraySize = jsonArray.size();
			List<T> list = new ArrayList<T>(jsonArraySize);
			for (int i = 0; i < jsonArraySize; i++) {
				list.add(jsonArray.getJSONObject(i).toJavaObject(classType));
			}
			
			jsonArray.clear();
			jsonArray = null;
			return list;
		}
		
		Log4jUtil.debug("[RedisHandlerUtil.getFromRedisByJsonArrList]方法，查询key为:{}，对应的list为null", key);
		return null;
	}
	//*****************************************************************************

	
	/**
	 * 将值存储到redis中【value为string方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	public static void setInRedisByString(String key, String value){
		defaultRedisOps.setInRedisByString(key, value);
	}
	
	/**
	 * *根据key，从redis中取值【value为string方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public static String getFromRedisByString(String key){
		return defaultRedisOps.getFromRedisByString(key);
	}
}
