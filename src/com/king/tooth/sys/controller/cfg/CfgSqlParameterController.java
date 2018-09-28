package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本参数信息表Controller
 * @author DougLei
 */
@Controller
public class CfgSqlParameterController extends AbstractController{
	
	/**
	 * 添加sql脚本参数
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(ijson, ComSqlScriptParameter.class, true);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).saveSqlScriptParameter(sqlScriptParameters);
			sqlScriptParameters.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改sql脚本参数
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(ijson, ComSqlScriptParameter.class, true);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).updateSqlScriptParameter(sqlScriptParameters);
			sqlScriptParameters.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 删除sql脚本参数
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String sqlScriptParameterIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(sqlScriptParameterIds)){
			return "要删除的sql脚本参数id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).deleteSqlScriptParameter(sqlScriptParameterIds);
		processResultObject(BuiltinParameterKeys._IDS, sqlScriptParameterIds);
		return getResultObject();
	}
}
