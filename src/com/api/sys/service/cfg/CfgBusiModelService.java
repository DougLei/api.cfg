package com.api.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Service;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.entity.cfg.CfgBusiModel;
import com.api.sys.entity.cfg.CfgBusiModelResRelations;
import com.api.sys.service.AService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 业务模型表Service
 * @author DougLei
 */
@Service
public class CfgBusiModelService extends AService{

	/**
	 * 验证业务模型资源名是否存在
	 * @param busiModel
	 * @return 
	 */
	private String validBusiModelResourceNameIsExists(CfgBusiModel busiModel) {
		if(BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).resourceIsExistByRefResourceName(busiModel.getResourceName())){
			return "系统中已经存在相同资源名["+busiModel.getResourceName()+"]的数据，请修改业务模型的资源名";
		}
		return null;
	}
	
	/**
	 * 保存业务模型
	 * @param busiModel
	 * @return
	 */
	public Object saveBusiModel(CfgBusiModel busiModel) {
		String operResult = validBusiModelResourceNameIsExists(busiModel);
		if(operResult == null){
			JSONObject busiModelJsonObject = HibernateUtil.saveObject(busiModel, null);
			String busiModelId = busiModelJsonObject.getString(ResourcePropNameConstants.ID);
			
			// 因为保存资源数据的时候，需要busiModel对象的id，所以放到最后
			busiModel.setId(busiModelId);
			BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).saveCfgResource(busiModel);
		
			return busiModelJsonObject;
		}
		return operResult;
	}
	
	/**
	 * 修改业务模型
	 * @param busiModel
	 * @return
	 */
	public Object updateBusiModel(CfgBusiModel busiModel) {
		CfgBusiModel oldBusiModel = getObjectById(busiModel.getId(), CfgBusiModel.class);
		if(oldBusiModel == null){
			return "没有找到id为["+busiModel.getId()+"]的业务模型对象信息";
		}
		String operResult = null;
		if(!oldBusiModel.getResourceName().equals(busiModel.getResourceName())){
			operResult = validBusiModelResourceNameIsExists(busiModel);
		}
		
		if(operResult == null){
			if(busiModel.isUpdateResourceInfo(oldBusiModel)){
				BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).updateResourceInfo(busiModel.getId(), busiModel.getResourceName(), busiModel.getRequestMethod(), busiModel.getIsEnabled());
			}
			return HibernateUtil.updateEntityObject(busiModel, null);
		}
		return operResult;
	}

	/**
	 * 删除业务模型
	 * @param busiModelId
	 * @return
	 */
	public Object deleteBusiModel(String busiModelId) {
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgBusiModel where "+ResourcePropNameConstants.ID+" = ?", busiModelId);
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgBusiModelResRelations where refBusiModelId = ?", busiModelId);
		BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).deleteCfgResource(busiModelId);
		return null;
	}
	
	// -----------------------------------------------------------------------------
	/**
	 * 查询指定id的获取业务模型对象
	 * <p>包括该业务模型下的所有资源关系</p>
	 * @param busiModelId
	 * @return
	 */
	public CfgBusiModel findBusiModel(String busiModelId) {
		CfgBusiModel busiModel = getObjectById(busiModelId, CfgBusiModel.class);
		busiModel.setBusiModelResRelationsList(getBusiModelResRelationsList(busiModel));
		return busiModel;
	}

	/**
	 * 获取指定业务模型id的资源关系数据集合
	 * @param busiModel
	 * @return
	 */
	private List<CfgBusiModelResRelations> getBusiModelResRelationsList(CfgBusiModel busiModel) {
		List<CfgBusiModelResRelations> tmpBusiModelResRelationsList = 
				HibernateUtil.extendExecuteListQueryByHqlArr(CfgBusiModelResRelations.class, null, null, 
						"from CfgBusiModelResRelations where refBusiModelId=? and projectId=? and customerId=? order by orderCode asc", 
						busiModel.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		
		if(tmpBusiModelResRelationsList == null || tmpBusiModelResRelationsList.size() == 0){
			throw new NullPointerException("["+busiModel.getResourceName()+"]业务模型，不存在任何资源关系，请检查配置");
		}
		
		List<CfgBusiModelResRelations> busiModelResRelationsList = new ArrayList<CfgBusiModelResRelations>();
		CfgBusiModelResRelations busiModelResRelations = null;
		for(int i=0;i<tmpBusiModelResRelationsList.size();i++){
			if(StrUtils.isEmpty(tmpBusiModelResRelationsList.get(i).getParentId())){
				busiModelResRelations = tmpBusiModelResRelationsList.remove(i);
				setSubBusiModelResRelations(busiModelResRelations, tmpBusiModelResRelationsList);
				busiModelResRelationsList.add(busiModelResRelations);
				i = -1;
			}
		}
		
		if(tmpBusiModelResRelationsList.size() > 0){
			tmpBusiModelResRelationsList.clear();
		}
		if(busiModelResRelationsList.size() == 0){
			throw new NullPointerException("["+busiModel.getResourceName()+"]业务模型，不存在任何根资源关系，请检查配置");
		}
		return busiModelResRelationsList;
	}

	/**
	 * 从busiModelResRelationsList中，查询pBusiModelResRelations的子数据，set到pBusiModelResRelations中
	 * @param pBusiModelResRelations
	 * @param busiModelResRelationsList
	 */
	private void setSubBusiModelResRelations(CfgBusiModelResRelations pBusiModelResRelations, List<CfgBusiModelResRelations> busiModelResRelationsList) {
		String subParentId = pBusiModelResRelations.getId();
		CfgBusiModelResRelations busiModelResRelations = null;
		
		for(int i=0;i<busiModelResRelationsList.size();i++){
			if(subParentId.equals(busiModelResRelationsList.get(i).getParentId())){
				busiModelResRelations = busiModelResRelationsList.remove(i);
				setSubBusiModelResRelations(busiModelResRelations, busiModelResRelationsList);
				pBusiModelResRelations.addSubBusiModelResRelations(busiModelResRelations);
				i = -1;
			}
		}
	}
}
