package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]数据库数据信息资源对象处理器
 * @author DougLei
 */
public class ComDatabaseService extends AbstractResourceService{
	
	/**
	 * 创建数据库模型
	 * <p>如果是使用我们的库，则创建数据库，并创建对应的dataSource和sessionFactory</p>
	 * <p>否则就是客户自己的数据库服务器，只创建对应的dataSource和sessionFactory</p>
	 * @param database
	 */
	public void createDatabaseModel(ComDatabase database){
		// 如果ip和port和我们的数据库配置一致，判断为使用我们的数据库
		if(SysConfig.getSystemConfig("db.default.ip").equals(database.getDbIp()) 
				&& SysConfig.getSystemConfig("db.default.port").equals(database.getDbPort()+"")
//				&& database.getIsCreated() == 0
				){
//			DatabaseHandler databaseHandler = new DatabaseHandler();
//			databaseHandler.createDatabase(database);
		}
//		if(database.getIsCreated() == 0){
//			// 修改被创建的状态为1，即被创建
//			database.setIsCreated(1);
//			HibernateUtil.updateObject(database, null);
//		}
	}
	
	/**
	 * 删除数据库模型
	 * <p>如果是使用我们的库，则删除数据库，并删除对应的dataSource和sessionFactory</p>
	 * <p>否则就是客户自己的数据库服务器，只删除对应的dataSource和sessionFactory</p>
	 * @param database
	 */
	public void dropDatabaseModel(ComDatabase database){
		
		// 如果ip和port和我们的数据库配置一致，判断为使用我们的数据库
		if(SysConfig.getSystemConfig("db.default.ip").equals(database.getDbIp()) 
				&& SysConfig.getSystemConfig("db.default.port").equals(database.getDbPort()+"")
//				&& database.getIsCreated() == 1
				){
//			DatabaseHandler databaseHandler = new DatabaseHandler();
//			databaseHandler.dropDatabase(database);
		}
//		if(database.getIsCreated() == 1){
//			// 修改被创建的状态为0，即没有创建
//			database.setIsCreated(0);
//			HibernateUtil.updateObject(database, null);
//		}
		
		// ---------hql写法
		// 修改该库下所有的表的isCreated=0
		String hql = "update CfgTabledata set isCreated=0 where isCreated = 1 and id in (select rightId from ComDatabaseCfgTabledataLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 修改该库下所有的sql脚本的isCreated=0
		hql = "update ComSqlScript set isCreated=0 where isCreated = 1 and id in (select rightId from ComDatabaseComSqlScriptLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 删除该库下，所有表的hbm数据、资源数据
		hql = "delete ComHibernateHbmConfdata where tableId in (select rightId from ComDatabaseCfgTabledataLinks where leftId = '"+database.getId()+"')";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		hql = "delete ComSysResource where databaseId = '"+database.getId()+"'";
		HibernateUtil.executeUpdateByHql(SqlStatementType.UPDATE, hql, null);
		
		// 将项目id和数据库id的映射关系清除
		ProjectIdRefDatabaseIdMapping.clearMapping(database.getId());
	}
	
	
	//----------------------------------------------------------------------------------------------
	/**
	 * 操作[表/sql脚本]资源数据模型时，验证数据库信息
	 * @param database
	 */
	private void resourceValidDatabase(ComDatabase database){
//		if(database.getIsEnabled() == ISysResource.UNENABLED_RESOURCE_STATUS){
//			throw new IllegalArgumentException("操作的数据库[displayName:"+database.getDbDisplayName()+"/instanceName:"+database.getDbInstanceName()+"]被禁用，请联系管理员");
//		}
//		if(database.getIsCreated() == 0){
//			throw new IllegalArgumentException("操作的数据库[displayName:"+database.getDbDisplayName()+"/instanceName:"+database.getDbInstanceName()+"]没有被创建，请联系管理员");
//		}
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * 获取指定database的sessionFactory
	 * @param database
	 * @return
	 */
//	private SessionFactoryImpl getSessionFactory(ComDatabase database){
//		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(database.getId());
//		if(sessionFactory == null){
//			throw new NullPointerException("指定数据库[displayName:"+database.getDbDisplayName()+"/instanceName:"+database.getDbInstanceName()+"/id:"+database.getId()+"]的sessionFactory为空，请联系管理员");
//		}
//		return null;
//	}
	
	/**
	 * 创建数据表模型
	 * <p>同时会创建hbm映射文件</p>
	 * @param database
	 * @param table
	 */
	public void createTableModel(ComDatabase database, CfgTabledata table){
		List<CfgTabledata> tables = new ArrayList<CfgTabledata>(1);
		tables.add(table);
		createTableModels(database, tables);
	}
	/**
	 * 创建数据表模型
	 * <p>同时会创建hbm映射文件</p>
	 * @param database
	 * @param tables
	 */
	public void createTableModels(ComDatabase database, List<CfgTabledata> tables){
		resourceValidDatabase(database);
		int len = tables.size();
		for(int i = 0; i < len; i++){
//			if(tables.get(i).getIsCreated() == 1){
//				tables.remove(i--);
//			}
		}
		
		if(tables != null && tables.size() > 0){
			// 执行建表操作
//			DBTableHandler tableHandler = new DBTableHandler();
//			tableHandler.createTable(tables);
			
			ComSysResourceService comSysResourceService = new ComSysResourceService();
			// 修改表是否被创建的字段值
			for (CfgTabledata table : tables) {
//				table.setIsCreated(1);
				HibernateUtil.updateObject(table, null);
				
				// 将hbm资源加入到资源表中
				comSysResourceService.insertSysResource(database.getId(), table);
			}
			
			// 创建对应的hibernate hbm文件数据
//			SessionFactoryImpl sessionFactory = getSessionFactory(database);
//			HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
//			hibernateHbmHandler.createHbmMappingContent(tables, sessionFactory);
			
			tables.clear();// 清空内存
		}
	}
	
	/**
	 * 删除数据表模型
	 * <p>同时会删除hbm映射文件</p>
	 * @param database
	 * @param table
	 */
	public void dropTableModel(ComDatabase database, CfgTabledata table){
		List<CfgTabledata> tables = new ArrayList<CfgTabledata>(1);
		tables.add(table);
		dropTableModels(database, tables);
	}
	/**
	 * 删除数据表模型
	 * <p>同时会删除hbm映射文件</p>
	 * @param database
	 * @param tables
	 */
	public void dropTableModels(ComDatabase database, List<CfgTabledata> tables){
		resourceValidDatabase(database);
		if(tables != null && tables.size() > 0){
			int len = tables.size();
			for(int i = 0; i < len; i++){
//				if(tables.get(i).getIsCreated() == 0){
//					tables.remove(i--);
//				}
			}
			
			if(tables != null && tables.size() > 0){
				// 执行删表操作
//				DBTableHandler tableHandler = new DBTableHandler();
//				tableHandler.dropTable(tables);
				
				// 修改表是否被创建的字段值
				List<Object> tableIds = new ArrayList<Object>(tables.size());// 记录被删除的表的id，后续删除对应的资源数据
				for (CfgTabledata table : tables) {
//					table.setIsCreated(0);
					HibernateUtil.updateObject(table, null);
					
					tableIds.add(table.getId());
				}
				
				// 将hbm资源从资源表中删除
//				ComSysResourceService comSysResourceService = new ComSysResourceService();
//				comSysResourceService.deleteSysResource(tableIds);
				
				// 创建对应的hibernate hbm文件数据
//				SessionFactoryImpl sessionFactory = getSessionFactory(database);
//				HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
//				hibernateHbmHandler.dropHbmMappingContent(tables, sessionFactory);
				
				// 清空内存
				tableIds.clear();
				tables.clear();
			}
		}
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * 创建sql脚本模型
	 * @param database
	 * @param sqlScript
	 */
	public void createSqlScriptModel(ComDatabase database, ComSqlScript sqlScript){
		List<ComSqlScript> sqlScripts = new ArrayList<ComSqlScript>(1);
		sqlScripts.add(sqlScript);
		createSqlScriptModels(database, sqlScripts);
	}
	/**
	 * 创建sql脚本模型
	 * @param database
	 * @param sqlScripts
	 */
	public void createSqlScriptModels(ComDatabase database, List<ComSqlScript> sqlScripts){
		resourceValidDatabase(database);
		int len = sqlScripts.size();
		for(int i = 0; i < len; i++){
//			if(sqlScripts.get(i).getIsCreated() == 1){
//				sqlScripts.remove(i--);
//			}
		}
		
		if(sqlScripts != null && sqlScripts.size() > 0){
			ComSysResourceService comSysResourceService = new ComSysResourceService();
			// 修改sql脚本资源是否被创建的字段值
			for (ComSqlScript sqlScript : sqlScripts) {
//				sqlScript.setIsCreated(1);
				HibernateUtil.updateObject(sqlScript, null);
				
				// 将sql脚本资源加入到资源表中
				comSysResourceService.insertSysResource(database.getId(), sqlScript);
			}
			sqlScripts.clear();// 清空内存
		}
	}
	
	/**
	 * 删除sql脚本模型
	 * @param database
	 * @param sqlScript
	 */
	public void dropSqlScriptModel(ComDatabase database, ComSqlScript sqlScript){
		List<ComSqlScript> sqlScripts = new ArrayList<ComSqlScript>(1);
		sqlScripts.add(sqlScript);
		dropSqlScriptModels(database, sqlScripts);
	}
	/**
	 * 删除sql脚本模型
	 * @param database
	 * @param sqlScripts
	 */
	public void dropSqlScriptModels(ComDatabase database, List<ComSqlScript> sqlScripts){
		resourceValidDatabase(database);
		if(sqlScripts != null && sqlScripts.size() > 0){
			int len = sqlScripts.size();
			for(int i = 0; i < len; i++){
//				if(sqlScripts.get(i).getIsCreated() == 0){
//					sqlScripts.remove(i--);
//				}
			}
			
			if(sqlScripts != null && sqlScripts.size() > 0){
				// 修改sql脚本资源是否被创建的字段值
				List<Object> sqlScriptIds = new ArrayList<Object>(sqlScripts.size());// 记录被删除的sql脚本的id，后续删除对应的资源数据
				for (ComSqlScript sqlScript : sqlScripts) {
//					sqlScript.setIsCreated(0);
					HibernateUtil.updateObject(sqlScript, null);
					
					sqlScriptIds.add(sqlScript.getId());
				}
				
				// 将表资源从资源表中删除
//				ComSysResourceService comSysResourceService = new ComSysResourceService();
//				comSysResourceService.deleteSysResource(sqlScriptIds);
				
				// 清空内存
				sqlScriptIds.clear();
				sqlScripts.clear();
			}
		}
	}
}
