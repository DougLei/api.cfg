package com.api.web.builtin.method.common.util.resulttype.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.api.util.StrUtils;
import com.api.web.builtin.method.common.util.resulttype.IResultType;
import com.api.web.builtin.method.sqlresource.query.SelectNaming;
import com.api.web.entity.resulttype.TextResultEntity;

/**
 * _resultType=Text
 * <p>文本</p>
 * <p>这将返回一个包含多行数据的字符串文本，其中每行字符串的形式为 <Value1><Split><Value2><Split><Value2>...，其中一个或多个Value由 _select 参数指定，若没有指定，则取所请求的原始资源类型的所有属性。Split 由参数 _split 指定，默认为","(逗号)，行于行之间由回车换行符(\r\n)分隔。如下：</p>
 * <p>  "Data":{
				"Result":
						  "用户180322153922,180322153922[这里是换行符\n]
						          用户180315153407,180315153407[这里是换行符\n]
						          用户8,nb"
		       }
   </p>
 * @author DougLei
 */
public class TextResultType implements IResultType{

	public boolean toSql(String[] columnArr, SelectNaming[] selectNamingArr, String split, StringBuilder sql) {
		if(selectNamingArr != null && selectNamingArr.length > 0){
			// 如果用户指定了查询的属性，则拼装对应的语句
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
			// 如果用户指定了查询的属性，则拼装对应的语句
			for (String prop : propArrCopyOnlyPropName) {
				hql.append(prop).append(",");
			}
			hql.setLength(hql.length()-1);
		}else{
			// hql.append(" * "); // hibernate 不支持select * 这种格式，查询全表使用 from EntityName即可
			hql.setLength(0);
		}
		return true;
	}

	// 处理查询到的数据结果集合【将查询到的集合中的每个对象的属性值，按照split，拼接成一个字符串；再通过\n，将每个对象，拼接成一个大的字符串】
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList, String split) {
		List list = new ArrayList();
		split = processSplitDefaultValue(split);
		StringBuilder valueStr = new StringBuilder();// 存储每条数据的拼接结果
		
		Object obj = dataList.get(0);
		if(obj instanceof Object[]){
			// object[]类型，表示查询的是指定属性
			processObjectArrType(dataList, split, valueStr);
		}else if(obj instanceof Map){
			// map类型，表示查询的是全表属性
			processMapType(dataList, split, valueStr);
		}
		
		TextResultEntity textResult = new TextResultEntity(); 
		textResult.setResult(valueStr);
		list.add(textResult);
		
		dataList.clear();
		return list;
	}
	
	/**
	 * 处理map类型，表示查询的是全表属性
	 * @param dataList
	 * @param split
	 * @param valueStr
	 */
	private void processMapType(List<Map<String, Object>> dataList, String split, StringBuilder valueStr) {
		Collection<Object> values = null;// 每条数据的value集合
		for (Map<String, Object> map : dataList) {
			values = map.values();
			if(values != null && values.size() > 0){
				for (Object val : values) {
					valueStr.append(val).append(split);
				}
				valueStr.setLength(valueStr.length()-split.length());
				valueStr.append("\n");
				
				// 清空内存
				values.clear();
			}
		}
	}
	
	/**
	 * 处理object[]类型，表示查询的是指定属性
	 * @param dataList
	 * @param split
	 * @param valueStr
	 */
	@SuppressWarnings("rawtypes")
	private void processObjectArrType(List dataList, String split, StringBuilder valueStr) {
		int len = dataList.size();
		Object[] objs = null;
		for(int i = 0; i<len ; i++){
			objs = (Object[]) dataList.get(i);
			if(objs != null && objs.length > 0){
				for(Object obj : objs){
					valueStr.append(obj).append(split);
				}
				valueStr.setLength(valueStr.length()-split.length());
				valueStr.append("\n");
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
