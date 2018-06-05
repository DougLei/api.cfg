package com.king.tooth.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.common.ComSysAccountOnlineStatusService;
import com.king.tooth.sys.service.common.ComSysAccountService;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 验证请求是否有效的过滤器
 * <pre>
 * 	1.登录验证
 * 	2.登录是否有效验证
 * 	3.请求的资源是否有效验证
 * 	4.等等
 * </pre>
 * @author DougLei
 */
public class VarifyReqValidFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		String varifyResultMessage = varifyRequestIsValid((HttpServletRequest)req);
		if(varifyResultMessage != null){
			printResult(varifyResultMessage, resp);
		}else{
			chain.doFilter(req, resp);
		}
	}
	
	/**
	 * 验证请求是否有效
	 * @param req
	 * @return
	 * @throws IOException
	 */
	private String varifyRequestIsValid(HttpServletRequest request) throws IOException{
		if(ResourceHandlerUtil.isLoginRequest(request)){
			return null;
		}
		
		String token = request.getHeader("_token");
		if(StrUtils.isEmpty(token)){
			return "请先登录";
		}
		
		ComSysAccountOnlineStatusService accountOnlineStatusService = new ComSysAccountOnlineStatusService();
		ComSysAccountOnlineStatus onlineStatus = accountOnlineStatusService.validAccountOfOnLineStatus(request, token);
		if(onlineStatus.getMessage() != null){
			return onlineStatus.getMessage();
		}
		
		ComSysAccountService accountService = new ComSysAccountService();
		ComSysAccount currentAccount = accountService.validAccountOfStatus(onlineStatus.getAccountId());
		if(currentAccount.getMessage() != null){
			return currentAccount.getMessage();
		}
		
		CurrentThreadContext.setCurrentAccountOnlineStatus(onlineStatus);// 记录当前账户在线对象到当前线程中
		
		// 修改最后的操作时间
		onlineStatus.setLastOperDate(new Date());
		HibernateUtil.updateObject(onlineStatus, null);
		return null;
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
