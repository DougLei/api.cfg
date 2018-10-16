package com.king.tooth.plugins.alibaba.json.extend.string;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;

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
	
	public Object getJson(String focusedOper) {
		if(jsonArray != null && jsonArray.size() > 0){
			JSONObject tmpJson;
			int size = jsonArray.size();
			for (int i = 0; i < size ; i++) {
				tmpJson = jsonArray.getJSONObject(i);
				if(tmpJson.get(ResourcePropNameConstants.ID) != null){
					tmpJson.put(ResourcePropNameConstants.ID, tmpJson.get(ResourcePropNameConstants.ID) + "_" + focusedOper);
				}
			}
		}
		return jsonArray;
	}
}
