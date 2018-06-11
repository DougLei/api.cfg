package com.king.tooth.sys.service.common;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.constants.CoreTableResourceConstants;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.jdbc.database.DatabaseHandler;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
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
			if(publishInfoService.validResourceIsPublished(oldDatabase.getId(), null, null)){ // 如果已发布，则发出提示信息
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
		if(publishInfoService.validResourceIsPublished(oldDatabase.getId(), null, null)){
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
	/**
	 * 发布数据库
	 * @param databaseId
	 * @return
	 */
	public String publishDatabase(String databaseId){
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return "没有找到id为["+databaseId+"]的数据库对象信息";
		}
		if(database.getIsNeedDeploy() == 0){
			return "id为["+databaseId+"]的数据库不该被发布，请联系管理员";
		}
		if(database.getIsEnabled() == 0){
			return "id为["+databaseId+"]的数据库信息无效，请联系管理员";
		}
		if(publishInfoService.validResourceIsPublished(databaseId, null, null)){
			return "id为["+databaseId+"]的数据库已发布，无法再次发布";
		}
		
		// 如果是自己的库，要创建
		if(database.compareIsSameDatabase(CurrentSysInstanceConstants.currentSysDatabaseInstance)){
			DatabaseHandler databaseHandler = new DatabaseHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			databaseHandler.createDatabase(database);
		}
		// 还要测试库能不能正常连接上
		String testLinkResult = database.testDbLink();
		if(testLinkResult.startsWith("err")){
			return testLinkResult;
		}
		Log4jUtil.debug("连接数据库测试[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]：" + testLinkResult);
		
		// 创建运行系统基础表
		DBTableHandler dbTableHandler = new DBTableHandler(database);
		List<ComTabledata> appSystemCoreTables = CoreTableResourceConstants.getAppsystemcoretables();
		dbTableHandler.createTable(appSystemCoreTables, false);
		
		// 创建dataSource和sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		if(sessionFactory == null){
			DynamicDBUtil.addDataSource(database);
			sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
			sessionFactory.appendNewHbmConfig(CoreTableResourceConstants.getCoretableresourcemappinginputstreams());
		}
		
		// 准备发布数据库数据信息，以及记录发布信息
		ComPublishInfo publishInfo = database.turnToPublish();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(database.getEntityName(), database.toEntityJson());
			session.getTransaction().commit();
			publishInfo.setIsSuccess(1);
		} catch (Exception e) {
			session.getTransaction().rollback();
			publishInfo.setIsSuccess(0);
			publishInfo.setErrMsg(ExceptionUtil.getErrMsg(e));
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
			// 删除部署失败的数据【以防万一，如果之前有失败的，这里先删除】
			HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, "delete ComPublishInfo where isSuccess =0 and publishDatabaseId = '"+databaseId+"'", null);
			// 再添加新的部署的信息数据
			HibernateUtil.saveObject(publishInfo, null);
		}
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
			return "id为["+databaseId+"]的数据库不该被发布，请联系管理员";
		}
		if(database.getIsEnabled() == 0){
			return "id为["+databaseId+"]的数据库信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(databaseId, null, null)){
			return "id为["+databaseId+"]的数据库未发布，无法取消发布";
		}
		
		// 先测试库能不能正常连接上
		String testLinkResult = database.testDbLink();
		if(testLinkResult.startsWith("err")){
			return "取消发布数据库失败:" + testLinkResult;
		}
		
		// 如果是自己的库，要删除
		if(database.compareIsSameDatabase(CurrentSysInstanceConstants.currentSysDatabaseInstance)){
			DatabaseHandler databaseHandler = new DatabaseHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			databaseHandler.dropDatabase(database);
		}
		
		// 移除dataSource和sessionFacotry
		DynamicDBUtil.removeDataSource(databaseId);
		
		// 删除发布信息的数据
		HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, "delete ComPublishInfo where publishDatabaseId = '"+databaseId+"'", null);
		return null;
	}
	//--------------------------------------------------------------------------------------------------------
	
	
}
