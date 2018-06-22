package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 表数据信息资源对象处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComTabledataService extends AbstractPublishService {
	
	/**
	 * 验证表名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validTableNameIsExists(ComTabledata table) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComTabledata where tableName = ? and createUserId = ?", table.getTableName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(count > 0){
			return "您已经创建过相同表名["+table.getTableName()+"]的数据";
		}
		return null;
	}
	
	/**
	 * 验证表关联的项目是否存在
	 * @param project
	 * @return operResult
	 */
	private String validTableRefProjIsExists(String projectId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProject where id = ?", projectId);
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
		String hql = "select count("+ResourceNameConstants.ID+") from " + 
				"ComDatabase d, ComProject p, ComProjectComTabledataLinks pt, ComTabledata tb" +
				"where d.id = '"+project.getRefDatabaseId()+"' and d.id = p.refDatabaseId and p.id=pt.leftId and tb.id=pt.rightId and tb.tableName='"+tableName + "'";
		long count = (long) HibernateUtil.executeUniqueQueryByHql(hql, null);
		if(count > 1){
			return "项目关联的数据库中已经存在表名为["+tableName+"]的数据";
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
			boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
			String projectId = table.getProjectId();
			table.setProjectId(null);
			
			if(!isPlatformDeveloper){// 非平台开发者，建的表一开始，一定要和一个项目关联起来
				if(StrUtils.isEmpty(projectId)){
					return "表关联的项目id不能为空！";
				}
				operResult = validTableRefProjIsExists(projectId);
				if(operResult == null){
					operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
				}
			}
			if(operResult == null){
				String tableId = HibernateUtil.saveObject(table, null);
				// 保存表和项目的关联关系
				if(isPlatformDeveloper){
					HibernateUtil.saveDataLinks("ComProjectComTabledataLinks", CurrentThreadContext.getProjectId(), tableId);
				}else{
					HibernateUtil.saveDataLinks("ComProjectComTabledataLinks", projectId, tableId);
				}
			}
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
		String operResult = null;
		if(!oldTable.getTableName().equals(table.getTableName())){
			operResult = validTableNameIsExists(table);
		}
		
		if(operResult == null){
			boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
			String projectId = table.getProjectId();
			table.setProjectId(null);
			
			if(!isPlatformDeveloper){
				if(StrUtils.isEmpty(projectId)){
					return "表关联的项目id不能为空！";
				}
				operResult = validTableRefProjIsExists(projectId);
				if(operResult == null){
					operResult = validTableIsExistsInDatabase(projectId, table.getTableName());
				}
				if(operResult == null && publishInfoService.validResourceIsPublished(null, projectId, oldTable.getId())){
						return "该表已经发布，不能修改表信息，或取消发布后再修改";
				}
			}
			
			if(operResult == null){
				HibernateUtil.updateObjectByHql(table, null);
			}
		}
		return operResult;
	}
	
	/**
	 * 删除表
	 * @param tableId
	 * @param projectId
	 * @return
	 */
	public String deleteTable(String tableId, String projectId) {
		ComTabledata oldTable = getObjectById(tableId, ComTabledata.class);
		if(oldTable == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		if(!isPlatformDeveloper){
			if(StrUtils.isEmpty(projectId)){
				return "要删除的表，关联的项目id不能为空";
			}
			if(publishInfoService.validResourceIsPublished(null, projectId, oldTable.getId())){
				return "该表已经发布，无法删除，请先取消发布";
			}
		}
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks("ComProjectComTabledataLinks", null, tableId);
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
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComTabledata where id = '"+tableId+"'");
		HibernateUtil.deleteDataLinks("ComProjectComTabledataLinks", null, tableId);
		
		// 如果是平台开发者账户，则需删除资源信息，要删表，以及映射文件数据，并从当前的sessionFacotry中移除
		if(isPlatformDeveloper && oldTable.getIsCreated() == 1){
			cancelBuildModel(oldTable);
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
		if(table.getIsCreated() == 1){
			return "["+table.getTableName()+"]已完成建模";
		}
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		
		// 1、建表
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
		List<ComTabledata> tables = dbTableHandler.createTable(table, true); // 表信息集合，有可能有关系表
		
		HibernateHbmHandler hbmHandler = new HibernateHbmHandler();
		List<String> hbmContents = new ArrayList<String>(tables.size());
		int i = 0;
		for (ComTabledata tb : tables) {
			hbmContents.add(hbmHandler.createHbmMappingContent(tb, false));
			
			// 2、插入hbm
			ComHibernateHbm hbm = new ComHibernateHbm();
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.tableTurnToHbm(tb);
			hbm.setHbmContent(hbmContents.get(i++));
			HibernateUtil.saveObject(hbm, null);
			
			// 3、插入资源数据
			new ComSysResourceService().saveSysResource(tb);
			
		}
		// 4、将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
		
		// 5、修改表是否创建的状态
		modifyIsCreatedPropVal(table.getEntityName(), 1, table.getId());
		ResourceHandlerUtil.clearTables(tables);
		return null;
	}
	
	/**
	 * 取消建模
	 * @param tableId
	 * @return
	 */
	public String cancelBuildModel(String tableId){
		ComTabledata table = getObjectById(tableId, ComTabledata.class);
		if(table == null){
			return "没有找到id为["+tableId+"]的表对象信息";
		}
		if(table.getIsCreated() == 0){
			return "["+table.getTableName()+"]未建模，无法取消建模";
		}
		cancelBuildModel(table);
		return null;
	}
	
	/**
	 * 取消建模
	 * @param table
	 */
	private void cancelBuildModel(ComTabledata table){
		long count = (long) HibernateUtil.executeUniqueQueryByHql("select count("+ResourceNameConstants.ID+") from ComHibernateHbm where projectId='"+CurrentThreadContext.getProjectId()+"' and refTableId = '"+table.getId()+"'", null);
		if(count > 0){
			// 删除hbm信息
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComHibernateHbm where projectId='"+CurrentThreadContext.getProjectId()+"' and refTableId = '"+table.getId()+"'");
			// 删除资源
			new ComSysResourceService().deleteSysResource(table.getId());
			
			// drop表
			DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
			String[] tableResourceNames = dbTableHandler.dropTable(table).split(",");
			
			// 修改表是否创建的状态
			modifyIsCreatedPropVal(table.getEntityName(), 0, table.getId());
			
			// 从sessionFactory中移除映射
			List<String> resourceNames = new ArrayList<String>(tableResourceNames.length);
			for (String tableResourceName : tableResourceNames) {
				resourceNames.add(tableResourceName);
			}
			HibernateUtil.removeConfig(resourceNames);
			resourceNames.clear();
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
			HibernateUtil.saveDataLinks("ComProjectComTabledataLinks", projectId, tableId);
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
		HibernateUtil.deleteDataLinks("ComProjectComTabledataLinks", projectId, tableId);
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布表
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String publishTable(String projectId, String tableId) {
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
		ComDatabase database = getObjectById(project.getRefDatabaseId(), ComDatabase.class);
		table.setDbType(database.getDbType());
		String validResult = table.analysisResourceProp();
		if(validResult != null){
			return validResult;
		}
		
		DBTableHandler tableHandler = new DBTableHandler(database);
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		List<ComTabledata> tables = tableHandler.createTable(table, true);
		
		List<ComHibernateHbm> hbms = new ArrayList<ComHibernateHbm>(tables.size());
		HibernateHbmHandler hbmHandler = new HibernateHbmHandler();
		for (ComTabledata tb : tables) {
			ComHibernateHbm hbm = new ComHibernateHbm();
			if(tb.getIsDatalinkTable() == 0){
				hbm.setId(tableId);
			}else{
				hbm.setId(ResourceHandlerUtil.getIdentity());
			}
			hbm.tableTurnToHbm(tb);
			hbm.setRefDatabaseId(project.getRefDatabaseId());
			hbm.setProjectId(projectId);
			hbm.setHbmContent(hbmHandler.createHbmMappingContent(tb, false));
			hbms.add(hbm);
		}
		ResourceHandlerUtil.clearTables(tables);
		
		publishInfoService.deletePublishedData(projectId, tableId);
		executeRemotePublishTable(project.getRefDatabaseId(), projectId, hbms, "ComProjectComHibernateHbmLinks");
		hbms.clear();
		
		return useLoadPublishApi(tableId, projectId, "table", "1", projectId);
	}
	
	/**
	 * 执行远程发布操作
	 * @param databaseId  
	 * @param projectId  
	 * @param hbms
	 * @param comProjectComHibernateHbmLinkResourceName
	 */
	private void executeRemotePublishTable(String databaseId, String projectId, List<ComHibernateHbm> hbms, String comProjectComHibernateHbmLinkResourceName){
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
			for (ComHibernateHbm hbm : hbms) {
				if(hbm == null){
					break;
				}
				publishEntityJson = hbm.toPublishEntityJson(projectId);
				session.save(hbm.getEntityName(), publishEntityJson);
				
				datalink = ResourceHandlerUtil.getDataLinksObject(projectId, publishEntityJson.getString(ResourceNameConstants.ID), ""+(orderCode++), null, null);
				datalink.put("projectId", projectId);
				datalink.put(ResourceNameConstants.ID, ResourceHandlerUtil.getIdentity());
				session.save(comProjectComHibernateHbmLinkResourceName, datalink);
				
				ComSysResource csr = hbm.turnToPublishResource(projectId, publishEntityJson.getString(ResourceNameConstants.ID));
				session.save(csr.getEntityName(), csr.toEntityJson());
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			errMsg = ExceptionUtil.getErrMsg(e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		
		// 添加新的发布信息数据
		ComPublishInfo publishInfo;
		for (ComHibernateHbm hbm : hbms) {
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
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String cancelPublishTable(String projectId, String tableId) {
		ComTabledata table = getObjectById(tableId, ComTabledata.class);
		if(table == null){
			return "没有找到id为["+table+"]的表对象信息";
		}
		if(!publishInfoService.validResourceIsPublished(null, projectId, tableId)){
			return "["+table.getTableName()+"]表未发布，无法取消发布";
		}
		String result = validTableRefProjIsExists(projectId);
		if(result != null){
			return result;
		}
		
		// 远程过去drop表
		ComDatabase database = getObjectById(projectId, ComDatabase.class);
		DBTableHandler tableHandler = new DBTableHandler(database);
		String deleteTableResourceNames = tableHandler.dropTable(table);
		
		executeRemoteUpdate(null, projectId, 
				"delete ComProjectComHibernateHbmLinks where projectId='"+projectId+"' and leftId='"+projectId+"' and rightId in (select "+ResourceNameConstants.ID+" from "+new ComHibernateHbm().getEntityName()+" where projectId='"+projectId+"' and refDataId = '"+tableId+"')",
				"delete " + new ComHibernateHbm().getEntityName() + " where projectId='"+projectId+"' and refDataId = '"+tableId+"'",
				"delete ComSysResource where projectId='"+projectId+"' and refDataId = '"+tableId+"'");
		publishInfoService.deletePublishedData(projectId, tableId);
		
		return useLoadPublishApi(deleteTableResourceNames, projectId, "table", "-1", projectId);
	}

	/**
	 * 批量发布表
	 * @param databaseId
	 * @param projectId
	 * @param tableIds
	 */
	public void batchPublishTable(String databaseId, String projectId, List<Object> tableIds) {
		List<ComTabledata> tables = new ArrayList<ComTabledata>(tableIds.size()*2);
		ComDatabase database = getObjectById(projectId, ComDatabase.class);
		ComTabledata table;
		String validResult;
		for (Object tableId : tableIds) {
			table = getObjectById(tableId.toString(), ComTabledata.class);
			if(table.getIsNeedDeploy() == 0){
				Log4jUtil.info("id为["+tableId+"]的表不该被发布，如需发布，请联系管理员");
				continue;
			}else if(table.getIsEnabled() == 0){
				Log4jUtil.info("id为["+tableId+"]的表信息无效，请联系管理员");
				continue;
			}else if(publishInfoService.validResourceIsPublished(null, projectId, table.getId())){
				Log4jUtil.info("["+table.getTableName()+"]表已经发布，无需再次发布，或取消发布后重新发布");
				continue;
			}
			
			table.setDbType(database.getDbType());
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
		List<ComHibernateHbm> hbms = new ArrayList<ComHibernateHbm>(limitSize);// 记录表对应的hbm内容，要发布的是这个
		
		// 准备远程过去create表
		DBTableHandler tableHandler = new DBTableHandler(database);
		// 准备创建hbm
		HibernateHbmHandler hbmHandler = new HibernateHbmHandler();
		
		List<ComTabledata> tmpTables;// 在创建表的时候，记录每次创建表的数据
		ComHibernateHbm hbm = null;
		StringBuilder tableIdStr = new StringBuilder();
		for(ComTabledata tb : tables){
			if(tb == null){
				break;
			}
			tableIdStr.append(tb.getId()).append(",");
			
			tb.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId ='"+tb.getId()+"'"));
			tmpTables = tableHandler.createTable(tb, true);
			
			for(ComTabledata tempTb : tmpTables) {
				hbm = new ComHibernateHbm();
				if(tempTb.getIsDatalinkTable() == 0){
					hbm.setId(tb.getId());
				}else{
					hbm.setId(ResourceHandlerUtil.getIdentity());
				}
				hbm.tableTurnToHbm(tempTb);
				hbm.setRefDatabaseId(databaseId);
				hbm.setProjectId(projectId);
				hbm.setHbmContent(hbmHandler.createHbmMappingContent(tempTb, false));
				hbms.add(hbm);
			}
			ResourceHandlerUtil.clearTables(tmpTables);
			tb.clear();
			
			if(hbms.get((limitSize-1)) != null){
				executeRemotePublishTable(databaseId, projectId, hbms, "ComProjectComHibernateHbmLinks");
				hbms.clear();
			}
		}
		if(hbms.get(0) != null){
			executeRemotePublishTable(databaseId, projectId, hbms, "ComProjectComHibernateHbmLinks");
			hbms.clear();
		}
		tables.clear();
		tableIdStr.setLength(tableIdStr.length()-1);
		
		useLoadPublishApi(tableIdStr.toString(), projectId, "table", "1", projectId);
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
		ComDatabase database = getObjectById(projectId, ComDatabase.class);
		DBTableHandler tableHandler = new DBTableHandler(database);
		String deleteTableResourceNames = tableHandler.dropTable(tables);
		tables.clear();
		
		useLoadPublishApi(deleteTableResourceNames, projectId, "table", "-1", projectId);
	}

	//--------------------------------------------------------------------------------------------------------
	protected String loadPublishData(String projectId, String publishDataId) {
		Object[] tableIds = publishDataId.split(",");
		List<String> hbmContents = new ArrayList<String>(tableIds.length*2);
		String hql = "select hbmContent from ComHibernateHbm where isCreated=0 and refDataId = ? and projectId='"+projectId+"'";
		for (Object tableId : tableIds) {
			hbmContents.add(HibernateUtil.executeListQueryByHqlArr(null, null, hql, tableId)+"");
		}
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
		
		StringBuilder hb = new StringBuilder("update ComHibernateHbm set isCreated=1 where projectId = '"+projectId+"' and refDataId in(");
		int len = tableIds.length;
		for (int i = 0;i<len ;i++) {
			hb.append("?,");
		}
		hb.setLength(hb.length()-1);
		hb.append(")");
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.UPDATE, hb.toString(), tableIds);
		hb.setLength(0);
		return "success";
	}

	protected String unloadPublishData(String projectId, String tableResourceNames) {
		String[] tableResourceNameArr = tableResourceNames.split(",");
		List<String> tableResourceNameList = new ArrayList<String>(tableResourceNameArr.length);
		for (String en : tableResourceNameArr) {
			tableResourceNameList.add(en.trim());
		}
		HibernateUtil.removeConfig(tableResourceNameList);
		tableResourceNameList.clear();
		return "success";
	}
}
