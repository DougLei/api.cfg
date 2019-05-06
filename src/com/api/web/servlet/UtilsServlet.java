package com.api.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.tools.UtilsController;
import com.api.util.CloseUtil;
import com.api.web.entity.resulttype.ResponseBody;

/**
 * 平台调用的通用servlet
 * 通用处理器
 * @author DougLei
 */
@SuppressWarnings("serial")
public class UtilsServlet extends HttpServlet implements Serializable{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResponseBody responseBody = null;
		String[] uri = request.getRequestURI().split("/");
		if(uri.length == 4){
			String method = request.getMethod().toLowerCase();
			
			Object result = null;
			
			if("get".equals(method) && "getClientIp".equals(uri[3])){
				result = BuiltinResourceInstance.getInstance("UtilsController", UtilsController.class).getClientIp(request);
			}
			
			if(result == null){
				responseBody = new ResponseBody("操作结果信息为null，请联系系统开发人员", null);
			}else if(result instanceof String){
				responseBody = new ResponseBody(result.toString(), null);
			}else{
				responseBody = new ResponseBody(null, result);
			}
		}else{
			responseBody = new ResponseBody("系统不支持请求的资源", null);
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseBody.toStrings());
		CloseUtil.closeIO(out);
	}
}
