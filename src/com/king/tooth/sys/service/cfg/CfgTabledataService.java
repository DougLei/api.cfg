package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.constants.SysDatabaseInstanceConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [配置系统]表数据信息资源对象处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class CfgTabledataService extends AbstractResourceService{

	private ComSysResourceService comSysResourceService = new ComSysResourceService();
	
	/**
	 * 添加表
	 * @param table
	 */
	public void saveTable(CfgTabledata table) {
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().getAccountType() == 0){
			table.setIsBuiltin(1);
		}
		HibernateUtil.saveObject(table, null);
	}
	
	/**
	 * 修改表
	 * @param table
	 */
	public void updateTable(CfgTabledata table) {
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().getAccountType() == 0){
			table.setIsBuiltin(1);
		}
		
		HibernateUtil.updateObject(table, null);
	}
	
	/**
	 * 删除表
	 * @param tableIdArr
	 */
	public void deleteTable(Object[] tableIdArr) {
		int len = tableIdArr.length;
		StringBuilder in = new StringBuilder("");
		if(len == 1){
			in.append(" = ?");
		}else{
			in.append(" in (");
			for (int i=0;i<len;i++) {
				in.append("?").append(",");
			}
			in.setLength(in.length()-1);
			in.append(")");
		}
		
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComSysResource where refResourceId " + in, tableIdArr);
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.UPDATE, "delete CfgTabledata where id " + in, tableIdArr);
	}
	
	//--------------------------------------------------------
	
	/**
	 * 根据表id，获取其表的所有信息，包括列的信息集合
	 * @param tableId
	 * @return
	 */
	private CfgTabledata getTableAllByTableId(String tableId){
		CfgTabledata table = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgTabledata.class, "from CfgTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableId);
		if(table != null){
			table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(CfgColumndata.class, null, null, "from CfgColumndata where isEnabled =1 and tableId =?", tableId));
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
		ComHibernateHbm hbm = null;
		List<CfgTabledata> tabledatas = new ArrayList<CfgTabledata>(tableIdArr.length);
		List<String> hbmContents = new ArrayList<String>(tableIdArr.length);
		for (String tableId : tableIdArr) {
			table = getTableAllByTableId(tableId);
			if(table == null){
				continue;
			}
			tabledatas.add(table);
			
			hbm = new ComHibernateHbm();
//			hbm.setTableId(table.getId());
			hbm.setHbmContent(hibernateHbmHandler.createHbmMappingContent(table));
			HibernateUtil.saveObject(hbm, null);
			hbmContents.add(hbm.getHbmContent());
			
			table.clear();
			table.setIsCreatedResource(1);
			HibernateUtil.updateObject(table, null);
			
			comSysResourceService.insertSysResource(table);
		}
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
		
		DBTableHandler dbTableHandler = new DBTableHandler(SysDatabaseInstanceConstants.CFG_DATABASE);
		dbTableHandler.createTable(tabledatas);
		tabledatas.clear();
	}

	/**
	 * 删除表数据模型
	 * @param tableIdArr
	 */
	public void dropTableModel(Object[] tableIdArr) {
		int len = tableIdArr.length;
		List<String> entityNames = new ArrayList<String>(len);
		List<CfgTabledata> tabledatas = new ArrayList<CfgTabledata>(tableIdArr.length);
		
		CfgTabledata tmpTable;
		Map<String, Object> tableInfo = null;
		StringBuilder in = new StringBuilder(" in (");
		for (int i=0;i<len;i++) {
			in.append("?").append(",");
			tableInfo = (Map<String, Object>) HibernateUtil.executeUniqueQueryByHqlArr("select tableName,resourceName from CfgTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableIdArr[i]);
			entityNames.add(tableInfo.get("tableName")+"");
			
			tmpTable = new CfgTabledata();
			tmpTable.setResourceName(tableInfo.get("resourceName")+"");
			tabledatas.add(tmpTable);
			
			tableInfo.clear();
		}
		in.setLength(in.length()-1);
		in.append(")");
		
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete ComHibernateHbm where tableId " + in, tableIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete com_sys_resource where ref_resource_id " + in, tableIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.UPDATE, "update CfgTabledata set isCreateHbm=0 where id " + in, tableIdArr);
		HibernateUtil.removeConfig(entityNames);
		entityNames.clear();
		
		DBTableHandler dbTableHandler = new DBTableHandler(SysDatabaseInstanceConstants.CFG_DATABASE);
		dbTableHandler.dropTable(tabledatas);
		tabledatas.clear();
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
