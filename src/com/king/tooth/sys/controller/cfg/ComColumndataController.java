package com.king.tooth.sys.controller.cfg;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.service.cfg.ComColumndataService;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 字段数据信息资源对象控制器
 * @author DougLei
 */
public class ComColumndataController extends AbstractController{
	
	private ComColumndataService columnataService = new ComColumndataService();
	
	/**
	 * 添加列
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public ResponseBody add(HttpServletRequest request, String json){
		ComColumndata column = JsonUtil.parseObject(json, ComColumndata.class);
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
	public ResponseBody update(HttpServletRequest request, String json){
		ComColumndata column = JsonUtil.parseObject(json, ComColumndata.class);
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
	public ResponseBody delete(HttpServletRequest request, String json){
		String columnId = request.getParameter("columnId");
		if(StrUtils.isEmpty(columnId)){
			return installOperResponseBody("要删除的列id不能为空", null);
		}
		String result = columnataService.deleteColumn(columnId);
		return installOperResponseBody(result, null);
	}
}
