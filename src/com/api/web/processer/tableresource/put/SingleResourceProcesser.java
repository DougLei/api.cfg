package com.api.web.processer.tableresource.put;

import com.api.util.hibernate.HibernateUtil;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends PutProcesser {

	public String getProcesserName() {
		return "【Put-TableResource】SingleResourceProcesser";
	}
	
	protected boolean doPutProcess() {
		// 遍历提交的数据，拼装update语句，更新数据
		for(int i=0; i < json.size(); i++){
			HibernateUtil.updateObject(requestBody.getRouteBody().getResourceName(), json.get(i), builtinQueryCondMethodProcesser.getHql().toString(), hqlParameterValues);
		}
		installResponseBodyForUpdateData(null, json.getJson());
		return true;
	}
}
