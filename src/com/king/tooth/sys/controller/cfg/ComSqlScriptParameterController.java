package com.king.tooth.sys.controller.cfg;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本参数信息表Controller
 * @author DougLei
 */
@Controller
public class ComSqlScriptParameterController extends AbstractPublishController{
	
	/**
	 * 添加sql脚本参数
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(ijson, ComSqlScriptParameter.class);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = BuiltinObjectInstance.sqlScriptService.saveSqlScriptParameter(sqlScriptParameters);
		}
		return getResultObject();
	}
	
	/**
	 * 修改sql脚本参数
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(ijson, ComSqlScriptParameter.class);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = BuiltinObjectInstance.sqlScriptService.updateSqlScriptParameter(sqlScriptParameters);
		}
		return getResultObject();
	}
	
	/**
	 * 删除sql脚本参数
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String sqlScriptParameterIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(sqlScriptParameterIds)){
			return "要删除的sql脚本参数id不能为空";
		}
		resultObject = BuiltinObjectInstance.sqlScriptService.deleteSqlScriptParameter(sqlScriptParameterIds);
		processResultObject(BuiltinParameterKeys._IDS, sqlScriptParameterIds);
		return getResultObject();
	}
}
