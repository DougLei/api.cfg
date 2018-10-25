package com.king.tooth.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

	/**
	 * 获取单元格对应的列字母
	 * <p>例如，第一列为A，第二列为B...</p>
	 * @param cellIndex
	 * @return
	 */
	public static String getColumnCharWordByIndex(int cellIndex) {
		if(cellIndex < 1){
			throw new IllegalArgumentException("excel的列下标值不能小于1");
		}
		if(cellIndex > maxColumnIndex){
			throw new IllegalArgumentException("excel的列下标值不能大于" + maxColumnIndex);
		}
		if(cellIndex < 27){
			return words[cellIndex];
		}
		String result = words[cellIndex/26];
		int last = cellIndex%26;
		if(last != 0){
			result += words[last];
		}
		return result;
	}
	private static final int maxColumnIndex = 26*9+22;// excel最大的列数，到IV
	private static final String[] words = {null,"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};// 26个应为字母
	
	/**
	 * 设置单元格的数据有效性
	 * @param fileSuffix
	 * @param sheet
	 * @param firstRow
	 * @param lastRow 如果小于1，则默认为10
	 * @param firstCol
	 * @param lastCol
	 * @param valueCellStart
	 * @param valueCellEnd
	 */
	public static void setDataValidation(String fileSuffix, Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, String valueCellStart, String valueCellEnd){
		if(lastRow < 1){
			lastRow = 10;
		}
		
		if("xls".equals(fileSuffix)){
			DVConstraint constraint = DVConstraint.createFormulaListConstraint(valueCellStart+":"+valueCellEnd);
			CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);// 指定哪些单元格有数据有效性
			DataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, constraint);
			sheet.addValidationData(dataValidation);
		}else if("xlsx".equals(fileSuffix)){
			XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(valueCellStart+":"+valueCellEnd);
			
			 // TODO 研究怎么和xls一样!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			DataValidation dataValidation = null;
			CellRangeAddressList cellRangeAddressList = null;
			for(int i=firstRow;i<=lastRow;i++){
				cellRangeAddressList = new CellRangeAddressList(i, i, firstCol, lastCol);// 指定哪些单元格有数据有效性
				dataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, cellRangeAddressList);
				sheet.addValidationData(dataValidation);
			}
		}else{
			throw new IllegalArgumentException("系统不支持后缀为["+fileSuffix+"]的excel文件，系统支持的后缀包括：[xls , xlsx]");
		}
	}
}
