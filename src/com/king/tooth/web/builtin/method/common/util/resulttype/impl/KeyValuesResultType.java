package com.king.tooth.web.builtin.method.common.util.resulttype.impl;

import java.util.List;
import java.util.Map;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DBUtil;
import com.king.tooth.web.builtin.method.common.util.resulttype.IResultType;
import com.king.tooth.web.builtin.method.sqlresource.query.SelectNaming;

/**
 * _resultType=KeyValues
 * <p>键值对列表</p>
 * <p>这将返回一个字符串列表，其中每个字符串的形式为 <Key>=<Value>，其中Key和Value由 _select 参数指定，若没有指定，则取所请求的原始资源类型的前两个属性。如下：</p>
 * <p> "Data":[
			    "用户180322153922=180322153922",
			    "用户180315153434=180315153434"
			  ]
   </p>
 * @author DougLei
 */
public class KeyValuesResultType implements IResultType{

	public boolean toSql(String[] columnArr, SelectNaming[] selectNamingArr, String split, StringBuilder sql) {
		String column1 = "'column1'", column2 = "'column2'";
		if(selectNamingArr != null && selectNamingArr.length > 0){
			column1 = selectNamingArr[0].getSelectName();
			if(selectNamingArr.length > 1){
				column2 = selectNamingArr[1].getSelectName();
			}else{
				column2 = column1;
			}
		}
		
		split = processSplitDefaultValue(split);
		String strAppendCharacter = DBUtil.getStrAppendCharacter();
		sql.append(column1).append(strAppendCharacter).append("'").append(split).append("'").append(strAppendCharacter).append(column2);
		return false;
	}
	
	public boolean toHql(String[] propArr, String[] propArrCopyOnlyPropName, String split, StringBuilder hql) {
		String prop1 = ResourceNameConstants.ID, prop2 = ResourceNameConstants.ID;
		if(propArrCopyOnlyPropName != null && propArrCopyOnlyPropName.length > 0){
			prop1 = propArrCopyOnlyPropName[0];
			if(propArrCopyOnlyPropName.length >= 2){
				prop2 = propArrCopyOnlyPropName[1];
			}else{
				prop2 = prop1;
			}
		}
		
		split = processSplitDefaultValue(split);
		hql.append(prop1).append("||'").append(split).append("'||").append(prop2);
		return false;
	}
	
	public List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList, String split) {
		return dataList;
	}
	
	/**
	 * 处理split的默认值
	 * @param split
	 * @return
	 */
	protected String processSplitDefaultValue(String split){
		if(StrUtils.isEmpty(split)){
			split = "=";
		}
		return split;
	}
}
