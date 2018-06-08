package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * sql脚本资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSqlScript")
public class ComSqlScriptController extends AbstractResourceController{
	
	private ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	
}
