package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 表数据信息资源对象处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComTabledataService extends AbstractService{

	private ComSysResourceService comSysResourceService = new ComSysResourceService();
	
	/**
	 * 根据表id，获取其表的所有信息，包括列的信息集合
	 * @param tableId
	 * @return
	 */
	private ComTabledata getTableAllById(String tableId){
		ComTabledata table = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComTabledata.class, "from ComTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableId);
		if(table != null){
			table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		}
		return table;
	}
	
	/**
	 * 保存表
	 * @param table
	 * @return
	 */
	public String saveTable(ComTabledata table) {
		return null;
	}

	/**
	 * 修改表
	 * @param table
	 * @return
	 */
	public String updateTable(ComTabledata table) {
		return null;
	}

	/**
	 * 删除表
	 * @param tableId
	 * @return
	 */
	public String deleteTable(String tableId) {
		return null;
	}
	
	
	
	//--------------------------------------------------------
	
	
	
	/**
	 * 创建表数据模型
	 * @param tableIdArr
	 */
	public void createTableModel(String[] tableIdArr) {
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		ComTabledata table = null;
		ComHibernateHbm hbm = null;
		List<ComTabledata> tabledatas = new ArrayList<ComTabledata>(tableIdArr.length);
		List<String> hbmContents = new ArrayList<String>(tableIdArr.length);
		for (String tableId : tableIdArr) {
			table = getTableAllById(tableId);
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
			
			HibernateUtil.updateObject(table, null);
			
			comSysResourceService.insertSysResource(table);
		}
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
		
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
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
		List<ComTabledata> tabledatas = new ArrayList<ComTabledata>(tableIdArr.length);
		
		ComTabledata tmpTable;
		Map<String, Object> tableInfo = null;
		StringBuilder in = new StringBuilder(" in (");
		for (int i=0;i<len;i++) {
			in.append("?").append(",");
			tableInfo = (Map<String, Object>) HibernateUtil.executeUniqueQueryByHqlArr("select tableName,resourceName from ComTabledata where isBuiltin=1 and isCreateHbm =0 and id =?", tableIdArr[i]);
			entityNames.add(tableInfo.get("tableName")+"");
			
			tmpTable = new ComTabledata();
			tmpTable.setResourceName(tableInfo.get("resourceName")+"");
			tabledatas.add(tmpTable);
			
			tableInfo.clear();
		}
		in.setLength(in.length()-1);
		in.append(")");
		
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete ComHibernateHbm where tableId " + in, tableIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete com_sys_resource where ref_resource_id " + in, tableIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.UPDATE, "update ComTabledata set isCreateHbm=0 where id " + in, tableIdArr);
		HibernateUtil.removeConfig(entityNames);
		entityNames.clear();
		
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
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