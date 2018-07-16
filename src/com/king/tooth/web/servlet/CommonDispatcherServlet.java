package com.king.tooth.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.sys.builtin.data.BuiltinParametersKeys;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.util.StrUtils;
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
		ResponseBody responseBody = null;
		String analysisResult = analysisRequestBody(request);// 解析出请求体、路由规则
		if(analysisResult == null){
			if(requestBody.getRouteBody().isAction() 
					|| (ISysResource.CODE.equals(requestBody.getRequestResourceType()) && StrUtils.isEmpty(requestBody.getRouteBody().getParentResourceName())) ){
				Object object = CodeResourceMapping.invokeCodeResource(requestBody.getReqCodeResourceKey(), request, requestBody.getFormData());
				if(object instanceof String){
					responseBody = new ResponseBody(object.toString(), null, false);
				}else{
					responseBody = new ResponseBody(null, object, true);
				}
			}else{
				IRequestProcesser process = ProcesserConfig.getProcess(requestBody);// 获取处理器
				responseBody = process.doRequestProcess();
			}
		}else{
			responseBody = new ResponseBody(analysisResult, null, false);
		}
		request.setAttribute(BuiltinParametersKeys._RESPONSE_BODY_KEY, responseBody);
	}
}
