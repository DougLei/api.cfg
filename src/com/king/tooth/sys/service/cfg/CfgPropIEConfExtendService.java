package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.cfg.CfgPropIEConfExtend;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 属性导入导出配置扩展表Service
 * @author DougLei
 */
@Service
public class CfgPropIEConfExtendService extends AService{
	
	/**
	 * 保存属性导入导出的扩展配置
	 * @param propIEConfExtend
	 * @return
	 */
	public Object savePropIEConfExtend(CfgPropIEConfExtend propIEConfExtend) {
		return HibernateUtil.saveObject(propIEConfExtend, null);
	}

	/**
	 * 修改属性导入导出的扩展配置
	 * @param propIEConfExtend
	 * @return
	 */
	public Object updatePropIEConfExtend(CfgPropIEConfExtend propIEConfExtend) {
		return HibernateUtil.updateObject(propIEConfExtend, null);
	}

	/**
	 * 删除属性导入导出的扩展配置
	 * @param propIEConfExtendIds
	 * @return
	 */
	public String deletePropIEConfExtend(String propIEConfExtendIds) {
		return deleteDataById("CfgPropIEConfExtend", propIEConfExtendIds);
	}
}
