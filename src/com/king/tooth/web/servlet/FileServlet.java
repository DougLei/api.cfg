package com.king.tooth.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.service.sys.SysFileService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.FileUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 操作文件servlet
 * @author DougLei
 */
@SuppressWarnings("serial")
public class FileServlet extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.EXCEL);// 标识日志的类型为excel
		
		ResponseBody responseBody = null;
		if(FileUtil.saveToService){
			String[] uri = request.getRequestURI().split("/");
			if(uri.length == 4){
				String method = request.getMethod().toLowerCase();
				
				Object result = null;
				
				// 下载
				if("get".equals(method) && "download".equals(uri[3])){
					result = BuiltinResourceInstance.getInstance("SysFileService", SysFileService.class).download(request, response);
					if(result instanceof String){// 如果下载出现问题，则最后需要打印responseBody
						response.setHeader("Content-Type", "application/json;charset="+ EncodingConstants.UTF_8);
						request.setAttribute(BuiltinParameterKeys._IS_PRINT_RESPONSEBODY, true);
					}
				}
				// 上传
				else if("post".equals(method) && "upload".equals(uri[3])){
					result = BuiltinResourceInstance.getInstance("SysFileService", SysFileService.class).upload(request);
				}
				// 删除
				else if("delete".equals(method) && "delete".equals(uri[3])){
					result = BuiltinResourceInstance.getInstance("SysFileService", SysFileService.class).delete(request);
				}
				// 暂不支持
				else{
					result = "操作文件接口，目前还不支持处理["+method+"]方式["+uri[3]+"]的请求";
				}
				
				if(result == null){
					responseBody = new ResponseBody("操作文件结果信息为null，请联系系统开发人员");
				}else if(result instanceof String){
					responseBody = new ResponseBody(result.toString());
				}else{
					responseBody = new ResponseBody(null, result, true);
				}
			}else{
				responseBody = new ResponseBody("请求操作文件的api路径格式错误，请检查：[/file/upload][/file/download][/file/delete]");
			}
		}else{
			responseBody = new ResponseBody("目前系统还不支持["+FileUtil.saveType+"]方式的保存文件，请联系后端系统开发人员");
		}
		request.setAttribute(BuiltinParameterKeys._RESPONSE_BODY_KEY, responseBody);
	}
}
