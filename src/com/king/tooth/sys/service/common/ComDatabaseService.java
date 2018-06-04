package com.king.tooth.sys.service.common;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.constants.SysDatabaseInstanceConstants;
import com.king.tooth.plugins.jdbc.database.DatabaseHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]数据库数据信息资源对象处理器
 * @author DougLei
 */
public class ComDatabaseService extends AbstractResourceService{
	
	private ComDatabase getDatabaseById(String databaseId){
		return (ComDatabase) HibernateUtil.executeUniqueQueryByHqlArr("from ComDatabase where id = ?", databaseId);
	}
	
	/**
	 * 发布数据库
	 * @param databaseId
	 */
	public void deployingDatabase(String databaseId) {
		ComDatabase database = getDatabaseById(databaseId);
		if(database == null){
			return;
		}
		// 如果ip和port和我们的数据库配置一致，判断为使用我们的数据库，则可以进行创建库的操作
		if(SysConfig.getSystemConfig("db.default.ip").equals(database.getDbIp()) 
				&& SysConfig.getSystemConfig("db.default.port").equals(database.getDbPort()+"")){
			DatabaseHandler databaseHandler = new DatabaseHandler(SysDatabaseInstanceConstants.CFG_DATABASE);
			databaseHandler.createDatabase(database);
		}
		database.setIsDeploymentRun(1);
		HibernateUtil.saveObject(database, null);
	}
	
	/**
	 * 取消发布数据库库
	 * @param databaseId
	 */
	public void cancelDeployingDatabase(String databaseId) {
		ComDatabase database = getDatabaseById(databaseId);
		if(database == null){
			return;
		}
		// 如果ip和port和我们的数据库配置一致，判断为使用我们的数据库，则可以进行删除库的操作
		if(SysConfig.getSystemConfig("db.default.ip").equals(database.getDbIp()) 
				&& SysConfig.getSystemConfig("db.default.port").equals(database.getDbPort()+"")){
			DatabaseHandler databaseHandler = new DatabaseHandler(SysDatabaseInstanceConstants.CFG_DATABASE);
			databaseHandler.dropDatabase(database);
		}
		database.setIsDeploymentRun(0);
		HibernateUtil.saveObject(database, null);
		
		// ---------hql写法
		// 修改该库下所有的表的isDeploymentRun=0
		String hql = "update CfgTabledata set isDeploymentRun=0 where isDeploymentRun=1 and id in (select rightId from ComDatabaseCfgTabledataLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 修改该库下所有的sql脚本的isDeploymentRun=0
		hql = "update ComSqlScript set isDeploymentRun=0 where isDeploymentRun=1 and id in (select rightId from ComDatabaseComSqlScriptLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 修改该库下所有的项目的isDeploymentRun=0
		hql = "update ComProject set isDeploymentRun=0 where isDeploymentRun=1 and databaseId  = '"+database.getId()+"'";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
	}
}
