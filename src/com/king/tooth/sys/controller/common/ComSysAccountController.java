package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.common.ComSysAccountService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.JsonUtil;

/**
 * 系统账户资源对象控制器
 * @author DougLei
 */
@Scope("prototype")
@Controller
@RequestMapping("/ComSysAccount")
public class ComSysAccountController extends AbstractController{
	
	private ComSysAccountService accountService = new ComSysAccountService();
	
	/**
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param account
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String login(@RequestBody String accountJson, HttpServletRequest request){
		ComSysAccount account = JsonUtil.parseObject(accountJson, ComSysAccount.class);
		ComSysAccountOnlineStatus accountOnlineStatus = accountService.modifyAccountOfOnLineStatus(HttpHelperUtil.getClientIp(request), account.getLoginName(), account.getLoginPwd());
		if(accountOnlineStatus.getIsError() == 1){
			return installResponseBody(accountOnlineStatus.getMessage(), null);
		}
		return installResponseBody(null, accountOnlineStatus);
	}
	
//	/**
//	 * 注册
//	 * <p>请求方式：POST</p>
//	 * @param account
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	@org.springframework.web.bind.annotation.ResponseBody
//	public ResponseBody register(@RequestBody ComSysAccount account, HttpServletRequest request){
//		String registerResult = accountService.register(HttpHelperUtil.getClientIp(request), account);
//		return installResponseBody(registerResult, null);
//	}
//	
//	/**
//	 * 判断账户名是否存在接口
//	 * <p>请求方式：POST</p>
//	 * @param account
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/validAccountName", method = RequestMethod.POST)
//	@org.springframework.web.bind.annotation.ResponseBody
//	public ResponseBody validAccountName(@RequestBody ComSysAccount account, HttpServletRequest request){
//		String validResult = accountService.validLoginNameIsExists(account.getLoginName());
//		return installResponseBody(validResult, null);
//	}
}
