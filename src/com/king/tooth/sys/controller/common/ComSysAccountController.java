package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.king.tooth.sys.controller.AbstractResourceController;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.common.ComSysAccountService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * [通用的]系统账户资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSysAccount")
public class ComSysAccountController extends AbstractResourceController{
	
	private ComSysAccountService accountService = new ComSysAccountService();
	
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
	public ResponseBody search(@RequestBody ComSysAccount account){
		return null;
	}
	
	/**
	 * 新增
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody add(@RequestBody ComSysAccount account){
		return null;
	}
	
	/**
	 * 修改
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/update", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody update(@RequestBody ComSysAccount account){
		return null;
	}
	
	/**
	 * 删除
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody delete(@RequestBody ComSysAccount account){
		return null;
	}
	
	/**
	 * 修改密码
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping(value="/editpwd", method = RequestMethod.PUT)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody editPwd(@RequestBody ComSysAccount account){
		return null;
	}
	
	/**
	 * 分配角色
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping(value="/grantrole", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody grantRole(@RequestBody ComSysAccount account){
		return null;
	}
	
	
	/**
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param accountOnlineStatus
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody toLogin(@RequestBody ComSysAccount account, HttpServletRequest request){
		ComSysAccountOnlineStatus accountOnlineStatus = accountService.modifyAccountOfOnLineStatus(HttpHelperUtil.getClientIp(request), account.getLoginName(), account.getLoginPwd());
		if(accountOnlineStatus.getIsError() == 1){
			return installResponseBody(accountOnlineStatus.getMessage(), null);
		}
		return installResponseBody(null, accountOnlineStatus);
	}
}
