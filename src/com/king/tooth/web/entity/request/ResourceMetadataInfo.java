package com.king.tooth.web.entity.request;


/**
 * 请求的资源元数据信息
 * @author DougLei
 */
public class ResourceMetadataInfo {
	
	public ResourceMetadataInfo() {
	}
	public ResourceMetadataInfo(RequestBody requestBody) {
		validResourceMetadata(requestBody);
	}
	
	//------------------------------------------------------------------
	/**
	 * 验证资源的元数据
	 * @param requestBody
	 */
	private void validResourceMetadata(RequestBody requestBody) {
		if(requestBody.getResourceInfo().isTableResource()){
			validTableResourceMetadata(requestBody);
			return;
		}
		if(requestBody.getResourceInfo().isSqlResource()){
			validSqlResourceMetadata(requestBody);
			return;
		}
	}
	
	/**
	 * 验证表资源的元数据
	 * @param requestBody
	 */
	private void validTableResourceMetadata(RequestBody requestBody) {
	}
	
	/**
	 * 验证sql资源的元数据
	 * @param requestBody
	 */
	private void validSqlResourceMetadata(RequestBody requestBody) {
	}
}
