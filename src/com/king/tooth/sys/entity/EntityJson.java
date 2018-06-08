package com.king.tooth.sys.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * 实体json类
 * @author DougLei
 */
public class EntityJson {
	
	/**
	 * 内置的json对象
	 */
	private final JSONObject json;

	public EntityJson(JSONObject json) {
		this.json = json;
	}

	/**
	 * 设置值
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value){
		if(key == null || value == null){
			return;
		}
		if(value instanceof Date){
			json.put(key, value);
		}else{
			json.put(key, value.toString());
		}
	}
	
	/**
	 * 删除值
	 * @param key
	 */
	public void remove(String key){
		json.remove(key);
	}
	
	/**
	 * 获得内置的json对象
	 * @return
	 */
	public JSONObject getEntityJson(){
		return json;
	}
}
