package com.king.tooth.sys.service;

import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 服务处理器的抽象类
 * @author DougLei
 */
public abstract class AbstractService {

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
		
		String className = clazz.toString();
		className = className.substring(className.lastIndexOf(".")+1);
		return HibernateUtil.extendExecuteUniqueQueryByHqlArr(clazz, "from "+className +" where id=?", id);
	}
}
