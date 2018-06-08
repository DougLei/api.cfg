package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.common.ComDatabaseService;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 数据库数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComDatabase")
public class ComDatabaseController extends AbstractResourceController{
	
	private ComDatabaseService databaseService = new ComDatabaseService();
	
	/**
	 * 添加数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComDatabase database){
		database.analysisResourceProp();
		databaseService.saveDatabase(database);
		return installResponseBody("添加成功", null);
	}
	
	/**
	 * 修改数据库
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComDatabase database){
		database.analysisResourceProp();
		databaseService.updateDatabase(database);
		return installResponseBody("修改成功", null);
	}
	
}
