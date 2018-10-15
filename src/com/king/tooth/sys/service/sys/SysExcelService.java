package com.king.tooth.sys.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.tools.excel.ImportExcel;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.PoiExcelUtil;

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
		Object wb = PoiExcelUtil.getWorkBookInstance(importExcel.getExcelFilePath(), importExcel.getExcelFileSuffix());
		if(wb instanceof String){
			return wb;
		}
		Workbook workbook = (Workbook) wb;
		String[] sheetResourceNames = importExcel.getSheetResourceNames();
		
		int i, j, sheetIndex = 0, columnIndex = 0;
		List<Map<String, Object>> objMaps = null;
		
		Sheet sheet = null;// excel sheet对象
		Row row = null;// excel行对象
		Cell cell;// excel列对象
		int rowCount = 0;// 行数
		short columnCount = 0; // 列数
		for (String resourceName : sheetResourceNames) {
			sheet = workbook.getSheetAt(sheetIndex);
			rowCount = sheet.getLastRowNum()+1;
			
			
			for(i=0;i<rowCount;i++){
				row = sheet.getRow(i);
				if(row != null){
					
					
					if(i==0 && objMaps == null){
						objMaps = new ArrayList<Map<String, Object>>(rowCount);
					}
					
					
					
					
					
					
					
					columnCount = row.getLastCellNum();
					for (j=0; j<columnCount; j++) {
						cell = row.getCell(j);
						if(cell != null){
							
						}
					}
				}
			}
			sheetIndex++;
		}
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
