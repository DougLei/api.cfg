package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.cfg.CfgBusiResModel;
import com.king.tooth.sys.entity.cfg.CfgBusiResModelRelations;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 资源信息表Service
 * @author DougLei
 */
@Service
public class CfgBusiResModelService extends AService{

	/**
	 * 查询获取业务资源模型对象
	 * <p>包括该业务资源模型下的所有关系</p>
	 * @param busiResModelId
	 * @return
	 */
	public CfgBusiResModel findBusiResModel(String busiResModelId) {
		CfgBusiResModel busiResModel = getObjectById(busiResModelId, CfgBusiResModel.class);
		busiResModel.setBusiResModelRelationsList(getBusiResModelRelationsList(busiResModel));
		return busiResModel;
	}

	/**
	 * 获取指定业务资源模型id的，(业务资源模型)关系数据集合
	 * @param busiResModel
	 * @return
	 */
	private List<CfgBusiResModelRelations> getBusiResModelRelationsList(CfgBusiResModel busiResModel) {
		List<CfgBusiResModelRelations> tmpBusiResModelRelationsList = 
				HibernateUtil.extendExecuteListQueryByHqlArr(CfgBusiResModelRelations.class, null, null, 
						"from CfgBusiResModelRelations where refBusiResModelId=? and isEnabled=1 and projectId=? and customerId=? order by orderCode asc", 
						busiResModel.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		
		if(tmpBusiResModelRelationsList == null || tmpBusiResModelRelationsList.size() == 0){
			throw new NullPointerException("["+busiResModel.getResourceName()+"]业务资源模型，不存在任何资源关系，请检查配置");
		}
		
		List<CfgBusiResModelRelations> busiResModelRelationsList = new ArrayList<CfgBusiResModelRelations>();
		CfgBusiResModelRelations busiResModelRelations = null;
		for(int i=0;i<tmpBusiResModelRelationsList.size();i++){
			if(StrUtils.isEmpty(tmpBusiResModelRelationsList.get(i).getParentId())){
				busiResModelRelations = tmpBusiResModelRelationsList.remove(i);
				setSubBusiResModelRelations(busiResModelRelations, tmpBusiResModelRelationsList);
				busiResModelRelationsList.add(busiResModelRelations);
				i = 0;
			}
		}
		
		if(tmpBusiResModelRelationsList.size() > 0){
			tmpBusiResModelRelationsList.clear();
		}
		if(busiResModelRelationsList.size() == 0){
			throw new NullPointerException("["+busiResModel.getResourceName()+"]业务资源模型，不存在任何根资源关系，请检查配置");
		}
		
		Log4jUtil.debug("解析出来的业务资源数据为:{}", JsonUtil.toJsonString(busiResModelRelationsList, true));
		return busiResModelRelationsList;
	}

	/**
	 * 从busiResModelRelationsList中，查询busiResModelRelations的子数据，set到busiResModelRelations中
	 * @param pBusiResModelRelations
	 * @param tmpBusiResModelRelationsList
	 */
	private void setSubBusiResModelRelations(CfgBusiResModelRelations pBusiResModelRelations, List<CfgBusiResModelRelations> busiResModelRelationsList) {
		String subParentId = pBusiResModelRelations.getId();
		CfgBusiResModelRelations busiResModelRelations = null;
		
		for(int i=0;i<busiResModelRelationsList.size();i++){
			if(busiResModelRelationsList.get(i).getParentId().equals(subParentId)){
				busiResModelRelations = busiResModelRelationsList.remove(i);
				setSubBusiResModelRelations(busiResModelRelations, busiResModelRelationsList);
				busiResModelRelations.addSubBusiResModelRelations(busiResModelRelations);
				i = 0;
			}
		}
	}
}
