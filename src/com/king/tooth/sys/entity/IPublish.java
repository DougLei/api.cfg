package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.common.ComPublishInfo;

/**
 * 发布接口
 * @author DougLei
 */
public interface IPublish {
	
	/**
	 * 转换为发布信息对象
	 */
	public ComPublishInfo turnToPublish();
}
