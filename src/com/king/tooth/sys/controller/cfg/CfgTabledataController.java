package com.king.tooth.sys.controller.cfg;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
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
	
	private CfgTabledataService cfgTabledataService = new CfgTabledataService();
	
	/**
	 * 创建表，即建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/create/{ids}", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody createTabledataModel(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要创建的数据模型id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		cfgTabledataService.createTabledataModel(tableIdArr);
		return installResponseBody("建模成功", null);
	}
	
	/**
	 * 删除表，即删模
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/drop/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody dropTabledataModel(@PathVariable String ids){
		if(StrUtils.isEmpty(ids)){
			return installResponseBody("要删除的数据模型id不能为空", null);
		}
		String[] tableIdArr = ids.split(",");
		cfgTabledataService.dropTabledataModel(tableIdArr);
		return installResponseBody("删除成功", null);
	}
}
