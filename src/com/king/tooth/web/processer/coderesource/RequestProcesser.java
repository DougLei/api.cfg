package com.king.tooth.web.processer.coderesource;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.sys.entity.common.ComCode;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.CommonProcesser;
import com.king.tooth.web.processer.IRequestProcesser;

/**
 * 请求处理器
 * @author DougLei
 */
public abstract class RequestProcesser extends CommonProcesser implements IRequestProcesser{

	/**
	 * 处理方法
	 * <p>RequestProcesser抽象类定义，由各个子类去实现</p>
	 * <p>最后各个子类通过调用super.setResponseBody(..)方法，给ResponseBody响应体属性赋予操作结果</p>
	 * @return 是否继续向下执行
	 */
	protected abstract boolean doProcess();
	
	/**
	 * 处理方法
	 * <p>对外的统一接口</p>
	 * @return ResponseBody
	 */
	public final ResponseBody doRequestProcess(){
		Log4jUtil.debug("请求的类为：{}", requestBody.getReqCodeResource().getClassPath());
		Log4jUtil.debug("请求的方法为：{}", requestBody.getReqCodeResource().getMethodName());
		Log4jUtil.debug("请求的请求体值为：{}", requestBody.getFormData());
		
		doProcess();// 进行实际的业务处理，由子类实现
		return responseBody;
	}
	
	protected void invokeMethod(){
		ComCode code = requestBody.getReqCodeResource();
		ResponseBody responseBody = invokeMethodForCodeResource(code.getCodeClassInstance(), code.getMethodName(), 
				new Class[]{HttpServletRequest.class, String.class}, 
				new Object[]{requestBody.getRequest(), requestBody.getFormData()+""});
		setResponseBody(responseBody);
	}
	
	/**
	 * 调用代码资源的方法
	 * @param obj
	 * @param methodName
	 * @param clz
	 * @param params
	 * @return
	 */
	 private ResponseBody invokeMethodForCodeResource(Object obj, String methodName, Class<?>[] clz, Object[] params){
		ResponseBody responseBody = null;
		String errMsg;
		try {
			Method method = obj.getClass().getDeclaredMethod(methodName, clz);
			responseBody = (ResponseBody) method.invoke(obj, params);
		} catch (Exception e) {
			errMsg = ExceptionUtil.getErrMsg(e);
			Log4jUtil.debug("[ReflectUtil.invokeMethodForCodeResource]方法出现异常信息:{}", errMsg);
			throw new IllegalArgumentException(errMsg);
		} 
		return responseBody;
	}
}