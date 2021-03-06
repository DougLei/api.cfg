package com.api.web.entity.request.valid.data;

import com.api.sys.entity.cfg.CfgResource;
import com.api.web.entity.request.RequestBody;
import com.api.web.entity.request.valid.data.impl.BusiModelResourceVerifier;
import com.api.web.entity.request.valid.data.impl.CodeResourceVerifier;
import com.api.web.entity.request.valid.data.impl.SqlResourceVerifier;
import com.api.web.entity.request.valid.data.impl.TableResourceVerifier;

/**
 * 资源的数据校验类
 * @author DougLei
 */
public class ResourceDataVerifier {
	
	private AbstractResourceVerifier resourceVerifier;
	
	//------------------------------------------------------------------
	public void clearValidData(){
		if(resourceVerifier != null){
			resourceVerifier.clearValidData();
		}
	}
	
	/**
	 * 校验请求资源的数据
	 * @param requestBody
	 * @return
	 */
	public String doValidResourceData(RequestBody requestBody) {
		CfgResource resource = requestBody.getResourceInfo().getReqResource();
		if(resource.isTableResource()){
			resourceVerifier = new TableResourceVerifier(requestBody);
		}else if(resource.isSqlResource()){
			resourceVerifier = new SqlResourceVerifier(requestBody);
		}else if(resource.isCodeResource()){
			resourceVerifier = new CodeResourceVerifier(requestBody);
		}else if(resource.isBusinessModelResource()){
			resourceVerifier = new BusiModelResourceVerifier(requestBody);
		}else{
			return "系统目前只存在[表、sql脚本、代码、业务模型]四种资源类型，本次请求的资源类型为["+requestBody.getResourceInfo().getResourceType()+"]，请联系后台系统开发人员";
		}
		return resourceVerifier.doValid();
	}
}
