package com.king.tooth.sys.entity.other;

import com.king.tooth.util.StrUtils;

/**
 * sql资源元数据信息对象
 * @author DougLei
 */
public class SqlResourceMetadataInfo extends AResourceMetadataInfo{

	public SqlResourceMetadataInfo(String propName) {
		super(propName);
	}

	public SqlResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled);
		this.propName = propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
