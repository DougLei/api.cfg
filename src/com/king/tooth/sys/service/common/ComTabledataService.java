package com.king.tooth.sys.service.common;

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
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.ExceptionUtil;
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
	// 项目和表的关联关系资源名
	private static final String comProjectComTabledataLinkResourceName = "ComProjectComTabledataLinks";
	
	/**
	 * 验证表名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validTableNameIsExists(ComTabledata table) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComTabledata where tableName = ? and createUserId = ?", table.getTableName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(count > 0){
			return "表名为["+table.getTableName()+"]的已存在";
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
	 * 保存表
	 * @param table
	 * @return
	 */
	public String saveTable(ComTabledata table) {
		String operResult = validTableNameIsExists(table);
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		
		String projectId = table.getProjectId();
		table.setProjectId(null);
		if(!isPlatformDeveloper){// 非平台开发者，建的表一开始，一定要和一个项目关联起来
			if(StrUtils.isEmpty(projectId)){
				return "表关联的项目id不能为空！";
			}
			operResult = validTableRefProjIsExists(projectId);
		}
		if(operResult == null){
			String tableId = HibernateUtil.saveObject(table, null);
			// 保存表和项目的关联关系
			if(isPlatformDeveloper){
				HibernateUtil.saveDataLinks(comProjectComTabledataLinkResourceName, CurrentThreadContext.getProjectId(), tableId);
			}else{
				HibernateUtil.saveDataLinks(comProjectComTabledataLinkResourceName, projectId, tableId);
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
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		
		String operResult = null;
		if(!isPlatformDeveloper && !oldTable.getTableName().equals(table.getTableName())){
			if(StrUtils.isEmpty(table.getProjectId())){
				return "表关联的项目id不能为空！";
			}
			String projectId = table.getProjectId();
			table.setProjectId(null);
			
			if(publishInfoService.validResourceIsPublished(null, projectId, oldTable.getId(), null)){
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
			if(publishInfoService.validResourceIsPublished(null, projectId, oldTable.getId(), null)){
				return "该表已经发布，无法删除，请先取消发布";
			}
		}
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks(comProjectComTabledataLinkResourceName, null, tableId);
		if(datalinks.size() > 1){
			List<Object> projectIds = new ArrayList<Object>(datalinks.size());
			StringBuilder hql = new StringBuilder("select projName from ComProject where id in (");
			for (JSONObject json : datalinks) {
				projectIds.add(json.getString(ResourceNameConstants.LEFT_ID));
				hql.append("?,");
			}
			hql.setLength(hql.length() - 1);
			hql.append(")");
			
			List<Object> projNames = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), projectIds);
			projectIds.clear();
			return "该表关联多个项目，无法删除，请先取消和其他项目的关联，关联的项目包括：" + projNames;
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComTabledata where id = '"+tableId+"'");
		HibernateUtil.deleteDataLinks(comProjectComTabledataLinkResourceName, null, tableId);
		
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
		if(table.getIsCreated() == 1){
			return "["+table.getTableName()+"]已完成建模";
		}
		
		// 如果之前有数据，则删除之前的数据
		long count = (long) HibernateUtil.executeUniqueQueryByHql("select count("+ResourceNameConstants.ID+") from ComHibernateHbm where refTableId = '"+tableId+"'", null);
		if(count > 0){
			// 删除hbm信息
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComHibernateHbm where refTableId = '"+tableId+"'");
			// 删除资源
			new ComSysResourceService().deleteSysResource(tableId);
			// drop表
			DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			dbTableHandler.dropTable(table);
			// 从sessionFactory中移除映射
			HibernateUtil.removeConfig(table.getEntityName());
		}
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		
		// 获的hbm内容
		HibernateHbmHandler hbmHandler = new HibernateHbmHandler();
		String hbmContent = hbmHandler.createHbmMappingContent(table, true);
		
		// 1、建表
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
		dbTableHandler.createTable(table, false);
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

	/**
	 * 建立项目和表的关联关系
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String addProjTableRelation(String projectId, String tableId) {
		HibernateUtil.saveDataLinks(comProjectComTabledataLinkResourceName, projectId, tableId);
		return null;
	}
	
	/**
	 * 取消项目和表的关联关系
	 * @param projectId
	 * @param tableId
	 * @return
	 */
	public String cancelProjTableRelation(String projectId, String tableId) {
		HibernateUtil.deleteDataLinks(comProjectComTabledataLinkResourceName, projectId, tableId);
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
			return "id为["+tableId+"]的表不该被发布，请联系管理员";
		}
		if(table.getIsEnabled() == 0){
			return "id为["+tableId+"]的表信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(null, projectId, tableId, null)){
			return "["+table.getTableName()+"]表所属的项目还未发布，请先发布项目";
		}
		if(publishInfoService.validResourceIsPublished(null, projectId, tableId, null)){
			return "["+table.getTableName()+"]表已经发布，无法再次发布";
		}
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "表关联的，id为["+projectId+"]的项目信息不存在";
		}
		
		// 远程过去create表
		ComDatabase database = getObjectById(projectId, ComDatabase.class);
		DBTableHandler tableHandler = new DBTableHandler(database);
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableId));
		List<ComTabledata> tables = tableHandler.createTable(table, true);
		
		List<ComHibernateHbm> hbms = new ArrayList<ComHibernateHbm>(tables.size());
		ComHibernateHbm hbm = null;
		for (ComTabledata tb : tables) {
			hbm = new ComHibernateHbm();
			if(tb.getIsDatalinkTable() == 0){
				hbm.setId(tableId);
			}else{
				hbm.setId(ResourceHandlerUtil.getIdentity());
				hbm.setRefTableId(tableId);
			}
			hbm.setHbmResourceName(tb.getResourceName());
			hbm.setIsDataLinkTableHbm(tb.getIsDatalinkTable());
			hbm.setHbmContent(new HibernateHbmHandler().createHbmMappingContent(tb, false));
			hbm.setReqResourceMethod(tb.getReqResourceMethod());
			hbm.setProjectId(projectId);
			hbm.setRefDatabaseId(project.getRefDatabaseId());
			hbms.add(hbm);
			tb.clear();
		}
		tables.clear();
		
		publishInfoService.deletePublishedData(projectId, tableId);
		executeRemotePublish(projectId, hbms);
		hbms.clear();
		return null;
	}
	
	/**
	 * 执行远程发布操作
	 * @param projectId  
	 * @param hbms
	 */
	private void executeRemotePublish(String projectId, List<ComHibernateHbm> hbms){
		String remotePublishErrMsg = null;
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(ProjectIdRefDatabaseIdMapping.getDbId(projectId));
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			for (ComHibernateHbm hbm : hbms) {
				session.save(hbm.getEntityName(), hbm.toEntityJson());
				
				ComSysResource csr = hbm.turnToPublishResource();
				session.save(csr.getEntityName(), csr.toEntityJson());
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			remotePublishErrMsg = ExceptionUtil.getErrMsg(e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		
		// 再添加新的发布信息数据
		ComPublishInfo publishInfo;
		for (ComHibernateHbm hbm : hbms) {
			publishInfo = hbm.turnToPublish();
			if(remotePublishErrMsg == null){
				publishInfo.setIsSuccess(1);
			}else{
				publishInfo.setIsSuccess(0);
				publishInfo.setErrMsg(remotePublishErrMsg);
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
		if(!publishInfoService.validResourceIsPublished(null, projectId, tableId, null)){
			return "["+table.getTableName()+"]表未发布，无法取消发布";
		}
		String result = validTableRefProjIsExists(projectId);
		if(result != null){
			return result;
		}
		
		// 远程过去drop表
		ComDatabase database = getObjectById(projectId, ComDatabase.class);
		DBTableHandler tableHandler = new DBTableHandler(database);
		tableHandler.dropTable(table);
		
		executeRemoteUpdate(null, projectId, 
				"delete " + new ComHibernateHbm().getEntityName() + " where projectId='"+projectId+"' and (" + ResourceNameConstants.ID + "='"+tableId+"' or refTableId = '"+tableId+"')",
				"delete ComSysResource where projectId='"+projectId+"' and refResourceId = '"+tableId+"'");
		publishInfoService.deletePublishedData(projectId, tableId);
		return null;
	}
}
