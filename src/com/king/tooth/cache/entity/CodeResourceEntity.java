package com.king.tooth.cache.entity;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
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
	private Class[] methodParameterClassTypeArr = {HttpServletRequest.class, IJson.class};
	
	/**
	 * 代码对应的实例
	 */
	private Object instance;
	/**
	 * 方法名
	 */
	private String methodName;
	
	public CodeResourceEntity(Object instance, String methodName) {
		this.instance = instance;
		this.methodName = methodName;
	}

	/**
	 * 调用代码资源的方法
	 * @param request
	 * @param ijson
	 * @return
	 */
	 public Object invokeMethodForCodeResource(HttpServletRequest request, IJson ijson){
		Object object = null;
		try {
			Log4jUtil.debug(" ========================> 此次请求调用的类为：{}", instance.getClass());
			Log4jUtil.debug(" ========================> 此次请求调用的方法为：{}", methodName);
			Method method = instance.getClass().getDeclaredMethod(methodName, methodParameterClassTypeArr);
			object = method.invoke(instance, new Object[]{request, ijson});
		} catch (Exception e) {
			object = ExceptionUtil.getErrMsg(e);
			Log4jUtil.debug("[CodeResourceEntity.invokeMethodForCodeResource]方法出现异常信息:{}", object);
		} 
		return object;
	}
}
