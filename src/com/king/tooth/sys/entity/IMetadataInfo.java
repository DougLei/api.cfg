package com.king.tooth.sys.entity;

import java.util.List;

/**
 * 元数据信息接口
 * @author DougLei
 */
public interface IMetadataInfo {
	
	/**
	 * 获取元数据信息集合
	 * @return
	 */
	public List<ResourceMetadataInfo> getResourceMetadataInfos();
}
