package com.king.tooth.cache;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代码资源缓存
 * @author DougLei
 */
public class CodeResourceCache {
	/**
	 * 代码资源的缓存
	 * <p>key=代码资源名</p>
	 * <p>value=资源对应的方法</p>
	 */
	private transient static Map<String, Method> codeResourceMappings;// 这个长度根据以后实际开发后，决定如何处理
	
	/**
	 * 初始化代码资源缓存
	 * @param currentSysType 
	 */
	public static void initCodeResourceMappings(String currentSysType){
		// 根据配置系统，或运行系统，查询对应的代码资源并初始化
//		codeResourceMappings = new HashMap<String, Method>(30);
	}
	
	/**
	 * 根据资源名，获取对应的方法
	 * @param resourceName
	 * @return
	 */
	public static Method getMethod(String resourceName){
		Method method = codeResourceMappings.get(resourceName);
		if(method == null){
			throw new NullPointerException("目前系统不存在名为["+resourceName+"]的代码资源信息，请检查请求是否正确");
		}
		return method;
	}
}
