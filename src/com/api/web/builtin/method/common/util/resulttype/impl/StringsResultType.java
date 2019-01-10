package com.api.web.builtin.method.common.util.resulttype.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.api.util.StrUtils;
import com.api.web.builtin.method.common.util.resulttype.IResultType;
import com.api.web.builtin.method.sqlresource.query.SelectNaming;

/**
 * _resultType=Strings
 * <p>字符串列表</p>
 * <p>这将返回一个字符串列表，其中每个字符串的形式为 <Value1><Split><Value2><Split><Value2>...，其中一个或多个Value由 _select 参数指定，若没有指定，则取所请求的原始资源类型的所有属性。Split 由参数 _split 指定，默认为","(逗号)。如下：</p>
 * <p> "Data":[
			     "用户180322153922,180322153922",
			     "用户180315153434,180315153434"
			  ]
   </p>
 * @author DougLei
 */
public class StringsResultType implements IResultType{

	public boolean toSql(String[] columnArr, SelectNaming[] selectNamingArr, String split, StringBuilder sql) {
		if(selectNamingArr != null && selectNamingArr.length > 0){
			// 如果用户指定了查询的属性，则拼装对应的语句，完成查询
			for (SelectNaming sn : selectNamingArr) {
				sql.append(sn.getSelectName()).append(",");
			}
			sql.setLength(sql.length()-1);
		}else{
			 sql.append(" * "); 
		}
		return true;
	}
	
	public boolean toHql(String[] propArr, String[] propArrCopyOnlyPropName, String split, StringBuilder hql) {
		if(propArrCopyOnlyPropName != null && propArrCopyOnlyPropName.length > 0){
			// 如果用户指定了查询的属性，则拼装对应的语句，完成查询
			for (String prop : propArrCopyOnlyPropName) {
//				hql.append(prop).append("||'").append(split).append("'||"); // sqlserver多个字段拼接查询，如果有一个字段为null,则会导致整个值都为null，所以这里不做直接查询处理，后续对数据进行二次处理
				hql.append(prop).append(",");
			}
			hql.setLength(hql.length()-1);
		}else{
			// hql.append(" * "); // hibernate 不支持select * 这种格式，查询全表使用 from EntityName即可
			hql.setLength(0);
		}
		return true;
	}
	
	// 最后处理查询到的数据结果集合【将查询到的集合中的每个对象的属性值，按照split，拼接成多个字符串】
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList, String split) {
		List list = new ArrayList();
		split = processSplitDefaultValue(split);
		
		Object obj = dataList.get(0);
		if(obj instanceof Object[]){
			// object[]类型，表示查询的是指定属性
			processObjectArrType(dataList, split, list);
		}else if(obj instanceof Map){
			// map类型，表示查询的是全表属性
			processMapType(dataList, split, list);
		}
		
		dataList.clear();
		return list;
	}
	
	/**
	 * 处理map类型，表示查询的是全表属性
	 * @param dataList
	 * @param split
	 * @param list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processMapType(List<Map<String, Object>> dataList, String split, List list) {
		StringBuilder valueStr = null;// 存储每条数据的拼接结果
		Collection<Object> values = null;// 每条数据的value集合
		for (Map<String, Object> map : dataList) {
			values = map.values();
			if(values != null && values.size() > 0){
				valueStr = new StringBuilder();
				for (Object val : values) {
					valueStr.append(val).append(split);
				}
				valueStr.setLength(valueStr.length()-split.length());
				list.add(valueStr);
				
				// 清空内存
				values.clear();
			}
		}
	}
	
	/**
	 * 处理object[]类型，表示查询的是指定属性
	 * @param dataList
	 * @param split
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processObjectArrType(List dataList, String split, List list) {
		StringBuilder valueStr = null;// 存储每条数据的拼接结果
		int len = dataList.size();
		Object[] objs = null;
		for(int i = 0; i<len ; i++){
			objs = (Object[]) dataList.get(i);
			if(objs != null && objs.length > 0){
				valueStr = new StringBuilder();
				for(Object obj : objs){
					valueStr.append(obj).append(split);
				}
				valueStr.setLength(valueStr.length()-split.length());
				list.add(valueStr);
			}
		}
	}
	
	/**
	 * 处理split的默认值
	 * @param split
	 * @return
	 */
	protected String processSplitDefaultValue(String split){
		if(StrUtils.isEmpty(split)){
			split = ",";
		}
		return split;
	}
}
