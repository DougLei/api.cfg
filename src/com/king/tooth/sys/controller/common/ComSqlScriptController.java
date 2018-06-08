package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.king.tooth.sys.controller.AbstractResourceController;

/**
 * sql脚本资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSqlScript")
public class ComSqlScriptController extends AbstractResourceController{
	
//	private ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	
}
