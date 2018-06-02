package com.king.tooth.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.service.common.ComReqLogService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 记录请求日志的过滤器
 */
public class RecordReqLogFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		ComReqLogService reqLogService = new ComReqLogService();
		ComReqLog reqLog = reqLogService.initReqLogInstance((HttpServletRequest)req);
		req.setAttribute(ResourceNameConstants.REQ_LOG_KEY, reqLog);
		chain.doFilter(req, resp);
		reqLog.setRespDate(new Date());
		HibernateUtil.saveObject(reqLog, reqLog.getReqIp() + ":日志记录");
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
