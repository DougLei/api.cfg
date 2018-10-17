package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.util.StrUtils;

/**
 * sql脚本信息表Controller
 * @author DougLei
 */
@Controller
public class CfgSqlController extends AController{
	
	/**
	 * 添加sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComSqlScript> sqlScripts = getDataInstanceList(ijson, ComSqlScript.class, true);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			for (ComSqlScript sqlScript : sqlScripts) {
				resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).saveSqlScript(sqlScript);
				sqlScript.clear();
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(sqlScripts, OperDataTypeConstants.ADD);
	}
	
	/**
	 * 修改sql脚本
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComSqlScript> sqlScripts = getDataInstanceList(ijson, ComSqlScript.class, true);
		analysisResourceProp(sqlScripts);
		if(analysisResult == null){
			for (ComSqlScript sqlScript : sqlScripts) {
				resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).updateSqlScript(sqlScript);
				sqlScript.clear();
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(sqlScripts, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 删除sql脚本
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String sqlScriptIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(sqlScriptIds)){
			return "要删除的sql脚本id不能为空";
		}

		String[] sqlScriptIdArr = sqlScriptIds.split(",");
		for (String sqlScriptId : sqlScriptIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).deleteSqlScript(sqlScriptId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, sqlScriptIds);
		return getResultObject(null, null);
	}
	
	/**
	 * 创建sql脚本对象
	 * <p>存储过程、视图等</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object createSqlObject(HttpServletRequest request, IJson ijson){
		vaildIJsonNotNull(ijson);
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).createSqlObject(ijson);
		if(resultObject == null){
			int size = ijson.size();
			ijsonIsArray = ijson.isArray();
			resultJsonArray = new JSONArray(size);
			for (int i = 0; i < size; i++) {
				resultJsonArray.add(ijson.get(i));
			}
		}
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 删除sql脚本对象
	 * <p>存储过程、视图等</p>
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object dropSqlObject(HttpServletRequest request, IJson ijson){
		vaildIJsonNotNull(ijson);
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).dropSqlObject(ijson);
		if(resultObject == null){
			int size = ijson.size();
			ijsonIsArray = ijson.isArray();
			resultJsonArray = new JSONArray(size);
			for (int i = 0; i < size; i++) {
				resultJsonArray.add(ijson.get(i));
			}
		}
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object addProjSqlScriptRelation(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).addProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject(null, null);
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object cancelProjSqlScriptRelation(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的sql脚本id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).cancelProjSqlScriptRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject(null, null);
	}
}
