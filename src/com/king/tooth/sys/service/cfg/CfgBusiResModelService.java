package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.cfg.CfgBusiResModel;
import com.king.tooth.sys.service.AService;

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
		
		busiResModel.setBusiResModelRelationsList(null);
		if(busiResModel.getBusiResModelRelationsList() == null || busiResModel.getBusiResModelRelationsList().size() == 0){
			throw new NullPointerException("["+busiResModel.getResourceName()+"]业务资源模型，不存在任何资源关系，请检查配置");
		}
		return busiResModel;
	}
}
