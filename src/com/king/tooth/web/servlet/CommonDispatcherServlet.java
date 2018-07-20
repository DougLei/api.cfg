package com.king.tooth.web.servlet;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.sys.builtin.data.BuiltinParametersKeys;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.IRequestProcesser;
import com.king.tooth.web.processer.ProcesserConfig;

/**
 * 平台调用的通用servlet
 * 通用处理器
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CommonDispatcherServlet extends HttpServlet implements Serializable{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestBody requestBody = (RequestBody) request.getAttribute(BuiltinParametersKeys._REQUEST_BODY_KEY);
		
		ResponseBody responseBody = null;
		if(requestBody.getResourceInfo().isCodeResource()){
			Object object = CodeResourceMapping.invokeCodeResource(requestBody.getResourceInfo().getCodeResourceKey(), request, requestBody.getFormData());
			if(object instanceof String){
				responseBody = new ResponseBody(object.toString(), null, false);
			}else{
				responseBody = new ResponseBody(null, object, true);
			}
		}else{
			IRequestProcesser process = ProcesserConfig.getProcess(requestBody);// 获取处理器
			responseBody = process.doRequestProcess();
		}
		request.setAttribute(BuiltinParametersKeys._RESPONSE_BODY_KEY, responseBody);
	}
}
