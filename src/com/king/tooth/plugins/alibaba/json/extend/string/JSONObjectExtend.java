package com.king.tooth.plugins.alibaba.json.extend.string;

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
}
