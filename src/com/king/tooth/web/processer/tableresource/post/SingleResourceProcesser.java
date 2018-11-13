package com.king.tooth.web.processer.tableresource.post;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends PostProcesser {

	protected boolean doPostProcess() {
		JSONObject data = null;
		String resourceName = requestBody.getRouteBody().getResourceName();
		for(int i=0; i < json.size(); i++){
			data = json.get(i);
			PropCodeRuleUtil.setTableResourceFinalCodeVal(data, i, requestBody.getResourcePropCodeRule());
			HibernateUtil.saveObject(resourceName , data, null);
		}
		installResponseBodyForSaveData(null, json.getJson());
		return true;
	}
	
	public String getProcesserName() {
		return "【Post-TableResource】SingleResourceProcesser";
	}
}
