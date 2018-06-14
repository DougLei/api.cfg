package com.king.tooth.web.servlet.app;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;

/**
 * 监听数据发布的servlet
 * <p>这个是在运行系统配置的servlet</p>
 * <p>当配置系统将数据成功发布到运行系统后，调用运行系统的该servlet(即接口)，使运行系统去加载刚刚发布过来的数据</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class MonitoringDataPublishServlet extends HttpServlet implements Serializable{

	/**
	 * 调用这个接口时的token验证值
	 * 只有一致了，才能成功调用
	 */
	private static String publishDataToken;
	
	/**
	 * 进行接口处理
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!request.getMethod().equalsIgnoreCase("head")){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口", HttpHelperUtil.getClientIp(request), request.getMethod());
			return;
		}
		String token = request.getParameter("_token");
		if(StrUtils.isEmpty(publishDataToken)){
			publishDataToken = token;
		}
		if(!token.equals(publishDataToken)){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口，发送了错误的token值[{}]，调用接口失败！", HttpHelperUtil.getClientIp(request), request.getMethod(), token);
			return;
		}
		
	}
}
