package com.king.tooth.sys.controller.cfg;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.util.StrUtils;

/**
 * 字段数据信息资源对象控制器
 * @author DougLei
 */
public class ComColumndataController extends AbstractController{
	
	/**
	 * 添加列
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComColumndata> columns = getDataInstanceList(ijson, ComColumndata.class);
		analysisResourceProp(columns);
		if(analysisResult == null){
			if(columns.size() == 1){
				resultObject = BuiltinInstance.columndataService.saveColumn(columns.get(0));
			}else{
				for (ComColumndata column : columns) {
					resultObject = BuiltinInstance.columndataService.saveColumn(column);
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
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComColumndata> columns = getDataInstanceList(ijson, ComColumndata.class);
		analysisResourceProp(columns);
		if(analysisResult == null){
			if(columns.size() == 1){
				resultObject = BuiltinInstance.columndataService.updateColumn(columns.get(0));
			}else{
				for (ComColumndata column : columns) {
					resultObject = BuiltinInstance.columndataService.updateColumn(column);
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
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String columnIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(columnIds)){
			return "要删除的列id不能为空";
		}
		resultObject = BuiltinInstance.columndataService.deleteColumn(columnIds);
		processResultObject(ResourceNameConstants.IDS, columnIds);
		return getResultObject();
	}
}
