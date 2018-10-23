package com.king.tooth.sys.entity.tools.resource.metadatainfo.ie;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgPropIEConfExtend;
import com.king.tooth.util.StrUtils;

/**
 * 表资源导入导出的元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IETableResourceMetadataInfo extends IEResourceMetadataInfo{

	public IETableResourceMetadataInfo() {
	}
	public IETableResourceMetadataInfo(String propName) {
		super(propName);
	}
	public IETableResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, String id, CfgPropIEConfExtend ieConfExtend, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid, id, ieConfExtend);
		this.propName = propName.equalsIgnoreCase("id")?ResourcePropNameConstants.ID:propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
