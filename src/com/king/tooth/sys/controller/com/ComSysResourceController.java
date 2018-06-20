package com.king.tooth.sys.controller.com;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 系统资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSysResource")
public class ComSysResourceController extends AbstractController{
	
	/**
	 * 修改是否启用状态
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/modifyIsEnabled", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody modifyIsEnabled(@RequestBody ComSysResource resource){
		return null;
	}
	
	/**
	 * 修改请求资源的方式
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/modifyReqResourceMethod", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody updateEnabled(@RequestBody ComSysResource resource){
		return null;
	}
}
