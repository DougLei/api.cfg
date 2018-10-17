package com.king.tooth.sys.entity.sys.file;

import java.io.Serializable;
import java.util.Arrays;

import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.StrUtils;

/**
 * 导入文件模版类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ImportFileTemplate implements Serializable, IEntityPropAnalysis{

	/**
	 * 导入模版文件的后缀
	 */
	private String fileSuffix;
	/**
	 * 生成导入模版文件，对应的资源名
	 * <p>即生成哪个资源的导入模版文件</p>
	 */
	private String resourceName;
	
	// --------------------------------------------------
	public String getFileSuffix() {
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(fileSuffix)){
			return "导入模版文件的后缀不能为空";
		}
		if(StrUtils.isEmpty(resourceName)){
			return "生成导入模版文件，对应的资源名不能为空";
		}
		return null;
	}

	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			boolean isSupportFileSuffix = false;
			fileSuffix = fileSuffix.toLowerCase();
			for (String supportFileSuffix : supportFileSuffixArray) {
				if(supportFileSuffix.equals(fileSuffix)){
					isSupportFileSuffix = true;
					break;
				}
			}
			if(!isSupportFileSuffix){
				return "系统不支持后缀为["+fileSuffix+"]的导入模版文件，系统支持的导入模版文件后缀包括：" +Arrays.toString(supportFileSuffixArray);
			}
		}
		return result;
	}
	
	/** 系统目前支持的文件后缀 */
	private static final String[] supportFileSuffixArray = {"xls", "xlsx"};

	public String getEntityName() {
		return "ImportFileTemplate";
	}
}
