package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本资源对象控制器
 * @author DougLei
 */
public class ComSqlScriptParameterController extends AbstractPublishController{
	
	private ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	
	/**
	 * 添加sql脚本参数
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(json, ComSqlScriptParameter.class);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = sqlScriptService.saveSqlScriptParameter(sqlScriptParameters);
		}
		return getResultObject();
	}
	
	/**
	 * 修改sql脚本参数
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json){
		List<ComSqlScriptParameter> sqlScriptParameters = getDataInstanceList(json, ComSqlScriptParameter.class);
		analysisResourceProp(sqlScriptParameters);
		if(analysisResult == null){
			resultObject = sqlScriptService.updateSqlScriptParameter(sqlScriptParameters);
		}
		return getResultObject();
	}
	
	/**
	 * 删除sql脚本参数
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String sqlScriptParameterIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(sqlScriptParameterIds)){
			return "要删除的sql脚本参数id不能为空";
		}
		resultObject = sqlScriptService.deleteSqlScriptParameter(sqlScriptParameterIds);
		processResultObject(ResourceNameConstants.IDS, sqlScriptParameterIds);
		return getResultObject();
	}
}
