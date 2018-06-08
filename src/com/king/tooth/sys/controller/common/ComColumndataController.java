package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.service.common.ComColumndataService;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 字段数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComColumndata")
public class ComColumndataController extends AbstractResourceController{
	
	private ComColumndataService columnataService = new ComColumndataService();
	
	/**
	 * 添加列
	 * @param column
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComColumndata column){
		column.analysisResourceProp();
		columnataService.saveColumn(column);
		return installOperResponseBody("添加成功", null);
	}
	
	/**
	 * 修改列
	 * @param column
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComColumndata column){
		column.analysisResourceProp();
		columnataService.updateColumn(column);
		return installOperResponseBody("修改成功", null);
	}
}
