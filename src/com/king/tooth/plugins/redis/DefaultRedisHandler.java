package com.king.tooth.plugins.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.exception.redis.RedisException;
import com.king.tooth.util.Log4jUtil;

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
		try {
			super.setInRedisBySerialize(key, value);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.setInRedisBySerialize]方法操作redis出现异常信息:{}", e.getMessage());
		}
	}
	
	/**
	 * *根据key，从redis中取值【value为序列化方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public Object getFromRedisBySerialize(String key){
		try {
			return super.getFromRedisBySerialize(key);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.getFromRedisBySerialize]方法操作redis出现异常信息:{}", e.getMessage());
		}
		return null;
	}
	//*****************************************************************************
	
	
	/**
	 * *将值存储到redis中【value为json方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	public void setInRedisByJson(String key, Object value){
		try {
			super.setInRedisByJson(key, value);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.setInRedisByJson]方法操作redis出现异常信息:{}", e.getMessage());
		}
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonObject方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public JSONObject getFromRedisByJsonObj(String key){
		try {
			return super.getFromRedisByJsonObj(key);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.getFromRedisByJsonObj]方法操作redis出现异常信息:{}", e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据key，从redis中取值【value为jsonArray方式存储】 
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public JSONArray getFromRedisByJsonArr(String key){
		try {
			return super.getFromRedisByJsonArr(key);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.getFromRedisByJsonArr]方法操作redis出现异常信息:{}", e.getMessage());
		}
		return null;
	}
	//*****************************************************************************

	
	/**
	 * 将值存储到redis中【value为string方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	public void setInRedisByString(String key, String value){
		try {
			super.setInRedisByString(key, value);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.setInRedisByString]方法操作redis出现异常信息:{}", e.getMessage());
		}
	}
	
	/**
	 * *根据key，从redis中取值【value为string方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	public String getFromRedisByString(String key){
		try {
			return super.getFromRedisByString(key);
		} catch (RedisException e) {
			Log4jUtil.debug("[DefaultRedisHandler.getFromRedisByString]方法操作redis出现异常信息:{}", e.getMessage());
		}
		return null;
	}
}
