package com.king.tooth.filter;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;

import com.king.tooth.sys.builtin.data.BuiltinParametersKeys;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 抽象filter类
 * @author DougLei
 */
public abstract class AbstractFilter implements Filter{
	
	/**
	 * 组装失败的responseBody
	 * @param request
	 * @param message
	 * @param isSuccess
	 */
	protected void installFailResponseBody(ServletRequest request, String message){
		ResponseBody responseBody = new ResponseBody();
		responseBody.setMessage(message);
		request.setAttribute(BuiltinParametersKeys._RESPONSE_BODY_KEY, responseBody);
	}
}
