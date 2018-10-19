package com.king.tooth.sys.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.SysFileImportExportLog;
import com.king.tooth.sys.entity.sys.file.ImportFile;
import com.king.tooth.sys.entity.sys.file.ImportFileTemplate;
import com.king.tooth.sys.service.sys.SysExcelService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.thread.operdb.file.ie.log.RecordFileIELogThread;
import com.king.tooth.util.JsonUtil;
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
					excelIELog.recordResult(resultObject.toString(), 0);
					break;
				}
				excelIELog.recordResult(null, 1);
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
	 * @return
	 */
	public Object createExportExcelFile(String resourceName, String exportExcelFileSuffix, PageResultEntity pageResultEntity, Query query){
		return getResultObject(null, null);
	}
}
