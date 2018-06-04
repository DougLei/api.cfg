package com.king.tooth.sys.service.common;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]请求操作日志资源服务处理器
 * @author DougLei
 */
public class ComReqLogService extends AbstractResourceService{

	/**
	 * 初始化一个请求操作日志资源对象实例
	 * @param request
	 * @return
	 */
	public ComReqLog initReqLogInstance(HttpServletRequest request) {
		ComReqLog reqLog = new ComReqLog();
		reqLog.setReqAccountId(request.getHeader("_accountId"));
		reqLog.setReqMethod(request.getMethod());
		reqLog.setReqIp(HttpHelperUtil.getClientIp(request));
		reqLog.setReqMac(request.getHeader("_mac"));
		reqLog.setReqUrl(request.getRequestURI());
		reqLog.setReqUrlParams(JsonUtil.toJsonString(request.getParameterMap(), false));
		reqLog.setReqDate(new Date());
		reqLog.setReqToken(request.getHeader("_token"));
		return reqLog;
	}
}
