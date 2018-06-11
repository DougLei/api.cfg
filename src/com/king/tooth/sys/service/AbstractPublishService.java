package com.king.tooth.sys.service;

import com.king.tooth.sys.service.common.ComPublishInfoService;

/**
 * 发布服务器的抽象类
 * @author DougLei
 */
public abstract class AbstractPublishService extends AbstractService{
	/**
	 * 发布信息的服务层
	 */
	protected ComPublishInfoService publishInfoService = new ComPublishInfoService();
}
