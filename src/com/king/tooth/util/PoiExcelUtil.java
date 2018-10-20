package com.king.tooth.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * poi操作excel的工具类
 * @author DougLei
 */
public class PoiExcelUtil {
	
	/**
	 * 获取poi操作excel文件的【读】workbook对象实例
	 * @param excelFilePath excel文件的绝对路径
	 * @param excelFileSuffix excel文件的后缀
	 * @return
	 */
	public static Object getReadWorkBookInstance(String excelFilePath, String excelFileSuffix){
		File file = new File(excelFilePath);
		if(file.exists()){
			if(excelFileSuffix == null || !(excelFileSuffix.equalsIgnoreCase("xls") || excelFileSuffix.equalsIgnoreCase("xlsx"))){
				excelFileSuffix = excelFilePath.substring(excelFilePath.lastIndexOf(".")+1).toLowerCase();
			}
			
			Workbook workbook = null;
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				if("xls".equals(excelFileSuffix)){
					workbook = new HSSFWorkbook(in);
				}else if("xlsx".equals(excelFileSuffix)){
					workbook = WorkbookFactory.create(in);
				}else{
					return "系统不支持后缀为["+excelFileSuffix+"]的excel文件，系统支持的后缀包括：[xls , xlsx]";
				}
			} catch (FileNotFoundException e) {
				Log4jUtil.debug("[PoiExcelUtil.getWorkBookInstance]出现异常:{}", ExceptionUtil.getErrMsg(e));
			} catch (InvalidFormatException e) {
				Log4jUtil.debug("[PoiExcelUtil.getWorkBookInstance]出现异常:{}", ExceptionUtil.getErrMsg(e));
			} catch (IOException e) {
				Log4jUtil.debug("[PoiExcelUtil.getWorkBookInstance]出现异常:{}", ExceptionUtil.getErrMsg(e));
			} finally{
				if(in != null){
					CloseUtil.closeIO(in);
				}
			}
			if(workbook == null){
				return "系统没有根据指定的excel文件，获取操作的WorkBook对象实例，请联系后端系统开发人员";
			}else{
				return workbook;
			}
		}else{
			return "在路径["+excelFilePath+"]下，没有找到要操作的excel文件";
		}
	}
	
	/**
	 * 获取poi操作excel文件的【写】workbook对象实例
	 * @param excelFilePath excel文件的绝对路径
	 * @param excelFileSuffix excel文件的后缀
	 * @return
	 */
	public static Object getWriteWorkBookInstance(String excelFileSuffix){
		Workbook workbook = null;
		if("xls".equals(excelFileSuffix)){
			workbook = new HSSFWorkbook();
		}else if("xlsx".equals(excelFileSuffix)){
			workbook = new XSSFWorkbook();
		}else{
			return "系统不支持后缀为["+excelFileSuffix+"]的excel文件，系统支持的后缀包括：[xls , xlsx]";
		}
		return workbook;
	}
	
	// -----------------------------------------------------------------
	/**
	 * 合并单元格并输入值
	 * @param sheet
	 * @param rowIndex
	 * @param columnIndex
	 * @param cellValue
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public static void mergedCells(Sheet sheet, int rowIndex, int columnIndex, Object cellValue, int firstRow, int lastRow, int firstCol, int lastCol){
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
		Row row = sheet.createRow(rowIndex);
		Cell cell = row.createCell(columnIndex);
		if(cellValue != null){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(cellValue.toString());
		}
	}
}
