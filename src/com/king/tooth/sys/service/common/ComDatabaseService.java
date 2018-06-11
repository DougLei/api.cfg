package com.king.tooth.sys.service.common;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.jdbc.database.DatabaseHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.AbstractPublishService;
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
	
	
	
	
	/**
	 * 发布数据库
	 * @param databaseId
	 */
	public void deployingDatabase(String databaseId) {
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return;
		}
		// 如果ip和port和我们的数据库配置一致，判断为使用我们的数据库，则可以进行创建库的操作
		if(SysConfig.getSystemConfig("db.default.ip").equals(database.getDbIp()) 
				&& SysConfig.getSystemConfig("db.default.port").equals(database.getDbPort()+"")){
			DatabaseHandler databaseHandler = new DatabaseHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			databaseHandler.createDatabase(database);
		}
//		database.setIsDeploymentApp(1);
		HibernateUtil.saveObject(database, null);
	}
	
	/**
	 * 取消发布数据库库
	 * @param databaseId
	 */
	public void cancelDeployingDatabase(String databaseId) {
		ComDatabase database = getObjectById(databaseId, ComDatabase.class);
		if(database == null){
			return;
		}
		// 如果ip和port和我们的数据库配置一致，判断为使用我们的数据库，则可以进行删除库的操作
		if(SysConfig.getSystemConfig("db.default.ip").equals(database.getDbIp()) 
				&& SysConfig.getSystemConfig("db.default.port").equals(database.getDbPort()+"")){
			DatabaseHandler databaseHandler = new DatabaseHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			databaseHandler.dropDatabase(database);
		}
//		database.setIsDeploymentApp(0);
		HibernateUtil.saveObject(database, null);
		
		// ---------hql写法
		// 修改该库下所有的表的isDeploymentRun=0
		String hql = "update ComTabledata set isDeploymentRun=0 where isDeploymentRun=1 and id in (select rightId from ComDatabaseCfgTabledataLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 修改该库下所有的sql脚本的isDeploymentRun=0
		hql = "update ComSqlScript set isDeploymentRun=0 where isDeploymentRun=1 and id in (select rightId from ComDatabaseComProjectLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 修改该库下所有的项目的isDeploymentRun=0
		hql = "update ComProject set isDeploymentRun=0 where isDeploymentRun=1 and databaseId  = '"+database.getId()+"'";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
	}
}
