package com.king.tooth.sys.controller.com;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.service.com.ComColumndataService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 字段数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComColumndata")
public class ComColumndataController extends AbstractController{
	
	private ComColumndataService columnataService = new ComColumndataService();
	
	/**
	 * 添加列
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComColumndata column){
		String result = column.analysisResourceProp();
		if(result == null){
			result = columnataService.saveColumn(column);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改列
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComColumndata column){
		String result = column.analysisResourceProp();
		if(result == null){
			result = columnataService.updateColumn(column);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除列
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(HttpServletRequest request){
		String columnId = request.getParameter("columnId");
		if(StrUtils.isEmpty(columnId)){
			return installOperResponseBody("要删除的列id不能为空", null);
		}
		String result = columnataService.deleteColumn(columnId);
		return installOperResponseBody(result, null);
	}
}
