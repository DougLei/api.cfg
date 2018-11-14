package com.king.tooth.web.processer.busimodel.post;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.busimodel.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	protected boolean doProcess() {
		CfgBusiModel busiModel = requestBody.getResourceInfo().getBusiModel();
		JSONArray resultDataJSONArray = recursiveDoSaveBusiModelData(busiModel.getBusiModelResRelationsList(), 1);
		if(resultDataJSONArray == null){
			setResponseBody(new ResponseBody("执行业务模型资源["+busiModel.getResourceName()+"]时，没有返回任何结果信息，请联系后端系统开发人员", null));
		}else if(requestBody.getFormData().size() == 1){
			setResponseBody(new ResponseBody(null, resultDataJSONArray.get(0)));
		}else{
			setResponseBody(new ResponseBody(null, resultDataJSONArray));
		}
		return true;
	}

	/**
	 * 递归保存业务模型资源数据
	 * @param busiModelResRelationsList
	 * @param recursiveLevel
	 * @return
	 */
	private JSONArray recursiveDoSaveBusiModelData(List<CfgBusiModelResRelations> busiModelResRelationsList, int recursiveLevel) {
		if(busiModelResRelationsList != null && busiModelResRelationsList.size() > 0){
			JSONArray resultDataJSONArray = new JSONArray(busiModelResRelationsList.size());
			for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationsList) {
				JSONArray subResultDataJSONArray = recursiveDoSaveBusiModelData(busiModelResRelations.getSubBusiModelResRelationsList(), recursiveLevel+1);
				JSONArray tmpResultDatasJSONArray = null;
				
				List<Object> resultDatasList = busiModelResRelations.doSaveBusiDataList();
				if(resultDatasList != null && resultDatasList.size() > 0){
					int size = resultDatasList.size();
					Object resultDatas = null;
					for(int i=0;i<size;i++){
						resultDatas = resultDatasList.get(i);
						if(resultDatas instanceof JSONObject){
							((JSONObject)resultDatas).put(busiModelResRelations.getRefSubResourceKeyName(), subResultDataJSONArray);
						}else if(resultDatas instanceof JSONArray){
							tmpResultDatasJSONArray = (JSONArray)resultDatas;
							if(subResultDataJSONArray != null && i< subResultDataJSONArray.size() && i<tmpResultDatasJSONArray.size()){
								if(tmpResultDatasJSONArray.get(i) == null){
									tmpResultDatasJSONArray.add(i, new JSONObject(1));
								}
								tmpResultDatasJSONArray.getJSONObject(i).put(busiModelResRelations.getRefSubResourceKeyName(), subResultDataJSONArray.get(i));
							}
						}
						resultDataJSONArray.add(resultDatas);
					}
				}
			}
			return resultDataJSONArray;
		}
		return null;
	}

	public String getProcesserName() {
		return "【Post-BusiModelResource】SingleResourceProcesser";
	}
}
