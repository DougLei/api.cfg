package com.king.tooth.sys.entity.tools.resource.metadatainfo;

import com.king.tooth.util.StrUtils;

/**
 * sql资源元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlResourceMetadataInfo extends ResourceMetadataInfo{

	public SqlResourceMetadataInfo() {
	}
	public SqlResourceMetadataInfo(String propName, String dataType) {
		super(propName);
		this.dataType = dataType;
	}
	public SqlResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid);
		this.propName = propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
