package com.king.tooth.sys.service.common;

import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段数据信息资源对象处理器
 * @author DougLei
 */
public class ComColumndataService extends AbstractService{
	
	/**
	 * 添加列
	 * @param column
	 */
	public void saveColumn(ComColumndata column) {
		HibernateUtil.saveObject(column, null);
	}

	/**
	 * 修改列
	 * @param column
	 */
	public void updateColumn(ComColumndata column) {
		HibernateUtil.updateObjectByHql(column, null);
	}
}
