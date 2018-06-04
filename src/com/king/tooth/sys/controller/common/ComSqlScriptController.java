package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]sql脚本资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSqlScript")
public class ComSqlScriptController extends AbstractResourceController{
	
	/**
	 * 查询
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody detail(@RequestBody ComSqlScript sqlScript){
		return null;
	}
	
	/**
	 * 新增
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComSqlScript sqlScript){
		return null;
	}
	
	/**
	 * 修改
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComSqlScript sqlScript){
		return null;
	}
	
	/**
	 * 删除
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(@RequestBody ComSqlScript sqlScript, @PathVariable String ids){
		return null;
	}
	
	/**
	 * 执行sql脚本
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/exec", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody execSqlScript(@RequestBody ComSqlScript sqlScript){
		return null;
	}
}
