package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.common.ComDatabaseService;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 数据库数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComDatabase")
public class ComDatabaseController extends AbstractPublishController{
	
	private ComDatabaseService databaseService = new ComDatabaseService();
	
	/**
	 * 添加数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String add(@RequestBody String databaseJson){
		ComDatabase database = JsonUtil.parseObject(databaseJson, ComDatabase.class);
		String result = database.analysisResourceProp();
		if(result == null){
			result = databaseService.saveDatabase(database);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 修改数据库
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String update(@RequestBody String databaseJson){
		ComDatabase database = JsonUtil.parseObject(databaseJson, ComDatabase.class);
		String result = database.analysisResourceProp();
		if(result == null){
			result = databaseService.updateDatabase(database);
		}
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 删除数据库
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String delete(HttpServletRequest request){
		String databaseId = request.getParameter("databaseId");
		if(StrUtils.isEmpty(databaseId)){
			return installOperResponseBody("要删除的数据库id不能为空", null);
		}
		String result = databaseService.deleteDatabase(databaseId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 测试数据库连接
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/linkTest", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String linkTest(HttpServletRequest request){
		String databaseId = request.getParameter("databaseId");
		if(StrUtils.isEmpty(databaseId)){
			return installOperResponseBody("测试连接的数据库id不能为空", null);
		}
		String result = databaseService.databaseLinkTest(databaseId);
		return installOperResponseBody(result, null);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布数据库
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/publish", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String publish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String databaseId = request.getParameter("databaseId");
		if(StrUtils.isEmpty(databaseId)){
			return installOperResponseBody("要发布的数据库id不能为空", null);
		}
		String result = databaseService.publishDatabase(databaseId);
		return installOperResponseBody(result, null);
	}
	
	/**
	 * 取消发布数据库
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/cancelPublish", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String cancelPublish(HttpServletRequest request){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
			return installOperResponseBody("取消发布功能，目前只提供给一般开发账户使用", null);
		}
		
		String databaseId = request.getParameter("databaseId");
		if(StrUtils.isEmpty(databaseId)){
			return installOperResponseBody("要取消发布的数据库id不能为空", null);
		}
		String result = databaseService.cancelPublishDatabase(databaseId);
		return installOperResponseBody(result, null);
	}
}
