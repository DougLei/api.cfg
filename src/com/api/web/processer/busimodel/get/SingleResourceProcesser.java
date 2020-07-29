package com.api.web.processer.busimodel.get;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.entity.cfg.CfgBusiModelResRelations;
import com.api.util.ExceptionUtil;
import com.api.util.JsonUtil;
import com.api.util.httpclient.HttpClientUtil;
import com.api.web.entity.resulttype.ResponseBody;
import com.api.web.processer.busimodel.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	public String getProcesserName() {
		return "【Get-BusiModelResource】SingleResourceProcesser";
	}

	private Map<String, String> headers = new HashMap<String, String>(1);
	private int totalSize = 0;
	private Map<String, Object> generalUrlParams;// 记录通用的url参数
	private String[] specialUrlParamKeys;// 记录各个资源自己的url参数的key
	private Map<String, Object> specialUrlParams;// 记录各个资源自己的url参数
	private String exceptionDesc;
	
	protected boolean doProcess() {
		init();
		List<CfgBusiModelResRelations> busiModelResRelationsList = requestBody.getResourceInfo().getBusiModel().getBusiModelResRelationsList();
		
		int size = busiModelResRelationsList.size();
		JSONObject json = new JSONObject(size);
		JSONObject resultJson = null;
		CfgBusiModelResRelations relations;
		JSONArray result = null;
		Map<String, Object> params;
		try {
			for(int i=0;i<size;i++){
				relations = busiModelResRelationsList.get(i);
				params = paramsHandler(relations.getRefResourceKeyName());
				resultJson = JsonUtil.parseJsonObject(HttpClientUtil.doGetBasic(requestBody.getScheme() + "://localhost:" + requestBody.getServerPort() + requestBody.getContextPath() + "/common/" + relations.getRefResourceName(), params, headers));
				if(resultJson == null){
					json.put(relations.getRefResourceKeyName(), "网络连接超时, 未获取到数据");
				}else if(resultJson.getBooleanValue("isSuccess")){
					result =  (JSONArray) resultJson.remove("data");
					json.put(relations.getRefResourceKeyName(), result);
				}else{
					setResponseBody(new ResponseBody(exceptionDesc + resultJson.getString("message"), null));
					return false;
				}
				
				if(!recursiveQuery(relations.getIdPropName(), result, relations.getSubBusiModelResRelationsList())){
					return false;
				}
				result = null;
			}
			setResponseBody(new ResponseBody(null, json));
		} catch (Exception e) {
			setResponseBody(new ResponseBody(exceptionDesc + ExceptionUtil.getErrMsg(e), null));
			return false;
		}
		return true;
	}
	
	
	private boolean recursiveQuery(String idPropName, JSONArray parentResult, List<CfgBusiModelResRelations> subBusiModelResRelationsList) {
		if(parentResult != null && parentResult.size() > 0 && subBusiModelResRelationsList != null && subBusiModelResRelationsList.size() > 0){
			String pid;
			JSONObject parentJson;
			JSONArray result = null;
			Map<String, Object> params;
			JSONObject resultJson = null;
			for(int i=0;i<parentResult.size();i++){
				parentJson = parentResult.getJSONObject(i);
				pid = parentJson.getString(idPropName);
				for (CfgBusiModelResRelations sub : subBusiModelResRelationsList) {
					params = paramsHandler(sub.getRefResourceKeyName());
					params.put(sub.getRefParentResourcePropName(true), pid);
					resultJson = JsonUtil.parseJsonObject(HttpClientUtil.doGetBasic(requestBody.getScheme() + "://localhost:" + requestBody.getServerPort() + requestBody.getContextPath() + "/common/" + sub.getRefResourceName(), params, headers));
					
					if(resultJson == null){
						parentJson.put(sub.getRefResourceKeyName(), "网络连接超时, 未获取到数据");
					}else if(resultJson.getBooleanValue("isSuccess")){
						result =  (JSONArray) resultJson.remove("data");
						parentJson.put(sub.getRefResourceKeyName(), result);
					}else{
						setResponseBody(new ResponseBody(exceptionDesc + resultJson.getString("message"), null));
						return false;
					}
					
					if(!recursiveQuery(sub.getIdPropName(), result, sub.getSubBusiModelResRelationsList())){
						return false;
					}
					result = null;
				}
			}
		}
		return true;
	}

	/**
	 * 处理参数
	 * @param resourceKeyName
	 * @return
	 */
	private Map<String, Object> paramsHandler(String resourceKeyName) {
		Map<String, Object> params = new HashMap<String, Object>(totalSize);
		params.putAll(generalUrlParams);
		
		sortHandler(params, resourceKeyName);
		conditionHandler(params, resourceKeyName);
		return params;
	}
	
	/**
	 * 从 specialUrlParams中，找出resourceKeyName对应的排序参数
	 * @param params
	 * @param resourceKeyName 
	 */
	private void sortHandler(Map<String, Object> params, String resourceKeyName) {
		if(specialUrlParams.size() > 0){
			Object orderBy = specialUrlParams.remove(resourceKeyName+"._sort");
			if(orderBy != null){
				params.put("_sort", orderBy.toString());
			}
		}
	}
	
	/**
	 * 从 specialUrlParams中，找出resourceKeyName对应的条件参数
	 * @param params
	 * @param resourceKeyName 
	 */
	private void conditionHandler(Map<String, Object> params, String resourceKeyName) {
		if(specialUrlParams.size() > 0){
			for(String key : specialUrlParamKeys){
				if(key.startsWith(resourceKeyName)){
					params.put(key.replace(resourceKeyName+".", ""), specialUrlParams.remove(key));
				}
			}
		}
	}

	private void init(){
		headers.put("_token", requestBody.getToken());
		
		prepareParams();
		prepareSetExceptionDesc();
	}
	
	// 处理参数
	private void prepareParams(){
		generalUrlParams = new HashMap<String, Object>(requestBody.getAllUrlParams());
		generalUrlParams.remove(BuiltinParameterKeys.RESOURCE_NAME);// 在解析的时候会放过来，这里把他移除
		
		totalSize = generalUrlParams.size();
		if(totalSize > 0){
			specialUrlParams = new HashMap<String, Object>(totalSize);
			
			Set<String> keys = generalUrlParams.keySet();
			for (String key : keys) {
				if(key.indexOf(".") != -1){
					specialUrlParams.put(key, generalUrlParams.get(key));
				}
			}
					
			if(specialUrlParams.size() > 0){
				StringBuilder sb = new StringBuilder();
				
				keys = specialUrlParams.keySet();
				for (String key : keys) {
					generalUrlParams.remove(key);
					sb.append(key).append(",");
				}
				sb.setLength(sb.length() - 1);
				specialUrlParamKeys = sb.toString().split(",");
			}
		}else{
			specialUrlParams = new HashMap<String, Object>(1);
		}
	}
	
	private void prepareSetExceptionDesc() {
		exceptionDesc = "在调用业务模型资源["+requestBody.getResourceInfo().getBusiModel().getResourceName()+"]，查询报表数据时，出现异常：";
	}
}
