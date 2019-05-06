package com.api.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.code.resource.CodeResourceProcesser;
import com.api.util.CloseUtil;
import com.api.web.entity.request.RequestBody;
import com.api.web.entity.resulttype.ResponseBody;
import com.api.web.processer.IRequestProcesser;
import com.api.web.processer.ProcesserConfig;

/**
 * 平台调用的通用servlet
 * 通用处理器
 * @author DougLei
 */
@SuppressWarnings("serial")
public class OpenServlet extends HttpServlet implements Serializable{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestBody requestBody = (RequestBody) request.getAttribute(BuiltinParameterKeys._REQUEST_BODY_KEY);
		
		ResponseBody responseBody = null;
		if(requestBody.getRouteBody().getIsCode()){
			Object object = CodeResourceProcesser.invokeCodeResource(requestBody.getRouteBody().getCodeUri(), request, requestBody.getFormData());
			if(object instanceof String){
				responseBody = new ResponseBody(object.toString(), null);
			}else{
				responseBody = new ResponseBody(null, object);
			}
		}else{
			IRequestProcesser process = ProcesserConfig.getProcess(requestBody);// 获取处理器
			responseBody = process.doRequestProcess();
		}
		PrintWriter out = response.getWriter();
		out.write(responseBody.toStrings());
		CloseUtil.closeIO(out);
	}
}
