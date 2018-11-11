package com.king.tooth.web.entity.request.valid.data.impl;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.util.StrUtils;
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
		
		// 获取当前层级，配置的关系资源可为空的数量
		int isNullableCount = 0;
		for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationsList) {
			if(busiModelResRelations.getIsNullabled() == 1){
				isNullableCount++;
			}
		}
		
		// 判断传入的数据数量，和配置的数量是否一致
		int actualJsonDataSize = ijson.size();
		int confJsonDataSize = busiModelResRelationsList.size() - isNullableCount;
		if(actualJsonDataSize < confJsonDataSize){
			return "业务模型["+resourceName+"]中，关联的第"+(recursiveLevel+1)+"级资源数量["+actualJsonDataSize+"个]，与传入的资源数量["+confJsonDataSize+"]不匹配，请检查";
		}
		
		JSONObject jsonObject = null;
		String dataRefResourceName = null;
		for(int i=0;i<actualJsonDataSize;i++){
			jsonObject = ijson.get(i);
			if(StrUtils.isEmpty(jsonObject.get(ResourcePropNameConstants.DATA_REF_RESOURCENAME))){
				dataRefResourceName = busiModelResRelationsList.get(i).getRefResourceName();
			}else{
				dataRefResourceName = jsonObject.getString(ResourcePropNameConstants.DATA_REF_RESOURCENAME);
			}
			
			
			
		}
		
		
		return null;
	}
	/** 操作递归的层级 */
	private int recursiveLevel;
}
