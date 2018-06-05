package com.king.tooth.sys.controller.cfg;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.service.cfg.CfgColumndataService;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [配置系统]字段数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/CfgColumndata")
public class CfgColumndataController extends AbstractResourceController{
	
	private CfgColumndataService columnataService = new CfgColumndataService();
	
	/**
	 * 添加列
	 * @param column
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody addColumn(@RequestBody CfgColumndata column){
		columnataService.saveColumn(column);
		return installResponseBody("添加成功", null);
	}
	
	/**
	 * 修改列
	 * @param column
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody updateColumn(@RequestBody CfgColumndata column){
		columnataService.updateColumn(column);
		return installResponseBody("修改成功", null);
	}
}
