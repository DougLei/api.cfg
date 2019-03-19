package com.api.sys.controller.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.constants.ResourceInfoConstants;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.cfg.CfgResource;
import com.api.sys.entity.sys.SysFileIELog;
import com.api.sys.entity.sys.file.ie.ExportFile;
import com.api.sys.entity.sys.file.ie.ImportFile;
import com.api.sys.entity.sys.file.ie.ImportFileTemplate;
import com.api.sys.service.sys.SysExcelService;
import com.api.thread.current.CurrentThreadContext;
import com.api.thread.operdb.file.ie.log.RecordFileIELogThread;
import com.api.thread.pool.ThreadPool;
import com.api.util.JsonUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;
import com.api.web.entity.resulttype.PageResultEntity;

/**
 * excel操作Controller
 * @author DougLei
 */
@Controller
public class SysExcelController extends AController{

	/**
	 * 记录excel导入导出日志数据
	 * @param excelIELogs
	 */
	private void recordExcelIELogs(List<SysFileIELog> excelIELogs){
		ThreadPool.execute(new RecordFileIELogThread(HibernateUtil.openNewSession(),
				excelIELogs,
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
				CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
				CurrentThreadContext.getProjectId(),
				CurrentThreadContext.getCustomerId()));
	}
	
	/**
	 * 导入excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object importExcel(HttpServletRequest request, IJson ijson){
		List<ImportFile> importFiles = getDataInstanceList(ijson, ImportFile.class, true);
		analysisResourceProp(importFiles, false);
		List<SysFileIELog> excelIELogs = null;
		if(analysisResult == null){
			excelIELogs = new ArrayList<SysFileIELog>(importFiles.size());
			
			SysFileIELog excelIELog = null;
			String importResourceName = importFiles.get(0).getResourceName();
			if(StrUtils.isEmpty(importResourceName)){
				analysisResult = "导入excel时，调用的资源名不能为空";
			}
			for (ImportFile importFile : importFiles) {
				if(StrUtils.isEmpty(importFile.getResourceName())){
					importFile.setResourceName(importResourceName);
				}
				excelIELog = new SysFileIELog(ResourceInfoConstants.FILE_IMPORT, importFile.getFileId(), JsonUtil.toJsonString(importFile, false));
				excelIELogs.add(excelIELog);
				resultObject = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).importExcel(request, importFile);
				if(resultObject instanceof String){
					excelIELog.recordResult(resultObject.toString());
					break;
				}
				excelIELog.recordResult(null);
				resultJsonArray.add(resultObject);
			}
		}
		recordExcelIELogs(excelIELogs);
		return getResultObject(importFiles, null);
	}
	
	/**
	 * 生成excel导入模版
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object createImportExcelTemplate(HttpServletRequest request, IJson ijson){
		List<ImportFileTemplate> importFileTemplates = getDataInstanceList(ijson, ImportFileTemplate.class, true);
		analysisResourceProp(importFileTemplates, false);
		if(analysisResult == null){
			for (ImportFileTemplate importFileTemplate : importFileTemplates) {
				resultObject = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).createImportExcelTemplate(importFileTemplate);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(importFileTemplates, null);
	}
	
	/**
	 * 生成导出excel文件
	 * @param resource
	 * @param exportExcelFileSuffix
	 * @param exportTitle
	 * @param exportBasicPropNames
	 * @param pageResultEntity
	 * @param query
	 * @param requestUrlParams
	 * @return
	 */
	public Object createExportExcelFile(CfgResource resource, String exportExcelFileSuffix, String exportTitle, String exportBasicPropNames, PageResultEntity pageResultEntity, Query query, Map<String, String> requestUrlParams){
		ExportFile exportFile = new ExportFile(ResourceHandlerUtil.getIdentity(), resource, exportExcelFileSuffix, exportTitle, exportBasicPropNames, pageResultEntity, query);
		analysisResult = exportFile.analysisResourceProp();
		List<SysFileIELog> excelIELogs = null;
		if(analysisResult == null){
			excelIELogs = new ArrayList<SysFileIELog>(1);
			SysFileIELog excelIELog = new SysFileIELog(ResourceInfoConstants.FILE_IMPORT, exportFile.getFileId(), JsonUtil.toJsonString(requestUrlParams, false));
			excelIELogs.add(excelIELog);
			
			resultObject = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).createExportExcelFile(exportFile);
			if(resultObject instanceof String){
				excelIELog.recordResult(resultObject.toString());
			}else{
				excelIELog.recordResult(null);
			}
		}
		recordExcelIELogs(excelIELogs);
		return getResultObject(null, null);
	}
}
