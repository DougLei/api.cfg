package com.king.tooth.sys.entity;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;

/**
 * 发布信息接口
 * @author DougLei
 */
public interface IPublish extends IEntity{
	
	/**
	 * 转换为发布信息对象
	 */
	public ComPublishInfo turnToPublish();
	
	/**
	 * 获的批量发布时的消息
	 * @return
	 */
	public String getBatchPublishMsg();
	
	/**
	 * 转换为发布出去的实体json对象
	 * <p>和toEntityJson方法相同，只是在发布的时候，需要将实际的id值存到资源对象的refDataId字段中</p>
	 * @return
	 */
	public JSONObject toPublishEntityJson();

	/**
	 * 获取id
	 * @return
	 */
	public String getId();
}
