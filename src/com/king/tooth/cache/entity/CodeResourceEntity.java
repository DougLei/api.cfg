package com.king.tooth.cache.entity;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;

/**
 * 代码资源实体
 * @author DougLei
 */
@SuppressWarnings("rawtypes")
public class CodeResourceEntity {
	/**
	 * 方法参数的类型数组
	 */
	private Class[] methodParameterClassTypeArr = {HttpServletRequest.class, String.class};
	
	/**
	 * 代码对应的类
	 */
	private Class clz;
	/**
	 * 方法名
	 */
	private String methodName;
	
	public CodeResourceEntity(Class clz, String methodName) {
		this.clz = clz;
		this.methodName = methodName;
	}

	/**
	 * 调用代码资源的方法
	 * @param request
	 * @param formDataJson
	 * @return
	 */
	 public Object invokeMethodForCodeResource(HttpServletRequest request, String formDataJson){
		Object object = null;
		try {
			Object obj = clz.newInstance();
			Log4jUtil.debug(" ========================> 此次请求调用的类为：{}", obj.getClass());
			Log4jUtil.debug(" ========================> 此次请求调用的方法为：{}", methodName);
			Method method = obj.getClass().getDeclaredMethod(methodName, methodParameterClassTypeArr);
			object = method.invoke(obj, new Object[]{request, formDataJson});
		} catch (Exception e) {
			object = ExceptionUtil.getErrMsg(e);
			Log4jUtil.debug("[CodeResourceEntity.invokeMethodForCodeResource]方法出现异常信息:{}", object);
		} 
		return object;
	}
}
