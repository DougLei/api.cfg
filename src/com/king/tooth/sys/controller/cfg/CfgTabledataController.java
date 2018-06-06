package com.king.tooth.sys.controller.cfg;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.service.cfg.CfgTabledataService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [配置系统]表数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/CfgTabledata")
public class CfgTabledataController extends AbstractResourceController{
	
	private CfgTabledataService tabledataService = new CfgTabledataService();
	
	/**
	 * 添加表
	 * <p>请求方式：POST</p>
	 * @param table
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody addTable(@RequestBody CfgTabledata table){
		tabledataService.saveTable(table);
		return installResponseBody("添加成功", null);
	}
	
	/**
	 * 修改表
	 * <p>请求方式：PUT</p>
	 * @param table
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody updateTable(@RequestBody CfgTabledata table){
		tabledataService.updateTable(table);
		return installResponseBody("修改成功", null);
	}
	

	/**
	 * 删除表
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody deleteTable(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要删除的表id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		tabledataService.deleteTable(tableIdArr);
		return installResponseBody("删除成功", null);
	}
		
	//--------------------------------------------------------
		
	/**
	 * 创建表，即建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/create/{ids}", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody createTableModel(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要创建的表id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		tabledataService.createTableModel(tableIdArr);
		return installResponseBody("创建成功", null);
	}
	
	/**
	 * 删除表，即删模
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/drop/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody dropTableModel(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要删除的表id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		tabledataService.dropTableModel(tableIdArr);
		return installResponseBody("删除成功", null);
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
			return installResponseBody("要发布的表id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		tabledataService.deployingTable(tableIdArr);
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
			return installResponseBody("要取消发布的表id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		tabledataService.cancelDeployingTable(tableIdArr);
		return installResponseBody("取消发布成功", null);
	}
}
