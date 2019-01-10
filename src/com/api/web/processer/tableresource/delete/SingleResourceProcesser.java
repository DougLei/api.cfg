package com.api.web.processer.tableresource.delete;

import java.util.ArrayList;
import java.util.List;

import com.api.constants.SqlStatementTypeConstants;
import com.api.util.hibernate.HibernateUtil;

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
		List<Object> tmpParameters = new ArrayList<Object>(hqlParameterValues);
		HibernateUtil.executeUpdateByHql(SqlStatementTypeConstants.DELETE, deleteHql, tmpParameters);
		installResponseBodyForDeleteData(null, hqlParameterValues);
		return true;
	}
	
	protected StringBuilder getDeleteHql() {
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ").append(requestBody.getRouteBody().getResourceName())
			.append(builtinQueryCondMethodProcesser.getHql());
		return hql;
	}
}
