package com.king.tooth.web.entity.request.valid.data.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.busi.model.resource.data.BusiModelResourceData;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.request.valid.data.AbstractResourceVerifier;

/**
 * 业务模型资源的数据校验类
 * @author DougLei
 */
public class BusiModelResourceVerifier extends AbstractResourceVerifier{

	private String busiModelResourceName;
	private CfgBusiModel busiModel;
	
	public BusiModelResourceVerifier(RequestBody requestBody) {
		super(requestBody);
		busiModel = requestBody.getResourceInfo().getBusiModel();
		busiModelResourceName = busiModel.getResourceName();
	}
	
	public String doValid(){
		return doValidBusiModelResourceMetadata();
	}
	
	/**
	 * 验证业务模型资源的元数据
	 * @return
	 */
	private String doValidBusiModelResourceMetadata() {
		if(requestBody.isPostRequest()){
			return validPostBusiModelResourceMetadata();
		}
		return "系统只支持[post]一种请求方式处理业务模型资源";
	}
	
	/**
	 * 验证post请求的业务模型资源数据
	 * @return
	 */
	private String validPostBusiModelResourceMetadata() {
		IJson ijson = requestBody.getFormData();
		validResult = recursiveValidBusiModelData(busiModel.getBusiModelResRelationsList(), ijson, null, 1);
		recursiveClearValidMetadataDatas(busiModel.getBusiModelResRelationsList());
		return validResult;
	}
	
	/**
	 * 递归验证业务资源的数据
	 * @param busiModelResRelationsList
	 * @param ijson
	 * @param recursiveLevel
	 * @return
	 */
	public String recursiveValidBusiModelData(List<CfgBusiModelResRelations> busiModelResRelationsList, IJson ijson, String dataParentId, int recursiveLevel){
		if(busiModelResRelationsList != null && busiModelResRelationsList.size() > 0){
			int busiModelResRelationsListSize = busiModelResRelationsList.size();
			CfgBusiModelResRelations busiModelResRelations = null;
			IJson ijsonData = null;
			int ijsonDataSize = 0;
			JSONObject json = null;
			String refResourceIdPropName = null;
			Object dataParentIdObj = null;
			Object operDataType = null;
			for(int i = 0; i < busiModelResRelationsListSize; i++){
				busiModelResRelations = busiModelResRelationsList.get(i);
				
				if(busiModelResRelationsListSize > 1 && busiModelResRelations.haveSubBusiModelResRelationsList()){
					ijsonData = ijson.getIJson(i);
				}else{
					ijsonData = ijson;
				}
				
				if(ijsonData == null || (ijsonDataSize = ijsonData.size()) == 0){
					return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据不能为空";
				}
				refResourceIdPropName = busiModelResRelations.getRefResourceIdPropName();
				
				// 处理每个对象主键，如果没有就要赋值
				if(busiModelResRelations.getRefTable() != null){
					int[] flag = new int[2];
					for(int j=0;j<ijsonDataSize;j++){
						json = ijsonData.get(j);
						operDataType = json.get(ResourcePropNameConstants.OPER_DATA_TYPE);
						
						if(OperDataTypeConstants.ADD.equals(operDataType)){
							json.put(refResourceIdPropName, ResourceHandlerUtil.getIdentity());
							flag[1] = 2;
						}else if(OperDataTypeConstants.EDIT.equals(operDataType)){
							if(StrUtils.isEmpty(json.get(refResourceIdPropName))){
								return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要修改的，第"+(j+1)+"个数据的"+refResourceIdPropName+"参数值不能为空";
							}
							flag[1] = 2;
						}else if(OperDataTypeConstants.DELETE.equals(operDataType)){
							if(StrUtils.isEmpty(json.get(refResourceIdPropName))){
								return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要删除的，第"+(j+1)+"个数据的"+refResourceIdPropName+"参数值不能为空";
							}
							flag[1] = 2;
						}else if(OperDataTypeConstants.SELECT.equals(operDataType)){
							Log4jUtil.info("不验证查询语句");
							flag[0] = 1;
						}else{
							return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，第"+(j+1)+"个数据的$operDataType$参数值不能为空，且值只能为add/edit/delete/select";
						}
					}
					if(flag[0]==1 && flag[1]==2){
						throw new IllegalArgumentException("在业务模型["+resourceName+"]中，系统目前不支持同时操作[增删改]和[查询]表资源数据");
					}
				}else if(busiModelResRelations.getRefSql() != null){
					for(int j=0;j<ijsonDataSize;j++){
						json = ijsonData.get(j);
						json.remove(ResourcePropNameConstants.OPER_DATA_TYPE);// 尝试移除，因为用不上
						
						if(busiModelResRelations.getRefSql().isInsertSql()){
							json.put(refResourceIdPropName, ResourceHandlerUtil.getIdentity());
						}else if(busiModelResRelations.getRefSql().isUpdateSql()){
							if(StrUtils.isEmpty(json.get(refResourceIdPropName))){
								return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要修改的，第"+(j+1)+"个数据的"+refResourceIdPropName+"参数值不能为空";
							}
						}else if(busiModelResRelations.getRefSql().isDeleteSql()){
							if(StrUtils.isEmpty(json.get(refResourceIdPropName))){
								return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要删除的，第"+(j+1)+"个数据的"+refResourceIdPropName+"参数值不能为空";
							}
						}else if(busiModelResRelations.getRefSql().isSelectSql()){
							Log4jUtil.info("不验证查询语句");
						}else{
							return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，第"+(j+1)+"个数据的$operDataType$参数值不能为空，且值只能为insert/update/delete/select";
						}
					}
				}
				
				if(busiModelResRelations.haveSubBusiModelResRelationsList()){
					for(int j=0;j<ijsonDataSize;j++){
						json = ijsonData.get(j);
						dataParentIdObj = json.get(refResourceIdPropName);
						validResult = recursiveValidBusiModelData(busiModelResRelations.getSubBusiModelResRelationsList(), IJsonUtil.getIJson(json.remove(busiModelResRelations.getRefSubResourceKeyName())), dataParentIdObj==null?null:dataParentIdObj.toString(), recursiveLevel+1);
						if(validResult != null){
							return validResult;
						}
					}
				}
				validResult = busiModelResRelations.validResourceData(new BusiModelResourceData(requestBody.getToken(), requestBody.getRequestURL(), busiModelResourceName, dataParentId, ijsonData));
			}
		}else if(ijson != null && ijson.size() > 0){
			return "业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，不存在任何关联的资源配置，但却传入了数据："+ijson.toString()+"，请检查配置";
		}
		return validResult;
	}
	private String validResult;
	
	/**
	 * 递归清空验证用的元数据信息
	 * @param busiModelResRelationsList
	 */
	private void recursiveClearValidMetadataDatas(List<CfgBusiModelResRelations> busiModelResRelationsList) {
		if(busiModelResRelationsList != null && busiModelResRelationsList.size() > 0){
			for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationsList) {
				recursiveClearValidMetadataDatas(busiModelResRelations.getSubBusiModelResRelationsList());
				busiModelResRelations.clearValidMetadataDatas();
			}
		}
	}
}
