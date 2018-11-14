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
				
				List<Object> resultDatasList = busiModelResRelations.doSaveBusiDataList();
				if(resultDatasList != null && resultDatasList.size() > 0){
					int size = resultDatasList.size();
					Object resultDatas = null;
					for(int i=0;i<size;i++){
						resultDatas = resultDatasList.get(i);
						resultDataJSONArray.add(resultDatas);
						
						if(resultDatas != null && subResultDataJSONArray != null){
							if(resultDatas instanceof JSONObject){
								((JSONObject)resultDatas).put(busiModelResRelations.getRefSubResourceKeyName(), subResultDataJSONArray);
							}else if(resultDatas instanceof JSONArray){
								tmpResultDatasJSONArray = (JSONArray)resultDatas;
								for(int j=0;j<tmpResultDatasJSONArray.size();j++){
									if( j< subResultDataJSONArray.size()){
										tmpResultDatasJSONArray.getJSONObject(j).put(busiModelResRelations.getRefSubResourceKeyName(), subResultDataJSONArray.get(j));
									}
								}
							}
						}
					}
				}
			}
			return resultDataJSONArray;
		}
		return null;
	}
	private JSONArray tmpResultDatasJSONArray;

	public String getProcesserName() {
		return "【Post-BusiModelResource】SingleResourceProcesser";
	}
}
