package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.service.common.ComTabledataService;
import com.king.tooth.util.StrUtils;
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
		String result = table.analysisResourceProp();
		if(result == null){
			result = tabledataService.saveTable(table);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改表
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComTabledata table){
		String result = table.analysisResourceProp();
		if(result == null){
			result = tabledataService.updateTable(table);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除表
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(HttpServletRequest request){
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要删除的表id不能为空", null);
		}
		String result = tabledataService.deleteTable(tableId);
		return installOperResponseBody(result, null);
	}
	

	/**
	 * 建模
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/buildModel", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody buildModel(HttpServletRequest request){
		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("建模功能目前只提供给平台开发人员使用", null);
		}
		
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要建模的表id不能为空", null);
		}
		String result = tabledataService.buildModel(tableId);
		return installOperResponseBody(result, null);
	}
}
