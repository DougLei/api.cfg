package com.king.tooth.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.file.ImportFile;
import com.king.tooth.sys.entity.sys.file.ImportFileTemplate;
import com.king.tooth.sys.service.sys.SysExcelService;

/**
 * excel操作Controller
 * @author DougLei
 */
@Controller
public class SysExcelController extends AController{

	/**
	 * 导入excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object importExcel(HttpServletRequest request, IJson ijson){
		List<ImportFile> importFiles = getDataInstanceList(ijson, ImportFile.class, true);
		analysisResourceProp(importFiles);
		if(analysisResult == null){
			for (ImportFile importFile : importFiles) {
				resultObject = BuiltinResourceInstance.getInstance("SysExcelService", SysExcelService.class).importExcel(importFile);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
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
	 * 导出excel
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object exportExcel(HttpServletRequest request, IJson ijson){
		return getResultObject(null, null);
	}
}
