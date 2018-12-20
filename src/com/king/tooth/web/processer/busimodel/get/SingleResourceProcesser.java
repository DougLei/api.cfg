package com.king.tooth.web.processer.busimodel.get;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.httpclient.HttpClientUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.busimodel.RequestProcesser;

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
	private Map<String, Object> urlParams;
	private String exceptionDesc;
	
	private void init(){
		requestURL = requestBody.getRequestURL();
		headers.put("_token", requestBody.getToken());
		urlParams = getAllUrlParams();
	}
	
	private Map<String, Object> getAllUrlParams(){
		exceptionDesc = "在调用业务模型资源["+requestBody.getResourceInfo().getBusiModel().getResourceName()+"]，查询报表数据时，出现异常：";
		Map<String, String> tmpUrlParams = requestBody.getAllUrlParams();
		tmpUrlParams.remove(BuiltinParameterKeys.RESOURCE_NAME);// 在解析的时候会放过来，这里把他移除
		Map<String, Object> urlParams = new HashMap<String, Object>(tmpUrlParams);
		tmpUrlParams.clear();
		return urlParams;
	}
	
	protected boolean doProcess() {
		init();
		List<CfgBusiModelResRelations> busiModelResRelationsList = requestBody.getResourceInfo().getBusiModel().getBusiModelResRelationsList();
		
		int size = busiModelResRelationsList.size();
		JSONObject json = new JSONObject(size);
		JSONObject resultJson = null;
		try {
			for(int i=0;i<size;i++){
				resultJson = JsonUtil.parseJsonObject(HttpClientUtil.doGetBasic(requestURL + "/common/" + busiModelResRelationsList.get(i).getRefResourceName(), urlParams, headers));
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
}
