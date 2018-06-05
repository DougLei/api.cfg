package com.king.tooth.sys.service.cfg;

import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [配置系统]字段数据信息资源对象处理器
 * @author DougLei
 */
public class CfgColumndataService extends AbstractResourceService{

	/**
	 * 添加列
	 * @param column
	 */
	public void saveColumn(CfgColumndata column) {
		HibernateUtil.saveObject(column, null);
	}

	/**
	 * 修改列
	 * @param column
	 */
	public void updateColumn(CfgColumndata column) {
		HibernateUtil.updateObject(column, null);
	}
}
