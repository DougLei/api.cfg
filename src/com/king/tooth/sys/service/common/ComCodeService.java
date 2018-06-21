package com.king.tooth.sys.service.common;

import com.king.tooth.sys.entity.common.ComCode;
import com.king.tooth.sys.service.AbstractService;

/**
 * 代码资源服务处理器
 * @author DougLei
 */
public class ComCodeService extends AbstractService{
	
	/**
	 * 根据id，获取代码资源对象
	 * @param comCodeId
	 * @return
	 */
	public ComCode getComCodeById(String comCodeId){
		return getObjectById(comCodeId, ComCode.class);
	}
}
