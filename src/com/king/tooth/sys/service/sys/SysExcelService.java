package com.king.tooth.sys.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.tools.excel.ImportExcel;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.PoiExcelUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

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
		
		String currentResourceName = null;
		int i, j, batchImportCount, sheetIndex=0, columnIndex=0;
		List<ResourceMetadataInfo> resourceMetadataInfos = null;
		List<JSONObject> jsons = null;
		JSONObject json = null;
		Object value = null;
		
		Sheet sheet = null;// excel sheet对象
		Row row = null;// excel行对象
		Cell cell;// excel列对象
		int rowCount = 0;// 行数
		short columnCount = 0; // 列数
		for (String resourceName : sheetResourceNames) {
			currentResourceName = resourceName;
			sheet = workbook.getSheetAt(sheetIndex);
			rowCount = sheet.getLastRowNum()+1;
			
			if(sheet.getRow(1) != null){
				batchImportCount = importExcel.calcBatchImportCount(rowCount-1);
				resourceMetadataInfos = ResourceHandlerUtil.getResourceMetadataInfos(currentResourceName);
				jsons = new ArrayList<JSONObject>(batchImportCount);
				
				for(i=1;i<rowCount;i++){
					row = sheet.getRow(i);
					if(row != null){
						columnCount = sheet.getRow(i).getLastCellNum();
						if(columnCount > resourceMetadataInfos.size()){
							return "导入excel文件，第"+(sheetIndex+1)+"个sheet中，第"+(i+1)+"行数据的列数量("+columnCount+"个)大于资源["+currentResourceName+"]配置的导入字段数量("+resourceMetadataInfos.size()+"个)，系统无法匹配，请调整配置，或sheet中的列";
						}
						json = new JSONObject(columnCount);
						jsons.add(json);
						
						for (j=0; j<columnCount; j++) {
							cell = row.getCell(j);
							if(cell != null){
								cell.setCellType(Cell.CELL_TYPE_STRING);
								value = resourceMetadataInfos.get(columnIndex).analyzeData(cell.getStringCellValue());
								if(value instanceof String && value.toString().startsWith("error:")){
									return "导入excel文件，第"+(sheetIndex+1)+"个sheet中，第"+(i+1)+"行数据的第"+(j+1)+"列，数据值验证失败，失败原因为：" + value.toString().replace("error:", "");
								}
								json.put(resourceMetadataInfos.get(columnIndex).getPropName(), value);
							}
							columnIndex++;
						}
						columnIndex=0;
					}
					
					if(jsons.size() == batchImportCount && !importExcel.isAllImportByOnce()){
						saveData(currentResourceName, jsons);
					}
				}
				saveData(currentResourceName, jsons);
			}
			sheetIndex++;
		}
		saveData(currentResourceName, jsons);
		return null;
	}
	private void saveData(String resourceName, List<JSONObject> jsons){
		if(jsons.size() > 0){
			for (JSONObject json : jsons) {
				HibernateUtil.saveObject(resourceName, json, null);
			}
			jsons.clear();
		}
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
