package com.api.plugins.ijson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * com.alibaba.fastjson.JSONObject的扩展
 * @author DougLei
 */
@SuppressWarnings("serial")
public class JSONObjectExtend implements IJson{
	private JSONObject jsonObject;

	/**
	 * 需要再调用parse(data)方法
	 */
	public JSONObjectExtend() {
	}
	
	/**
	 * 构造的同时，parse(data)
	 * @param data
	 */
	public JSONObjectExtend(String data) {
		parse(data);
	}
	
	public IJson parse(String data) {
		jsonObject = JSONObject.parseObject(data);
		return this;
	}
	
	public JSONObject get(int index) {
		return jsonObject;
	}
	
	public IJson getIJson(int index) {
		return this;
	}
	
	public int size() {
		if(jsonObject == null){
			return 0;
		}
		return 1;
	}

	public Object getJson() {
		return jsonObject;
	}

	public boolean isObject() {
		return true;
	}

	public boolean isArray() {
		return false;
	}

	public void clear() {
		if(jsonObject != null && jsonObject.size()>0){
			jsonObject.clear();
		}
	}
	
	public String toString(){
		if(jsonObject == null){
			return null;
		}
		return jsonObject.toJSONString();
	}

	public void add(JSONObject json) {
		if(jsonObject != null && jsonObject.size() > 0){
			jsonObject.clear();
		}
		jsonObject = json;
	}
	
	public JSONObject remove(int index) {
		if(index > 1){
			throw new IndexOutOfBoundsException("要从集合中删除的下标值大于集合的实际长度");
		}
		if(index < 0){
			throw new IndexOutOfBoundsException("要从集合中删除的下标值小于0");
		}
		JSONObject json = jsonObject;
		jsonObject = null;
		return json;
	}
	
	public void addAll(JSONArray jsonArray) {
		if(jsonArray != null && jsonArray.size() > 0){
			jsonObject = jsonArray.getJSONObject(0);
		}
	}
}
