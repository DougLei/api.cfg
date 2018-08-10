package com.king.tooth.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
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
		ResponseBody responseBody = null;
		if(StrUtils.isEmpty(token)){
			// TODO 这里暂时写成固定值，这个是配置系统的项目id
			projectId = "90621e37b806o6fe8538c5eb782901bb";
		}else{
			projectId = TokenRefProjectIdMapping.getProjectId(token);
			if(StrUtils.isEmpty(projectId)){
				responseBody = new ResponseBody("token无效，请先登录");
				printResult(resp, responseBody);
				return;
			}
		}
		
		CurrentThreadContext.setProjectId(projectId);
		// TODO customerId写成 unknow
		CurrentThreadContext.setCustomerId("unknow");
		
		// 默认是要打印responseBody的
		request.setAttribute(BuiltinParameterKeys._IS_PRINT_RESPONSEBODY, true);
		try {
			
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			chain.doFilter(req, resp);
			
			responseBody = (ResponseBody) request.getAttribute(BuiltinParameterKeys._RESPONSE_BODY_KEY);
			if(responseBody == null){
				responseBody = new ResponseBody("本次请求处理后的responseBody为空，请联系开发人员");
			}
			
			if(responseBody.getIsSuccess()){
				HibernateUtil.commitTransaction();
			}else{
				HibernateUtil.rollbackTransaction();
			}
			
			Boolean isPrintResponseBody = (Boolean) request.getAttribute(BuiltinParameterKeys._IS_PRINT_RESPONSEBODY);
			if(isPrintResponseBody){
				printResult(resp, responseBody);
			}
		} catch (Exception err) {
			String errMsg = ExceptionUtil.getErrMsg("PrepareFilter", "doFilter", err);
			Log4jUtil.debug("请求处理出现异常，异常信息为:{}", errMsg);
			HibernateUtil.rollbackTransaction();
			responseBody = new ResponseBody(errMsg);
			printResult(resp, responseBody);
		}finally{
			// 关闭连接
			HibernateUtil.closeCurrentThreadSession();
			
			// 如果存在请求体，也清空
			RequestBody requestBody = (RequestBody) req.getAttribute(BuiltinParameterKeys._REQUEST_BODY_KEY);
			if(requestBody != null){
				requestBody.clear();
			}
			
			// 记录日志
			if(!"get".equals(CurrentThreadContext.getReqLogData().getReqLog().getMethod())){
				CurrentThreadContext.getReqLogData().getReqLog().setRespData(responseBody.toStrings());
			}
			CurrentThreadContext.getReqLogData().getReqLog().setRespDate(new Date());
			CurrentThreadContext.getReqLogData().recordLogs();
			
			// 清除本次请求的线程数据
			CurrentThreadContext.clearCurrentThreadData();
		}
	}
	
	/**
	 * 打印结果
	 * @param resp
	 * @param responseBody
	 * @throws IOException 
	 */
	private void printResult(ServletResponse resp, ResponseBody responseBody) throws IOException {
		PrintWriter out = resp.getWriter();
		out.write(responseBody.toStrings());
		CloseUtil.closeIO(out);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}