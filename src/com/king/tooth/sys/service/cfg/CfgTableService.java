package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgHibernateHbm;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.cfg.CfgProject;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateHbmUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 表信息表Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Service
public class CfgTableService extends AService {
	
	/**
	 * 验证表名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validTableNameIsExists(CfgTable table) {
//		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgTable where tableName = ? and createUserId = ? and customerId=?", table.getTableName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(), CurrentThreadContext.getCustomerId());
//		if(count > 0){
//			return "您已经创建过相同表名["+table.getTableName()+"]的数据";
//		}
		if(BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).resourceIsExists(table.getResourceName())){
			return "系统中已经存在相同的资源名["+table.getResourceName()+"]的数据，请修改表名";
		}
		return null;
	}
	
	/**
	 * 验证表关联的项目是否存在
	 * @param project
	 * @return operResult
	 */
	private String validTableRefProjIsExists(String projectId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgProject where "+ResourcePropNameConstants.ID+" = ?", projectId);
		if(count != 1){
			return "表关联的，id为["+projectId+"]的项目信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证数据库中是否存在相同表名
	 * @param projectId
	 * @param tableName
	 * @return
	 */
	private String validTableIsExistsInDatabase(String projectId, String tableName) {
		CfgProject project = getObjectById(projectId, CfgProject.class);
		String hql = "select count(tb."+ResourcePropNameConstants.ID+") from " + 
				"CfgDatabase d, CfgProject p, CfgProjectTableLinks pt, CfgTable tb " +
				"where d.id = '"+project.getRefDatabaseId()+"' and d.id = p.refDatabaseId and p.id=pt.leftId and tb.id=pt.rightId and tb.tableName='"+tableName + "'";
		long count = (long) HibernateUtil.executeUniqueQueryByHql(hql, null);
		if(count > 0){
			return "项目关联的数据库中已经存在表名为["+tableName+"]的数据";
		}
		return null;
	}
	
	/**
	 * 保存表
	 * @param table
	 * @return
	 */
	public Object saveTable(CfgTable table) {
		String operResult = validTableNameIsExists(table);
		if(operResult == null){
			String projectId = CurrentThreadContext.getConfProjectId();
			
			operResult = validTableRefProjIsExists(projectId);
			if(operResult == null){
				operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
			}
			if(operResult == null){
				JSONObject tableJsonObject = HibernateUtil.saveObject(table, null);
				String tableId = tableJsonObject.getString(ResourcePropNameConstants.ID);
				
				// 保存表和项目的关联关系
				HibernateUtil.saveDataLinks("CfgProjectTableLinks", projectId, tableId);
				return tableJsonObject;
			}
		}
		return operResult;
	}

	/**
	 * 根据id，获取表脚本资源对象
	 * @param tableId
	 * @return
	 */
	public CfgTable findTableResourceById(String tableId){
		CfgTable table = getObjectById(tableId, CfgTable.class);
		if(table.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的表脚本资源被禁用，请联系管理员");
		}
		return table;
	}
	
	/**
	 * 修改表
	 * @param table
	 * @return
	 */
	public Object updateTable(CfgTable table) {
		CfgTable oldTable = getObjectById(table.getId(), CfgTable.class);
		if(oldTable == null){
			return "没有找到id为["+table.getId()+"]的表对象信息";
		}
		String operResult = null;
		if(!oldTable.getTableName().equals(table.getTableName())){
			operResult = validTableNameIsExists(table);
			if(operResult == null){
				table.setIsBuildModel(0);
				if(StrUtils.isEmpty(oldTable.getOldTableName()) && oldTable.getIsCreated() == 1 && oldTable.getIsBuildModel() == 1){
					table.setOldTableName(oldTable.getTableName());
				}
			}
		}
		
		if(operResult == null){
			String projectId = CurrentThreadContext.getConfProjectId();
			
			if(StrUtils.isEmpty(projectId)){
				return "表关联的项目id不能为空！";
			}
			operResult = validTableRefProjIsExists(projectId);
			if(operResult == null && !oldTable.getTableName().equals(table.getTableName())){
				operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
			}
			
			if(operResult == null){
				if(table.isUpdateResourceInfo(oldTable)){
					BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).updateResourceInfo(table.getId(), table.getResourceName(), table.getRequestMethod(), table.getIsEnabled());
				}
				return HibernateUtil.updateObject(table, null);
			}
		}
		return operResult;
	}
	
	/**
	 * 删除表
	 * @param tableId
	 * @return
	 */
	public String deleteTable(String tableId) {
		CfgTable oldTable = getObjectById(tableId, CfgTable.class);
		if(oldTable == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		
		if(oldTable.getIsBuildModel() == 1){
			return "表["+oldTable.getTableName()+"]已经完成建模，无法删除，请先进行取消建模操作，再进行删除操作";
		}
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks("CfgProjectTableLinks", null, tableId);
		if(datalinks.size() > 1){
			List<Object> projectIds = new ArrayList<Object>(datalinks.size());
			StringBuilder hql = new StringBuilder("select name from CfgProject where ").append(ResourcePropNameConstants.ID).append(" in (");
			for (JSONObject json : datalinks) {
				projectIds.add(json.getString("leftId"));
				hql.append("?,");
			}
			hql.setLength(hql.length() - 1);
			hql.append(")");
			
			List<Object> projNames = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), projectIds);
			projectIds.clear();
			return "该表关联多个项目，无法删除，请先取消和其他项目的关联，关联的项目包括：" + projNames;
		}
		
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgTable where "+ResourcePropNameConstants.ID+" = '"+tableId+"'");
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgColumn where tableId = '"+tableId+"'");
		HibernateUtil.deleteDataLinks("CfgProjectTableLinks", null, tableId);
		// 删除资源
		BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).deleteCfgResource(tableId);
		return null;
	}
	
	/**
	 * 建模
	 * @param tableId
	 * @param deleteTableIds 如果建模失败，要将这些数据删除，主要是drop表
	 * @param dbTableHandler
	 * @return
	 */
	public Object buildModel(String tableId, List<String> deleteTableIds, DBTableHandler dbTableHandler){
		try {
			CfgTable table = getObjectById(tableId, CfgTable.class);
			if(table.getIsBuildModel() == 1){
				return "表["+table.getTableName()+"]已经完成建模，且在无表名被修改、或任何字段信息被修改的情况下，无法重复进行建模操作";
			}
			boolean isNeedInitBasicColumns = false;
			List<CfgColumn> columns = HibernateUtil.extendExecuteListQueryByHqlArr(CfgColumn.class, null, null, "from CfgColumn where tableId =? order by orderCode asc", tableId);
			
			if(table.getType() == CfgTable.SINGLE_TABLE){
				if(table.getIsCreated() == 0){
					// 只记录创建了表的id，修改表的id不能记录，否则如果抛出异常，会将修改表也一并drop掉，不安全
					deleteTableIds.add(tableId);
					table.setColumns(columns);
					// 1、建表
					dbTableHandler.createTable(table, true); // 表信息集合，有可能有关系表
				}else if(table.getIsCreated() == 1 && table.getIsBuildModel() == 0){
					// 删除hbm信息
					HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgHibernateHbm where projectId='"+CurrentThreadContext.getProjectId()+"' and refTableId = '"+table.getId()+"'");
					// 删除资源
					BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).deleteCfgResource(table.getId());
					
					// 判断该表是否存在
					List<String> tableNames = dbTableHandler.filterTable(true, table.getTableName());
					if(tableNames.size() == 0){// 如果不存在，则create
						// 只记录创建了表的id，修改表的id不能记录，否则如果抛出异常，会将修改表也一并drop掉，不安全
						deleteTableIds.add(tableId);
						removeDeleteColumns(columns);
						table.setColumns(columns);
						// 1、建表
						dbTableHandler.createTable(table, true); // 表信息集合，有可能有关系表
					}else{// 如果存在，则update
						tableNames.clear();
						removeDeleteColumns(columns);
						
						String oldTableName = table.getOldTableName();
						if(StrUtils.notEmpty(oldTableName)){// 说明修改了表名
							// 修改表名
							dbTableHandler.reTableName(table.getTableName(), oldTableName);
							// 移除hibernate中之前表的缓存
							HibernateUtil.removeConfig(NamingProcessUtil.tableNameTurnClassName(oldTableName));
						}
						
						// 修改数据库中的列
						dbTableHandler.modifyColumn(table.getTableName(), columns, true);
						table.setColumns(columns);
						isNeedInitBasicColumns = true;
					}
				}else{
					return "建模时，表["+table.getTableName()+"]的isCreated="+table.getIsCreated()+"，isBuildModel="+table.getIsBuildModel()+"。请联系系统后端开发人员";
				}
				
				// 2、插入hbm
				CfgHibernateHbm hbm = new CfgHibernateHbm(table);
				hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
				hbm.setContent(HibernateHbmUtil.createHbmMappingContent(table, isNeedInitBasicColumns));
				HibernateUtil.saveObject(hbm, null);
				
				// 3、插入资源数据
				BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).saveCfgResource(table);
				
				// 4、将hbm配置内容，加入到sessionFactory中
				HibernateUtil.appendNewConfig(hbm.getContent());
			}else if(table.getType() == CfgTable.TABLE_DATATYPE){
				if(table.getIsCreated() == 0){
					table.setColumns(columns);
					dbTableHandler.createTableDataType(table);
				}else if(table.getIsCreated() == 1 && table.getIsBuildModel() == 0){
					dbTableHandler.dropTableDataType(table);
					
					removeDeleteColumns(columns);
					table.setColumns(columns);
					dbTableHandler.createTableDataType(table);
				}else{
					return "建模时，表["+table.getTableName()+"]的isCreated="+table.getIsCreated()+"，isBuildModel="+table.getIsBuildModel()+"。请联系系统后端开发人员";
				}
			}else{
				return "系统目前不支持值为["+table.getType()+"]的表类型，请联系后端开发人员";
			}
			
			// 5、修改表是否创建的状态，以及是否建模的字段值，均改为1，且置空oldTableName字段
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update CfgTable set isCreated=1, isBuildModel =1, oldTableName=null where "+ResourcePropNameConstants.ID+" = ?", tableId);
			
			// 6、修改字段状态，如果操作状态是被删除的，则删除掉数据；其他操作状态的，均改为已创建状态，且置空oldInfoJson字段的值
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgColumn where tableId = ? and operStatus=?", tableId, CfgColumn.DELETED);
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update CfgColumn set operStatus=?, oldInfoJson=null where tableId = ? and operStatus != ?", CfgColumn.CREATED, tableId, CfgColumn.DELETED);
			
			table.clear();
		} catch (Exception e) {
			batchCancelBuildModel(dbTableHandler, deleteTableIds, false);// 如果建模出现异常，要将一起建模操作过的表都删除掉
			return ExceptionUtil.getErrMsg(e);
		}
		
		JSONObject json = new JSONObject(2);
		json.put(ResourcePropNameConstants.ID, tableId);
		return json;
	}
	
	/**
	 * 移除被删除的列对象
	 * @param columns
	 */
	private void removeDeleteColumns(List<CfgColumn> columns){
		for (int i=0;i<columns.size() ;i++) {
			if(columns.get(i).getOperStatus() == CfgColumn.DELETED){
				columns.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * 批量取消建模
	 * <p>目前用在建模失败的时候，要进行的批量撤销，且忽略所有异常</p>
	 * @param dbTableHandler
	 * @param deleteTableIds
	 * @param modifyRelationDatas 是否修改相关数据，例如资源数据，CfgHibernateHbm数据；如果是在建模的时候，这个值应该是false，因为相关数据会被rollback；其他时候，这个值应该是true
	 */
	private void batchCancelBuildModel(DBTableHandler dbTableHandler, List<String> deleteTableIds, boolean modifyRelationDatas) {
		if(deleteTableIds != null && deleteTableIds.size() > 0){
			CfgTable table;
			for (String tableId : deleteTableIds) {
				table = getObjectById(tableId, CfgTable.class);
				cancelBuildModel(dbTableHandler, table, modifyRelationDatas);
			}
		}
	}
	
	/**
	 * 取消建模
	 * @param dbTableHandler
	 * @param table
	 * @param tableId
	 * @param deleteRelationDatas 是否删除相关数据：主要是资源信息，建模状态，以及hbm信息
	 * 											   如果在建模的过程中出现异常，回滚的时候，这个值应该传递为false，因为数据会回滚，所以没必要删除
	 * 											   如果是重新建模，或取消建模，这个值应该传递为true，因为这个是必要操作
	 */
	public Object cancelBuildModel(DBTableHandler dbTableHandler, CfgTable table, String tableId, boolean deleteRelationDatas){
		if(table == null){
			table = getObjectById(tableId, CfgTable.class);
		}
		if(table.getIsCreated() == 0){
			return "表["+table.getTableName()+"]还未建模，无法进行取消建模操作";
		}
		
		String cancelBuildModelResult = cancelBuildModel(dbTableHandler, table, deleteRelationDatas);
		if(cancelBuildModelResult == null){
			JSONObject json = new JSONObject(2);
			json.put(ResourcePropNameConstants.ID, tableId);
			return json;
		}
		return cancelBuildModelResult;
	}
	
	/**
	 * 取消建模
	 * @param dbTableHandler 
	 * @param table
	 * @param deleteRelationDatas 是否删除相关数据：主要是删除资源信息、hbm信息、表的建模状态、字段的状态、同时会将已删除的字段删除掉
	 * 											   如果在建模的过程中出现异常，回滚的时候，这个值应该传递为false，因为数据会回滚，所以没必要删除
	 * 											   如果是重新建模，或取消建模，这个值应该传递为true，因为这个是必要操作
	 */
	private String cancelBuildModel(DBTableHandler dbTableHandler, CfgTable table, boolean deleteRelationDatas){
		if(table.getType() == CfgTable.SINGLE_TABLE){
			// drop表
			dbTableHandler.dropTable(table);
			// 从sessionFactory中移除映射
			HibernateUtil.removeConfig(table.getTableName());
		}else if(table.getType() == CfgTable.TABLE_DATATYPE){
			dbTableHandler.dropTableDataType(table);
		}else{
			return "系统目前不支持值为["+table.getType()+"]的表类型，请联系后端开发人员";
		}
		
		if(deleteRelationDatas){
			String tableId = table.getId();
			// 修改表是否创建的状态，以及是否建模的字段值，均改为0，且置空oldTableName字段
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update CfgTable set isCreated =0, isBuildModel=0, oldTableName=null  where "+ResourcePropNameConstants.ID+" = ?", tableId);
			// 修改字段状态，如果操作状态是被删除的，则删除掉数据；其他操作状态的，均改为待创建状态，且置空oldInfoJson字段的值
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgColumn where tableId = ? and operStatus=?", tableId, CfgColumn.DELETED);
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update CfgColumn set operStatus=?, oldInfoJson=null where tableId = ? and operStatus != ?", CfgColumn.UN_CREATED, tableId, CfgColumn.DELETED);
			
			if(table.getType() == CfgTable.SINGLE_TABLE){
				// 删除hbm信息
				HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgHibernateHbm where projectId=? and refTableId = ?", CurrentThreadContext.getProjectId(), tableId);
				// 删除资源
				BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).deleteCfgResource(tableId);
			}
		}
		return null;
	}
	
	/**
	 * 建立项目和表的关联关系
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String addProjTableRelation(String projectId, String tableId) {
		CfgTable table = getObjectById(tableId, CfgTable.class);
		String operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
		if(operResult == null){
			HibernateUtil.saveDataLinks("CfgProjectTableLinks", projectId, tableId);
		}
		return operResult;
	}
	
	/**
	 * 取消项目和表的关联关系
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String cancelProjTableRelation(String projectId, String tableId) {
		HibernateUtil.deleteDataLinks("CfgProjectTableLinks", projectId, tableId);
		return null;
	}
}
