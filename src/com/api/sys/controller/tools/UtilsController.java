package com.api.sys.controller.tools;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.sys.controller.AController;
import com.api.util.HttpHelperUtil;

/**
 * 系统工具类的Controller
 * @author DougLei
 */
@Controller
public class UtilsController extends AController{
	
	/**
	 * 获取客户端ip
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@RequestMapping
	public Object getClientIp(HttpServletRequest request){
		Map<String, String> clientIpMap = new HashMap<String, String>(1);
		clientIpMap.put("clientIp", HttpHelperUtil.getClientIp(request));
		return clientIpMap;
	}
}
