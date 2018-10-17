package com.king.tooth.sys.entity.sys.file;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.StrUtils;

/**
 * 导入文件类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ImportFile implements Serializable, IEntityPropAnalysis{

	/**
	 * 要导入的文件id
	 */
	private String fileId;
	/**
	 * 一次批量导入的数量
	 * <p>例如文件中有1000条数据，系统会为了性能，分批次导入，这个参数决定一次导入多少条，默认为300条，如果值为-1，则一次全部导入</p>
	 */
	private int batchImportCount;
	/**
	 * 要导入的资源名
	 */
	private String resourceName;
	
	// --------------------------------------------------
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public void setBatchImportCount(int batchImportCount) {
		this.batchImportCount = batchImportCount;
	}
	public int getBatchImportCount() {
		return batchImportCount;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(fileId)){
			return "要导入的文件id不能为空";
		}
		if(StrUtils.isEmpty(resourceName)){
			return "要导入的资源名不能为空";
		}
		return null;
	}
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ImportFile";
	}
	
	/**
	 * 计算一次批量导入的数据数量
	 * @param rowCount
	 * @return
	 */
	public int calcBatchImportCount(int rowCount){
		if(batchImportCount == 0 || batchImportCount < -1){
			batchImportCount = 300;
		}else if(batchImportCount == -1){
			batchImportCount = rowCount;
		}
		return batchImportCount;
	}
	
	/**
	 * 是否一次性全部导入
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isAllImportByOnce(){
		return batchImportCount == -1;
	}
}
