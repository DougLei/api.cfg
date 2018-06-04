package com.king.tooth.sys.service.cfg;

import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [配置系统]表数据信息资源对象处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class CfgTabledataService extends AbstractResourceService{

	/**
	 * 根据表id，获取其表的所有信息，包括列的信息集合
	 * @param tableId
	 * @return
	 */
	private CfgTabledata getTableAllByTableId(String tableId){
		CfgTabledata table = (CfgTabledata) HibernateUtil.executeUniqueQueryByHqlArr("from CfgTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableId);
		table.setColumns(HibernateUtil.executeListQueryByHqlArr("from CfgColumndata where isEnabled =1 and tableId =?", tableId));
		return table;
	}
	
	/**
	 * 创建表数据模型
	 * @param tableIdArr
	 */
	public void createTabledataModel(String[] tableIdArr) {
		CfgTabledata table = null;
		for (String tableId : tableIdArr) {
			table = getTableAllByTableId(tableId);
			
			
			table.clear();
		}
	}

	/**
	 * 删除表数据模型
	 * @param tableIdArr
	 */
	public void dropTabledataModel(String[] tableIdArr) {
		for (String tableId : tableIdArr) {
			
		}
	}
}
