package com.king.tooth.web.builtin.method.common.util.resulttype.impl;

import java.util.List;
import java.util.Map;

import com.king.tooth.web.builtin.method.common.util.resulttype.IResultType;
import com.king.tooth.web.builtin.method.sqlresource.query.SelectNaming;

/**
 * _resultType=
 * <p>即_resultType=的值不写，或者压根不写_resultType=</p>
 * <p>(匿名类型），由 _select 指定的一个或多个属性组成的匿名类型，即只与_select的值有关系</p>
 * <p>这将返回一个对象数组或单个对象，其中每个对象的属性由 _select 参数指定。如： GET http://.../Res/AppUser?_select=Name as 用户名,NickName 。如下：</p>
 * <p> "Data" :[
					{
				      "用户名":"张三",
				      "NickName":"会飞的猪"
				    },
				    {
				      "用户名":"李四",
				      "NickName":"哆啦A梦"
				    }
			   ]
   </p>
 * @author DougLei
 */
public class AnonymousResultType implements IResultType{

	public boolean toSql(String[] columnArr, SelectNaming[] selectNamingArr, String split, StringBuilder sql) {
		// 如果用户指定了查询的列名，则拼装对应的语句，完成查询
		if(columnArr != null && columnArr.length > 0){
			for (String col : columnArr) {
				sql.append(col).append(",");
			}
			sql.setLength(sql.length()-1);
			sql.append(" ") ;
		}else{
			// 否则，就查询全部数据
			sql.append(" * "); 
		}
		return false;
	}
	
	public boolean toHql(String[] propArr, String[] propArrCopyOnlyPropName, String split, StringBuilder hql) {
		// 如果用户指定了查询的属性，则拼装对应的语句，完成查询[这个不用分隔符，原因可以参看下面的main方法，查询的结果实际类似于一个map对象，所以split的值根本用不上]
		if(propArr != null && propArr.length > 0){
			hql.append(" new map(");
			for (String prop : propArr) {
				hql.append(prop).append(",");
			}
			hql.setLength(hql.length()-1);
			hql.append(") ") ;
		}else{
			// 否则，就查询全部数据
			// hql.append(" * "); // hibernate 不支持select * 这种格式，查询全表使用 from EntityName即可
			hql.setLength(0);
		}
		return false;
	}
	
	public List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList, String split) {
		return dataList;
	}
	
//	public static void main(String[] args) {
//		Map<String, String> m = new HashMap<String, String>();
//		m.put("姓名", "石磊");
//		m.put("Code", "123");
//		System.out.println(JSONObjectExtend.toJSON(m));
//	}
}
