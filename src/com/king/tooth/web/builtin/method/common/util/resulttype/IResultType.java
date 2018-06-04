package com.king.tooth.web.builtin.method.common.util.resulttype;

import java.util.List;
import java.util.Map;

import com.king.tooth.web.builtin.method.sqlresource.query.SelectNaming;

/**
 * _resultType处理接口
 * @author DougLei
 */
public interface IResultType {

	/**
	 * 拼装hql语句
	 * <p>方法的参数，都根据子类的情况，自定是否使用</p>
	 * @param propArr 属性数组，包括 as xxx别名
	 * @param propArrCopyOnlyPropName 属性数组拷贝，不包括 as xxx 别名 
	 * @param split 分隔符
	 * @param hql 通过引用传递，将拼装的数据库脚本语句返回给调用者
	 * @return 是否需要再最后处理查询到的数据结果集合
	 *         例如，Text类型，需要将查询到的数据结果集合，组装成一个大的字符串  @see TextResultType
	 */
	public boolean toHql(String[] propArr, String[] propArrCopyOnlyPropName, String split, StringBuilder hql);
	
	/**
	 * 拼装sql语句
	 * <p>方法的参数，都根据子类的情况，自定是否使用</p>
	 * @param columnArr 列名数组，包括 as xxx别名
	 * @param selectNamingArr 查询的列命名对象
	 * @param split 分隔符
	 * @param sql 通过引用传递，将拼装的数据库脚本语句返回给调用者
	 * @return 是否需要再最后处理查询到的数据结果集合
	 *         例如，Text类型，需要将查询到的数据结果集合，组装成一个大的字符串  @see TextResultType
	 */
	public boolean toSql(String[] columnArr, SelectNaming[] selectNamingArr, String split, StringBuilder sql);

	/**
	 * 二次处理查询的数据结果集合
	 * @param dataList
	 * @param split 
	 * @return
	 */
	public List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList, String split);
}
