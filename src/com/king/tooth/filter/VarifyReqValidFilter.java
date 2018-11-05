package com.king.tooth.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.service.sys.SysAccountOnlineStatusService;
import com.king.tooth.sys.service.sys.SysAccountService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * 验证请求是否有效的过滤器
 * @author DougLei
 */
public class VarifyReqValidFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		String token = request.getHeader("_token");
		
		String varifyResultMessage = varifyRequestIsValid(request, token);
		if(varifyResultMessage != null){
			// 登录验证失败时，尝试移除传递的token和对应项目id的映射缓存
			TokenRefProjectIdMapping.removeMapping(token);
			installFailResponseBody(req, varifyResultMessage);
		}else{
			chain.doFilter(req, resp);
		}
	}
	
	/**
	 * 验证请求是否有效
	 * @param request
	 * @param token
	 * @return
	 * @throws IOException
	 */
	private String varifyRequestIsValid(HttpServletRequest request, String token) throws IOException{
		if(isIgnoreLoginValid(request)){
			return null;
		}
		if(StrUtils.isEmpty(token)){
			return "请先登录";
		}
		
		SysAccountOnlineStatusService accountOnlineStatusService = new SysAccountOnlineStatusService();
		SysAccountOnlineStatus onlineStatus = accountOnlineStatusService.validAccountOfOnLineStatus(request, token);
		if(onlineStatus.getMessage() != null){
			return onlineStatus.getMessage();
		}
		
		SysAccountService accountService = new SysAccountService();
		SysAccount currentAccount = accountService.validAccountOfStatus(onlineStatus.getAccountId());
		if(currentAccount.getMessage() != null){
			return currentAccount.getMessage();
		}
		
		CurrentThreadContext.setCurrentAccountOnlineStatus(onlineStatus);// 记录当前账户在线对象到当前线程中
		
		// 记录配置的项目id，配置系统使用
		CurrentThreadContext.setConfProjectId(onlineStatus.getConfProjectId());
		
		// 修改最后的操作时间
		onlineStatus.setLastOperDate(new Date());
		// 标识，要修改记录最后操作时间等信息
		onlineStatus.setIsUpdate(true);
		return null;
	}

	/**
	 * 可以忽略登录验证的请求url集合
	 */
	private static final String[] ignoreLoginValidUri;
	static{
		ignoreLoginValidUri = SysConfig.getSystemConfig("ignore.loginvalid.uri").split(",");
	}
	/**
	 * 是否需要忽略登录验证
	 * @param request
	 * @return
	 */
	private boolean isIgnoreLoginValid(HttpServletRequest request) {
		for (String ignoreUri : ignoreLoginValidUri) {
			if(request.getRequestURI().endsWith(ignoreUri)){
				return true;
			}
		}
		return false;
	}
	
	public void init(FilterConfig arg0) throws ServletException {
	}
}
