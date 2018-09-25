package com.king.tooth.sys.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 实体接口
 * @author DougLei
 */
public interface IEntity {
	
	/**
	 * 获取实体名
	 * @return
	 */
	public String getEntityName();
	
	/**
	 * 转换为实体json对象
	 * @return
	 */
	public JSONObject toEntityJson();
}
