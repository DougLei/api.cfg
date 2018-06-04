package com.king.tooth.sys.controller.cfg;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [配置系统]表数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/CfgTabledata")
public class CfgTabledataController extends AbstractResourceController{
	
	/**
	 * 创建表，即建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody createTabledataModel(@RequestBody CfgTabledata tabledata){
		return null;
	}
	
	/**
	 * 删除表，即删模
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/drop", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody dropTabledataModel(@RequestBody CfgTabledata tabledata){
		return null;
	}
	
	/**
	 * 映射文件查看
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/hbmdetail/{id}", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody toHbmDetail(@PathVariable String id){
		return null;
	}
}
