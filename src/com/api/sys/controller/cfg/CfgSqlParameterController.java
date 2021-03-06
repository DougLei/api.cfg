package com.api.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.cfg.CfgSqlParameter;
import com.api.sys.service.cfg.CfgSqlService;
import com.api.util.StrUtils;

/**
 * sql脚本参数信息表Controller
 * @author DougLei
 */
@Controller
public class CfgSqlParameterController extends AController{
	
	/**
	 * 添加sql脚本参数
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgSqlParameter> sqlScriptParameters = getDataInstanceList(ijson, CfgSqlParameter.class, true);
		analysisResourceProp(sqlScriptParameters, false);
		if(analysisResult == null){
			for (CfgSqlParameter sqlParam : sqlScriptParameters) {
				resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).saveSqlScriptParameter(sqlParam);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgSqlParameter对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(sqlScriptParameters, null);
	}
	
	/**
	 * 修改sql脚本参数
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgSqlParameter> sqlScriptParameters = getDataInstanceList(ijson, CfgSqlParameter.class, true);
		analysisResourceProp(sqlScriptParameters, true);
		if(analysisResult == null){
			for (CfgSqlParameter sqlParam : sqlScriptParameters) {
				resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).updateSqlScriptParameter(sqlParam);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgSqlParameter对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(sqlScriptParameters, null);
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
		return getResultObject(null, null);
	}
}
