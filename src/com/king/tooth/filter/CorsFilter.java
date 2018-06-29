package com.king.tooth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.Log4jUtil;

/**
 * 跨域请求过滤器
 * @author DougLei
 */
public class CorsFilter extends AbstractFilter{

	public void init(FilterConfig arg0) throws ServletException {
	}
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		response.setHeader("Access-Control-Allow-Origin", "*");  
		response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");  
		response.setHeader("Access-Control-Max-Age", "3600");  
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, _token"); 
		
		String requestMethod = request.getMethod();
		if("options".equalsIgnoreCase(requestMethod)){
			Log4jUtil.debug("来自ip地址为{}的客户端发起跨域请求", HttpHelperUtil.getClientIp(request));
			return;
		}
		Log4jUtil.debug("来自ip地址为{}的客户端发起的{}请求", HttpHelperUtil.getClientIp(request), requestMethod);
		chain.doFilter(req, resp);
	}
}

/*
	相信大家在写前端脚本的时候经常会遇到发送数据到后台的情况，但是由于浏览器的限制，不同域名之间的数据是不能互相访问的，那前端怎么和后端如何进行数据之间的交换呢？
	JavaScript由于安全性方面的考虑，不允许页面跨域调用其他页面的对象，那么问题来了，什么是跨域问题？
	答：这是由于浏览器同源策略的限制，现在所有支持JavaScript的浏览器都使用了这个策略。那么什么是同源呢？所谓的同源是指三个方面“相同”：
		域名相同
		协议相同
		端口相同
	
	下面就举几个例子来帮助更好的理解同源策略:
	          URL							         说明				       是否允许通信
	------------------------------------------------------------------------------
	http://www.a.com/a.js 
	http://www.a.com/b.js					       同一域名					 允许
	------------------------------------------------------------------------------
	http://www.a.com/a.js 
	http://www.b.com/a.js					      不同域名					不允许
	------------------------------------------------------------------------------
	http://www.a.com:8000/a.js
	http://www.a.com/b.js					同一域名不同端口				不允许
	------------------------------------------------------------------------------
	https://www.a.com/a.js 
	http://www.a.com/b.js					同一域名不同协议				不允许	
	------------------------------------------------------------------------------
	
	通过在拦截器中设置服务器的响应头，表示服务器可以接受浏览器的跨域请求
	response.setHeader("Access-Control-Allow-Origin", "*");  
	response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");  // "GET,POST,PUT,DELETE" 也可以写为 *，表示服务器可以接受所有方法的跨域请求
	response.setHeader("Access-Control-Max-Age", "3600");  
	response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	
	浏览器再接收到该消息后，会自动再发起一次实际的请求，完成跨域请求
 */
