package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * sql脚本资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSqlScript")
public class ComSqlScriptController extends AbstractResourceController{
	
	private ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	
	/**
	 * 添加sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody String sqlScriptJson){
		ComSqlScript sqlScript = JsonUtil.parseObject(sqlScriptJson, ComSqlScript.class);
		String result = sqlScript.validNotNullProps();
		if(result == null){
			result = sqlScriptService.saveSqlScript(sqlScript);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改sql脚本
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody String sqlScriptJson){
		ComSqlScript sqlScript = JsonUtil.parseObject(sqlScriptJson, ComSqlScript.class);
		String result = sqlScript.validNotNullProps();
		if(result == null){
			result = sqlScriptService.updateSqlScript(sqlScript);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除sql脚本
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(HttpServletRequest request){
		String sqlScriptId = request.getParameter("sqlScriptId");
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要删除的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.deleteSqlScript(sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/addProjSqlScriptRelation", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody addProjSqlScriptRelation(HttpServletRequest request){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要操作的项目id不能为空", null);
		}
		String sqlScriptId = request.getParameter("sqlScriptId");
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要操作的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.addProjSqlScriptRelation(projectId, sqlScriptId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/cancelProjSqlScriptRelation", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelProjSqlScriptRelation(HttpServletRequest request){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要操作的项目id不能为空", null);
		}
		String sqlScriptId = request.getParameter("sqlScriptId");
		if(StrUtils.isEmpty(sqlScriptId)){
			return installOperResponseBody("要操作的sql脚本id不能为空", null);
		}
		String result = sqlScriptService.cancelProjSqlScriptRelation(projectId, sqlScriptId);
		return installOperResponseBody(result, null);
	}
}
