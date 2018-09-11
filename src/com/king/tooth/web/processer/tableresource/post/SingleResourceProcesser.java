package com.king.tooth.web.processer.tableresource.post;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.util.column.code.rule.ColumnCodeRuleUtil;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends PostProcesser {

	protected boolean doPostProcess() {
		JSONObject data = null;
		for(int i=0; i < json.size(); i++){
			data = json.get(i);
			ColumnCodeRuleUtil.setFinalCodeVal(data, i, requestBody.getResourcePropCodeRule());
			saveData(requestBody.getRouteBody().getResourceName(), data);
		}
		
		installResponseBodyForSaveData(null, json.getJson(), true);
		return true;
	}

	public String getProcesserName() {
		return "【Post-TableResource】SingleResourceProcesser";
	}
}
