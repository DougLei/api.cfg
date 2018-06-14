package com.king.tooth.sys.service.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.jdbc.database.DatabaseHandler;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 数据库数据信息资源对象处理器
 * @author DougLei
 */
public class ComDatabaseService extends AbstractPublishService {
	
	/**
	 * 验证数据库数据是否存在
	 * @param database
	 * @return operResult
	 */
	private String validDatabaseDataIsExists(ComDatabase database) {
		String hql = "select count("+ResourceNameConstants.ID+") from ComDatabase where dbType=? and dbInstanceName=? and loginUserName=? and loginPassword=? and dbIp=? and dbPort=?";
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
	public String saveDatabase(ComDatabase database) {
		String operResult = validDatabaseDataIsExists(database);
		if(operResult == null){
			HibernateUtil.saveObject(database, null);
		}
		return operResult;
	}

	/**
	 * 修改数据库
	 * @param database
	 * @return operResult
	 */
	public String updateDatabase(ComDatabase database) {
		ComDatabase oldDatabase = getObjectById(database.getId(), ComDatabase.class);
		if(oldDatabase == null){
			return "没有找到id为["+database.getId()+"]的数据库对象信息";
		}
		
		String operResult = null;
		boolean databaseLinkInfoIsSame = oldDatabase.compareLinkInfoIsSame(database);
		if(!databaseLinkInfoIsSame){// 如果修改了连接信息
			if(publishInfoService.validResourceIsPublished(oldDatabase.getId(), null, null, null)){ // 如果已发布，则发出提示信息
				return "【慎重操作】:["+oldDatabase.getDbDisplayName()+"]数据库已经发布，不能修改连接信息，或取消发布后再修改";
			}
			operResult = validDatabaseDataIsExists(database);
		}
		if(operResult == null){
			HibernateUtil.updateObjectByHql(database, null);
		}
		return null;
	}
	
	/**
	 * 删除数据库
	 * @param databaseId
	 * @return
	 */
	public String deleteDatabase(String databaseId) {
		ComDatabase oldDatabase = getObjectById(databaseId, ComDatabase.class);
		if(oldDatabase == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
		if(publishInfoService.validResourceIsPublished(oldDatabase.getId(), null, null, null)){
			return "["+oldDatabase.getDbDisplayName()+"]数据库已经发布，无法删除，请先取消发布";
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProject where refDatabaseId = ?", databaseId);
		if(count > 0){
			return "该数据库下还存在项目，无法删除，请先删除相关项目";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComDatabase where id = '"+databaseId+"'");
		return null;
	}
	
	/**
	 * 测试数据库连接
	 * @param databaseId
	 */
	public String databaseLinkTest(String databaseId) {
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
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
				"from ComTabledata where isEnabled =1 and isNeedDeploy=1 and isBuiltin=1 and belongPlatformType!="+ISysResource.CONFIG_PLATFORM);
		builtinAppBasicTables.addAll(HibernateUtil.extendExecuteListQueryByHqlArr(ComTabledata.class, null, null, 
				"from ComTabledata where isEnabled =1 and isNeedDeploy=1 and isBuiltin=0 and belongPlatformType="+ISysResource.COMMON_PLATFORM));
		for (ComTabledata table : builtinAppBasicTables) {
			if(table.getIsCore() == 1){
				coreTableCount++;
			}
			table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId ='"+table.getId()+"'"));
			DynamicBasicDataColumnUtil.initBasicColumnToTable(table);
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
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		String hbmContent;
		for (ComTabledata table : appSystemCoreTables) {
			if(table.getIsCore() == 1){
				hbmContent = hibernateHbmHandler.createHbmMappingContent(table, false);
				coreTableOfHbmInputstreams.add(new ByteArrayInputStream(hbmContent.getBytes()));
			}
		}
		return coreTableOfHbmInputstreams;
	}
	
	/**
	 * 发布数据库
	 * @param databaseId
	 * @return
	 */
	@SuppressWarnings("unused")
	public String publishDatabase(String databaseId){
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
		if(database.getIsNeedDeploy() == 0){
			return "id为["+databaseId+"]的数据库不该被发布，如需发布，请联系管理员";
		}
		if(database.getIsEnabled() == 0){
			return "id为["+databaseId+"]的数据库信息无效，请联系管理员";
		}
		ComPublishInfo ref = null;
		if(publishInfoService.validResourceIsPublished(databaseId, null, null, ref)){
			return "id为["+databaseId+"]的数据库已发布，无需再次发布，或取消发布后重新发布";
		}
		
		// 如果是自己的库，要创建
		if(database.compareIsSameDatabase(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance)){
			DatabaseHandler databaseHandler = new DatabaseHandler(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
			if(ref != null){
				databaseHandler.dropDatabase(database);
			}
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
		if(ref != null){
			dbTableHandler.dropTable(appSystemCoreTables);
		}
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
		ResourceHandlerUtil.clearTables(appSystemCoreTables);
		
		// 删除之前的发布数据【以防万一，如果之前有，这里先删除】
		publishInfoService.deletePublishedData(null, databaseId);
		executeRemotePublish(databaseId, null, database, null, null);
		
		database.setIsCreated(1);
		HibernateUtil.updateObject(database, null);
		return null;
	}
	
	/**
	 * 取消发布数据库
	 * @param databaseId
	 * @return
	 */
	public String cancelPublishDatabase(String databaseId){
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
		if(database.getIsNeedDeploy() == 0){
			return "id为["+databaseId+"]的数据库不该被发布，如需发布，请联系管理员";
		}
		if(database.getIsEnabled() == 0){
			return "id为["+databaseId+"]的数据库信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(databaseId, null, null, null)){
			return "id为["+databaseId+"]的数据库未发布，无法取消发布";
		}
		
		// 清除该数据库下，所有和项目id的映射信息
		ProjectIdRefDatabaseIdMapping.clearMapping(databaseId);
		
		try {
			// 先测试库能不能正常连接上
			String testLinkResult = database.testDbLink();
			if(testLinkResult.startsWith("err")){
				return "取消发布数据库失败:" + testLinkResult;
			}
			
			// 移除dataSource和sessionFacotry
			DynamicDBUtil.removeDataSource(databaseId);
			
			// 如果是自己的库，要删除
			if(database.compareIsSameDatabase(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance)){
				DatabaseHandler databaseHandler = new DatabaseHandler(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
				databaseHandler.dropDatabase(database);
			}
		} finally{
			// 删除该库下，所有发布的信息
			HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, "delete ComPublishInfo where publishDatabaseId = '"+databaseId+"'", null);
			
			database.setIsCreated(0);
			HibernateUtil.updateObject(database, null);
		}
		return null;
	}
	//--------------------------------------------------------------------------------------------------------
	
	/**
	 * 加载数据库
	 * <p>运行系统使用的方法，当配置系统，发布过来数据库信息的时候</p>
	 * @param databaseId
	 */
	public String loadPublishedDatabase(String databaseId){
//		if(SysConfig.getSystemConfig("current.sys.type").equals(ITable.CONFIG_PLATFORM+"")){
//			return installOperResponseBody("卸载数据库的功能，目前只提供给运行系统使用", null);
//		}
		
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
		DynamicDBUtil.addDataSource(database);
		return null;
	}
	
	/**
	 * 卸载数据库
	 * <p>运行系统使用的方法，当配置系统，取消发布数据库信息的时候</p>
	 * @param databaseId
	 */
	public String unloadPublishedDatabase(String databaseId){
		if(DynamicDBUtil.getDataSource(databaseId) == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
		DynamicDBUtil.removeDataSource(databaseId);
		return null;
	}
}
