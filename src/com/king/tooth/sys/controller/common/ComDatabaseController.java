package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]数据库数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComDatabase")
public class ComDatabaseController extends AbstractResourceController{
	
	/**
	 * 创建库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody createDatabase(@RequestBody ComDatabase database){
		return null;
	}
	
	/**
	 * 删除库
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/drop", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody dropDatabase(@RequestBody ComDatabase database){
		return null;
	}
}
