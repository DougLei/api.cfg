package com.king.tooth.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * poi操作excel的工具类
 * @author DougLei
 */
public class PoiExcelUtil {
	
	/**
	 * 获取poi操作excel文件的workbook对象实例
	 * @param excelFilePath excel文件的绝对路径
	 * @param excelFileSuffix excel文件的后缀
	 * @return
	 */
	public static final Object getWorkBookInstance(String excelFilePath, String excelFileSuffix){
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
}
