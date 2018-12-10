package com.king.tooth.web.processer.busimodel.post;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
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
		JSONArray resultDataJSONArray = recursiveDoProcessBusiModelData(busiModel.getBusiModelResRelationsList(), null);
		if(resultDataJSONArray == null){
			setResponseBody(new ResponseBody("执行业务模型资源["+busiModel.getResourceName()+"]时，没有返回任何结果信息，请联系后端系统开发人员", null));
		}else if(requestBody.getFormData().size() == 1 || busiModel.getBusiModelResRelationsList().size() == 1){
			setResponseBody(new ResponseBody(null, resultDataJSONArray.get(0)));
		}else{
			setResponseBody(new ResponseBody(null, resultDataJSONArray));
		}
		return true;
	}

	/**
	 * 递归处理业务模型资源数据
	 * @param busiModelResRelationsList
	 * @param pids 父id数组，查询的时候用到
	 * @return
	 */
	private JSONArray recursiveDoProcessBusiModelData(List<CfgBusiModelResRelations> busiModelResRelationsList, Object[] pids) {
		if(busiModelResRelationsList != null && busiModelResRelationsList.size() > 0){
			JSONArray resultDataJSONArray = new JSONArray(busiModelResRelationsList.size());
			for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationsList) {
				List<Object> resultDatasList = busiModelResRelations.doOperBusiDataList(pids);

				if(resultDatasList != null && resultDatasList.size() > 0){
					int size = resultDatasList.size();
					Object resultDatas = null;
					for(int i=0;i<size;i++){
						resultDatas = resultDatasList.get(i);
						resultDataJSONArray.add(resultDatas);
						
						JSONArray subResultDataJSONArray = null;
						if(resultDatas != null){
							if(resultDatas instanceof JSONObject){
								subResultDataJSONArray = recursiveDoProcessBusiModelData(busiModelResRelations.getSubBusiModelResRelationsList(), new Object[]{((JSONObject)resultDatas).get(ResourcePropNameConstants.ID)});
							
								if(subResultDataJSONArray != null){
									((JSONObject)resultDatas).put(busiModelResRelations.getRefSubResourceKeyName(), subResultDataJSONArray.get(i));
								}
							}else if(resultDatas instanceof JSONArray){
								tmpResultDatasJSONArray = (JSONArray)resultDatas;
								int tmpSize = tmpResultDatasJSONArray.size();
								Object[] tmpPids = new Object[tmpSize];
								for(int j=0;j<tmpSize;j++){
									tmpPids[j] = tmpResultDatasJSONArray.getJSONObject(j).get(ResourcePropNameConstants.ID);
								}
								subResultDataJSONArray = recursiveDoProcessBusiModelData(busiModelResRelations.getSubBusiModelResRelationsList(), tmpPids);
								
								if(subResultDataJSONArray != null){
									for(int j=0;j<tmpResultDatasJSONArray.size();j++){
										if( j< subResultDataJSONArray.size()){
											tmpResultDatasJSONArray.getJSONObject(j).put(busiModelResRelations.getRefSubResourceKeyName(), subResultDataJSONArray.get(j));
										}
									}
								}
							}
						}
					}
					resultDatasList.clear();
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
