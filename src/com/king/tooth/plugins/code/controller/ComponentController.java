package com.king.tooth.plugins.code.controller;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.code.builtin.data.BuiltinPluginResourceInstance;
import com.king.tooth.plugins.code.service.ComponentService;
import com.king.tooth.plugins.ijson.IJson;
import com.king.tooth.sys.controller.AController;

public class ComponentController extends AController{
	
	/**
	 * 组件准换json
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object componentJsonTurn(HttpServletRequest request, IJson ijson){
		
		// 具体的功能实现，放到componentService中去处理
		BuiltinPluginResourceInstance.getPluginInstance("ComponentService", ComponentService.class);
		
		return getResultObject(null, null);
	}
}
