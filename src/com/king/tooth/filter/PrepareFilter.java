package com.king.tooth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 预处理的过滤器
 * <pre>
 * 	给系统提供统一的session管理
 * 	由该过滤器获得session对象，最后也由它关闭
 * </pre>
 * @author DougLei
 */
public class PrepareFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		try {
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			chain.doFilter(req, resp);
			HibernateUtil.commitTransaction();
			Log4jUtil.debug("请求处理完成");
		} catch (Exception err) {
			Log4jUtil.debug("请求处理出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(err));
			HibernateUtil.rollbackTransaction();
			printResult(ExceptionUtil.getErrMsg(err), resp);
			err.printStackTrace();
		}finally{
			HibernateUtil.closeCurrentThreadSession();
			CurrentThreadContext.clearCurrentThreadData();
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}