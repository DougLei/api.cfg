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
	private String validResult;
	
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
		if(requestBody.isGetRequest()){
			return validGetBusiModelResourceMetadata();
		}else if(requestBody.isPostRequest()){
			return validPostBusiModelResourceMetadata();
		}
		return "系统只支持[get、post]两种请求方式处理业务模型资源";
	}
	
	/**
	 * 验证get请求的业务模型资源数据
	 * @return
	 */
	private String validGetBusiModelResourceMetadata() {
		List<CfgBusiModelResRelations> list = busiModel.getBusiModelResRelationsList();
		for (int i=0;i<list.size();i++) {
			if(list.get(i).isSqlResource() && !list.get(i).getRefSql().isSelectSql()){
				return "请求报表业务资源中，引用的第"+(i+1)+"个sql资源非select类型，请修改";
			}
		}
		return null;
	}

	/**
	 * 验证post请求的业务模型资源数据
	 * @return
	 */
	private String validPostBusiModelResourceMetadata() {
		IJson rootIjson = requestBody.getFormData();
		validResult = validRootBusiModelData(busiModel.getBusiModelResRelationsList(), rootIjson);
		recursiveClearValidMetadataDatas(busiModel.getBusiModelResRelationsList());
		return validResult;
	}
	
	/**验证数据*/
	private void validData(CfgBusiModelResRelations busiModelResRelations, IJson ijson, String idPropName, int recursiveLevel){
		int size = ijson.size();
		JSONObject json = null;
		if(busiModelResRelations.getRefTable() != null){
			int[] flag = new int[2];
			for(int j=0;j<size;j++){
				json = ijson.get(j);
				Object operDataType = json.get(ResourcePropNameConstants.OPER_DATA_TYPE);
				
				if(OperDataTypeConstants.ADD.equals(operDataType)){
					json.put(idPropName, ResourceHandlerUtil.getIdentity());// 业务建模保存数据时，添加数据无论如何，都用后端生成id **********
					flag[1] = 2;
				}else if(OperDataTypeConstants.EDIT.equals(operDataType)){
					if(StrUtils.isEmpty(json.get(idPropName))){
						throw new NullPointerException("业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，table资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要修改的，第"+(j+1)+"个数据的"+idPropName+"参数值不能为空");
					}
					flag[1] = 2;
				}else if(OperDataTypeConstants.DELETE.equals(operDataType)){
					if(StrUtils.isEmpty(json.get(idPropName))){
						throw new NullPointerException("业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，table资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要删除的，第"+(j+1)+"个数据的"+idPropName+"参数值不能为空");
					}
					flag[1] = 2;
				}else if(OperDataTypeConstants.SELECT.equals(operDataType)){
					Log4jUtil.info("不验证查询语句");
					flag[0] = 1;
				}else{
					throw new IllegalArgumentException("业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，table资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，第"+(j+1)+"个数据的$operDataType$参数值不能为空，且值只能为add/edit/delete/select");
				}
			}
			if(flag[0]==1 && flag[1]==2){
				throw new IllegalArgumentException("在业务模型["+resourceName+"]中，系统目前不支持同时操作[增删改]和[查询]表资源数据");
			}
		}else if(busiModelResRelations.getRefSql() != null){
			for(int j=0;j<size;j++){
				json = ijson.get(j);
				json.remove(ResourcePropNameConstants.OPER_DATA_TYPE);// 尝试移除，因为用不上
				
				if(busiModelResRelations.getRefSql().isInsertSql()){
					json.put(idPropName, ResourceHandlerUtil.getIdentity());// 业务建模保存数据时，添加数据无论如何，都用后端生成id **********
				}else if(busiModelResRelations.getRefSql().isUpdateSql()){
					if(StrUtils.isEmpty(json.get(idPropName))){
						throw new NullPointerException("业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，sql资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要修改的，第"+(j+1)+"个数据的"+idPropName+"参数值不能为空");
					}
				}else if(busiModelResRelations.getRefSql().isDeleteSql()){
					if(StrUtils.isEmpty(json.get(idPropName))){
						throw new NullPointerException("业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，sql资源名为["+busiModelResRelations.getRefResourceName()+"]的数据集合中，要删除的，第"+(j+1)+"个数据的"+idPropName+"参数值不能为空");
					}
				}else if(busiModelResRelations.getRefSql().isSelectSql()){
					Log4jUtil.info("不验证查询语句");
				}else{
					throw new IllegalArgumentException("业务模型["+resourceName+"]中，关联的第"+recursiveLevel+"层级，sql资源名为["+busiModelResRelations.getRefResourceName()+"]的资源confType值不符合要求");
				}
			}
		}
	}
	
	/**
	 * 验证业务资源根数据
	 * @param rootList
	 * @param rootIjson
	 * @return
	 */
	private String validRootBusiModelData(List<CfgBusiModelResRelations> rootList, IJson rootIjson){
		if(rootList != null && rootList.size() > 0){
			CfgBusiModelResRelations root = null;
			IJson ijson = null;
			IJson subIJson = null;
			for(int i = 0; i < rootList.size(); i++){
				root = rootList.get(i);
				
				if(rootList.size() > 1){
					ijson = rootIjson.getIJson(i);
				}else{
					ijson = rootIjson;
				}
				if(ijson == null || (ijson.size() == 0)){
					return "业务模型["+resourceName+"]中，关联的第1层级，资源名为["+root.getRefResourceName()+"]的数据不能为空";
				}
				
				validData(root, ijson, root.getIdPropName(), 1);
				if(root.haveSubBusiModelResRelationsList()){
					for(int j=0;j<ijson.size();j++){
						for(CfgBusiModelResRelations sub: root.getSubBusiModelResRelationsList()){
							subIJson = IJsonUtil.getIJson(ijson.get(j).remove(sub.getRefResourceKeyName()));
							if(subIJson != null && subIJson.size() > 0){
								validResult = recursiveValidBusiModelData(sub, subIJson, ijson.get(j).get(root.getIdPropName()), 2);
								if(validResult != null){
									return validResult;
								}
							}
						}
					}
				}
				validResult = root.validResourceData(new BusiModelResourceData(requestBody.getToken(), requestBody.getRequestURL(), busiModelResourceName, null, ijson));
			}
		}
		return validResult;
	}
	
	/**
	 * 递归验证业务资源子数据
	 * @param busiModelRelation
	 * @param ijson
	 * @param parentId
	 * @param level
	 * @return
	 */
	private String recursiveValidBusiModelData(CfgBusiModelResRelations busiModelRelation, IJson ijson, Object parentId, int level){
		validData(busiModelRelation, ijson, busiModelRelation.getIdPropName(), level);
		if(busiModelRelation.haveSubBusiModelResRelationsList()){
			IJson subIJson = null;
			for(int i=0;i<ijson.size();i++){
				for(CfgBusiModelResRelations sub: busiModelRelation.getSubBusiModelResRelationsList()){
					subIJson = IJsonUtil.getIJson(ijson.get(i).remove(sub.getRefResourceKeyName()));
					if(subIJson != null && subIJson.size() > 0){
						validResult = recursiveValidBusiModelData(sub, subIJson, ijson.get(i).get(busiModelRelation.getIdPropName()), (level+1));
						if(validResult != null){
							return validResult;
						}
					}
				}
			}
		}
		validResult = busiModelRelation.validResourceData(new BusiModelResourceData(requestBody.getToken(), requestBody.getRequestURL(), busiModelResourceName, parentId, ijson));
		return validResult;
	}
	
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
