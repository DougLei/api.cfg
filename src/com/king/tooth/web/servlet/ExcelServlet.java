package com.king.tooth.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.service.sys.SysExcelService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 导入导出Excel的servlet
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ExcelServlet extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.FILE);// 标识日志的类型为文件
		ResponseBody responseBody = null;
		
		String operDesc = "导入、导出";
		String method = request.getMethod().toLowerCase();
		if("post".equals(method)){
			String[] uri = request.getRequestURI().split("/");
			if(uri.length == 4){
				Object result = null;
				
				// 导入
				if("import".equals(uri[3])){
					operDesc = "导入";
					result = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).importExcel(request);
				}
				// 导出
				else if("export".equals(uri[3])){
					operDesc = "导出";
					result = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).exportExcel(request);
				}
				
				if(result == null){
					responseBody = new ResponseBody(operDesc+"excel的操作结果信息为null，请联系后端系统开发人员");
				}else if(result instanceof String){
					responseBody = new ResponseBody(result.toString());
				}else{
					responseBody = new ResponseBody(null, result, true);
				}
			}else{
				responseBody = new ResponseBody(operDesc + "excel的api路径格式错误，请检查：[/excel/import][/excel/export]");
			}
		}else{
			responseBody = new ResponseBody(operDesc + "excel的api只支持post请求");
		}
		request.setAttribute(BuiltinParameterKeys._RESPONSE_BODY_KEY, responseBody);
	}
}
