package com.king.tooth.sys.service.other;

import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRuleDetail;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.sys.SysHibernateHbm;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 开发工具的Service
 * @author DougLei
 */
public class DevToolService extends AbstractService{
	
	public static void main(String[] args) {
		syncTablesToService(
				new CfgColumnCodeRule().toCreateTable(), 
				new CfgColumnCodeRuleDetail().toCreateTable());
	}
	
	/**
	 * 同步表到服务器
	 * <p>解决每次开发新加的表，后续还要手工到服务器数据库中建表、加建模数据</p>
	 * @param tables
	 */
	public static void syncTablesToService(ComTabledata... tables){
		// 服务器数据库对象
		CfgDatabase serviceDatabaseInstance = new CfgDatabase();
		// 服务器数据库连接信息
		serviceDatabaseInstance.setType("sqlserver");
		serviceDatabaseInstance.setInstanceName("SmartOneCfg");
		serviceDatabaseInstance.setLoginUserName("sa");
		serviceDatabaseInstance.setLoginPassword("123_abc");
		serviceDatabaseInstance.setIp("192.168.1.111");
		serviceDatabaseInstance.setPort(1433);
		
		// 数据库表处理对象【根据服务器数据库对象获取】
		DBTableHandler dbTableHandler = new DBTableHandler(serviceDatabaseInstance);
		
		String[] insertSqlArr = new String[tables.length*2];
		int index = 0;
		SysHibernateHbm hbm;
		SysResource resource;
		for (ComTabledata table : tables) {
			if(dbTableHandler.filterTable(true, table.getTableName()).size() == 1){
				System.err.println("表名为["+table.getTableName()+"]的表，在服务器数据库中已经存在，不能能再进行操作");
				continue;
			}
			
			// 1、建表
			dbTableHandler.createTable(table, true);
			
			// 2、获取hbm文件， 组装hbm对象，并获取插入hbm数据的sql
			hbm = new SysHibernateHbm();
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.tableTurnToHbm(table);
//			hbm.setHbmContent(HibernateHbmUtil.createHbmMappingContent(table, false));
			insertSqlArr[index++] = getInsertSysHibernateHbmSql(hbm);
			
			// 3、获取插入资源数据的sql
			resource = table.turnToResource();
			insertSqlArr[index++] = getInsertSysResourceSql(resource);
			
			// 4.清除
			table.clear();
		}

		// 数据库数据处理对象【根据服务器数据库对象获取】
		DBLink dblink = new DBLink(serviceDatabaseInstance);
		dblink.executeUpdate(insertSqlArr);
		
		System.out.println("******同步表到服务器数据库完成******");
	}
	// 获取插入SysHibernateHbm表的sql语句
	private static String getInsertSysHibernateHbmSql(SysHibernateHbm hbm) {
		String insertSysResourceSql = "insert into sys_hibernate_hbm(ref_database_id, hbm_resource_name, id, customer_id, project_id, is_enabled, req_resource_method, is_builtin, is_need_deploy, belong_platform_type, is_created, create_date, last_update_date, create_user_id, last_update_user_id) ";
		insertSysResourceSql += "values('5k7f1ef02728y7018f9df0e9edcr8d37','"+hbm.getHbmResourceName()+"','"+ResourceHandlerUtil.getIdentity()+"','unknow','90621e37b806o6fe8538c5eb782901bb',1, 'all', 1, "+hbm.getIsNeedDeploy()+", "+hbm.getBelongPlatformType()+", "+hbm.getIsCreated()+", getdate(), getdate(), '16ed21bd7a7a41f5bea2ebaa258908cf', '16ed21bd7a7a41f5bea2ebaa258908cf')";
		return insertSysResourceSql;
	}
	// 获取插入SysResource表的sql语句
	private static String getInsertSysResourceSql(SysResource resource) {
		String insertSysResourceSql = "insert into sys_resource(resource_name, resource_type, id, customer_id, project_id, is_enabled, req_resource_method, is_builtin, is_need_deploy, belong_platform_type, is_created, create_date, last_update_date, create_user_id, last_update_user_id) ";
		insertSysResourceSql += "values('"+resource.getResourceName()+"',1,'"+ResourceHandlerUtil.getIdentity()+"','unknow','90621e37b806o6fe8538c5eb782901bb',1, 'all', 1, "+resource.getIsNeedDeploy()+", "+resource.getBelongPlatformType()+", "+resource.getIsCreated()+", getdate(), getdate(), '16ed21bd7a7a41f5bea2ebaa258908cf', '16ed21bd7a7a41f5bea2ebaa258908cf')";
		return insertSysResourceSql;
	}
	
	
}