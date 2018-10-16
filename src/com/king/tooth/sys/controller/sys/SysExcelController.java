package com.king.tooth.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.tools.excel.ImportExcel;
import com.king.tooth.sys.service.sys.SysExcelService;

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
		List<ImportExcel> importExcels = getDataInstanceList(ijson, ImportExcel.class, true);
		analysisResourceProp(importExcels);
		if(analysisResult == null){
			for (ImportExcel importExcel : importExcels) {
				resultObject = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).importExcel(importExcel);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add((JSONObject) resultObject);
			}
		}
		return getResultObject(importExcels, null);
	}
	
	/**
	 * 导出excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object exportExcel(HttpServletRequest request, IJson ijson){
		return getResultObject(null, null);
	}
}
