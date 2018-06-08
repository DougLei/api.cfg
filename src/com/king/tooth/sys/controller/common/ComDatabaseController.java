package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.common.ComDatabaseService;
import com.king.tooth.util.StrUtils;
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
		
		return installResponseBody("添加成功", null);
	}
	
	
	
	
	
	/**
	 * 发布数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/deploying/{databaseId}", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody deployingDatabase(@PathVariable String databaseId){
		if(StrUtils.isEmpty(databaseId)){
			return installResponseBody("要部署的数据库id不能为空", null);
		}
		databaseService.deployingDatabase(databaseId);
		return installResponseBody("部署成功", null);
	}
	
	/**
	 * 取消发布数据库库
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/cancelDeploy/{databaseId}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelDeployingDatabase(@PathVariable String databaseId){
		if(StrUtils.isEmpty(databaseId)){
			return installResponseBody("要删除的数据库id不能为空", null);
		}
		databaseService.cancelDeployingDatabase(databaseId);
		return installResponseBody("删除成功", null);
	}
}
