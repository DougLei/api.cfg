package com.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.api.cache.SysContext;
import com.api.constants.EncodingConstants;
import com.api.web.entity.resulttype.ResponseBody;
import com.douglei.mini.license.client.ValidationResult;

/**
 * 编码过滤器
 * @author DougLei
 */
public class EncodingFilter extends AbstractFilter{

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		resp.setContentType("application/json;charset=" + EncodingConstants.UTF_8);
		
		ValidationResult result = SysContext.licenseValidator.getResult();
		if(result == null){
			chain.doFilter(req, resp);
		}else{
			ResponseBody responseBody = new ResponseBody();
			responseBody.setMessage(result.getMessage());
			printResult(resp, responseBody);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
