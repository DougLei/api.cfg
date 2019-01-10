package com.api.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.util.CloseUtil;
import com.api.web.entity.resulttype.ResponseBody;

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
	protected ResponseBody installFailResponseBody(ServletRequest request, String message){
		ResponseBody responseBody = new ResponseBody();
		responseBody.setMessage(message);
		request.setAttribute(BuiltinParameterKeys._RESPONSE_BODY_KEY, responseBody);
		return responseBody;
	}
	
	/**
	 * 打印结果
	 * @param resp
	 * @param responseBody
	 * @throws IOException 
	 */
	protected void printResult(ServletResponse resp, ResponseBody responseBody) throws IOException {
		PrintWriter out = resp.getWriter();
		out.write(responseBody.toStrings());
		CloseUtil.closeIO(out);
	}
}
