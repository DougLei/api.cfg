package com.king.tooth.web.entity.request;

/**
 * 资源的数据校验类
 * @author DougLei
 */
public class ResourceDataVerifier {
	
	private RequestBody requestBody;
	
	public ResourceDataVerifier(RequestBody requestBody) {
		this.requestBody = requestBody;
	}

	//------------------------------------------------------------------
	/**
	 * 校验请求资源的数据
	 * @param requestBody
	 */
	public void doValidResourceData() {
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
