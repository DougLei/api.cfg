package com.king.tooth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinParametersKeys;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.ResponseBody;

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
			projectId = "90621e37b806o6fe8538c5eb782901bb";
		}else{
			projectId = TokenRefProjectIdMapping.getProjectId(token);
			if(StrUtils.isEmpty(projectId)){
				printResult("token无效，请先登录", resp, true);
				return;
			}
		}
		
		CurrentThreadContext.setProjectId(projectId);
		try {
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			chain.doFilter(req, resp);
			
			ResponseBody responseBody = (ResponseBody) request.getAttribute(BuiltinParametersKeys._RESPONSE_BODY_KEY);
			processResponseBody(resp, responseBody);
			
			Log4jUtil.debug("请求处理完成");
		} catch (Exception err) {
			String errMsg = ExceptionUtil.getErrMsg(err);
			Log4jUtil.debug("请求处理出现异常，异常信息为:{}", errMsg);
			HibernateUtil.rollbackTransaction();
			printResult(errMsg, resp, false);
		}finally{
			HibernateUtil.closeCurrentThreadSession();
			
			CurrentThreadContext.clearCurrentThreadData();
			
			RequestBody requestBody = (RequestBody) req.getAttribute(BuiltinParametersKeys._REQUEST_BODY_KEY);
			if(requestBody != null){
				requestBody.clear();
			}
		}
	}

	/**
	 * 处理最终的响应体
	 * @param responseBody
	 * @throws IOException 
	 */
	private void processResponseBody(ServletResponse resp, ResponseBody responseBody) throws IOException {
		if(responseBody == null){
			responseBody = new ResponseBody("本次请求处理后的responseBody为空，请联系开发人员", null, false);
		}
		if(responseBody.getIsSuccess()){
			HibernateUtil.commitTransaction();
		}else{
			HibernateUtil.rollbackTransaction();
		}
		printResult(resp, responseBody);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}