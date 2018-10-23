package com.king.tooth.sys.entity.tools.resource.metadatainfo.ie;

import com.king.tooth.sys.entity.cfg.CfgPropIEConfExtend;
import com.king.tooth.util.StrUtils;

/**
 * sql资源导入导出的元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IESqlResourceMetadataInfo extends IEResourceMetadataInfo{

	public IESqlResourceMetadataInfo() {
	}
	public IESqlResourceMetadataInfo(String propName) {
		super(propName);
	}
	public IESqlResourceMetadataInfo(String id, String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, CfgPropIEConfExtend ieConfExtend, String propName, String descName) {
		super(id, columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid, ieConfExtend);
		this.propName = propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
