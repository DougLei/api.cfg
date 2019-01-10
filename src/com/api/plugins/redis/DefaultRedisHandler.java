package com.api.plugins.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 封装BasicRedisOps类，默认实现
 * 开发人员可以自行继承BasicRedisOps类，实现自定义
 * 方法前加*号的表示比较常用的方法
 * @author DougLei
 */
public class DefaultRedisHandler extends BasicRedisHandler<String, Object>{
	
	/**
	 * *将值存储到redis中【value为序列化方式存储】
	 * @param key
	 * @param value
	 * @throws Exception 
	 */
	public void setInRedisBySerialize(String key, Object value){
		super.setInRedisBySerialize(key, value);
	}
	
	/**
	 * *根据key，从redis中取值【value为序列化方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public Object getFromRedisBySerialize(String key){
		return super.getFromRedisBySerialize(key);
	}
	//*****************************************************************************
	
	
	/**
	 * *将值存储到redis中【value为json方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	public void setInRedisByJson(String key, Object value){
		super.setInRedisByJson(key, value);
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonObject方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public JSONObject getFromRedisByJsonObj(String key){
		return super.getFromRedisByJsonObj(key);
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonArray方式存储】 
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public JSONArray getFromRedisByJsonArr(String key){
		return super.getFromRedisByJsonArr(key);
	}
	//*****************************************************************************

	
	/**
	 * 将值存储到redis中【value为string方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	public void setInRedisByString(String key, String value){
		super.setInRedisByString(key, value);
	}
	
	/**
	 * *根据key，从redis中取值【value为string方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public String getFromRedisByString(String key){
		return super.getFromRedisByString(key);
	}
}
