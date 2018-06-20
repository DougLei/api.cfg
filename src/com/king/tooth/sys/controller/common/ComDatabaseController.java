package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.common.ComDatabaseService;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 数据库数据信息资源对象控制器
 * @author DougLei
 */
public class ComDatabaseController extends AbstractPublishController{
	
	private ComDatabaseService databaseService = new ComDatabaseService();
	
	/**
	 * 添加数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public ResponseBody add(HttpServletRequest request, String databaseJson){
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
	public ResponseBody update(@RequestBody String databaseJson){
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
	public ResponseBody delete(HttpServletRequest request){
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
	public ResponseBody linkTest(HttpServletRequest request){
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
	public ResponseBody publish(HttpServletRequest request){
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
	public ResponseBody cancelPublish(HttpServletRequest request){
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
