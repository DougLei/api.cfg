package com.api.sys.entity.tools.resource.metadatainfo;

import com.api.constants.ResourcePropNameConstants;
import com.api.util.StrUtils;

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
	public TableResourceMetadataInfo(String propName, String dataType) {
		super(propName);
		this.dataType = dataType;
	}
	public TableResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid);
		this.propName = propName.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
