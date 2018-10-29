package com.king.tooth.web.processer.tableresource.post;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;
import com.king.tooth.web.entity.request.ResourcePropCodeRule;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends PostProcesser {

	protected boolean doPostProcess() {
		JSONObject data = null;
		for(int i=0; i < json.size(); i++){
			data = json.get(i);
			setFinalCodeVal(data, i, requestBody.getResourcePropCodeRule());
			saveData(requestBody.getRouteBody().getResourceName(), data);
		}
		
		installResponseBodyForSaveData(null, json.getJson());
		return true;
	}
	
	/**
	 * 给对象的属性设置最终的编码值
	 * @param data
	 * @param index
	 * @param resourcePropCodeRule
	 */
	private void setFinalCodeVal(JSONObject data, int index, ResourcePropCodeRule resourcePropCodeRule) {
		if(resourcePropCodeRule == null){
			return;
		}
		
		List<CfgColumnCodeRule> rules = resourcePropCodeRule.getRules();
		if(rules != null && rules.size() > 0){
			for (CfgColumnCodeRule rule : rules) {
				data.put(rule.getRefPropName(), rule.getFinalCodeVal(index));
			}
		}
	}

	public String getProcesserName() {
		return "【Post-TableResource】SingleResourceProcesser";
	}
}
