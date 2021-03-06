package com.api.sys.service;

import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 服务处理器的抽象类
 * @author DougLei
 */
public abstract class AService {

	/**
	 * 根据id，查询获取对应的数据对象
	 * @param id 
	 * @param clazz 对应的数据类型
	 * @return
	 */
	protected <T> T getObjectById(String id, Class<T> clazz){
		if(StrUtils.isEmpty(id)){
			throw new NullPointerException("根据id，查询获取对应的数据对象时，传入的id值不能为空！");
		}
		
		String className = clazz.getSimpleName();
		T t = HibernateUtil.extendExecuteUniqueQueryByHqlArr(clazz, "from "+className +" where " + ResourcePropNameConstants.ID +"=?", id);
		if(t == null){
			throw new NullPointerException("不存在id值为'"+id+"'的"+className+"数据对象");
		}
		return t;
	}
	
	/**
	 * 根据id删除数据，多个id用,隔开
	 * @param entityName
	 * @param ids
	 * @return
	 */
	protected String deleteDataById(String entityName, String ids){
		String[] idArr = ids.split(",");
		StringBuilder hql = new StringBuilder("delete "+entityName+" where ");
		hql.append(ResourcePropNameConstants.ID);
		if(idArr.length ==1){
			hql.append(" = '").append(idArr[0]).append("'");
		}else if(idArr.length > 1){
			hql.append(" in (");
			for (String columnId : idArr) {
				hql.append("'").append(columnId).append("',");
			}
			hql.setLength(hql.length()-1);
			hql.append(")");
		}else{
			return "删除数据时，传入的id数据数组长度小于1";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, hql.toString());
		hql.setLength(0);
		return null;
	}
}
