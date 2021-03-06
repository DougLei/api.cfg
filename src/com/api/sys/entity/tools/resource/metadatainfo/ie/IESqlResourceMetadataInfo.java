package com.api.sys.entity.tools.resource.metadatainfo.ie;

import com.api.sys.entity.cfg.CfgPropExtendConf;
import com.api.util.StrUtils;

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
	public IESqlResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, String id, CfgPropExtendConf ieConfExtend, String propName, String descName) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid, id, ieConfExtend);
		this.propName = propName;
		this.descName = StrUtils.isEmpty(descName)?propName:descName;
	}
}
