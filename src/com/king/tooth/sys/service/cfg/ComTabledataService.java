package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysHibernateHbm;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateHbmUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 表信息表Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Service
public class ComTabledataService extends AbstractPublishService {
	
	/**
	 * 验证表名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validTableNameIsExists(ComTabledata table) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComTabledata where tableName = ? and createUserId = ? and customerId=?", table.getTableName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(), CurrentThreadContext.getCustomerId());
		if(count > 0){
			return "您已经创建过相同表名["+table.getTableName()+"]的数据";
		}
		count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysResource where resourceName = ? and projectId = ? and customerId = ?", table.getResourceName(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(count > 0){
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComProject where id = ?", projectId);
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
		ComProject project = getObjectById(projectId, ComProject.class);
		String hql = "select count(tb."+ResourcePropNameConstants.ID+") from " + 
				"CfgDatabase d, ComProject p, CfgProjectTableLinks pt, ComTabledata tb " +
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
	public Object saveTable(ComTabledata table) {
		String operResult = validTableNameIsExists(table);
		if(operResult == null){
			// TODO 单项目，取消是否平台开发者的判断
//			boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
			
			String projectId = CurrentThreadContext.getConfProjectId();
			
			// TODO 单项目，取消是否平台开发者的判断
//			if(!isDeveloper){// 非平台开发者，建的表一开始，一定要和一个项目关联起来
				operResult = validTableRefProjIsExists(projectId);
				if(operResult == null){
					operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
				}
//			}
			if(operResult == null){
				JSONObject tableJsonObject = HibernateUtil.saveObject(table, null);
				String tableId = tableJsonObject.getString(ResourcePropNameConstants.ID);
				
				// 保存表和项目的关联关系
				// TODO 单项目，取消是否平台开发者的判断
//				if(isDeveloper){
//					HibernateUtil.saveDataLinks("CfgProjectTableLinks", CurrentThreadContext.getProjectId(), tableId);
//				}else{
					HibernateUtil.saveDataLinks("CfgProjectTableLinks", projectId, tableId);
//				}
				return tableJsonObject;
			}
		}
		return operResult;
	}

	/**
	 * 修改表
	 * @param table
	 * @return
	 */
	public Object updateTable(ComTabledata table) {
		ComTabledata oldTable = getObjectById(table.getId(), ComTabledata.class);
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
			// TODO 单项目，取消是否平台开发者的判断
//			boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
			
			String projectId = CurrentThreadContext.getConfProjectId();
			
			// TODO 单项目，取消是否平台开发者的判断
//			if(!isDeveloper){
				if(StrUtils.isEmpty(projectId)){
					return "表关联的项目id不能为空！";
				}
				operResult = validTableRefProjIsExists(projectId);
				if(operResult == null && !oldTable.getTableName().equals(table.getTableName())){
					operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
				}
				
				// TODO 单项目，取消是否平台开发者的判断
//				if(operResult == null && publishInfoService.validResourceIsPublished(null, projectId, oldTable.getId())){
//					return "该表已经发布，不能修改表信息，或取消发布后再修改";
//				}
//			}
			
			if(operResult == null){
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
		ComTabledata oldTable = getObjectById(tableId, ComTabledata.class);
		if(oldTable == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		
		// TODO 单项目，取消是否平台开发者的判断
//		boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
//		if(!isDeveloper){
//			if(publishInfoService.validResourceIsPublished(null, CurrentThreadContext.getConfProjectId(), oldTable.getId())){
//				return "该表已经发布，无法删除，请先取消发布";
//			}
//		}
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks("CfgProjectTableLinks", null, tableId);
		if(datalinks.size() > 1){
			List<Object> projectIds = new ArrayList<Object>(datalinks.size());
			StringBuilder hql = new StringBuilder("select projName from ComProject where id in (");
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
		
		// 如果是平台开发者账户，则需删除资源信息，要删表，以及映射文件数据，并从当前的sessionFacotry中移除
		// TODO 单项目，取消是否平台开发者的判断
//		if(isDeveloper && oldTable.getIsCreated() == 1){
		if(oldTable.getIsCreated() == 1 && oldTable.getIsBuildModel() == 1){
			cancelBuildModel(new DBTableHandler(CurrentThreadContext.getDatabaseInstance()), oldTable, true);
		}
		
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComTabledata where "+ResourcePropNameConstants.ID+" = '"+tableId+"'");
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComColumndata where tableId = '"+tableId+"'");
		HibernateUtil.deleteDataLinks("CfgProjectTableLinks", null, tableId);
		return null;
	}
	
	/**
	 * 建模
	 * @param tableId
	 * @param deleteTableIds 如果建模失败，要将这些数据删除，主要是drop表
	 * @param dbTableHandler
	 * @return
	 */
	public String buildModel(String tableId, List<String> deleteTableIds, DBTableHandler dbTableHandler){
		try {
			ComTabledata table = getObjectById(tableId, ComTabledata.class);
			if(table.getIsBuildModel() == 1){
				return "表["+table.getTableName()+"]已经完成建模，且在无表名被修改、或任何字段信息被修改的情况下，无法重复进行建模操作";
			}
			
			boolean isNeedInitBasicColumns = false;
			List<ComColumndata> columns = HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =? order by orderCode asc", tableId);
			List<ComTabledata> tables = null;
			if(table.getIsCreated() == 0){
				// 只记录创建了表的id，修改表的id不能记录，否则如果抛出异常，会将修改表也一并drop掉，不安全
				deleteTableIds.add(tableId);
				removeDeleteColumns(columns);
				table.setColumns(columns);
				// 1、建表
				tables = dbTableHandler.createTable(table, true); // 表信息集合，有可能有关系表
			}else if(table.getIsCreated() == 1 && table.getIsBuildModel() == 0){
				tables = new ArrayList<ComTabledata>(1);
				tables.add(table);
				
				// 删除hbm信息
				HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysHibernateHbm where projectId='"+CurrentThreadContext.getProjectId()+"' and refTableId = '"+table.getId()+"'");
				// 删除资源
				BuiltinObjectInstance.resourceService.deleteSysResource(table.getId());
				
				// 判断该表是否存在
				List<String> tableNames = dbTableHandler.filterTable(true, table.getTableName());
				if(tableNames.size() == 0){// 如果不存在，则create
					// 只记录创建了表的id，修改表的id不能记录，否则如果抛出异常，会将修改表也一并drop掉，不安全
					deleteTableIds.add(tableId);
					removeDeleteColumns(columns);
					table.setColumns(columns);
					// 1、建表
					tables = dbTableHandler.createTable(table, true); // 表信息集合，有可能有关系表
				}else{// 如果存在，则update
					tableNames.clear();
					
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
			
			List<String> hbmContents = new ArrayList<String>(tables.size());
			SysHibernateHbm hbm;
			int i = 0;
			for (ComTabledata tb : tables) {
				hbmContents.add(HibernateHbmUtil.createHbmMappingContent(tb, isNeedInitBasicColumns));
				
				// 2、插入hbm
				hbm = new SysHibernateHbm();
				hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
				hbm.tableTurnToHbm(tb);
				hbm.setHbmContent(hbmContents.get(i++));
				HibernateUtil.saveObject(hbm, null);
				
				// 3、插入资源数据
				BuiltinObjectInstance.resourceService.saveSysResource(tb);
			}
			// 4、将hbm配置内容，加入到sessionFactory中
			HibernateUtil.appendNewConfig(hbmContents);
			hbmContents.clear();
			
			// 5、修改表是否创建的状态，以及是否建模的字段值，均改为1
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update ComTabledata set isCreated=1, isBuildModel =1 where "+ResourcePropNameConstants.ID+" = '"+table.getId()+"'");
			
			// 6、修改字段状态，如果操作状态是被删除的，则删除掉数据；其他操作状态的，均改为已创建状态，且置空oldInfoJson字段的值
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComColumndata where tableId = '"+table.getId()+"' and operStatus="+ComColumndata.DELETED);
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update ComColumndata set operStatus="+ComColumndata.CREATED+", oldInfoJson=null where tableId = '"+table.getId()+"'");
			
			// 7、置空ComTabledata表中，oldTableName字段的值
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update ComTabledata set oldTableName=null where "+ResourcePropNameConstants.ID+" = '"+table.getId()+"'");
			
			ResourceHandlerUtil.clearTables(tables);
		} catch (Exception e) {
			batchCancelBuildModel(dbTableHandler, deleteTableIds, false);// 如果建模出现异常，要将一起建模操作过的表都删除掉
			return ExceptionUtil.getErrMsg("ComTabledataService", "buildModel", e);
		}
		return null;
	}
	
	/**
	 * 移除被删除的列对象
	 * @param columns
	 */
	private void removeDeleteColumns(List<ComColumndata> columns){
		for (int i=0;i<columns.size() ;i++) {
			if(columns.get(i).getOperStatus() == ComColumndata.DELETED){
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
	 * @param modifyRelationDatas 是否修改相关数据，例如资源数据，SysHibernateHbm数据；如果是在建模的时候，这个值应该是false，因为相关数据会被rollback；其他时候，这个值应该是true
	 */
	private void batchCancelBuildModel(DBTableHandler dbTableHandler, List<String> deleteTableIds, boolean modifyRelationDatas) {
		if(deleteTableIds != null && deleteTableIds.size() > 0){
			ComTabledata table;
			for (String tableId : deleteTableIds) {
				table = getObjectById(tableId, ComTabledata.class);
				cancelBuildModel(dbTableHandler, table, modifyRelationDatas);
			}
		}
	}
	
	/**
	 * 取消建模
	 * @param dbTableHandler 
	 * @param table
	 * @param modifyRelationDatas 是否修改相关数据：
	 * 											如果在建模的过程中出现异常，回滚的时候，这个值应该传递为false，因为数据会回滚，所以没必要删除
	 * 											如果是在建模的过程中，先删除之前的数据，再重新建模，这个值应该传递为true，因为这个是必要操作
	 */
	public void cancelBuildModel(DBTableHandler dbTableHandler, ComTabledata table, boolean modifyRelationDatas){
		// drop表
		String[] tableResourceNames = dbTableHandler.dropTable(table).split(",");
		
		// 从sessionFactory中移除映射
		for (String tableResourceName : tableResourceNames) {
			HibernateUtil.removeConfig(tableResourceName);
		}
		
		if(modifyRelationDatas){
			// 修改表是否创建的状态
			modifyIsCreatedPropVal(table.getEntityName(), 0, table.getId());
			// 删除hbm信息
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysHibernateHbm where projectId='"+CurrentThreadContext.getProjectId()+"' and refTableId = '"+table.getId()+"'");
			// 删除资源
			BuiltinObjectInstance.resourceService.deleteSysResource(table.getId());
		}
	}
	
	/**
	 * 建立项目和表的关联关系
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String addProjTableRelation(String projectId, String tableId) {
		ComTabledata table = getObjectById(tableId, ComTabledata.class);
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
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布表
	 * @param tableId
	 * @return
	 */
	public String publishTable(String tableId) {
		ComTabledata table = getObjectById(tableId, ComTabledata.class);
		if(table == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		if(table.getIsNeedDeploy() == 0){
			return "id为["+tableId+"]的表不该被发布，如需发布，请联系管理员";
		}
		if(table.getIsEnabled() == 0){
			return "id为["+tableId+"]的表信息无效，请联系管理员";
		}
		String projectId = CurrentThreadContext.getConfProjectId();
		if(publishInfoService.validResourceIsPublished(null, projectId, tableId)){
			return "["+table.getTableName()+"]表已经发布，无需再次发布，或取消发布后重新发布";
		}
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "表关联的，id为["+projectId+"]的项目信息不存在";
		}
		if(project.getIsCreated() == 0){
			return "["+table.getTableName()+"]表所属的项目还未发布，请先发布项目";
		}
		
		// 远程过去create表
		CfgDatabase database = getObjectById(project.getRefDatabaseId(), CfgDatabase.class);
		table.setDbType(database.getType());
		String validResult = table.analysisResourceProp();
		if(validResult != null){
			return validResult;
		}
		
		DBTableHandler tableHandler = new DBTableHandler(database);
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		List<ComTabledata> tables = tableHandler.createTable(table, true);
		
		List<SysHibernateHbm> hbms = new ArrayList<SysHibernateHbm>(tables.size());
		for (ComTabledata tb : tables) {
			SysHibernateHbm hbm = new SysHibernateHbm();
			if(tb.getIsDatalinkTable() == 0){
				hbm.setId(tableId);
			}else{
				hbm.setId(ResourceHandlerUtil.getIdentity());
			}
			hbm.tableTurnToHbm(tb);
			hbm.setRefDatabaseId(project.getRefDatabaseId());
			hbm.setProjectId(projectId);
			hbm.setHbmContent(HibernateHbmUtil.createHbmMappingContent(tb, false));
			hbms.add(hbm);
		}
		ResourceHandlerUtil.clearTables(tables);
		
		publishInfoService.deletePublishedData(projectId, tableId);
		executeRemotePublishTable(project.getRefDatabaseId(), projectId, hbms, "CfgProjectHbmLinks");
		hbms.clear();
		
		return usePublishResourceApi(tableId, projectId, "table", "1", projectId);
	}
	
	/**
	 * 执行远程发布操作
	 * @param databaseId  
	 * @param projectId  
	 * @param hbms
	 * @param comProjectSysHibernateHbmLinkResourceName
	 */
	private void executeRemotePublishTable(String databaseId, String projectId, List<SysHibernateHbm> hbms, String comProjectSysHibernateHbmLinkResourceName){
		if(databaseId == null){
			databaseId = ProjectIdRefDatabaseIdMapping.getDbId(projectId);
		}
		
		String errMsg = null;
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			int orderCode = 1;
			JSONObject datalink;
			JSONObject publishEntityJson;
			for (SysHibernateHbm hbm : hbms) {
				if(hbm == null){
					break;
				}
				publishEntityJson = hbm.toPublishEntityJson(projectId);
				session.save(hbm.getEntityName(), publishEntityJson);
				
				datalink = ResourceHandlerUtil.getDataLinksObject(projectId, publishEntityJson.getString(ResourcePropNameConstants.ID), ""+(orderCode++), null, null);
				datalink.put("projectId", projectId);
				datalink.put(ResourcePropNameConstants.ID, ResourceHandlerUtil.getIdentity());
				session.save(comProjectSysHibernateHbmLinkResourceName, datalink);
				
				SysResource csr = hbm.turnToPublishResource(projectId, publishEntityJson.getString(ResourcePropNameConstants.ID));
				session.save(csr.getEntityName(), csr.toEntityJson());
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			errMsg = ExceptionUtil.getErrMsg("ComTabledataService", "executeRemotePublishTable", e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		
		// 添加新的发布信息数据
		DmPublishInfo publishInfo;
		for (SysHibernateHbm hbm : hbms) {
			publishInfo = hbm.turnToPublish();
			if(errMsg == null){
				publishInfo.setIsSuccess(1);
			}else{
				publishInfo.setIsSuccess(0);
				publishInfo.setErrMsg(errMsg);
			}
			HibernateUtil.saveObject(publishInfo, null);
		}
	}
	
	/**
	 * 取消发布表
	 * @param tableId
	 * @return
	 */
	public String cancelPublishTable(String tableId) {
		ComTabledata table = getObjectById(tableId, ComTabledata.class);
		if(table == null){
			return "没有找到id为["+table+"]的表对象信息";
		}
		String projectId = CurrentThreadContext.getConfProjectId();
		if(!publishInfoService.validResourceIsPublished(null, projectId, tableId)){
			return "["+table.getTableName()+"]表未发布，无法取消发布";
		}
		String result = validTableRefProjIsExists(projectId);
		if(result != null){
			return result;
		}
		
		// 远程过去drop表
		ComProject project = getObjectById(projectId, ComProject.class);
		CfgDatabase database = getObjectById(project.getRefDatabaseId(), CfgDatabase.class);
		DBTableHandler tableHandler = new DBTableHandler(database);
		String deleteTableResourceNames = tableHandler.dropTable(table);
		
		executeRemoteUpdate(null, projectId, 
				"delete CfgProjectHbmLinks where projectId='"+projectId+"' and leftId='"+projectId+"' and rightId in (select "+ResourcePropNameConstants.ID+" from "+new SysHibernateHbm().getEntityName()+" where projectId='"+projectId+"' and refDataId = '"+tableId+"')",
				"delete " + new SysHibernateHbm().getEntityName() + " where projectId='"+projectId+"' and refDataId = '"+tableId+"'",
				"delete SysResource where projectId='"+projectId+"' and refDataId = '"+tableId+"'");
		publishInfoService.deletePublishedData(projectId, tableId);
		
		return usePublishResourceApi(deleteTableResourceNames, projectId, "table", "-1", projectId);
	}

	/**
	 * 批量发布表
	 * @param databaseId
	 * @param projectId
	 * @param tableIds
	 */
	public void batchPublishTable(String databaseId, String projectId, List<Object> tableIds) {
		List<ComTabledata> tables = new ArrayList<ComTabledata>(tableIds.size()*2);
		CfgDatabase database = getObjectById(databaseId, CfgDatabase.class);
		ComTabledata table;
		String validResult;
		for (Object tableId : tableIds) {
			table = getObjectById(tableId.toString(), ComTabledata.class);
			if(publishInfoService.validResourceIsPublished(null, projectId, table.getId())){
				Log4jUtil.info("["+table.getTableName()+"]表已经发布，无需再次发布，或取消发布后重新发布");
				continue;
			}
			
			table.setDbType(database.getType());
			validResult = table.analysisResourceProp();
			if(validResult != null){
				Log4jUtil.info("["+table.getTableName()+"]表发布时验证失败：" + validResult);
				continue;
			}
			table.setRefDatabaseId(databaseId);
			tables.add(table);
		}
		
		if(tables.size() == 0){
			Log4jUtil.info("projectId为[{}]的项目，没有要发布的表数据", projectId);
			return;
		}
		publishInfoService.batchDeletePublishedData(null, tableIds);
		
		int limitSize = 40;
		List<SysHibernateHbm> hbms = new ArrayList<SysHibernateHbm>(limitSize);// 记录表对应的hbm内容，要发布的是这个
		
		// 准备远程过去create表
		DBTableHandler tableHandler = new DBTableHandler(database);
		
		List<ComTabledata> tmpTables;// 在创建表的时候，记录每次创建表的数据
		SysHibernateHbm hbm = null;
		StringBuilder tableIdStr = new StringBuilder();
		for(ComTabledata tb : tables){
			if(tb == null){
				break;
			}
			tableIdStr.append(tb.getId()).append(",");
			
			tb.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId ='"+tb.getId()+"'"));
			tmpTables = tableHandler.createTable(tb, true);
			
			for(ComTabledata tempTb : tmpTables) {
				hbm = new SysHibernateHbm();
				if(tempTb.getIsDatalinkTable() == 0){
					hbm.setId(tb.getId());
				}else{
					hbm.setId(ResourceHandlerUtil.getIdentity());
				}
				hbm.tableTurnToHbm(tempTb);
				hbm.setRefDatabaseId(databaseId);
				hbm.setProjectId(projectId);
				hbm.setHbmContent(HibernateHbmUtil.createHbmMappingContent(tempTb, false));
				hbms.add(hbm);
			}
			ResourceHandlerUtil.clearTables(tmpTables);
			tb.clear();
			
			if(hbms.size() == (limitSize-1)){
				executeRemotePublishTable(databaseId, projectId, hbms, "CfgProjectHbmLinks");
				hbms.clear();
			}
		}
		
		if(hbms.size() > 0){
			executeRemotePublishTable(databaseId, projectId, hbms, "CfgProjectHbmLinks");
			hbms.clear();
		}
		tables.clear();
		tableIdStr.setLength(tableIdStr.length()-1);
		
		usePublishResourceApi(tableIdStr.toString(), projectId, "table", "1", projectId);
		tableIdStr.setLength(0);
	}
	
	/**
	 * 批量取消发布表
	 * @param databaseId
	 * @param projectId
	 * @param tableIds
	 */
	public void batchCancelPublishTable(String databaseId, String projectId, List<Object> tableIds) {
		List<ComTabledata> tables = new ArrayList<ComTabledata>(tableIds.size());
		ComTabledata table;
		
		for (Object tableId : tableIds) {
			if(!publishInfoService.validResourceIsPublished(null, projectId, tableId.toString())){
				continue;
			}
			table = getObjectById(tableId.toString(), ComTabledata.class);
			tables.add(table);
		}
		publishInfoService.batchDeletePublishedData(projectId, tableIds);
		
		if(tables.size() == 0){
			Log4jUtil.info("projectId为[{}]的项目，没有可以进行取消发布操作的表数据", projectId);
			return;
		}
		
		// 远程过去drop表
		CfgDatabase database = getObjectById(databaseId, CfgDatabase.class);
		DBTableHandler tableHandler = new DBTableHandler(database);
		String deleteTableResourceNames = tableHandler.dropTable(tables);
		tables.clear();
		
		usePublishResourceApi(deleteTableResourceNames, projectId, "table", "-1", projectId);
	}

	/**
	 * 发布公用的表资源
	 * <p>公用的表一般在发布数据库的时候就已经完成，这里主要是将表和项目关联起来，在远程数据库的SysResource资源中插入数据</p>
	 * @param databaseId
	 * @param projectId
	 */
	public void publishCommonTableResource(String databaseId, String projectId) {
		List<ComTabledata> tables = HibernateUtil.extendExecuteListQueryByHqlArr(ComTabledata.class, null, null, 
				"from ComTabledata where isEnabled =1 and isNeedDeploy=1 and isBuiltin=1 ");
		List<SysResource> resources = new ArrayList<SysResource>(tables.size());
		SysResource resource;
		Date currentDate = new Date();
		String currentUserId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
		for (ComTabledata table : tables) {
			resource = table.turnToResource();
			resource.setProjectId(projectId);
			resource.setRefDataId(table.getId());
			resource.setId(ResourceHandlerUtil.getIdentity());
			resource.setCreateDate(currentDate);
			resource.setLastUpdateDate(currentDate);
			resource.setCreateUserId(currentUserId);
			resource.setLastUpdateUserId(currentUserId);
			resources.add(resource);
			table.clear();
		}
		tables.clear();
		
		if(databaseId == null){
			databaseId = ProjectIdRefDatabaseIdMapping.getDbId(projectId);
		}
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			for (SysResource csr : resources) {
				session.save(csr.getEntityName(), csr.toEntityJson());
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
			resources.clear();
		}
	}
}
