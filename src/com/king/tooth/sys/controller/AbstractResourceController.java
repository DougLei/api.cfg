package com.king.tooth.sys.controller;

import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 控制器的抽象父类
 * @author DougLei
 */
public abstract class AbstractResourceController {
	
	/**
	 * 组装responseBody对象
	 * @param message
	 * @param data
	 * @return
	 */
	protected ResponseBody installResponseBody(String message, Object data){
		ResponseBody responseBody = new ResponseBody(message, data);
		return responseBody;
	}
}
