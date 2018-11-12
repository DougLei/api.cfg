package com.king.tooth.web.entity.request.valid.data.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.busi.model.resource.data.BusiModelResourceData;
import com.king.tooth.util.ResourceHandlerUtil;
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
		return recursiveValidBusiModelData(busiModel.getBusiModelResRelationsList(), ijson, null, 1);
	}
	
	/**
	 * 递归验证业务资源的数据
	 * @param busiModelResRelationsList
	 * @param ijson
	 * @param recursiveLevel
	 * @return
	 */
	public String recursiveValidBusiModelData(List<CfgBusiModelResRelations> busiModelResRelationsList, IJson ijson, String dataParentId, int recursiveLevel){
		int busiModelResRelationsListSize = busiModelResRelationsList.size();
		CfgBusiModelResRelations busiModelResRelations = null;
		IJson ijsonData = null;
		int ijsonDataSize = 0;
		JSONObject json = null;
		Object dataParentIdObj = null;
		for(int i = 0; i < busiModelResRelationsListSize; i++){
			busiModelResRelations = busiModelResRelationsList.get(i);
			ijsonData = ijson.getIJson(i);
			if(ijsonData == null || (ijsonDataSize = ijsonData.size()) == 0){
				return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据不能为空";
			}
			
			if(busiModelResRelations.getSubBusiModelResRelationsList() != null && busiModelResRelations.getSubBusiModelResRelationsList().size() >0){
				for(int j=0;j<ijsonDataSize;j++){
					json = ijsonData.get(j);
					dataParentIdObj = json.get(ResourcePropNameConstants.ID);
					if(StrUtils.isEmpty(dataParentId)){
						dataParentIdObj = ResourceHandlerUtil.getIdentity();
						json.put(ResourcePropNameConstants.ID, dataParentIdObj);
					}
					validResult = recursiveValidBusiModelData(busiModelResRelations.getSubBusiModelResRelationsList(), IJsonUtil.getIJson(json.remove(busiModelResRelations.getRefSubResourceKeyName())), dataParentIdObj.toString(), recursiveLevel+1);
					if(validResult != null){
						return validResult;
					}
				}
			}
			validResult = busiModelResRelations.validResourceData(new BusiModelResourceData(dataParentId, ijsonData));
		}
		return validResult;
	}
	private String validResult;
}
