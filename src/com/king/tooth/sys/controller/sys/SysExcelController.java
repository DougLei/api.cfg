package com.king.tooth.sys.controller.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.SysFileIELog;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.sys.file.ie.ExportFile;
import com.king.tooth.sys.entity.sys.file.ie.ImportFile;
import com.king.tooth.sys.entity.sys.file.ie.ImportFileTemplate;
import com.king.tooth.sys.service.sys.SysExcelService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.thread.operdb.file.ie.log.RecordFileIELogThread;
import com.king.tooth.thread.pool.ThreadPool;
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
			for (ImportFile importFile : importFiles) {
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
	public Object createExportExcelFile(SysResource resource, String exportExcelFileSuffix, String exportTitle, String exportBasicPropNames, PageResultEntity pageResultEntity, Query query, Map<String, String> requestUrlParams){
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
