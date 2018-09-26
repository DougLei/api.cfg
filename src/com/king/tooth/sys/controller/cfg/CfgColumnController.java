package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.service.cfg.CfgColumnService;
import com.king.tooth.util.StrUtils;

/**
 * 字段信息表Controller
 * @author DougLei
 */
@Controller
public class CfgColumnController extends AbstractController{
	
	/**
	 * 添加列
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComColumndata> columns = getDataInstanceList(ijson, ComColumndata.class, true);
		analysisResourceProp(columns);
		if(analysisResult == null){
			if(columns.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).saveColumn(columns.get(0));
			}else{
				for (ComColumndata column : columns) {
					resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).saveColumn(column);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			columns.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改列
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComColumndata> columns = getDataInstanceList(ijson, ComColumndata.class, true);
		analysisResourceProp(columns);
		if(analysisResult == null){
			if(columns.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).updateColumn(columns.get(0));
			}else{
				for (ComColumndata column : columns) {
					resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).updateColumn(column);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			columns.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 删除列
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String columnIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(columnIds)){
			return "要删除的列id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).deleteColumn(columnIds);
		processResultObject(BuiltinParameterKeys._IDS, columnIds);
		return getResultObject();
	}
}