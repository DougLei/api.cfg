package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.service.common.ComTabledataService;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 表数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComTabledata")
public class ComTabledataController extends AbstractResourceController{
	
	private ComTabledataService tabledataService = new ComTabledataService();
	
	/**
	 * 添加表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComTabledata table){
		table.analysisResourceProp();
		tabledataService.saveTable(table);
		return installOperResponseBody("添加成功", null);
	}
	
	/**
	 * 修改表
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComTabledata table){
		table.analysisResourceProp();
		tabledataService.updateTable(table);
		return installOperResponseBody("修改成功", null);
	}
}
