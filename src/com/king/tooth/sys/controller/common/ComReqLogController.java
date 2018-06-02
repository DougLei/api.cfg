package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]请求日志资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComReqLog")
public class ComReqLogController extends AbstractResourceController{
	
	/**
	 * 查看详细
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/detail/{id}", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody detail(@PathVariable String id){
		return null;
	}
	
	/**
	 * 查询
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody search(@RequestBody ComReqLog reqLog){
		return null;
	}
	
	/**
	 * 查看详细操作日志
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/searchoperdetail", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody searchOperDetail(@RequestBody ComReqLog reqLog){
		return null;
	}
}
