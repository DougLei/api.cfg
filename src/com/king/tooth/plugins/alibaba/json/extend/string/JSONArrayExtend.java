package com.king.tooth.plugins.alibaba.json.extend.string;

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
		return jsonArray.getJSONObject(index);
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
			for (int i = 0; i < jsonArray.size(); i++) {
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
}
