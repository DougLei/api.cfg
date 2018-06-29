package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.service.cfg.ComColumndataService;
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
		List<ComColumndata> columns = getDataInstanceList(json, ComColumndata.class);
		String result = analysisResourceProp(columns);
		if(result == null){
			for (ComColumndata column : columns) {
				result = columnataService.saveColumn(column);
				if(result != null){
					throw new IllegalArgumentException(result);
				}
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改列
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public ResponseBody update(HttpServletRequest request, String json){
		List<ComColumndata> columns = getDataInstanceList(json, ComColumndata.class);
		String result = analysisResourceProp(columns);
		if(result == null){
			for (ComColumndata column : columns) {
				result = columnataService.updateColumn(column);
				if(result != null){
					throw new IllegalArgumentException(result);
				}
			}
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除列
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public ResponseBody delete(HttpServletRequest request, String json){
		String columnIds = request.getParameter(ResourceNameConstants.ID);
		if(StrUtils.isEmpty(columnIds)){
			return installOperResponseBody("要删除的列id不能为空", null);
		}
		String result = columnataService.deleteColumn(columnIds);
		return installOperResponseBody(result, null);
	}
}
