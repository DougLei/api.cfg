package com.king.tooth.sys.service.sys;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.tools.excel.ImportExcel;
import com.king.tooth.sys.service.AService;

/**
 * excel操作service
 * @author DougLei
 */
@Service
public class SysExcelService extends AService{
	
	/**
	 * 导入excel
	 * @param importExcel
	 * @return
	 */
	public Object importExcel(ImportExcel importExcel) {
		if(importExcel.getExcelFileSuffix().equals("xls")){
			return importXLSExcel(importExcel);
		}else if(importExcel.getExcelFileSuffix().equals("xlsx")){
			return importXLSXExcel(importExcel);
		}
		return null;
	}

	/**
	 * 导入xls后缀的excel
	 * @param importExcel
	 * @return
	 */
	private Object importXLSExcel(ImportExcel importExcel) {
		return null;
	}

	/**
	 * 导入xlsx后缀的excel
	 * @param importExcel
	 * @return
	 */
	private Object importXLSXExcel(ImportExcel importExcel) {
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ---------------------------------------------------------------------
	
	/**
	 * 导出excel
	 * @param request
	 * @return
	 */
	public Object exportExcel(HttpServletRequest request) {
		return null;
	}
}
