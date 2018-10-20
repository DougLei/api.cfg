package com.king.tooth.sys.entity.sys.file;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 导入文件类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ImportFile extends AIEFile implements Serializable, IEntityPropAnalysis{

	/**
	 * 一次批量导入的数量
	 * <p>例如文件中有1000条数据，系统会为了性能，分批次导入，这个参数决定一次导入多少条，默认为300条，如果值为-1，则一次全部导入</p>
	 */
	private int batchImportCount;
	
	// --------------------------------------------------
	/**
	 * 要导入的文件对象
	 * <p>根据fileId的值获得</p>
	 */
	@JSONField(serialize = false)
	private SysFile importFile;
	
	public SysFile getImportFile() {
		return importFile;
	}
	public void setBatchImportCount(int batchImportCount) {
		this.batchImportCount = batchImportCount;
	}
	public int getBatchImportCount() {
		return batchImportCount;
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
	
	@SuppressWarnings("unchecked")
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			importFile = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysFile.class, "from SysFile where "+ResourcePropNameConstants.ID+"=?", fileId);
			if(importFile == null){
				return "没有查询到id为["+fileId+"]的，要导入的文件信息";
			}
			if(!isSupportFileSuffix(importFile.getSuffix())){
				return "系统不支持后缀为["+importFile.getSuffix()+"]的导入文件，系统支持的导入文件后缀包括：" +Arrays.toString(supportFileSuffixArray);
			}
			
			Object obj = getIEResourceMetadataInfos(null, resourceName, 1);
			if(obj instanceof String){
				return obj.toString();
			}
			resourceMetadataInfos = (List<ResourceMetadataInfo>) obj;
		}
		return result;
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
