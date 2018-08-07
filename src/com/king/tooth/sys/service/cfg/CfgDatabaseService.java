package com.king.tooth.sys.service.cfg;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.jdbc.database.DatabaseHandler;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.sys.SysHibernateHbm;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 数据库信息表Service
 * @author DougLei
 */
public class CfgDatabaseService extends AbstractPublishService {
	
	/**
	 * 验证数据库数据是否存在
	 * @param database
	 * @return operResult
	 */
	private String validDatabaseDataIsExists(CfgDatabase database) {
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgDatabase where dbType=? and dbInstanceName=? and loginUserName=? and loginPassword=? and dbIp=? and dbPort=?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, database.getDbType(), database.getDbInstanceName(), database.getLoginUserName(), database.getLoginPassword(), database.getDbIp(), database.getDbPort()+"");
		if(count > 0){
			return "[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]的数据库连接信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存数据库
	 * @param database
	 * @return operResult
	 */
	public Object saveDatabase(CfgDatabase database) {
		String operResult = validDatabaseDataIsExists(database);
		if(operResult == null){
			return HibernateUtil.saveObject(database, null);
		}
		return operResult;
	}

	/**
	 * 修改数据库
	 * @param database
	 * @return operResult
	 */
	public Object updateDatabase(CfgDatabase database) {
		CfgDatabase oldDatabase = getObjectById(database.getId(), CfgDatabase.class);
		if(oldDatabase.getIsBuiltin() == 1){
			return "禁止修改内置的数据库信息";
		}
		if(oldDatabase.getIsCreated() == 1){ // 如果已发布，则发出提示信息
			return "["+oldDatabase.getDbDisplayName()+"]数据库已经发布，不能修改数据库信息，或取消发布后再修改";
		}
		
		String operResult = null;
		boolean databaseLinkInfoIsSame = oldDatabase.compareLinkInfoIsSame(database);
		if(!databaseLinkInfoIsSame){// 如果修改了连接信息
			operResult = validDatabaseDataIsExists(database);
		}
		if(operResult == null){
			return HibernateUtil.updateObjectByHql(database, null);
		}
		return operResult;
	}
	
	/**
	 * 删除数据库
	 * @param databaseId
	 * @return
	 */
	public String deleteDatabase(String databaseId) {
		CfgDatabase oldDatabase = getObjectById(databaseId, CfgDatabase.class);
		if(oldDatabase.getIsBuiltin() == 1){
			return "禁止删除内置的数据库信息";
		}
		if(oldDatabase.getIsCreated() == 1){
			return "["+oldDatabase.getDbDisplayName()+"]数据库已经发布，无法删除，请先取消发布";
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComProject where refDatabaseId = ?", databaseId);
		if(count > 0){
			return "该数据库下还存在项目，无法删除，请先删除相关项目";
		}
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete CfgDatabase where "+ResourcePropNameConstants.ID+" = '"+databaseId+"'");
		return null;
	}
	
	/**
	 * 测试数据库连接
	 * @param databaseId
	 */
	public String databaseLinkTest(String databaseId) {
		CfgDatabase database = getObjectById(databaseId, CfgDatabase.class);
		return database.testDbLink();
	}
	
	//--------------------------------------------------------------------------------------------------------
	private int coreTableCount = 0;
	/**
	 * 获取系统内置的运行系统基础表集合
	 * <p>包括列集合</p>
	 * @return
	 */
	private List<ComTabledata> getBuiltinAppBasicTables(){
		List<ComTabledata> builtinAppBasicTables = HibernateUtil.extendExecuteListQueryByHqlArr(ComTabledata.class, null, null, 
				"from ComTabledata where isEnabled =1 and isNeedDeploy=1 and isBuiltin=1 ");
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		for (ComTabledata table : builtinAppBasicTables) {
			if(table.getIsCore() == 1){
				coreTableCount++;
			}
			table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId ='"+table.getId()+"'"));
			table.setHbmContent(hibernateHbmHandler.createHbmMappingContent(table, true));
		}
		return builtinAppBasicTables;
	}
	/**
	 * 获取核心表的hbm InputStream流
	 * @param appSystemCoreTables
	 * @return
	 */
	private List<InputStream> getCoreTableOfHbmInputstreams(List<ComTabledata> appSystemCoreTables) {
		List<InputStream> coreTableOfHbmInputstreams = new ArrayList<InputStream>(coreTableCount);
		for (ComTabledata table : appSystemCoreTables) {
			if(table.getIsCore() == 1){
				coreTableOfHbmInputstreams.add(new ByteArrayInputStream(table.getHbmContent().getBytes()));
			}
		}
		return coreTableOfHbmInputstreams;
	}
	
	/**
	 * 发布数据库
	 * @param databaseId
	 * @return
	 */
	public String publishDatabase(String databaseId){
		if(BuiltinInstance.currentSysBuiltinDatabaseInstance.getId().equals(databaseId)){
			return "不能发布配置系统数据库";
		}
		CfgDatabase database = getObjectById(databaseId, CfgDatabase.class);
		if(database.getIsCreated() == 1){
			return "id为["+databaseId+"]的数据库已发布，无需再次发布，或取消发布后重新发布";
		}
		if(database.getIsNeedDeploy() == 0){
			return "id为["+databaseId+"]的数据库不该被发布，如需发布，请联系管理员";
		}
		if(database.getIsEnabled() == 0){
			return "id为["+databaseId+"]的数据库信息无效，请联系管理员";
		}
		
		// 如果是自己的库，要创建
		if(database.compareIsSameDatabase(BuiltinInstance.currentSysBuiltinDatabaseInstance)){
			DatabaseHandler databaseHandler = new DatabaseHandler(BuiltinInstance.currentSysBuiltinDatabaseInstance);
			databaseHandler.createDatabase(database);
		}
		// 还要测试库能不能正常连接上
		String testLinkResult = database.testDbLink();
		if(testLinkResult.startsWith("err")){
			return testLinkResult;
		}
		Log4jUtil.debug("连接数据库测试[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]：" + testLinkResult);
		
		// 创建运行系统所有需要的基础表
		DBTableHandler dbTableHandler = new DBTableHandler(database);
		List<ComTabledata> appSystemCoreTables = getBuiltinAppBasicTables();
		dbTableHandler.createTable(appSystemCoreTables, false);
		
		// 创建dataSource和sessionFactory
		List<InputStream> coreTableOfHbmInputstreams = null;
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		if(sessionFactory == null){
			DynamicDBUtil.addDataSource(database);
			sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
			coreTableOfHbmInputstreams = getCoreTableOfHbmInputstreams(appSystemCoreTables);
			sessionFactory.appendNewHbmConfig(coreTableOfHbmInputstreams);
			coreTableOfHbmInputstreams.clear();
		}
		
		// 将这些运行系统的基础表的hbm保存到远程数据库的ComHibernateHbm表中
		List<SysHibernateHbm> hbms = new ArrayList<SysHibernateHbm>(appSystemCoreTables.size());// 记录表对应的hbm内容，要发布的是这个
		SysHibernateHbm hbm = null;
		Date currentDate = new Date();
		String currentUserId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
		for(ComTabledata table : appSystemCoreTables){
			hbm = new SysHibernateHbm();
			hbm.tableTurnToHbm(table);
			hbm.setId(table.getId());
			hbm.setRefDatabaseId(databaseId);
			hbm.setHbmContent(table.getHbmContent());
			hbm.setRefDataId(table.getId());
			hbm.setCreateDate(currentDate);
			hbm.setLastUpdateDate(currentDate);
			hbm.setCreateUserId(currentUserId);
			hbm.setLastUpdateUserId(currentUserId);
			hbms.add(hbm);
			table.clear();
		}
		appSystemCoreTables.clear();
		
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			for (SysHibernateHbm h : hbms) {
				session.save(h.getEntityName(), h.toEntityJson());
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			return ExceptionUtil.getErrMsg("CfgDatabaseService", "publishDatabase", e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
			hbms.clear();
		}
		
		// 删除之前的发布数据【以防万一，如果之前有，这里先删除】
		publishInfoService.deletePublishedData(null, databaseId);
		executeRemotePublish(getAppSysDatabaseId(database), null, database, 0, null);
		
		modifyIsCreatedPropVal(database.getEntityName(), 1, database.getId());
		
		if(database.getIsBuiltin() == 1){
			return null;
		}

		return usePublishResourceApi(database.getId(), "null", "db", "1", 
				BuiltinInstance.currentSysBuiltinProjectInstance.getId());
	}
	
	/**
	 * 取消发布数据库
	 * @param databaseId
	 * @return
	 */
	public String cancelPublishDatabase(String databaseId){
		if(BuiltinInstance.currentSysBuiltinDatabaseInstance.getId().equals(databaseId)){
			return "不能取消发布配置系统数据库";
		}
		CfgDatabase database = getObjectById(databaseId, CfgDatabase.class);
		if(database.getIsCreated() == 0){
			return "id为["+databaseId+"]的数据库还未发布，不能取消发布";
		}
		if(database.getIsBuiltin() == 1){
			return "内置数据库不能进行取消发布操作！";
		}
		if(database.getIsNeedDeploy() == 0){
			return "id为["+databaseId+"]的数据库不该被发布，如需发布，请联系管理员";
		}
		if(database.getIsEnabled() == 0){
			return "id为["+databaseId+"]的数据库信息无效，请联系管理员";
		}
		
		// 先测试库能不能正常连接上
		String testLinkResult = database.testDbLink();
		if(testLinkResult.startsWith("err")){
			return "取消发布数据库失败:" + testLinkResult;
		}
		
		// 清除该数据库下，所有和项目id的映射信息
		ProjectIdRefDatabaseIdMapping.clearMapping(databaseId);
		
		// 移除dataSource和sessionFacotry
		DynamicDBUtil.removeDataSource(databaseId);
		
		// 如果是自己的库，要删除
		if(database.compareIsSameDatabase(BuiltinInstance.currentSysBuiltinDatabaseInstance)){
			DatabaseHandler databaseHandler = new DatabaseHandler(BuiltinInstance.currentSysBuiltinDatabaseInstance);
			databaseHandler.dropDatabase(database);
		}
		
		// 删除该库下，所有发布的信息
		HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, "delete DmPublishInfo where publishDatabaseId = '"+databaseId+"'", null);
		// 远程删除运行系统中的数据库信息
		executeRemoteUpdate(getAppSysDatabaseId(null), null, "delete "+database.getEntityName()+" where "+ResourcePropNameConstants.ID+" = '"+database.getId()+"'");
		
		modifyIsCreatedPropVal(database.getEntityName(), 0, database.getId());
		
		return usePublishResourceApi(database.getId(), "null", "db", "-1", 
				BuiltinInstance.currentSysBuiltinProjectInstance.getId());
	}
}
