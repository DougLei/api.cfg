package com.king.tooth.web.servlet;

import java.io.Serializable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.web.entity.request.RequestBody;


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
	 * @return null表示解析成功，否则标识解析出现问题
	 */
	protected abstract String analysisRequestBody(HttpServletRequest request);
}
