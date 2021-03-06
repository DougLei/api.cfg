package com.api.web.entity.request.valid.data.impl;

import com.api.web.entity.request.RequestBody;
import com.api.web.entity.request.valid.data.AbstractResourceVerifier;

/**
 * 代码资源的数据校验类
 * <p>**代码资源的元数据验证，放到各个代码资源中去自行验证，目前在这里不做统一处理**</p>
 * @author DougLei
 */
public class CodeResourceVerifier extends AbstractResourceVerifier{

	public CodeResourceVerifier(RequestBody requestBody) {
		super(requestBody);
	}
	
	public String doValid(){
		return doValidCodeResourceMetadata();
	}
	
	/**
	 * 验证代码资源的元数据
	 * @return
	 */
	private String doValidCodeResourceMetadata() {
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
