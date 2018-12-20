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
		JSONArray array = doProcessBusiModelRootData(busiModel.getBusiModelResRelationsList());
		if(array == null){
			setResponseBody(new ResponseBody("执行业务模型资源["+busiModel.getResourceName()+"]时，没有返回任何结果信息，请联系后端系统开发人员", null));
		}else if(busiModel.getBusiModelResRelationsList().size() == 1){
			setResponseBody(new ResponseBody(null, array.get(0)));
		}else{
			setResponseBody(new ResponseBody(null, array));
		}
		return true;
	}

	/**
	 * 处理业务模型资源根数据
	 * @param rootList
	 * @return
	 */
	private JSONArray doProcessBusiModelRootData(List<CfgBusiModelResRelations> rootList) {
		JSONArray array = new JSONArray(rootList.size());
		
		for (CfgBusiModelResRelations busiModelResRelations : rootList) {
			Object data = busiModelResRelations.doOperBusiDataList(null);
			array.add(data);
			
			if(data != null && busiModelResRelations.haveSubBusiModelResRelationsList()){
				if(data instanceof JSONObject){
					JSONObject json = (JSONObject) data;
					for(CfgBusiModelResRelations sub: busiModelResRelations.getSubBusiModelResRelationsList()){
						recursiveDoProcessBusiModelData(json, sub, json.get(busiModelResRelations.getIdPropName()));
					}
				}else{
					JSONArray jarray = (JSONArray) data;
					for(int j=0;j<jarray.size();j++){
						for(CfgBusiModelResRelations sub: busiModelResRelations.getSubBusiModelResRelationsList()){
							recursiveDoProcessBusiModelData(jarray.getJSONObject(j), sub , jarray.getJSONObject(j).get(busiModelResRelations.getIdPropName()));
						}
					}
				}
			}
		}
		return array;
	}
	
	/**
	 * 递归处理业务模型资源根数据
	 * @param parentJson
	 * @param busiModelResRelations
	 * @param queryConditionPID
	 */
	private void recursiveDoProcessBusiModelData(JSONObject parentJson, CfgBusiModelResRelations busiModelResRelations, Object queryConditionPID) {
		Object data = busiModelResRelations.doOperBusiDataList(queryConditionPID);
		parentJson.put(busiModelResRelations.getRefResourceKeyName(), data);
		
		if(data != null && busiModelResRelations.haveSubBusiModelResRelationsList()){
			if(data instanceof JSONObject){
				JSONObject json = (JSONObject) data;
				for(CfgBusiModelResRelations sub: busiModelResRelations.getSubBusiModelResRelationsList()){
					recursiveDoProcessBusiModelData(json, sub, json.get(busiModelResRelations.getIdPropName()));
				}
			}else{
				JSONArray jarray = (JSONArray) data;
				for(int j=0;j<jarray.size();j++){
					for(CfgBusiModelResRelations sub: busiModelResRelations.getSubBusiModelResRelationsList()){
						recursiveDoProcessBusiModelData(jarray.getJSONObject(j), sub , jarray.getJSONObject(j).get(busiModelResRelations.getIdPropName()));
					}
				}
			}
		}
	}

	public String getProcesserName() {
		return "【Post-BusiModelResource】SingleResourceProcesser";
	}
}
