package com.king.tooth.web.processer.busimodel.post;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.web.processer.busimodel.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	/**
	 * 用来存储操作结果的对象集合
	 */
	private JSONArray resultDataJSONArray;
	
	protected boolean doProcess() {
		CfgBusiModel busiModel = requestBody.getResourceInfo().getBusiModel();
		resultDataJSONArray = new JSONArray(busiModel.getBusiModelResRelationsList().size());
		
		recursiveSaveBusiModelData(busiModel.getBusiModelResRelationsList(), 1);
		
		
		return true;
	}

	/**
	 * 递归保存业务模型资源数据
	 * @param busiModelResRelationsList
	 * @param recursiveLevel
	 */
	private void recursiveSaveBusiModelData(List<CfgBusiModelResRelations> busiModelResRelationsList, int recursiveLevel) {
		if(busiModelResRelationsList != null && busiModelResRelationsList.size() > 0){
			for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationsList) {
				resultDataJSONArray.add(busiModelResRelations.doSaveBusiData());
				recursiveSaveBusiModelData(busiModelResRelations.getSubBusiModelResRelationsList(), recursiveLevel+1);
			}
		}
	}

	public String getProcesserName() {
		return "【Post-BusiModelResource】SingleResourceProcesser";
	}
}
