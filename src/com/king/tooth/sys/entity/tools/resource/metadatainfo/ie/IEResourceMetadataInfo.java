package com.king.tooth.sys.entity.tools.resource.metadatainfo.ie;

import com.king.tooth.sys.entity.cfg.CfgPropIEConfExtend;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;

/**
 * 资源导入导出的元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IEResourceMetadataInfo extends ResourceMetadataInfo{

	/**
	 * 导入导出的元数据扩展配置
	 */
	protected CfgPropIEConfExtend ieConfExtend;
	
	public IEResourceMetadataInfo() {
	}
	public IEResourceMetadataInfo(String id, String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, CfgPropIEConfExtend ieConfExtend) {
		super(id, columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid);
		this.ieConfExtend = ieConfExtend;
	}
	public IEResourceMetadataInfo(String propName) {
		super(propName);
	}
	
	public CfgPropIEConfExtend getIeConfExtend() {
		return ieConfExtend;
	}
	public void setIeConfExtend(CfgPropIEConfExtend ieConfExtend) {
		this.ieConfExtend = ieConfExtend;
	}
}
