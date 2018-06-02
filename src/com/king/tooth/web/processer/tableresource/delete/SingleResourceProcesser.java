package com.king.tooth.web.processer.tableresource.delete;

import org.hibernate.Query;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends DeleteProcesser {

	public String getProcesserName() {
		return "【Delete-TableResource】SingleResourceProcesser";
	}
	
	protected boolean doDeleteProcess() {
		String deleteHql = getDeleteHql().toString();
		Query query = createQuery(deleteHql);
		int deleteRows = query.executeUpdate();
		
		installResponseBodyForDeleteData(deleteRows, null);
		return true;
	}
	
	protected StringBuilder getDeleteHql() {
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ").append(requestBody.getRouteBody().getResourceName())
			.append(builtinQueryCondMethodProcesser.getHql());
		return hql;
	}
}
