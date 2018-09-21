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
	 * @return
	 */
	public String doValidResourceData() {
		if(requestBody.getResourceInfo().isTableResource()){
			return validTableResourceMetadata(requestBody);
		}
		if(requestBody.getResourceInfo().isSqlResource()){
			return validSqlResourceMetadata(requestBody);
		}
		return null;
	}
	
	// ------------------------------------------------------------------------------------------------
	/**
	 * 验证表资源的元数据
	 * @param requestBody
	 * @return
	 */
	private String validTableResourceMetadata(RequestBody requestBody) {
		if(requestBody.isGetRequest()){
			return null;
		}else if(requestBody.isPostRequest()){
			return null;
			
		}else if(requestBody.isPutRequest()){
			return null;
			
		}else if(requestBody.isDeleteRequest()){
			return null;
			
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
	
	// ------------------------------------------------------------------------------------------------
	/**
	 * 验证sql资源的元数据
	 * @param requestBody
	 * @return
	 */
	private String validSqlResourceMetadata(RequestBody requestBody) {
		if(requestBody.isGetRequest()){
			return null;
		}else if(requestBody.isPostRequest()){
			return null;
			
		}else if(requestBody.isPutRequest()){
			return null;
			
		}else if(requestBody.isDeleteRequest()){
			return null;
			
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
}
