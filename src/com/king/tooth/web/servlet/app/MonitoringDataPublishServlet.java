package com.king.tooth.web.servlet.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.sys.service.ComDatabaseService;
import com.king.tooth.sys.service.ComModuleOperationService;
import com.king.tooth.sys.service.ComProjectModuleService;
import com.king.tooth.sys.service.ComProjectService;
import com.king.tooth.sys.service.ComSqlScriptService;
import com.king.tooth.sys.service.ComTabledataService;
import com.king.tooth.util.CloseUtil;
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
public class MonitoringDataPublishServlet extends HttpServlet {

	/**
	 * 调用这个接口时的token验证值
	 * 只有一致了，才能成功调用
	 */
	private static String publishDataToken;
	/**
	 * 配置系统的ip地址
	 */
	private static final String cfgWebSysIp = SysConfig.getSystemConfig("cfg.web.sys.ip");
	
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
		if(!requestMethod.equals("post")){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口", clientIp, requestMethod);
			return;
		}
		String token = request.getParameter("_token");
		if(StrUtils.isEmpty(token)){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口，发送了空的token值[{}]，调用接口失败！", clientIp, requestMethod, token);
			return;
		}
		if(StrUtils.isEmpty(publishDataToken)){
			publishDataToken = token;
		}
		if(!token.equals(publishDataToken)){
			Log4jUtil.info("ip为[{}]的客户端，通过{}方式，请求MonitoringDataPublishServlet接口，发送了错误的token值[{}]，调用接口失败！", clientIp, requestMethod, token);
			return;
		}
		
		String processResultMsg;
		String publishDataId = analysisFormData(request);
		if(StrUtils.isEmpty(publishDataId)){
			processResultMsg = "被发布的数据id不能为空";
		}else{
			String projectId = request.getParameter("projectId");
			if(StrUtils.isEmpty(projectId)){
				processResultMsg = "发布数据所属的projectId不能为空";
			}else{
				String publishDataType = request.getParameter("publishDataType");
				String publishType = request.getParameter("publishType");
				if("db".equals(publishDataType)){
					processResultMsg = new ComDatabaseService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("project".equals(publishDataType)){
					processResultMsg = new ComProjectService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("module".equals(publishDataType)){
					processResultMsg = new ComProjectModuleService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("oper".equals(publishDataType)){
					processResultMsg = new ComModuleOperationService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("table".equals(publishDataType)){
					processResultMsg = new ComTabledataService().processAppPublishData(projectId, publishDataId, publishType);
				}else if("sql".equals(publishDataType)){
					processResultMsg = new ComSqlScriptService().processAppPublishData(projectId, publishDataId, publishType);
				}else{
					processResultMsg = "请传入正确的发布数据类型：[publishDataType = db、project、module、oper、table、sql]";
				}
			}
		}
		printResult(response, processResultMsg);
	}

	/**
	 * 解析请求体
	 * @param request
	 * @return
	 */
	private String analysisFormData(HttpServletRequest request) {
		if(request.getContentLength() <= 0){
			return null;
		}
		StringBuilder formData = new StringBuilder();
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(request.getInputStream(), EncodingConstants.UTF_8);
			br = new BufferedReader(reader);
			String tmp = null;
			while((tmp = br.readLine()) != null){
				formData.append(tmp);
			}
		} catch (UnsupportedEncodingException e) {
			Log4jUtil.debug("[PlatformServlet.analysisBody]方法出现异常信息:{}", e.getMessage());
		} catch (IOException e) {
			Log4jUtil.debug("[PlatformServlet.analysisBody]方法出现异常信息:{}", e.getMessage());
		}finally{
			CloseUtil.closeIO(br);
			CloseUtil.closeIO(reader);
		}
		return formData+"";
	}
	
	/**
	 * 将响应结果打印并返回给客户端
	 * @param request 
	 * @param response
	 * @param responseBody
	 */
	private void printResult(HttpServletResponse response, String result){
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			Log4jUtil.debug("[MonitoringDataPublishServlet.printResult]方法出现异常信息:{}", e.getMessage());
		}finally{
			CloseUtil.closeIO(out);
		}
	}
}
