package com.king.tooth.plugins.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.exception.redis.RedisException;
import com.king.tooth.util.Log4jUtil;

/**
 * Redis的常用操作管理，与spring集成
 * 使用方式：
 * 	1、继承该类，参考DefaultRedisOps类
 * 	2、直接使用RedisOpsUtil类【推荐】
 * 	方法前加*号的表示比较常用的方法
 * @author DougLei
 */
public class BasicRedisHandler<K, V> {
	
	private RedisTemplate<K, V> redisTemplate;

	/**
	 * *将值存储到redis中【value为序列化方式存储】
	 * @param key
	 * @param value
	 * @throws Exception 
	 */
	protected void setInRedisBySerialize(K key, V value) throws RedisException{
		if(key == null || value == null){
			throw new RedisException("[BasicRedisHandler.setInRedisBySerialize]方法,参数key和value均不能为null");
		}
		
		boolean result = true;
		try {
			ValueOperations<K, V> ops = redisTemplate.opsForValue();
			ops.set(key, value);
		} catch (Exception e) {
			result = false;
		}
		Log4jUtil.debug("[BasicRedisHandler.setInRedisBySerialize]方法，存储的key为:{}，value为:{}，存储结果为:{}", key, value, result);
	}
	
	/**
	 * *根据key，从redis中取值【value为序列化方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	protected V getFromRedisBySerialize(K key) throws RedisException{
		if(key == null){
			throw new RedisException("[BasicRedisHandler.getFromRedisBySerialize]方法,参数key不能为null");
		}
		
		if(redisTemplate.hasKey(key)){
			ValueOperations<K, V> ops = redisTemplate.opsForValue();
			return (V) ops.get(key);
		}else{
			Log4jUtil.debug("[BasicRedisHandler.getFromRedisBySerialize]方法，redis缓存中不存在key={}的值", key);
			return null;
		}
	}
	//*****************************************************************************
	
	
	/**
	 * *将值存储到redis中【value为json方式存储】           json本质是string，只是存储格式不一样
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	protected void setInRedisByJson(K key, V value) throws RedisException{
		if(key == null || value == null){
			throw new RedisException("[BasicRedisHandler.setInRedisByJson]方法,参数key和value均不能为null");
		}
		
		setInRedisByString(key.toString(), JSONObject.toJSONString(value));
	}
	
	/**
	 * 根据key，从redis中取值【value为json方式存储，json为object对象】           json本质是string，只是存储格式不一样
	 * @param key
	 * @return JSONObjectExtend
	 * @throws RedisException 
	 */
	protected JSONObject getFromRedisByJsonObj(K key) throws RedisException{
		String json = getFromRedisByString(key);
		return JSONObject.parseObject(json);
	}
	
	/**
	 * 根据key，从redis中取值【value为json方式存储，json为array对象】           json本质是string，只是存储格式不一样
	 * @param key
	 * @return JSONArrayExtend
	 * @throws RedisException 
	 */
	protected JSONArray getFromRedisByJsonArr(K key) throws RedisException{
		String json = getFromRedisByString(key);
		return JSONArray.parseArray(json);
	}
	//*****************************************************************************

	
	/**
	 * 将值存储到redis中【value为string方式存储】
	 * @param key
	 * @param value
	 * @throws RedisException 
	 */
	protected void setInRedisByString(final String key, final String value) throws RedisException{
		if(key == null || value == null){
			throw new RedisException("[BasicRedisHandler.setInRedisByString]方法,参数key和value均不能为null");
		}
		
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					connection.set(key.getBytes(), value.getBytes());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		Log4jUtil.debug("[BasicRedisHandler.setInRedisByString]方法，存储的key为:{}，value为:{}，存储结果为:{}", key, value, result);
	}
	
	/**
	 * *根据key，从redis中取值【value为string方式存储】
	 * @param key
	 * @return
	 * @throws RedisException 
	 */
	protected String getFromRedisByString(final K key) throws RedisException{
		if(key == null){
			throw new RedisException("[BasicRedisHandler.getFromRedisByString]方法,参数key不能为null");
		}
		
		if(redisTemplate.hasKey(key)){
			String str = redisTemplate.execute(new RedisCallback<String>() {
				public String doInRedis(RedisConnection connection)
						throws DataAccessException {
					byte[] bytes = connection.get(key.toString().getBytes());
					return new String(bytes);
				}
			});
			return str;
		}else{
			Log4jUtil.debug("[BasicRedisHandler.getFromRedisByString]方法，redis缓存中不存在key={}的值", key);
			return null;
		}
	}
}
