package com.king.tooth.web.entity.request.valid.data.impl;

import java.util.List;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.busi.model.resource.data.BusiModelResourceData;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.request.valid.data.AbstractResourceVerifier;

/**
 * 业务模型资源的数据校验类
 * @author DougLei
 */
public class BusiModelResourceVerifier extends AbstractResourceVerifier{

	private CfgBusiModel busiModel;
	
	public BusiModelResourceVerifier(RequestBody requestBody) {
		super(requestBody);
		busiModel = requestBody.getResourceInfo().getBusiModel();
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
			return validPostBusiModelResourceMetadata();
		}else if(requestBody.isPutRequest()){
			return null;
		}else if(requestBody.isDeleteRequest()){
			return null;
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
	
	/**
	 * 验证post请求的业务模型资源数据
	 * @return
	 */
	private String validPostBusiModelResourceMetadata() {
		IJson ijson = requestBody.getFormData();
		List<CfgBusiModelResRelations> busiModelResRelationsList = busiModel.getBusiModelResRelationsList();
		
		// 递归的层级
		int recursiveLevel = 1;
		
		int size = ijson.size();
		IJson ijsonData = null;
		CfgBusiModelResRelations busiModelResRelations = null;
		int ijsonDataSize = 0;
		for(int i=0;i<size;i++){
			ijsonData = ijson.getIJson(i);
			busiModelResRelations = busiModelResRelationsList.get(i);
			if((ijsonData == null || ijsonData.size() == 0)){
				continue;
			}
			
			ijsonDataSize = ijsonData.size();
			for(int j=0;j<ijsonDataSize;j++){
				
			}
		}
		return null;
	}
}
