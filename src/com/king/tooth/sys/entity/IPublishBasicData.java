package com.king.tooth.sys.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 要发布的基础数据接口
 * <p>例如数据字典数据等</p>
 * @author DougLei
 */
public interface IPublishBasicData {
	
	/**
	 * 获取基础数据所属的资源名
	 * <p>即IEntity.getEntityName()</p>
	 * @return
	 */
	@JSONField(serialize = false)
	public String getBasicDataResourceName();
	
	/**
	 * 将基础数据对象转换为json数据格式
	 * <p>即IEntity.toEntityJson()</p>
	 * @return
	 */
	public JSONObject toJsonData();
}
