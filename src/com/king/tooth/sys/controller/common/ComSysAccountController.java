package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param accountOnlineStatus
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody login(@RequestBody ComSysAccount account, HttpServletRequest request){
		ComSysAccountOnlineStatus accountOnlineStatus = accountService.modifyAccountOfOnLineStatus(HttpHelperUtil.getClientIp(request), account.getLoginName(), account.getLoginPwd());
		if(accountOnlineStatus.getIsError() == 1){
			return installResponseBody(accountOnlineStatus.getMessage(), null);
		}
		return installResponseBody(null, accountOnlineStatus);
	}
	
	/**
	 * 注册
	 * <p>请求方式：POST</p>
	 * @param accountOnlineStatus
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@org.springframework.web.bind.annotation.ResponseBody
	public ResponseBody register(@RequestBody ComSysAccount account, HttpServletRequest request){
		accountService.register(HttpHelperUtil.getClientIp(request), account.getLoginName(), account.getLoginPwd());
		return installResponseBody("注册成功", null);
	}
}
