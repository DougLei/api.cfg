package com.king.tooth.web.processer.tableresource.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

/**
 * 处理这种请求路径格式的处理器：/Values/{resourceType}/{propName}
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public final class SingleResourceValuesProcesser extends GetProcesser {

	public String getProcesserName() {
		return "【Get-TableResource】SingleResourceValuesProcesser";
	}

	protected boolean doGetProcess() {
		List<Map<String, Object>> dataList = null;// 存储最终查询的资源属性通用代码数据
		String queryHql = getFromHql().toString();
		Query query = createQuery(queryHql);
		
		List<Object[]> dataObjectArr = query.list();// 查询
		if(dataObjectArr != null && dataObjectArr.size() > 0){
			dataList = new ArrayList<Map<String, Object>>();
			Map<String, Object> tmp = null;
			for (Object[] data : dataObjectArr) {
				tmp = new HashMap<String, Object>();
				tmp.put("name", data[0]);
				tmp.put("value", data[1]);
				dataList.add(tmp);
			}
			dataObjectArr.clear();
		}
		installResponseBodyForQueryDataList(null, dataList, null);
		return false;
	}
	
	protected StringBuilder getFromHql() {
		hqlParameterValues.clear();// 清空所有的查询条件值
		
		StringBuilder hql = new StringBuilder();
		hql.append("select caption, val from SysDataDictionary where code = ? and isEnabled = 1 and isDelete=0 order by orderCode asc");
		String queryValue = requestBody.getRouteBody().getResourceName()+"."+requestBody.getRouteBody().getPropName();
		hqlParameterValues.add(queryValue.toLowerCase());
		return hql;
	}
}