package com.king.tooth.sys.controller;

import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 控制器的抽象父类
 * @author DougLei
 */
public abstract class AbstractController {
	
	/**
	 * 组装操作responseBody对象，主要用于返回操作结果内容
	 * @param operResult 操作结果内容
	 * @param definedMessage 自定义的消息内容
	 * @return
	 */
	protected ResponseBody installOperResponseBody(String operResult, String definedMessage){
		ResponseBody responseBody = new ResponseBody();
		if(operResult == null){
//			if(definedMessage == null){
//				responseBody.setMessage("操作成功");
//			}else{
				responseBody.setMessage(definedMessage);
//			}
		}else{
			responseBody.setMessage(operResult);
		}
		return responseBody;
	}
	
	/**
	 * 组装responseBody对象，返回结果内容和对象
	 * @param message 内容
	 * @param data 对象
	 * @return
	 */
	protected ResponseBody installResponseBody(String message, Object data){
		ResponseBody responseBody = new ResponseBody(message, data);
		return responseBody;
	}
}
