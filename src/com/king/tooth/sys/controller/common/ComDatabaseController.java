package com.king.tooth.sys.controller.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [配置系统]数据库数据信息资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComDatabase")
public class ComDatabaseController extends AbstractResourceController{
	
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
	public ResponseBody search(@RequestBody ComDatabase database){
		return null;
	}
	
	/**
	 * 新增
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComDatabase database){
		return null;
	}
	
	/**
	 * 修改
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComDatabase database){
		return null;
	}
	
	/**
	 * 删除
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete/{ids}", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(@RequestBody ComDatabase database, @PathVariable String ids){
		return null;
	}
	
	/**
	 * 创建库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/create", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody createDatabase(@RequestBody ComDatabase database){
		return null;
	}
	
	/**
	 * 删除库
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/drop", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody dropDatabase(@RequestBody ComDatabase database){
		return null;
	}
}