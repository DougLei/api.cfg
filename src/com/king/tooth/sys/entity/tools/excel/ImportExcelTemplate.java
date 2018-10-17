package com.king.tooth.sys.entity.tools.excel;

import java.io.Serializable;

import com.king.tooth.sys.entity.IEntityPropAnalysis;

/**
 * 导入excel模版文件类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ImportExcelTemplate implements Serializable, IEntityPropAnalysis{

	
	
	
	public String validNotNullProps() {
		return null;
	}

	public String analysisResourceProp() {
		return validNotNullProps();
	}

	public String getEntityName() {
		return "ImportExcelTemplate";
	}
}
