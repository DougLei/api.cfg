package com.api.web.processer.busimodel.get;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private String requestURL;
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
		try {
			for(int i=0;i<size;i++){
				Map<String, Object> params = paramsHandler(busiModelResRelationsList.get(i).getRefResourceKeyName());
				resultJson = JsonUtil.parseJsonObject(HttpClientUtil.doGetBasic(requestURL + "/common/" + busiModelResRelationsList.get(i).getRefResourceName(), params, headers));
				if(resultJson.getBooleanValue("isSuccess")){
					json.put(busiModelResRelationsList.get(i).getRefResourceKeyName(), resultJson.remove("data"));
				}else{
					setResponseBody(new ResponseBody(exceptionDesc + resultJson.getString("message"), null));
					return false;
				}
			}
			setResponseBody(new ResponseBody(null, json));
		} catch (Exception e) {
			setResponseBody(new ResponseBody(exceptionDesc + ExceptionUtil.getErrMsg(e), null));
			return false;
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
		requestURL = requestBody.getRequestURL();
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
