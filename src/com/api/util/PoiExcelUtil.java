package com.api.util;

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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
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
	 * 计算下标值
	 * <p>如果下标小于0，则修改值为0</p>
	 * @param index
	 * @return
	 */
	public static int calcIndex(int index){
		return index<0?0:index;
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
	 * <p>xlsx的单元格有效性为 $K$1 形式，xls的单元格有效性为 K1 形式，所以有valueCellStart、valueCellStartIndex、valueCellEnd、valueCellEndIndex这四个参数</p>
	 * @param fileSuffix
	 * @param sheet
	 * @param firstRow 如果小于0，则默认为0，即第一行
	 * @param lastRow 如果小于0，则默认为0，即第一行
	 * @param firstCol 如果小于0，则默认为0，即第一列
	 * @param lastCol 如果小于0，则默认为0，即第一列
	 * @param valueCellStart
	 * @param valueCellStartIndex
	 * @param valueCellEnd
	 * @param valueCellEndIndex
	 */
	public static void setDataValidation(String fileSuffix, Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, String valueCellStart, int valueCellStartIndex, String valueCellEnd, int valueCellEndIndex){
		if(firstRow < 0){
			firstRow = 0;
		}
		if(lastRow < 0){
			lastRow = 0;
		}
		if(firstCol < 0){
			firstCol = 0;
		}
		if(lastCol < 0){
			lastCol = 0;
		}
		
		CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);// 指定哪些单元格有数据有效性
		DataValidation dataValidation = null;
		if("xls".equals(fileSuffix)){
			DVConstraint constraint = DVConstraint.createFormulaListConstraint(valueCellStart+valueCellStartIndex+":"+valueCellEnd+valueCellEndIndex);
			dataValidation = new HSSFDataValidation(cellRangeAddressList, constraint);
		}else if("xlsx".equals(fileSuffix)){
			XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("$"+valueCellStart+"$"+valueCellStartIndex+":"+"$"+valueCellEnd+"$"+valueCellEndIndex);
			dataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, cellRangeAddressList);
		}else{
			throw new IllegalArgumentException("系统不支持后缀为["+fileSuffix+"]的excel文件，系统支持的后缀包括：[xls , xlsx]");
		}
		sheet.addValidationData(dataValidation);
	}
	
	// -----------------------------------------------------------------
	/**
	 * 设置标题单元格的样式
	 * @param workbook
	 * @param sheet
	 * @param titleCell
	 * @param mergeFirstRow 合并单元格，开始的行位置，从0开始
	 * @param mergeLastRow 合并单元格，结束的行位置，从0开始
	 * @param mergeFirstCol 合并单元格，开始的列位置，从0开始
	 * @param mergeLastCol 合并单元格，结束的行位置，从0开始
	 * @return
	 */
	public static Cell setTitleCellStyle(Workbook workbook, Sheet sheet, Cell titleCell, int mergeFirstRow, int mergeLastRow, int mergeFirstCol, int mergeLastCol){
		initTitleCellStyle(workbook);
		if(titleCell == null){
			throw new NullPointerException("要添加样式的标题单元格对象[titleCell]不能为空");
		}
		titleCell.setCellStyle(titleCellStyle);
		
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(calcIndex(mergeFirstRow), calcIndex(mergeLastRow), calcIndex(mergeFirstCol), calcIndex(mergeLastCol)));
		return titleCell;
	}
	/**
	 * 初始化标题单元格样式
	 * @param workbook
	 */
	private static void initTitleCellStyle(Workbook workbook) {
		if(titleCellStyle == null){
			titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
			titleCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
		}
		if(titleCellFont == null){
			titleCellFont = workbook.createFont();
			titleCellFont.setFontName("宋体");// 设置字体书法
			titleCellFont.setBoldweight(Font.BOLDWEIGHT_BOLD);// 设置字体是否加粗
			titleCellFont.setFontHeightInPoints((short)20);// 设置字体大小
			
			titleCellStyle.setFont(titleCellFont);// 将字体添加到样式中
		}
	}
	/** 标题单元格的样式 */
	private static CellStyle titleCellStyle;
	/** 标题单元格的字体 */
	private static Font titleCellFont;
	
	// ----------
	/**
	 * 设置头单元格的样式
	 * @param workbook
	 * @param headCell
	 * @return
	 */
	public static Cell setHeadCellStyle(Workbook workbook, Cell headCell){
		initHeadCellStyle(workbook);
		if(headCell == null){
			throw new NullPointerException("要添加样式的头单元格对象[headCell]不能为空");
		}
		headCell.setCellStyle(headCellStyle);
		return headCell;
	}
	/**
	 * 初始化头单元格样式
	 * @param workbook
	 */
	private static void initHeadCellStyle(Workbook workbook) {
		if(headCellStyle == null){
			headCellStyle = workbook.createCellStyle();
			headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
			headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
			
			headCellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
			headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);// 下边框
			headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
			headCellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		}
		if(headCellFont == null){
			headCellFont = workbook.createFont();
			headCellFont.setFontName("宋体");// 设置字体书法
			headCellFont.setBoldweight(Font.BOLDWEIGHT_BOLD);// 设置字体是否加粗
			headCellFont.setFontHeightInPoints((short)10);// 设置字体大小
			
			headCellStyle.setFont(headCellFont);// 将字体添加到样式中
		}
	}
	/** 头单元格的样式 */
	private static CellStyle headCellStyle;
	/** 头单元格的字体 */
	private static Font headCellFont;
	
	// ----------
	/**
	 * 设置值单元格的样式
	 * @param workbook
	 * @param valueCell
	 * @return
	 */
	public static Cell setValueCellStyle(Workbook workbook, Cell valueCell){
		initValueCellStyle(workbook);
		if(valueCell == null){
			throw new NullPointerException("要添加样式的值单元格对象[valueCell]不能为空");
		}
		valueCell.setCellStyle(valueCellStyle);
		return valueCell;
	}
	/**
	 * 初始化值单元格样式
	 * @param workbook
	 */
	private static void initValueCellStyle(Workbook workbook) {
		if(valueCellStyle == null){
			valueCellStyle = workbook.createCellStyle();
			
			valueCellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
			valueCellStyle.setBorderBottom(CellStyle.BORDER_THIN);// 下边框
			valueCellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
			valueCellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		}
		if(valueCellFont == null){
			valueCellFont = workbook.createFont();
			valueCellFont.setFontName("宋体");// 设置字体书法
			valueCellFont.setFontHeightInPoints((short)10);// 设置字体大小
			
			valueCellStyle.setFont(valueCellFont);// 将字体添加到样式中
		}
	}
	/** 值单元格的样式 */
	private static CellStyle valueCellStyle;
	/** 值单元格的字体 */
	private static Font valueCellFont;
}
