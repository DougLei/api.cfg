package com.king.tooth.sys.entity.other;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.util.StrUtils;

/**
 * 表资源元数据信息对象
 * @author DougLei
 */
public class TableResourceMetadataInfo extends ResourceMetadataInfo{

	public TableResourceMetadataInfo(String propName) {
		super(propName);
	}

	public TableResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled);
		this.propName = propName.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
