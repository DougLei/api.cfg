package com.king.tooth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.util.Log4jUtil;

/**
 * 记录方法执行时间过滤器，用作性能监控
 * @author DougLei
 */
public class FuncExecTimeFilter extends AbstractFilter{

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		long startTime = System.currentTimeMillis();
		chain.doFilter(request, response);
		
		long execTime = System.currentTimeMillis() - startTime;
		if(execTime > 3000){
			Log4jUtil.error("*********性能提醒：请求路径：[{}]，执行耗时为 {}ms。",req.getRequestURI(), execTime);
		}else{
			Log4jUtil.debug("请求路径：[{}]，执行耗时为 {}ms。",req.getRequestURI(), execTime);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
	
	public void destroy() {
	}
}
