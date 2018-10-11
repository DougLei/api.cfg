package com.king.tooth.sys.controller.sys;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.controller.AController;

/**
 * excel操作Controller
 * @author DougLei
 */
@Controller
public class SysExcelController extends AController{

	/**
	 * 导入excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object importExcel(HttpServletRequest request, IJson ijson){
		return getResultObject();
	}
	
	/**
	 * 导出excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object exportExcel(HttpServletRequest request, IJson ijson){
		return getResultObject();
	}
}
