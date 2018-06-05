package com.king.tooth.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.IRequestProcesser;
import com.king.tooth.web.processer.ProcesserConfig;

/**
 * 平台调用的通用servlet
 * 通用处理器
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CommonDispatcherServlet extends PlatformServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		analysisRequestBody(request);// 解析出请求体、路由规则
		IRequestProcesser process = ProcesserConfig.getProcess(requestBody);// 获取处理器
		ResponseBody responseBody = process.doRequestProcess();
		printResult(response, responseBody);
	}
}
