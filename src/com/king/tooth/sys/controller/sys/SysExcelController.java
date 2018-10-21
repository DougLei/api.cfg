package com.king.tooth.sys.controller.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.SysFileImportExportLog;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.sys.file.ExportFile;
import com.king.tooth.sys.entity.sys.file.ImportFile;
import com.king.tooth.sys.entity.sys.file.ImportFileTemplate;
import com.king.tooth.sys.service.sys.SysExcelService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.thread.operdb.file.ie.log.RecordFileIELogThread;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.PageResultEntity;

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
	private void recordExcelIELogs(List<SysFileImportExportLog> excelIELogs){
		new RecordFileIELogThread(HibernateUtil.openNewSession(),
				excelIELogs,
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(),
				CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId(),
				CurrentThreadContext.getProjectId(),
				CurrentThreadContext.getCustomerId()).start();
	}
	
	/**
	 * 导入excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object importExcel(HttpServletRequest request, IJson ijson){
		List<ImportFile> importFiles = getDataInstanceList(ijson, ImportFile.class, true);
		analysisResourceProp(importFiles);
		List<SysFileImportExportLog> excelIELogs = null;
		if(analysisResult == null){
			excelIELogs = new ArrayList<SysFileImportExportLog>(importFiles.size());
			
			SysFileImportExportLog excelIELog = null;
			for (ImportFile importFile : importFiles) {
				excelIELog = new SysFileImportExportLog(SysFileImportExportLog.IMPORT, importFile.getFileId(), JsonUtil.toJsonString(importFile, false));
				excelIELogs.add(excelIELog);
				
				resultObject = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).importExcel(importFile);
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
		analysisResourceProp(importFileTemplates);
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
	public Object createExportExcelFile(SysResource resource, String exportExcelFileSuffix, String exportTitle, String exportBasicPropNames, PageResultEntity pageResultEntity, Query query, Map<String, String> requestUrlParams){
		ExportFile exportFile = new ExportFile(ResourceHandlerUtil.getIdentity(), resource, exportExcelFileSuffix, exportTitle, exportBasicPropNames, pageResultEntity, query);
		analysisResult = exportFile.analysisResourceProp();
		List<SysFileImportExportLog> excelIELogs = null;
		if(analysisResult == null){
			excelIELogs = new ArrayList<SysFileImportExportLog>(1);
			SysFileImportExportLog excelIELog = new SysFileImportExportLog(SysFileImportExportLog.IMPORT, exportFile.getFileId(), JsonUtil.toJsonString(requestUrlParams, false));
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
