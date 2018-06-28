package com.king.tooth.sys.controller.common;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.plugins.thread.CurrentThreadContext;
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
		ComSysAccountOnlineStatus accountOnlineStatus = accountService.login(HttpHelperUtil.getClientIp(request), account.getLoginName(), account.getLoginPwd());
		if(accountOnlineStatus.getIsError() == 1){
			return installResponseBody(accountOnlineStatus.getMessage(), null);
		}
		
		// 登录成功时，记录token和项目id的关系
		TokenRefProjectIdMapping.setTokenRefProjMapping(accountOnlineStatus.getToken(), CurrentThreadContext.getProjectId());
		return installResponseBody(null, accountOnlineStatus);
	}
	
	/**
	 * 退出
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	public ResponseBody loginOut(HttpServletRequest request, String json){
		String token = request.getHeader("_token");
		String result = accountService.loginOut(token);
		return installResponseBody(result, null);
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
