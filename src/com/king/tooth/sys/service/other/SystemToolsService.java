package com.king.tooth.sys.service.other;

import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 系统工具类的Service
 * @author DougLei
 */
public class SystemToolsService {
	
	/**
	 * 监听hibernate类元数据
	 * @param resourceNameArr
	 * @return
	 */
	public Object monitorHibernateClassMetadata(String[] resourceNameArr){
		return HibernateUtil.getHibernateClassMetadatas(resourceNameArr);
	}
}