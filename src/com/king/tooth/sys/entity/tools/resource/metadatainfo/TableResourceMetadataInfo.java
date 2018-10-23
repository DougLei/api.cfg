package com.king.tooth.sys.entity.tools.resource.metadatainfo;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.util.StrUtils;

/**
 * 表资源元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class TableResourceMetadataInfo extends ResourceMetadataInfo{

	public TableResourceMetadataInfo() {
	}
	public TableResourceMetadataInfo(String propName) {
		super(propName);
	}
	public TableResourceMetadataInfo(String id, String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, String propName, String descName) {
		super(id, columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid);
		this.propName = propName.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
