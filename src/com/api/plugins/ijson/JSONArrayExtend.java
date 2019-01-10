package com.api.plugins.ijson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * com.alibaba.fastjson.JSONArray的扩展
 * @author DougLei
 */
@SuppressWarnings("serial")
public class JSONArrayExtend implements IJson{
	private JSONArray jsonArray;

	/**
	 * 需要再调用parse(data)方法
	 */
	public JSONArrayExtend() {
	}
	public JSONArrayExtend(int initialCapacity) {
		this.jsonArray = new JSONArray(initialCapacity);
	}


	/**
	 * 构造的同时，parse(data)
	 * @param data
	 */
	public JSONArrayExtend(String data) {
		parse(data);
	}
	
	public IJson parse(String data) {
		jsonArray = JSONArray.parseArray(data); 
		return this;
	}
	
	public JSONObject get(int index) {
		if(jsonArray != null && jsonArray.size() > 0){
			return jsonArray.getJSONObject(index);
		}
		return null;
	}
	
	public IJson getIJson(int index) {
		if(jsonArray != null && jsonArray.size() > 0){
			return IJsonUtil.getIJson(jsonArray.get(index));
		}
		return null;
	}
	
	public int size() {
		return jsonArray.size();
	}

	public Object getJson() {
		return jsonArray;
	}

	public boolean isObject() {
		return false;
	}

	public boolean isArray() {
		return true;
	}

	public void clear() {
		if(jsonArray != null && jsonArray.size() > 0){
			JSONObject tmpJson;
			int size = jsonArray.size();
			for (int i = 0; i < size ; i++) {
				tmpJson = jsonArray.getJSONObject(i);
				if(tmpJson != null && tmpJson.size() > 0){
					tmpJson.clear();
				}
			}
			jsonArray.clear();
		}
	}
	
	public String toString(){
		if(jsonArray == null){
			return null;
		}
		return jsonArray.toJSONString();
	}

	public void add(JSONObject json) {
		if(jsonArray == null){
			jsonArray = new JSONArray();
		}
		jsonArray.add(json);
	}
	
	public JSONObject remove(int index) {
		if(index > jsonArray.size()){
			throw new IndexOutOfBoundsException("要从集合中删除的下标值大于集合的实际长度");
		}
		if(index < 0){
			throw new IndexOutOfBoundsException("要从集合中删除的下标值小于0");
		}
		JSONObject json = jsonArray.getJSONObject(index);
		jsonArray.remove(index);
		return json;
	}

	public void addAll(JSONArray jsonArray) {
		this.jsonArray.addAll(jsonArray);
	}
}
