package com.king.tooth.web.entity.request;

import java.util.List;

/**
 * 请求的资源元数据信息
 * @author DougLei
 */
public class ResourceMetadataInfo {
	
	/**
	 * 资源的元数据集合
	 */
	private List<ResourceMetadataEntity> resourceMetadataList;
	
	/**
	 * 资源的元数据集合
	 */
	private List<ResourceMetadataEntity> parentResourceMetadataList;
	
	/**
	 * 子资源的元数据集合
	 * <p>_subResourceName指定的资源</p>
	 */
	private List<ResourceMetadataEntity> subResourceMetadataList;
	
	//------------------------------------------------------------------
	public ResourceMetadataInfo() {
	}
	public ResourceMetadataInfo(RequestBody requestBody) {
		analysisResourceMetadata(requestBody);
	}
	
	//------------------------------------------------------------------
	/**
	 * 解析资源的元数据
	 * @param requestBody
	 */
	private void analysisResourceMetadata(RequestBody requestBody) {
		if(requestBody.getResourceInfo().isTableResource()){
		}
	}
	
	//------------------------------------------------------------------
	public List<ResourceMetadataEntity> getResourceMetadataList() {
		return resourceMetadataList;
	}
	public List<ResourceMetadataEntity> getParentResourceMetadataList() {
		return parentResourceMetadataList;
	}
	public List<ResourceMetadataEntity> getSubResourceMetadataList() {
		return subResourceMetadataList;
	}
}
