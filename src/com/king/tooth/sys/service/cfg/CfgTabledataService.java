package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgHibernateHbm;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [配置系统]表数据信息资源对象处理器
 * @author DougLei
 */
public class CfgTabledataService extends AbstractResourceService{

	private ComSysResourceService comSysResourceService = new ComSysResourceService();
	
	/**
	 * 根据表id，获取其表的所有信息，包括列的信息集合
	 * @param tableId
	 * @return
	 */
	private CfgTabledata getTableAllByTableId(String tableId){
		CfgTabledata table = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgTabledata.class, "from CfgTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableId);
		if(table != null){
			table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(CfgColumndata.class, "from CfgColumndata where isEnabled =1 and tableId =?", tableId));
		}
		return table;
	}
	
	/**
	 * 创建表数据模型
	 * @param tableIdArr
	 */
	public void createTableModel(String[] tableIdArr) {
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		CfgTabledata table = null;
		CfgHibernateHbm hbm = null;
		for (String tableId : tableIdArr) {
			table = getTableAllByTableId(tableId);
			if(table == null){
				continue;
			}
			hbm = new CfgHibernateHbm();
			hbm.setTableId(table.getId());
			hbm.setHbmContent(hibernateHbmHandler.createHbmMappingContent(table));
			HibernateUtil.saveObject(hbm, null);
			
			table.clear();
			table.setIsCreateHbm(1);
			HibernateUtil.updateObject(table, null);
			
			comSysResourceService.insertSysResource(table);
			
			HibernateUtil.appendNewConfig(hbm.getHbmContent());
		}
	}

	/**
	 * 删除表数据模型
	 * @param tableIdArr
	 */
	public void dropTableModel(Object[] tableIdArr) {
		int len = tableIdArr.length;
		List<String> entityNames = new ArrayList<String>(len);
		StringBuilder in = new StringBuilder(" in (");
		for (int i=0;i<len;i++) {
			in.append("?").append(",");
			entityNames.add(HibernateUtil.executeUniqueQueryByHqlArr("select resourceName from CfgTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableIdArr[i])+"");
		}
		in.setLength(in.length()-1);
		in.append(")");
		
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete CfgHibernateHbm where tableId " + in, tableIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete com_sys_resource where ref_resource_id " + in, tableIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.UPDATE, "update CfgTabledata set isCreateHbm=0 where id " + in, tableIdArr);
		HibernateUtil.removeConfig(entityNames);
		entityNames.clear();
	}

	//--------------------------------------------------------
	
	/**
	 * 发布表
	 * @return
	 */
	public void deployingTable(String[] tableIdArr) {
		
	}

	/**
	 * 删除表，即删模
	 * @return
	 */
	public void cancelDeployingTable(String[] tableIdArr) {
		
	}
}
