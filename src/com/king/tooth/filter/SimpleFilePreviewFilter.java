package com.king.tooth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.SysContext;
import com.king.tooth.util.FileUtil;

/**
 * (简单)文件预览的过滤器
 * <p>如果是请求文件，例如http://xxx/files/upload/xx.png的路径，这个不需要进行任何数据处理，直接让tomcat返回对应的文件即可</p>
 * @author DougLei
 */
public class SimpleFilePreviewFilter extends AbstractFilter{

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String requestURI = req.getServletPath();
		if(requestURI.startsWith("/files")){// files为上传文件时，保存到服务器中的根目录，即默认保存路径的根目录名称
			String fileSuffix = requestURI.substring(requestURI.lastIndexOf(".")+1);
			if(!FileUtil.isFileFormat(fileSuffix, SysContext.getSystemConfig("web.front.end.file.suffix").split(","))){
				printResult(response, installFailResponseBody(req, "系统目前只支持预览 ["+SysContext.getSystemConfig("web.front.end.file.suffix")+"] 的文件"));
				return;
			}
		}
		chain.doFilter(request, response);
	}
	
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
	public void destroy() {
	}
}
