package com.king.tooth.web.processer.tableresource.post;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 处理这种请求路径格式的处理器：/{parentResourceType}/{parentId}/{resourceType}
 * 
 * <pre>/{parentResourceType}/{parentId}/{resourceType}
 * 这种路由格式，传入主资源ID，则提交的json，只能是子资源的列表，平台会自动将这些子资源的数据，关联存储到指定主资源id下</pre>
 * 
 * <pre>/{parentResourceType}/null/{resourceType}
 * 这种路由格式，传入主资源ID是null，且必须写<b>null</b>，这时提交的json串，应该是包含主子资源的，其中子资源作为主资源的"Children"字段下的列表，平台会处理好这些数据的关联关系</pre>
 * 
 * @author DougLei
 */
public final class ParentResourceByIdToSubResourceProcesser extends PostProcesser {

	/**
	 * 存储主子表id关联集合
	 */
	private final List<JSONObject> dataLinkList = new ArrayList<JSONObject>();
	
	public String getProcesserName() {
		return "【Post-TableResource】ParentResourceByIdToSubResourceProcesser";
	}
	
	protected boolean doPostProcess() {
		String parentId = requestBody.getRouteBody().getParentId();
		
		if(StrUtils.isNullStr(parentId)){ // 标识提交的数据包含主子表
			JSONObject parentData = null;
			JSONArray subDatas = null;
			for(int i=0; i < json.size(); i++){
				parentData = json.get(i);
				saveData(requestBody.getRouteBody().getParentResourceName(), parentData);
				
				subDatas = parentData.getJSONArray(ResourceNameConstants.CHILDREN);
				saveSubData(parentData.getString(ResourceNameConstants.ID), subDatas);
			}
		}else{ // 否则，标识提交的数据只有子资源数据，父资源的id通过路由传递过来
			saveSubData(parentId, (JSONArray)json.getJson());
		}
		
		saveDataLinks();
		installResponseBodyForSaveData(null, json.getJson());
		return true;
	}

	/**
	 * 保存子资源数据
	 * @param parentId
	 * @param subDatas
	 */
	private void saveSubData(String parentId, JSONArray subDatas){
		if(subDatas == null || subDatas.size() == 0){
			Log4jUtil.debug("主子表保存数据，要保存的子表列表为null");
			return;
		}
		
		JSONObject datalink = null;
		JSONObject subData = null;
		for(int j=0; j<subDatas.size(); j++){
			subData = subDatas.getJSONObject(j);
			if(builtinParentsubQueryMethodProcesser.getIsSimpleParentSubQueryModel()){
				subData.put(builtinParentsubQueryMethodProcesser.getRefParentSubPropName(), parentId);
			}
			saveData(requestBody.getRouteBody().getResourceName(), subData);
			
			if(!builtinParentsubQueryMethodProcesser.getIsSimpleParentSubQueryModel()){
				datalink = ResourceHandlerUtil.getDataLinksObject(parentId, subData.getString(ResourceNameConstants.ID), 
						(j+1), requestBody.getRouteBody().getParentResourceName(), requestBody.getRouteBody().getResourceName());
				dataLinkList.add(datalink);
			}
		}
//		subDatas.clear();
	}
	
	/**
	 * 保存数据的关联关系
	 */
	private void saveDataLinks() {
		if(dataLinkList.size() < 1){
			Log4jUtil.debug("主子表保存数据，要保存的关联关系集合对象长度为0");
			Log4jUtil.debug("主子表保存数据，保存模式是否为简单模式：{}" + builtinParentsubQueryMethodProcesser.getIsSimpleParentSubQueryModel());
			return;
		}
		String dataLinkResourceName = HibernateUtil.getDataLinkResourceName(requestBody.getRouteBody().getParentResourceName(), requestBody.getRouteBody().getResourceName());
		for (JSONObject datalink : dataLinkList) {
			saveData(dataLinkResourceName, datalink);
		}
//		dataLinkList.clear();
	}
}
