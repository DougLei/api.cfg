package com.king.tooth.plugins.code.code.resource;

import com.king.tooth.plugins.code.controller.ComponentController;
import com.king.tooth.sys.code.resource.CodeResourceMapping;

/**
 * 插件代码资源映射
 * @author DougLei
 */
public class PluginCodeResourceMapping {
	/**
	 * 是否没有初始化代码资源映射
	 * 防止被多次初始化
	 */
	private static boolean unInitCodeResourceMapping = true;
	
	/**
	 * 初始化插件代码资源
	 */
	private static void initPluginCodeResource() {
		// 韩鹏用组建功能
		CodeResourceMapping.put("/component/json/turn/post".toLowerCase(), ComponentController.class, "componentJsonTurn");
	}
	
	/**
	 * 系统启动时，初始化系统插件代码资源
	 */
	public static void initPluginCodeResourceMapping(){
		if(unInitCodeResourceMapping){
			initPluginCodeResource();
			unInitCodeResourceMapping = false;
		}
	}
}
