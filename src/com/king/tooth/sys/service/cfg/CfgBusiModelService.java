package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 业务模型表Service
 * @author DougLei
 */
@Service
public class CfgBusiModelService extends AService{

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
						"from CfgBusiModelResRelations where refBusiModelId=? and isEnabled=1 and projectId=? and customerId=? order by orderCode asc", 
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
				i = 0;
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
			if(busiModelResRelationsList.get(i).getParentId().equals(subParentId)){
				busiModelResRelations = busiModelResRelationsList.remove(i);
				setSubBusiModelResRelations(busiModelResRelations, busiModelResRelationsList);
				busiModelResRelations.addSubBusiModelResRelations(busiModelResRelations);
				i = 0;
			}
		}
	}
}
