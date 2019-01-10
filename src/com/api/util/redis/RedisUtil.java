package com.api.util.redis;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.util.CloseUtil;
import com.api.util.Log4jUtil;
import com.api.util.SerializeUtil;

/**
 * redis的基础工具类
 * 		支持开发人员直接操作redis，使用的是Jedis操作
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RedisUtil implements Serializable{
	// 主机
	private transient static String host;
	// 端口
	private transient static int port;
	// 密码
	private transient static String password;
	// 初始化静态块
	static{
		Log4jUtil.debug("[RedisUtil.static]初次进入静态代码块，初始化redis的host、post、password信息");
		
		InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream("redis.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			Log4jUtil.debug("[RedisUtil.static]静态代码块方法出现异常信息:{}", e.getMessage());
		}
		host = prop.getProperty("redis.host");
		port = Integer.valueOf(prop.getProperty("redis.port"));
		password = prop.getProperty("redis.password");
		Log4jUtil.debug("[RedisUtil.static]静态代码块初始化redis的基础配置信息,host:{},port:{}", host, port);
		
		CloseUtil.closeIO(in);
	}
	
	/**
	 * 获取jedis
	 * @return
	 */
	private static Jedis getJedis(){
		Jedis jedis = new Jedis(host, port);
		jedis.auth(password);
		Log4jUtil.debug("[RedisUtil.getJedis]成功创建Jedis实例");
		return jedis;
	}
	/**
	 * 关闭jedis
	 */
	private static void closeJedis(Jedis jedis){
		if(jedis != null){
			jedis.close();
			jedis = null;
		}
		Log4jUtil.debug("[RedisUtil.getJedis]成功关闭Jedis实例");
	}

	
	//------------------------------------------------------------------------------
	/**
	 * 存储string类型的value
	 * @param key
	 * @param value
	 */
	public static void setString(String key, String value){
		Jedis jedis = getJedis();
		jedis.set(key, value);
		closeJedis(jedis);
	}
	/**
	 * 获取string类型的value
	 * @param key
	 * @return
	 */
	public static String getString(String key){
		Jedis jedis = getJedis();
		String value = jedis.get(key);
		closeJedis(jedis);
		return value;
	}
	
	
	//------------------------------------------------------------------------------
	/**
	 * 存储对象类型的value【序列化方式】
	 * @param key
	 * @param obj
	 */
	public static void setObjectBySerialize(String key, Object obj){
		if(obj == null){
			Log4jUtil.debug("[RedisUtil.setObjectBySerialize]参数obj的值为null");
			return;
		}
		byte[] bytes = SerializeUtil.serializeObjectToByte(obj);
		if(bytes == null){
			Log4jUtil.debug("[RedisUtil.setObjectBySerialize]参数obj序列化结果失败");
			return;
		}
		
		Jedis jedis = getJedis();
		jedis.set(key.getBytes(), bytes);
		closeJedis(jedis);
	}
	/**
	 * 获取对象类型的value【序列化方式】
	 * @param key
	 * @return object,需要强制转换
	 */
	public static Object getObjectBySerialize(String key){
		Jedis jedis = getJedis();
		byte[] bytes = jedis.get(key.getBytes());
		closeJedis(jedis);
		return SerializeUtil.unserializeObjectFromByte(bytes);
	}
	/**
	 * 获取对象类型的value【序列化方式】
	 * @param key
	 * @return CodeResourceEntity,不需要强制转换
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObjectBySerialize(String key, Class<T> classType){
		return (T) getObjectBySerialize(key);
	}
	
	
	//------------------------------------------------------------------------------
	/**
	 * 存储对象类型的value【json方式】
	 * @param key
	 * @param obj
	 */
	public static void setObjectByJson(String key, Object obj){
		if(obj == null){
			Log4jUtil.debug("[RedisUtil.setObjectByJson]参数obj的值为null");
			return;
		}
		Jedis jedis = getJedis();
		jedis.set(key, JSONObject.toJSON(obj).toString());
		closeJedis(jedis);
	}
	/**
	 * 获取对象类型的value【json方式】
	 * @param key
	 * @return 获得相关的json，要自己用JSONObject的方法处理
	 */
	public static String getObjectByJson(String key){
		Jedis jedis = getJedis();
		String json = jedis.get(key);
		closeJedis(jedis);
		return json;
	}
	/**
	 * 获取对象类型的value【json方式】
	 * @param key
	 * @return Object
	 */
	public static <T> T getObjectByJsonObj(String key, Class<T> classType){
		return JSONObject.parseObject(getObjectByJson(key), classType);
	}
	/**
	 * 获取对象类型的value【json方式】
	 * @param key
	 * @return List
	 */
	public static <T> List<T> getObjectByJsonList(String key, Class<T> classType){
		return JSONArray.parseArray(getObjectByJson(key), classType);
	}
}
