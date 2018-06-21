package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.common.ComSysAccountService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 系统账户资源对象控制器
 * @author DougLei
 */
public class ComSysAccountController extends AbstractController{
	
	private ComSysAccountService accountService = new ComSysAccountService();
	
	/**
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	public ResponseBody login(HttpServletRequest request, String json){
		ComSysAccount account = JsonUtil.parseObject(json, ComSysAccount.class);
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
//	public ResponseBody validAccountName(@RequestBody ComSysAccount account, HttpServletRequest request){
//		String validResult = accountService.validLoginNameIsExists(account.getLoginName());
//		return installResponseBody(validResult, null);
//	}
}
