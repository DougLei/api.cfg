package com.king.tooth.web.entity.request.valid.data;

import com.king.tooth.web.entity.request.RequestBody;

/**
 * 资源的数据校验类
 * @author DougLei
 */
public class ResourceDataVerifier {
	
	private AbstractResourceVerifier resourceVerifier;
	
	//------------------------------------------------------------------
	public void clear(){
		if(resourceVerifier != null){
			resourceVerifier.clear();
		}
	}
	
	/**
	 * 校验请求资源的数据
	 * @param requestBody
	 * @return
	 */
	public String doValidResourceData(RequestBody requestBody) {
		if(requestBody.getResourceInfo().isTableResource()){
			resourceVerifier = new TableResourceVerifier(requestBody, requestBody.getRouteBody().getResourceName(), requestBody.getRouteBody().getParentResourceName());
		}else if(requestBody.getResourceInfo().isSqlResource()){
			resourceVerifier = new SqlResourceVerifier(requestBody, requestBody.getRouteBody().getResourceName(), requestBody.getRouteBody().getParentResourceName());
		}else if(requestBody.getResourceInfo().isCodeResource()){
			resourceVerifier = new CodeResourceVerifier(requestBody, requestBody.getRouteBody().getResourceName(), requestBody.getRouteBody().getParentResourceName());
		}else{
			return "系统目前只存在[表、sql脚本、代码]三种资源类型，本次请求的资源类型为["+requestBody.getResourceInfo().getResourceType()+"]，请联系后台系统开发人员";
		}
		return resourceVerifier.doValid();
	}
}
