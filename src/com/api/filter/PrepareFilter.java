package com.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.api.cache.TokenRefProjectIdMapping;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.ExceptionUtil;
import com.api.util.Log4jUtil;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;
import com.api.web.entity.request.RequestBody;
import com.api.web.entity.resulttype.ResponseBody;

/**
 * 预处理的过滤器
 * <pre>
 * 	这里主要是针对dataSource，hibernate的sessionFactory和session
 * 	给系统提供统一的session管理
 * 	由该过滤器获得session对象，最后也由它关闭
 * </pre>
 * @author DougLei
 */
public class PrepareFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String token = request.getHeader("_token");
		String projectId;
		if(StrUtils.isEmpty(token)){
			// TODO 这里暂时写成固定值，这个是配置系统的项目id
			projectId = "90621e37b806o6fe8538c5eb782901bb";
		}else{
			projectId = TokenRefProjectIdMapping.getProjectId(token);
			if(StrUtils.isEmpty(projectId)){
				printResult(resp, new ResponseBody("token无效，请先登录", null));
				return;
			}
		}
		
		CurrentThreadContext.setProjectId(projectId);
		// TODO customerId暂时写成 unknow
		CurrentThreadContext.setCustomerId("unknow");
		
		ResponseBody responseBody = null;
		// 默认是要打印responseBody的
		request.setAttribute(BuiltinParameterKeys._IS_PRINT_RESPONSEBODY, true);
		try {
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			chain.doFilter(req, resp);
			
			responseBody = (ResponseBody) request.getAttribute(BuiltinParameterKeys._RESPONSE_BODY_KEY);
			if(responseBody == null){
				responseBody = new ResponseBody("本次请求处理后的responseBody为空，请联系开发人员", null);
			}
			
			if(responseBody.getIsSuccess() || 
					(CurrentThreadContext.getCurrentAccountOnlineStatus() != null && CurrentThreadContext.getCurrentAccountOnlineStatus().getIsDoLogin())){// 如果是处理成功，或是登陆操作，都要提交事务
				HibernateUtil.commitTransaction();
			}else{
				HibernateUtil.rollbackTransaction();
			}
			
			Boolean isPrintResponseBody = (Boolean) request.getAttribute(BuiltinParameterKeys._IS_PRINT_RESPONSEBODY);
			if(isPrintResponseBody){
				printResult(resp, responseBody);
			}
		} catch (Exception err) {
			String errMsg = request.getAttribute(BuiltinParameterKeys._CLIENT_IP) + "-" + ExceptionUtil.getErrMsg(err);
			Log4jUtil.error("请求[{}]：处理出现异常，异常信息为: {}", request.getRequestURI(), errMsg);
			HibernateUtil.rollbackTransaction();
			responseBody = new ResponseBody(errMsg, null);
			printResult(resp, responseBody);
		}finally{
			// 关闭连接
			HibernateUtil.closeCurrentThreadSession();
			
			// 如果存在请求体，也清空
			RequestBody requestBody = (RequestBody) req.getAttribute(BuiltinParameterKeys._REQUEST_BODY_KEY);
			if(requestBody != null){
				requestBody.clear();
			}
			
			// 记录日志，get请求返回的是查询的结果集合，量比较大，这里就不记录到日志中
			if(!"get".equals(CurrentThreadContext.getReqLogData().getReqLog().getMethod())){
				CurrentThreadContext.getReqLogData().getReqLog().setRespData(responseBody.toStrings());
			}
			CurrentThreadContext.updateDatas();
		}
	}
	
	public void init(FilterConfig arg0) throws ServletException {
	}
}