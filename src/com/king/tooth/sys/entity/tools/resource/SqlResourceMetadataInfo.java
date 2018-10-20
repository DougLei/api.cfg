package com.king.tooth.sys.entity.tools.resource;

import com.king.tooth.util.StrUtils;

/**
 * sql资源元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlResourceMetadataInfo extends ResourceMetadataInfo{

	public SqlResourceMetadataInfo(String propName) {
		super(propName);
	}

	public SqlResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isImport, Integer isExport, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isImport, isExport);
		this.propName = propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
