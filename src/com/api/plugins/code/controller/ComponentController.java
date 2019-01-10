package com.api.plugins.code.controller;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.RequestMapping;
import com.api.plugins.code.builtin.data.BuiltinPluginResourceInstance;
import com.api.plugins.code.service.ComponentService;
import com.api.plugins.ijson.IJson;
import com.api.sys.controller.AController;

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
