package com.king.tooth.plugins.code.builtin.data;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.plugins.code.controller.ComponentController;
import com.king.tooth.plugins.code.service.ComponentService;

/**
 * 系统内置的插件资源实例
 * @author DougLei
 */
public class BuiltinPluginResourceInstance {
	
	/**
	 * 插件类实例的map缓存
	 */
	private static final Map<String, Object> pluginInstanceCache = new HashMap<String, Object>(70); 
	static{
		pluginInstanceCache.put("ComponentController", new ComponentController());
		
		pluginInstanceCache.put("ComponentService", new ComponentService());
	}
	
	/**
	 * 获得插件实例
	 * @param name
	 * @return
	 */
	public static Object getPluginInstance(String name){
		return pluginInstanceCache.get(name);
	}
	/**
	 * 获得插件实例
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPluginInstance(String name, Class<T> clz){
		return (T) pluginInstanceCache.get(name);
	}
}
