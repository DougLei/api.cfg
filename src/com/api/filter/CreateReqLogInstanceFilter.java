package com.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.api.sys.entity.sys.SysReqLog;
import com.api.thread.current.CurrentThreadContext;

/**
 * 创建请求日志实例的过滤器
 */
public class CreateReqLogInstanceFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		SysReqLog reqLog = new SysReqLog((HttpServletRequest)req);
		CurrentThreadContext.getReqLogData().setReqLog(reqLog);
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
