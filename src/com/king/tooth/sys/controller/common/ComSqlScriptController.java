package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]sql脚本资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSqlScript")
public class ComSqlScriptController extends AbstractResourceController{
	
	private ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	
	/**
	 * 添加sql脚本
	 * @param column
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody addSqlScript(@RequestBody ComSqlScript sqlScript){
		sqlScriptService.saveSqlScript(sqlScript);
		return installResponseBody("添加成功", null);
	}
	
	/**
	 * 修改sql脚本
	 * @param column
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody updateSqlScript(@RequestBody ComSqlScript sqlScript){
		sqlScriptService.updateSqlScript(sqlScript);
		return installResponseBody("修改成功", null);
	}
	
	/**
	 * 删除sql脚本模型
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody deleteSqlScript(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要删除的sql脚本id不能为空", null);
		}
		String[] sqlScriptIdArr = ids.split(",");
		sqlScriptService.deleteSqlScript(sqlScriptIdArr);
		return installResponseBody("删除成功", null);
	}
	
	//--------------------------------------------------------
	
	/**
	 * 执行sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/exec/{id}", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody execSqlScript(@PathVariable String id){
		ComSqlScript sqlScript = sqlScriptService.findSqlScriptResourceById(id);
		if(sqlScript.getSqlScriptType().equals(SqlStatementType.PROCEDURE)){
			HibernateUtil.executeUpdateBySql("执行sql语句", sqlScript.getSqlScriptContent(), null);
		}
		return installResponseBody("执行成功", null);
	}
	
	//--------------------------------------------------------
	
	/**
	 * 发布表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/deploying/{ids}", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody deployingTable(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要发布的sql脚本id不能为空", null);
		}
		String[] sqlScriptIdArr = ids.split(",");
		sqlScriptService.deployingSqlScript(sqlScriptIdArr);
		return installResponseBody("发布成功", null);
	}
	
	/**
	 * 取消发布表
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/cancelDeploy/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody cancelDeployingTable(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要取消发布的sql脚本id不能为空", null);
		}
		String[] sqlScriptIdArr = ids.split(",");
		sqlScriptService.cancelDeployingSqlScript(sqlScriptIdArr);
		return installResponseBody("取消发布成功", null);
	}
}
