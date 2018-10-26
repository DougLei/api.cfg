package com.king.tooth.sys.entity.tools.resource.metadatainfo.ie;

import com.king.tooth.sys.entity.cfg.CfgPropExtendConf;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;

/**
 * 资源导入导出的元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IEResourceMetadataInfo extends ResourceMetadataInfo{

	/**
	 * id值
	 */
	protected String id;
	/**
	 * 导入导出的元数据扩展配置
	 */
	protected CfgPropExtendConf ieConfExtend;
	
	public IEResourceMetadataInfo() {
	}
	public IEResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid, String id, CfgPropExtendConf ieConfExtend) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid);
		this.id = id;
		this.ieConfExtend = ieConfExtend;
	}
	public IEResourceMetadataInfo(String propName) {
		super(propName);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CfgPropExtendConf getIeConfExtend() {
		return ieConfExtend;
	}
	public void setIeConfExtend(CfgPropExtendConf ieConfExtend) {
		this.ieConfExtend = ieConfExtend;
	}
}
