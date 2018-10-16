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

	/**
	 * 通用模式
	 * 1是0否
	 */
	private transient static final List<Map<String, Object>> isNot = new ArrayList<Map<String, Object>>();
	static{
		Map<String, Object> is = new HashMap<String, Object>();
		is.put("name", "是");
		is.put("value", 1);
		
		Map<String, Object> not = new HashMap<String, Object>();
		not.put("name", "否");
		not.put("value", 0);
		
		isNot.add(is);
		isNot.add(not);
	}
	
	public String getProcesserName() {
		return "【Get-TableResource】SingleResourceValuesProcesser";
	}

	protected boolean doGetProcess() {
		List<Map<String, Object>> dataList = null;// 存储最终查询的资源属性通用代码数据
		if(requestBody.getRouteBody().getPropName().startsWith("is")){// 标识是否的属性，则使用通用模式：1是0否
			dataList = isNot;
		}else{
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
		}
		installResponseBodyForQueryDataList(dataList, null, true);
		return false;
	}
	
	protected StringBuilder getFromHql() {
		hqlParameterValues.clear();// 清空所有的查询条件值
		
		StringBuilder hql = new StringBuilder();
		hql.append("select codeCaption, codeValue from SysDataDictionary where code = ? and isEnabled = 1 and isDelete=0 order by orderCode asc");
		String queryValue = requestBody.getRouteBody().getResourceName()+"."+requestBody.getRouteBody().getPropName();
		hqlParameterValues.add(queryValue.toLowerCase());
		return hql;
	}
}