package com.king.tooth.plugins.alibaba.json.extend.string;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json操作接口
 * <p>将jsonObjectExtend和jsonArrayExtend统一起来</p>
 * @author DougLei
 */
public interface IJson extends Serializable{
	
	/**
	 * 将对象转换为IJson类型
	 * @param data
	 * @return
	 */
	IJson parse(String data);
	
	/**
	 * 获取值
	 * @param index
	 */
	JSONObject get(int index);
	
	/**
	 * 获取值
	 * @param index
	 * @return
	 */
	IJson getIJson(int index);
	
	/**
	 * 获得长度
	 * @return
	 */
	int size();
	
	/**
	 * 获取自身的json对象
	 * <p>JSONObject或JSONArray</p>
	 * @return
	 */
	Object getJson();
	
	/**
	 * 是否是对象
	 * @return
	 */
	boolean isObject();
	
	/**
	 * 是否是数组
	 * @return
	 */
	boolean isArray();
	
	/**
	 * 清空
	 */
	void clear();
	
	/**
	 * 添加
	 * @param json
	 */
	void add(JSONObject json);
	
	/**
	 * 删除
	 * @param index
	 * @return
	 */
	JSONObject remove(int index);
	
	/**
	 * toString
	 * @return
	 */
	String toString();

	
	void addAll(JSONArray jsonArray);
}
