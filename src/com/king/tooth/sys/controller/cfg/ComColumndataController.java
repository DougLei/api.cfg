package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.service.cfg.ComColumndataService;
import com.king.tooth.util.StrUtils;

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
	public Object add(HttpServletRequest request, String json){
		List<ComColumndata> columns = getDataInstanceList(json, ComColumndata.class);
		analysisResourceProp(columns);
		if(analysisResult == null){
			if(columns.size() == 1){
				resultObject = columnataService.saveColumn(columns.get(0));
			}else{
				for (ComColumndata column : columns) {
					resultObject = columnataService.saveColumn(column);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 修改列
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json){
		List<ComColumndata> columns = getDataInstanceList(json, ComColumndata.class);
		analysisResourceProp(columns);
		if(analysisResult == null){
			if(columns.size() == 1){
				resultObject = columnataService.updateColumn(columns.get(0));
			}else{
				for (ComColumndata column : columns) {
					resultObject = columnataService.updateColumn(column);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 删除列
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String columnIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(columnIds)){
			return "要删除的列id不能为空";
		}
		resultObject = columnataService.deleteColumn(columnIds);
		processResultObject(ResourceNameConstants.IDS, columnIds);
		return getResultObject();
	}
}
