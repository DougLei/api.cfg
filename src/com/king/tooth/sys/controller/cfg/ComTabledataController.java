package com.king.tooth.sys.controller.cfg;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.service.cfg.ComTabledataService;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComTabledata")
public class ComTabledataController extends AbstractPublishController{
	
	private ComTabledataService tabledataService = new ComTabledataService();
	
	/**
	 * 添加表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String add(@RequestBody String tableJson){
		ComTabledata table = JsonUtil.parseObject(tableJson, ComTabledata.class);
		String result = table.analysisResourceProp();
		if(result == null){
			result = tabledataService.saveTable(table);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改表
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String update(@RequestBody String tableJson){
		ComTabledata table = JsonUtil.parseObject(tableJson, ComTabledata.class);
		String result = table.analysisResourceProp();
		if(result == null){
			result = tabledataService.updateTable(table);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除表
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String delete(HttpServletRequest request){
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要删除的表id不能为空", null);
		}
		String projectId = request.getParameter("projectId");
		String result = tabledataService.deleteTable(tableId, projectId);
		return installOperResponseBody(result, null);
	}
	

	/**
	 * 建模
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/buildModel", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String buildModel(HttpServletRequest request){
		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("建模功能目前只提供给平台开发人员使用", null);
		}
		
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要建模的表id不能为空", null);
		}
		String result = tabledataService.buildModel(tableId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 建立项目和表的关联关系
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/addProjTableRelation", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String addProjTableRelation(HttpServletRequest request){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要操作的项目id不能为空", null);
		}
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要操作的表id不能为空", null);
		}
		String result = tabledataService.addProjTableRelation(projectId, tableId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消项目和表的关联关系
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/cancelProjTableRelation", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String cancelProjTableRelation(HttpServletRequest request){
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要操作的项目id不能为空", null);
		}
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要操作的表id不能为空", null);
		}
		String result = tabledataService.cancelProjTableRelation(projectId, tableId);
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布表
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/publish", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String publish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要取消发布的表关联的项目id不能为空", null);
		}
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要发布的表id不能为空", null);
		}
		String result = tabledataService.publishTable(projectId, tableId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布表
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/cancelPublish", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String cancelPublish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String projectId = request.getParameter("projectId");
		if(StrUtils.isEmpty(projectId)){
			return installOperResponseBody("要取消发布的表关联的项目id不能为空", null);
		}
		String tableId = request.getParameter("tableId");
		if(StrUtils.isEmpty(tableId)){
			return installOperResponseBody("要取消发布的表id不能为空", null);
		}
		String result = tabledataService.cancelPublishTable(projectId, tableId);
		return installOperResponseBody(result, null);
	}
}
