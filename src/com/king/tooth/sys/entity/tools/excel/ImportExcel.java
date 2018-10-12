package com.king.tooth.sys.entity.tools.excel;

import java.io.Serializable;

import com.king.tooth.sys.entity.IEntityPropAnalysis;

/**
 * 导入excel类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ImportExcel implements Serializable, IEntityPropAnalysis{

	/**
	 * excel文件的存储路径
	 */
	private String excelFilePath;
	/**
	 * excel文件后缀
	 */
	private String excelFileSuffix;
	
	public String getExcelFilePath() {
		return excelFilePath;
	}
	public void setExcelFilePath(String excelFilePath) {
		this.excelFilePath = excelFilePath;
	}
	public String getExcelFileSuffix() {
		return excelFileSuffix;
	}
	public void setExcelFileSuffix(String excelFileSuffix) {
		this.excelFileSuffix = excelFileSuffix;
	}
	
	public String validNotNullProps() {
		return null;
	}
	public String analysisResourceProp() {
		return null;
	}
}
