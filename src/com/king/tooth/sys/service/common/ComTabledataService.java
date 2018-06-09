package com.king.tooth.sys.service.common;

import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 表数据信息资源对象处理器
 * @author DougLei
 */
public class ComTabledataService extends AbstractService{

	/**
	 * 验证表名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validTableNameIsExists(ComTabledata table) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComTabledata where tableName = ? and createUserId = ?", table.getTableName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(count > 0){
			return "表名为["+table.getTableName()+"]的表信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存表
	 * @param table
	 * @return
	 */
	public String saveTable(ComTabledata table) {
		String operResult = validTableNameIsExists(table);
		if(operResult == null){
			HibernateUtil.saveObject(table, null);
		}
		return operResult;
	}

	/**
	 * 修改表
	 * @param table
	 * @return
	 */
	public String updateTable(ComTabledata table) {
		ComTabledata oldTable = getObjectById(table.getId(), ComTabledata.class);
		if(oldTable == null){
			return "没有找到id为["+table.getId()+"]的表对象信息";
		}
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		
		String operResult = null;
		if(!isPlatformDeveloper && !oldTable.getTableName().equals(table.getTableName())){
			if(oldTable.getIsDeployed() == 1){
				return "该表已经发布，不能修改表名，或取消发布后再修改";
			}
			operResult = validTableNameIsExists(table);
		}
		
		if(operResult == null){
			if(isPlatformDeveloper){
				table.setIsCreated(0);// 只要修改表信息，就要重新建模
			}
			HibernateUtil.updateObjectByHql(table, null);
		}
		return operResult;
	}

	/**
	 * 删除表
	 * @param tableId
	 * @return
	 */
	public String deleteTable(String tableId) {
		ComTabledata oldTable = getObjectById(tableId, ComTabledata.class);
		if(oldTable == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		
		if(!isPlatformDeveloper && oldTable.getIsDeployed() == 1){
			return "该表已经发布，无法删除，请先取消发布";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComTabledata where id = '"+tableId+"'");
		
		// 如果是平台开发者账户，则需删除资源信息，要删表，以及映射文件数据，并从当前的sessionFacotry中移除
		if(isPlatformDeveloper){
			// 删除hbm信息
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComHibernateHbm where refTableId = '"+tableId+"'");
			
			// 删除资源
			new ComSysResourceService().deleteSysResource(tableId);
			
			// drop表
			DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			dbTableHandler.dropTable(oldTable);
			
			// 从sessionFactory中移除映射
			HibernateUtil.removeConfig(oldTable.getEntityName());
		}
		return null;
	}
	
	/**
	 * 建模
	 * <p>如果是平台开发者账户，则需要添加资源信息，建表，以及映射文件数据，并添加到当前的sessionFacotry中</p>
	 * @param tableId
	 * @return
	 */
	public String buildModel(String tableId){
		ComTabledata table = getObjectById(tableId, ComTabledata.class);
		if(table == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		
		// 获的hbm内容
		HibernateHbmHandler hbmHandler = new HibernateHbmHandler();
		String hbmContent = hbmHandler.createHbmMappingContent(table);
		
		// 1、建表
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
		dbTableHandler.createTable(table);
		table.clear();
		
		// 2、插入hbm
		ComHibernateHbm hbm = new ComHibernateHbm();
		hbm.tableTurnToHbm(table);
		hbm.setHbmContent(hbmContent);
		HibernateUtil.saveObject(hbm, null);
		
		// 3、插入资源数据
		new ComSysResourceService().saveSysResource(table);
		
		// 4、将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbmContent);
		
		// 5、修改表是否创建的状态
		HibernateUtil.executeUpdateBySql(SqlStatementType.UPDATE, "update com_tabledata set is_created = 1 where id = '"+tableId+"'", null);
		return null;
	}
}
