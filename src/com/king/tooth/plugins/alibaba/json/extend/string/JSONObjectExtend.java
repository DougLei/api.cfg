package com.king.tooth.plugins.alibaba.json.extend.string;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;

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
	
	public int size() {
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
		jsonObject = json;
	}
	
	public Object getJson(String focusedOper) {
		if(jsonObject != null && jsonObject.get(ResourcePropNameConstants.ID) != null){
			jsonObject.put(ResourcePropNameConstants.ID, jsonObject.get(ResourcePropNameConstants.ID) + "_" + focusedOper);
		}
		return jsonObject;
	}
}
