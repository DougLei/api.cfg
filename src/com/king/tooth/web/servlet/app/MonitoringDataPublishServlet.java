package com.king.tooth.web.servlet.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.service.common.ComDatabaseService;
import com.king.tooth.sys.service.common.ComModuleOperationService;
import com.king.tooth.sys.service.common.ComProjectModuleService;
import com.king.tooth.sys.service.common.ComProjectService;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.sys.service.common.ComTabledataService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.servlet.BasicHttpServlet;

/**
 * 监听数据发布的servlet
 * <p>这个是在运行系统配置的servlet</p>
 * <p>当配置系统将数据成功发布到运行系统后，调用运行系统的该servlet(即接口)，使运行系统去加载刚刚发布过来的数据</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class MonitoringDataPublishServlet extends BasicHttpServlet {

	/**
	 * 调用这个接口时的token验证值
	 * 只有一致了，才能成功调用
	 */
	private static String publishDataToken;
	/**
	 * 配置系统的ip地址
	 */
	private static final String cfgWebSysIp = SysConfig.getSystemConfig("cfg.web.sys.ip");
	private ResponseBody responseBody;
	
	/**
	 * 进行接口处理
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientIp = HttpHelperUtil.getClientIp(request);
		if(!clientIp.equals(cfgWebSysIp)){
			Log4jUtil.info("ip为[{}]的客户端尝试调用MonitoringDataPublishServlet接口", clientIp);
			return;
		}
		String requestMethod = request.getMethod().toLowerCase();
		if(!requestMethod.equals("get")){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口", clientIp, requestMethod);
			return;
		}
		String token = request.getParameter("_token");
		if(StrUtils.isEmpty(publishDataToken)){
			publishDataToken = token;
		}
		if(!token.equals(publishDataToken)){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口，发送了错误的token值[{}]，调用接口失败！", clientIp, requestMethod, token);
			return;
		}
		
		String publishDataId = request.getParameter("publishDataId");
		if(StrUtils.isEmpty(publishDataId)){
			responseBody = new ResponseBody("被发布的数据id不能为空", null);
		}else{
			String projectId = request.getParameter("projectId");
			if(StrUtils.isEmpty(projectId)){
				responseBody = new ResponseBody("发布数据所属的projectId不能为空", null);
			}else{
				String publishDataType = request.getParameter("publishDataType");
				String publishType = request.getParameter("publishType");
				
				String str;
				if("db".equals(publishDataType)){
					str = new ComDatabaseService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("project".equals(publishDataType)){
					str = new ComProjectService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("module".equals(publishDataType)){
					str = new ComProjectModuleService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("oper".equals(publishDataType)){
					str = new ComModuleOperationService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("table".equals(publishDataType)){
					str = new ComTabledataService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("sql".equals(publishDataType)){
					str = new ComSqlScriptService().processAppPublishData(projectId, publishDataId, publishType);
				}else{
					str = "请传入正确的发布数据类型：[publishDataType = db、project、module、oper、table、sql]";
				}
				
				String processResultMsg;
				if("success".equals(str)){
					processResultMsg = str;
				}else{
					processResultMsg = "error";
				}
				responseBody = new ResponseBody(processResultMsg, str);
			}
		}
		printResult(response, responseBody);
	}

	protected void analysisRequestBody(HttpServletRequest request) {
		throw new IllegalArgumentException("该servlet目前不支持analysisRequestBody功能");
	}
}
