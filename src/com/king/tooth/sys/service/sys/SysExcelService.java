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
		
		return null;
	}

	// ---------------------------------------------------------------------
	
	/**
	 * 导出excel
	 * @param request
	 * @return
	 */
	public Object exportExcel(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}
