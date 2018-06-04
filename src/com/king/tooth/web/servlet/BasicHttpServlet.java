package com.king.tooth.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.ResponseBody;


/**
 * httpServlet基础封装
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class BasicHttpServlet extends HttpServlet implements Serializable{

	/**
	 * 请求体
	 */
	protected RequestBody requestBody;
	
	/**
	 * 解析出请求体
	 * @param request
	 */
	protected abstract void analysisRequestBody(HttpServletRequest request);
	
	/**
	 * 将响应结果打印并返回给客户端
	 * @param request 
	 * @param response
	 * @param responseBody
	 */
	protected void printResult(HttpServletResponse response, ResponseBody responseBody){
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(responseBody.toStrings());
		} catch (IOException e) {
			Log4jUtil.debug("[BasicHttpServlet.printResult]方法出现异常信息:{}", e.getMessage());
		}finally{
			CloseUtil.closeIO(out);
		}
	}
}
