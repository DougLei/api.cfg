package com.king.tooth.sys.entity.tools.resource.metadatainfo.ie;

import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;

/**
 * 资源导入导出的元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IEResourceMetadataInfo extends ResourceMetadataInfo{

	public IEResourceMetadataInfo() {
	}
	public IEResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled) {
		super(columnName, dataType, length, precision, isUnique, isNullabled);
	}
	public IEResourceMetadataInfo(String propName) {
		super(propName);
	}
}
