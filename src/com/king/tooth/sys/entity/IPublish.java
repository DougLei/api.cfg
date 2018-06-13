package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.cfg.ComPublishInfo;

/**
 * 发布接口
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
}
