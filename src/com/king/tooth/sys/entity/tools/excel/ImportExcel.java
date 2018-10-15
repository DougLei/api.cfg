package com.king.tooth.sys.entity.tools.excel;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.StrUtils;

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
	/**
	 * 一次批量导入的数量
	 * <p>例如excel中有1000条数据，系统会为了性能，分批次导入，这个参数决定一次导入多少条，默认为200条，如果值为-1，则一次全部导入</p>
	 */
	private int batchImportCount;
	/**
	 * 每个sheet映射的资源名
	 * <p>按照excel中sheet从左到右的顺序，依次对应要导入的资源名</p>
	 */
	private String[] sheetResourceNames;
	
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
	public void setBatchImportCount(int batchImportCount) {
		this.batchImportCount = batchImportCount;
	}
	public int getBatchImportCount() {
		return batchImportCount;
	}
	public String[] getSheetResourceNames() {
		return sheetResourceNames;
	}
	public void setSheetResourceNames(String[] sheetResourceNames) {
		this.sheetResourceNames = sheetResourceNames;
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(excelFilePath)){
			return "要导入的excel文件路径不能为空";
		}
		if(StrUtils.isEmpty(excelFileSuffix)){
			return "要导入的excel文件后缀不能为空";
		}
		if(!"xls".equals(excelFileSuffix) && !"xlsx".equals(excelFileSuffix)){
			return "系统目前只支持导入后缀为[xls]或[xlsx]的excel文件";
		}
		if(sheetResourceNames == null || sheetResourceNames.length == 0){
			return "要导入的excel文件中，配置的每个sheet对应的resourceName不能为空";
		}
		
		int index = 1;
		for (String resourceName : sheetResourceNames) {
			if(StrUtils.isEmpty(resourceName)){
				return "sheetResourceNames数组中，第"+index+"个值不能为空";
			}
			index++;
		}
		return null;
	}
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ImportExcel1";
	}
	
	/**
	 * 计算一次批量导入的数据数量
	 * @param rowCount
	 * @return
	 */
	public int calcBatchImportCount(int rowCount){
		if(batchImportCount == 0 || batchImportCount < -1){
			return 200;
		}else if(batchImportCount == -1){
			return rowCount;
		}else{
			return batchImportCount;
		}
	}
	
	/**
	 * 是否一次性全部导入
	 * @return
	 */
	public boolean isAllImportByOnce(){
		return batchImportCount == -1;
	}
}
