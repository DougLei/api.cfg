package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本资源对象控制器
 * @author DougLei
 */
public class ComSqlScriptParameterController extends AbstractPublishController{
	
	/**
	 * 添加sql脚本参数
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(ijson, ComSqlScriptParameter.class);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = BuiltinInstance.sqlScriptService.saveSqlScriptParameter(sqlScriptParameters);
		}
		return getResultObject();
	}
	
	/**
	 * 修改sql脚本参数
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(ijson, ComSqlScriptParameter.class);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = BuiltinInstance.sqlScriptService.updateSqlScriptParameter(sqlScriptParameters);
		}
		return getResultObject();
	}
	
	/**
	 * 删除sql脚本参数
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, IJson ijson){
		String sqlScriptParameterIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(sqlScriptParameterIds)){
			return "要删除的sql脚本参数id不能为空";
		}
		resultObject = BuiltinInstance.sqlScriptService.deleteSqlScriptParameter(sqlScriptParameterIds);
		processResultObject(ResourceNameConstants.IDS, sqlScriptParameterIds);
		return getResultObject();
	}
}
